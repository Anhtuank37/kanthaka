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

public class QueryRunUtill extends Thread {

    Rule businessRule;
    private Keyspace keyspace;
    private Cluster cluster = null;
    private static final StringSerializer se = new StringSerializer();
    private static final LongSerializer le = new LongSerializer();
    static Logger logger = LoggerFactory.getLogger(QueryRunUtill.class.getName());

    /**
     *
     * @param businessRule
     */
    public QueryRunUtill(Rule businessRule) {
        this.businessRule = businessRule;
        cluster = HFactory.getOrCreateCluster(
                BasicConf.CASSANDRA_CLUSTER, BasicConf.CLUSTER_PORT);
        keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

        //To change body of created methods use File | Settings | File Templates.
    }

    /**
     *
     * @return
     */
    public Rule getBusinessRule() {
        return businessRule;
    }

    public void setBusinessRule(Rule businessRule) {
        this.businessRule = businessRule;
    }

    /**
     * compile query on Cassandra and return the result
     *
     * @param query
     * @return selected entries from running the query
     */
    HashSet<String> runQuery(StringBuffer query) {

        HashSet<String> returnSet = new HashSet<String>();

        CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
                HFactory.createKeyspace(BasicConf.KEYSPACE, cluster), se, se, se);


        String queryString = query.toString();
        logger.debug(queryString);
        cqlQuery.setQuery(queryString);

        QueryResult<CqlRows<String, String, String>> result = cqlQuery.execute();
        if (result != null && result.get() != null) {
            List<Row<String, String, String>> list = result.get().getList();
            for (Row row : list) {
                List columns = row.getColumnSlice().getColumns();
                for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                    HColumn column = (HColumn) iterator.next();

                    String s = (String) column.getName();
                    if ("KEY".equals(s)) {
                        returnSet.add((String) column.getValue());
                        System.out.println(column.getValue());

                    }
                }
            }
        }
        return returnSet;
    }

    /**
     * to run all the queries related to a rule and return a list of eligible subscribers. This generates the CQL queries
     * for each rule, select the users and send for AND -OR operations
     *
     * @param rule :
     */
    public synchronized void runRuleQueries(Rule rule) {
        ArrayList<ArrayList<CounterConditionFields>> rulesSet = rule.getCounterConditionFields();
        ArrayList<ArrayList<HashSet<String>>> ANDlist = rule.getCounterResultSet();

        for (ArrayList<CounterConditionFields> list : rulesSet) {
            ArrayList<HashSet<String>> ORlist = new ArrayList<HashSet<String>>();
            for (CounterConditionFields counterConditionFields : list) {
                StringBuffer query = new StringBuffer("SELECT KEY FROM " + rule.getRuleName() + " WHERE flag=1 AND " + counterConditionFields.getConditionName() + counterConditionFields.getCondition() + counterConditionFields.getValue());
                HashSet<String> resultset = this.runQuery(query);

                if (!resultset.isEmpty()) {
                    ORlist.add(resultset);
                }

            }
            if (!ORlist.isEmpty()) {
                ANDlist.add(ORlist);
            }// adding sets need to take AND
            logger.debug("Result set that need to perform AND {}." + rule.getCounterResultSet());


        }

        ProcessResultSet processResultSet = new ProcessResultSet();
        processResultSet.compareResultSet(rule, this);      // send to combine results to get ANDs ORs
    }

    /**
     * to set the flag of the already selected users to zero to avoid selecting the same user twise
     *
     * @param removeSet set of users who need to set the flag in zero
     * @param rule
     */
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

    /**
     * thread to run the queries on database periodically
     */
    @Override
    public void run() {
        runRuleQueries(getBusinessRule());
        logger.info("Query execute on Rule : {}.", businessRule.getRuleName());
    }
}
