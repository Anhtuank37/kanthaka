/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.cassandra.updater;

import java.util.ArrayList;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uom.kanthaka.preprocessor.cdrreader.RecordMap;
import com.uom.kanthaka.preprocessor.rulereader.Rule;

/**
 *
 */
public class CassandraUpdater extends TimerTask {

  private Keyspace keyspace;
  private Cluster cluster = null;
  ArrayList<Rule> businessRules;
  private static final StringSerializer se = new StringSerializer();
  private static final LongSerializer le = new LongSerializer();
  static Logger logger = LoggerFactory.getLogger(CassandraUpdater.class
      .getName());

  /**
   * the contructor of the CassandraUpdater
   * 
   * @param businessRules
   */

  public CassandraUpdater(ArrayList<Rule> businessRules) {
    this.businessRules = businessRules;
    this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
    keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);
  }

  /**
   * Insert data in to Cassandra from hashmaps this method runs periodically
   * 
   * @param ruleName
   *          rule name which is same as name as column family name
   * @param columnName
   *          column name of database column family
   * @param entries
   *          HashMaps <phoneNumber|count> </>
   */
  public void insertData(String ruleName, String columnName,
      ConcurrentHashMap<String, Long> entries) {

    Mutator<String> mutator = HFactory.createMutator(keyspace,
        StringSerializer.get());
    Set<String> keys = entries.keySet();
    for (String s : keys) {
      long currentValue = 0;
      try {

        currentValue = getValueInCell(ruleName, s, columnName);
      } catch (NullPointerException ex) {

      } catch (Exception e) {
        e.printStackTrace();
      }

      long newValue = currentValue + entries.get(s);
      mutator = mutator.addInsertion(s, ruleName, HFactory.createColumn(
          columnName, newValue, StringSerializer.get(), LongSerializer.get()));
      try {
        long l = this.getValueInCell(ruleName, s, "flag");

      } catch (Exception e) {
        mutator.addInsertion(s, ruleName,
            HFactory.createColumn("flag", 1L, se, le));
      }

    }
    mutator.execute();

  }

  /**
   * To get the current count in the databasecolumn family
   * 
   * @param tableName
   *          column familyname
   * @param rowID
   *          KEY value of the row
   * @param columnID
   *          column name
   * @return current count value in database
   * @throws Exception
   */
  public Long getValueInCell(String tableName, String rowID, String columnID)
      throws Exception {
    Keyspace keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

    ColumnQuery<String, String, Long> cq = HFactory.createColumnQuery(keyspace,
        StringSerializer.get(), StringSerializer.get(), LongSerializer.get());

    cq.setColumnFamily(tableName).setKey(rowID).setName(columnID);
    return cq.execute().get().getValue();
  }

  /**
   * thread to update cassandra periodically
   * 
   * @param businessRule
   */

  public synchronized void updateDatabaseTables(Rule businessRule) {
    ArrayList<RecordMap> recordMaps = businessRule.getRecordMaps();
    for (int i = 0; i < recordMaps.size(); i++) {
      RecordMap record = recordMaps.remove(0);
      ConcurrentHashMap<String, Long> datamap = record.getDataMap();
      insertData(businessRule.getRuleName(), record.getType(), datamap);
      logger.debug("Updated Cassandra with hashmap values : {}", datamap);
    }
  }

  /**
   * to update database table periodically
   */
  @Override
  public void run() {
    for (int i = 0; i < businessRules.size(); i++) {
      updateDatabaseTables(businessRules.get(i));
    }
    Thread.yield();
    logger.info("Thread - Updating Cassandra Database");
  }
}
