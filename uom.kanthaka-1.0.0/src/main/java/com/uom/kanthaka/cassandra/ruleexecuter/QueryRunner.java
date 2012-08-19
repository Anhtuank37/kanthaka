package com.uom.kanthaka.cassandra.ruleexecuter;


import com.uom.kanthaka.cassandra.updater.BasicConf;
import com.uom.kanthaka.preprocessor.rulereader.CounterConditionFields;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

//import org.apache.log4j.Logger;


public class QueryRunner extends TimerTask {
    ArrayList<Rule> businessRules;

    private Keyspace keyspace;
    private Cluster cluster = null;
    private static final StringSerializer se = new StringSerializer();
    private static final LongSerializer le = new LongSerializer();
    final Logger logger = LoggerFactory.getLogger(QueryRunner.class);


    public QueryRunner(ArrayList<Rule> businessRules) {
        this.businessRules = businessRules;
        cluster = HFactory.getOrCreateCluster(
                BasicConf.CASSANDRA_CLUSTER, BasicConf.CLUSTER_PORT);
        keyspace = HFactory
                .createKeyspace(BasicConf.KEYSPACE, cluster);

    }

    public ArrayList<Rule> getBusinessRules() {
        return businessRules;
    }

    // compile query on Cassandra and return the result
    HashSet<String> runQuery(StringBuffer query) {

        HashSet<String> returnSet = new HashSet<String>();

        CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
                HFactory.createKeyspace(BasicConf.KEYSPACE, cluster), se, se, se);


        String queryString = query.toString();
        System.out.println(queryString);
        cqlQuery.setQuery(queryString);

        QueryResult<CqlRows<String, String, String>> result = cqlQuery
                .execute();
        if (result != null && result.get() != null) {
            List<Row<String, String, String>> list = result.get().getList();
            for (Row row : list) {
                System.out.println(".");
                List columns = row.getColumnSlice().getColumns();
                for (Iterator iterator = columns.iterator(); iterator.hasNext(); ) {
                    HColumn column = (HColumn) iterator.next();

                    System.out.print(column.getName() + ":" + column.getValue()
                            + "\t");

                    String s = (String) column.getName();
                    if ("KEY".equals(s)) {
                        returnSet.add((String) column.getValue());
                    }
                }
                System.out.println("");
            }
        }
        logger.debug("results returned from query {}.", returnSet);
        return returnSet;
    }

    // to run all the queries related to a rule and return a list of eligible subscribers
    void runRuleQueries(Rule rule) {
        ArrayList<ArrayList<CounterConditionFields>> rulesSet = rule.getCounterConditionFields();
        ArrayList<ArrayList<HashSet<String>>> ANDlist = rule.getCounterResultSet();

        for (ArrayList<CounterConditionFields> list : rulesSet) {
            ArrayList<HashSet<String>> ORlist = new ArrayList<HashSet<String>>();
            for (CounterConditionFields counterConditionFields : list) {
                StringBuffer query = new StringBuffer("SELECT KEY FROM " + rule.getRuleName() + " WHERE flag=1 AND " + counterConditionFields.getConditionName() + counterConditionFields.getCondition() + counterConditionFields.getValue());
                HashSet<String> resultset = this.runQuery(query);

                ORlist.add(resultset);
                System.out.println(resultset);

            }
            ANDlist.add(ORlist);          // adding sets need to take AND
        }

        ProcessResultSet processResultSet = new ProcessResultSet();
        processResultSet.compareResultSet(rule, this);      // send to combine results to get ANDs ORs
    }

    // set the flag to zero of the entries in cassandra who got selected
    void removeUser(HashSet<String> removeSet, Rule rule) {

        for (String rmvNo : removeSet) {

            StringBuffer removeQuery = new StringBuffer("UPDATE " + rule.getRuleName() + " SET 'flag'=0 WHERE KEY=" + rmvNo);
            CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
                    HFactory.createKeyspace(BasicConf.KEYSPACE, cluster), se, se, se);


            String queryString = removeQuery.toString();
            cqlQuery.setQuery(queryString);
            cqlQuery.execute();


        }

    }


    @Override
    public void run() {
        for (int i = 0; i < getBusinessRules().size(); i++) {
            runRuleQueries(getBusinessRules().get(i));

        }

        Thread.yield();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        System.out.println("Query execute");
    }
}
