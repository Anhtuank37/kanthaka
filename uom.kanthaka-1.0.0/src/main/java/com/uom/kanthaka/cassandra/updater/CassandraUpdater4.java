package com.uom.kanthaka.cassandra.updater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;

import com.uom.kanthaka.preprocessor.cdrreader.RecordMap;
import com.uom.kanthaka.preprocessor.rulereader.Rule;

/**
 * Created with IntelliJ IDEA. User: amila Date: 8/14/12 Time: 5:28 PM To change
 * this template use File | Settings | File Templates.
 */
public class CassandraUpdater4 extends TimerTask {

  private Keyspace keyspace;
  private Cluster cluster = null;
  ArrayList<Rule> businessRules;
  private static final StringSerializer se = new StringSerializer();
  private static final LongSerializer le = new LongSerializer();

  public CassandraUpdater4(ArrayList<Rule> businessRules) {

    this.businessRules = businessRules;
    this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
  }

  public CassandraUpdater4() {

    this.businessRules = businessRules;
    this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
  }

  public void createKeyspace() {

    KeyspaceDefinition newKeyspaceDef = HFactory
        .createKeyspaceDefinition(BasicConf.KEYSPACE);
    if ((cluster.describeKeyspace(BasicConf.KEYSPACE)) == null) {
      cluster.addKeyspace(newKeyspaceDef, true);
    }
    keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);
  }

  public void createTable(String tableName) throws Exception {
    Cluster cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);

    ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
        BasicConf.KEYSPACE, tableName, ComparatorType.UTF8TYPE);
    cfDef.setKeyValidationClass("org.apache.cassandra.db.marshal.UTF8Type");

    Boolean isAlreadyExists = false;
    KeyspaceDefinition keyspaceDef = cluster
        .describeKeyspace(BasicConf.KEYSPACE);
    List<ColumnFamilyDefinition> columnFamilyList = keyspaceDef.getCfDefs();
    Iterator<ColumnFamilyDefinition> iterator = columnFamilyList.iterator();
    while (iterator.hasNext()) {
      if (iterator.next().getName().equals(cfDef.getName())) {
        isAlreadyExists = true;
        break;
      }
    }
    if (!isAlreadyExists) {
      cluster.addColumnFamily(cfDef);
      System.out.println("column Family" + tableName + "created");
    }
  }

  public void indexColumn(String idxColumnName, String cfName, String columnType) {

    Cluster cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
    KeyspaceDefinition keyspaceDefinition = cluster
        .describeKeyspace(BasicConf.KEYSPACE);

    List<ColumnFamilyDefinition> cdfs = keyspaceDefinition.getCfDefs();
    ColumnFamilyDefinition cfd = null;
    for (ColumnFamilyDefinition c : cdfs) {
      if (c.getName().toString().equals(cfName)) {
        System.out.println(c.getName());
        cfd = c;
        break;
      }
    }

    BasicColumnFamilyDefinition columnFamilyDefinition = new BasicColumnFamilyDefinition(
        cfd);

    BasicColumnDefinition bcdf = new BasicColumnDefinition();

    bcdf.setName(StringSerializer.get().toByteBuffer(idxColumnName));
    bcdf.setIndexName(cfName + idxColumnName + "index");
    bcdf.setIndexType(ColumnIndexType.KEYS);

    if (columnType.equals("l")) {
      bcdf.setValidationClass(ComparatorType.LONGTYPE.getClassName());
    } else if (columnType.equals("s")) {
      // bcdf.setValidationClass(ComparatorType.ASCIITYPE.getClassName());
      // bcdf.setValidationClass("org.apache.cassandra.db.marshal.UTF8Type");
      // bcdf.setValidationClass(ComparatorType.LONGTYPE.getClassName());
      // bcdf.setValidationClass(ComparatorType.BYTESTYPE.getClassName());
      bcdf.setValidationClass(ComparatorType.UTF8TYPE.getClassName());
    }

    columnFamilyDefinition.addColumnDefinition(bcdf);
    cluster.updateColumnFamily(new ThriftCfDef(columnFamilyDefinition));

    List<ColumnFamilyDefinition> cdfs1 = keyspaceDefinition.getCfDefs();
    ColumnFamilyDefinition cfd1 = null;
    for (ColumnFamilyDefinition c : cdfs1) {
      if (c.getName().toString().equals(cfName)) {
        System.out.println(c.getName());
        cfd1 = c;
        break;
      }
    }
    System.out.println(cfd1.getColumnMetadata());

  }

  private void dataInserter(String ruleName, String columnName,
      ConcurrentHashMap<String, Long> entries) {

    Mutator<String> mutator = HFactory.createMutator(keyspace,
        StringSerializer.get());
    Set<String> keys = entries.keySet();
    for (String s : keys) {
      long currentValue = 0;
      String rowKey = s + "000";
      try {

        currentValue = getValueInCell(ruleName, rowKey, columnName);
        System.out.println("updated value from");
      } catch (NullPointerException ex) {
        System.out.println("new row column addded");
      } catch (Exception e) {
        e.printStackTrace(); // To change body of catch statement use File |
                             // Settings | File Templates.
      }

      long newValue = currentValue + entries.get(s);
      long phone = Long.parseLong(s);
      mutator = mutator
          .addInsertion(
              rowKey,
              ruleName,
              HFactory.createColumn(columnName, newValue,
                  StringSerializer.get(), LongSerializer.get()))
          .addInsertion(rowKey, ruleName,
              HFactory.createColumn("flag", 1L, se, le))
          .addInsertion(rowKey, ruleName,
              HFactory.createColumn("phoneNumber", s, se, se));

    }
    mutator.execute();

  }

  public Long getValueInCell(String tableName, String rowID, String columnID)
      throws Exception {
    Keyspace keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

    ColumnQuery<String, String, Long> cq = HFactory.createColumnQuery(keyspace,
        StringSerializer.get(), StringSerializer.get(), LongSerializer.get());

    cq.setColumnFamily(tableName).setKey(rowID).setName(columnID);
    return cq.execute().get().getValue();
  }

  public void updateDatabaseTables(Rule businessRule) {
    ArrayList<RecordMap> recordMaps = businessRule.getRecordMaps();
    for (int i = 0; i < recordMaps.size(); i++) {
      RecordMap record = recordMaps.remove(0);
      dataInserter(businessRule.getRuleName(), record.getType(),
          record.getDataMap());
    }
  }

  @Override
  public void run() {
    for (int i = 0; i < businessRules.size(); i++) {
      updateDatabaseTables(businessRules.get(i));
    }
    Thread.yield();
    System.out.println("Thread - Updating Cassandra Database");
  }

  public static void main(String[] args) throws Exception {

    CassandraUpdater4 couUpdater = new CassandraUpdater4();
    couUpdater.createKeyspace();

    String ruleName = "rule2";

    List<String> counters = new ArrayList<String>();
    counters.add("sms");
    counters.add("ussd");

    // creating counter table for rule
    couUpdater.createTable(ruleName);
    for (String s : counters) {
      couUpdater.indexColumn(s, ruleName, "l");
    }
    couUpdater.indexColumn("flag", ruleName, "l");
    couUpdater.indexColumn("phoneNumber", ruleName, "s");

    // run periodically
    HashMap<String, ConcurrentHashMap<String, Long>> maps = new HashMap<String, ConcurrentHashMap<String, Long>>();
    ConcurrentHashMap<String, Long> innermap1 = new ConcurrentHashMap<String, Long>();
    innermap1.put("1235", 1L);
    innermap1.put("1236", 3L);
    innermap1.put("1237", 4L);
    innermap1.put("1238", 3L);
    maps.put("sms", innermap1);
    ConcurrentHashMap<String, Long> innermap2 = new ConcurrentHashMap<String, Long>();
    innermap2.put("1235", 3L);
    innermap2.put("1236", 3L);
    innermap2.put("1237", 4L);
    innermap2.put("1238", 3L);
    innermap2.put("1235", 2L);
    maps.put("ussd", innermap2);

    Set<String> keys = maps.keySet();
    for (String s : keys) {
      couUpdater.dataInserter(ruleName, s, maps.get(s));// cf,column,hashmap
    }
  }

}
