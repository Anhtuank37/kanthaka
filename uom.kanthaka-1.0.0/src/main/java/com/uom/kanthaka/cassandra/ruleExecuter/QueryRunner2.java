package com.uom.kanthaka.cassandra.ruleExecuter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.TimerTask;

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

import org.apache.log4j.Logger;

import com.uom.kanthaka.cassandra.updater.BasicConf;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import com.uom.kanthaka.preprocessor.rulereader.counterConditionFields;

public class QueryRunner2 extends TimerTask {
  ArrayList<Rule> businessRules;

  private Keyspace keyspace;
  private Cluster cluster = null;
  private static final StringSerializer se = new StringSerializer();
  private static final LongSerializer le = new LongSerializer();
  static Logger _logger = Logger.getLogger(QueryRunner2.class.getName());

  public QueryRunner2(ArrayList<Rule> businessRules) {
    this.businessRules = businessRules;
    cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
    keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

    // To change body of created methods use File | Settings | File Templates.
  }

  public ArrayList<Rule> getBusinessRules() {
    return businessRules;
  }

  // compile query on Cassandra and return the result
  HashSet<String> runQuery(StringBuffer query) {

    HashSet<String> returnSet = new HashSet<String>();

    CqlQuery<String, String, String> cqlQuery = new CqlQuery<String, String, String>(
        HFactory.createKeyspace(BasicConf.KEYSPACE, cluster), se, se, se);

    // cqlQuery.setQuery("SELECT * FROM rule01 WHERE No_of_SMSs=32");
    String queryString = query.toString();
    System.out.println(queryString);
    cqlQuery.setQuery(queryString);

    QueryResult<CqlRows<String, String, String>> result = cqlQuery.execute();
    if (result != null && result.get() != null) {
      List<Row<String, String, String>> list = result.get().getList();
      for (Row row : list) {
        System.out.println(".");
        List columns = row.getColumnSlice().getColumns();
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
          HColumn column = (HColumn) iterator.next();

          System.out.print(column.getName() + ":" + column.getValue() + "\t");

          String s = (String) column.getName();
          if ("phoneNumber".equals(s)) {
            returnSet.add((String) column.getValue());
            // System.out.print(column.getValue());
            System.out.println(column.getValue());
          }
        }
        System.out.println("");
      }
    }
    System.out.println(returnSet);
    return returnSet;
  }

  // to run all the queries related to a rule and return a list of eligible
  // subscribers
  void runRuleQueries(Rule rule) {
    ArrayList<ArrayList<counterConditionFields>> rulesSet = rule
        .getCounterConditionFields();
    ArrayList<ArrayList<HashSet<String>>> ANDlist = rule.getCounterResultSet();

    for (ArrayList<counterConditionFields> list : rulesSet) {
      ArrayList<HashSet<String>> ORlist = new ArrayList<HashSet<String>>();
      for (counterConditionFields counterConditionFields : list) {
        StringBuffer query = new StringBuffer("SELECT phoneNumber FROM "
            + rule.getRuleName() + " WHERE flag=1 AND "
            + counterConditionFields.getConditionName()
            + counterConditionFields.getCondition()
            + counterConditionFields.getValue());
        HashSet<String> resultset = this.runQuery(query);
        ORlist.add(resultset);
        System.out.println(resultset);

      }
      ANDlist.add(ORlist); // adding sets need to take AND
    }

    CassandraProcessResultSet processResultSet = new CassandraProcessResultSet();
    processResultSet.compareResultSet(rule); // send to combine results to get
                                             // ANDs ORs
  }

  public static void main(String[] args) {

    // (a|b)
    counterConditionFields conditionFieldsOr1 = new counterConditionFields(
        "No_of_SMSs", ">", 5L);
    counterConditionFields conditionFieldsOr2 = new counterConditionFields(
        "No_of_Calls", ">", 5L);
    ArrayList<counterConditionFields> conditionFieldsOR1Arr = new ArrayList<counterConditionFields>();// a|b
    conditionFieldsOR1Arr.add(conditionFieldsOr1);
    conditionFieldsOR1Arr.add(conditionFieldsOr2);

    counterConditionFields conditionFieldsOr11 = new counterConditionFields(
        "No_of_SMSs", ">", 3L);
    ArrayList<counterConditionFields> conditionFieldsOR2Arr = new ArrayList<counterConditionFields>();// (a)
    conditionFieldsOR2Arr.add(conditionFieldsOr11);

    // (a|b)&(a)
    ArrayList<ArrayList<counterConditionFields>> counterConditionFd = new ArrayList<ArrayList<counterConditionFields>>();
    counterConditionFd.add(conditionFieldsOR1Arr);
    counterConditionFd.add(conditionFieldsOR2Arr);

    Rule rule = new Rule();
    rule.setcounterConditionFields(counterConditionFd);
    rule.setRuleName("rule02");

    // QueryRunner2 queryRunner=new QueryRunner2();
    // queryRunner.runRuleQueries(rule);

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
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }
    System.out.println("Query execute");
  }
}
