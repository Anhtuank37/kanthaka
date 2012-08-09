package com.uom.kanthaka.cassandra.updater;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


import me.prettyprint.cassandra.model.BasicColumnDefinition;
import me.prettyprint.cassandra.model.BasicColumnFamilyDefinition;
import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftCfDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ColumnIndexType;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.QueryResult;

public class CassandraUpdater3 {

    /**
     * @param args
     */
    private Keyspace keyspace;
    private Cluster cluster = null;

    public CassandraUpdater3() {

        this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
                BasicConf.CLUSTER_PORT);
    }

    public void createKeyspace() {

        KeyspaceDefinition newKeyspaceDef = HFactory.createKeyspaceDefinition(BasicConf.KEYSPACE);
        if ((cluster.describeKeyspace(BasicConf.KEYSPACE)) == null) {
            cluster.addKeyspace(newKeyspaceDef, true);
        }
        keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);
    }

    public void createTable(String tableName,String keyType) {
        Cluster cluster = HFactory.getOrCreateCluster(
                BasicConf.CASSANDRA_CLUSTER, BasicConf.CLUSTER_PORT);

        ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
                BasicConf.KEYSPACE, tableName, ComparatorType.UTF8TYPE);
        if(keyType.equals("s")){
        cfDef.setKeyValidationClass("org.apache.cassandra.db.marshal.UTF8Type");
        }else if(keyType.equals("l")){
        	 cfDef.setKeyValidationClass("org.apache.cassandra.db.marshal.LongType");
        }
        Boolean isAlreadyExists = false;
        KeyspaceDefinition keyspaceDef = cluster.describeKeyspace(BasicConf.KEYSPACE);
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
            System.out.println("table"+tableName+"created");
        }
    }

    void indexColumn(String idxColumnName, String cfName) {

        Cluster cluster = HFactory.getOrCreateCluster(
                BasicConf.CASSANDRA_CLUSTER, BasicConf.CLUSTER_PORT);
        KeyspaceDefinition keyspaceDefinition = cluster.describeKeyspace(BasicConf.KEYSPACE);

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
        bcdf.setIndexName(idxColumnName + "index");
        bcdf.setIndexType(ColumnIndexType.KEYS);
        bcdf.setValidationClass(ComparatorType.LONGTYPE.getClassName());

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

    // tablename, sms, <4n#, count>, updates 2 table rule2smscount(1) and rule2current_count(2)
    public void dataInserter(String ruleName, String columnName,
            ConcurrentHashMap<String, Long> entries) {
        long currentValue = 0;
        Mutator<Long> mutator1 = HFactory.createMutator(keyspace,
                LongSerializer.get());

        Mutator<String> mutator2 = HFactory.createMutator(keyspace,
                StringSerializer.get());

        Set<String> keys = entries.keySet();//4n #s
        for (String phoneNumber : keys) {
            // searching the current value from (2)
            String searchCurrent = "select " + columnName + " FROM " + ruleName + "current_count WHERE KEY=" + phoneNumber;
            try {
                currentValue = getCurrentcount(searchCurrent);
            } catch (Exception ex) {
            }
            long newValue = currentValue + entries.get(phoneNumber);
            // adding to (1)
            mutator1 = mutator1.addInsertion(newValue, ruleName + columnName + "count", HFactory.createColumn(phoneNumber, " ", StringSerializer.get(),
                    StringSerializer.get()));
            // updating (2)
            mutator2 = mutator2.addInsertion(phoneNumber, ruleName + "current_count", HFactory.createColumn(columnName, newValue, StringSerializer.get(),
                    LongSerializer.get()));
            mutator2.execute();
            System.out.println(columnName+" value of "+phoneNumber+" updated in table ="+ruleName + "current_count");

        }
        mutator1.execute();
        System.out.println("data inserted to table "+ ruleName + columnName + "count");


    }

    long getCurrentcount(String query) {

        long currentcount = 0;

        CqlQuery<String, String, Long> cqlQuery = new CqlQuery<String, String, Long>(
                HFactory.createKeyspace(BasicConf.KEYSPACE, cluster),
                StringSerializer.get(), StringSerializer.get(),
                LongSerializer.get());
        cqlQuery.setQuery(query);

        QueryResult<CqlRows<String, String, Long>> result = cqlQuery.execute();
        if (result != null && result.get() != null) {
            List<Row<String, String, Long>> list = result.get().getList();
            for (Row row : list) {
                //	System.out.println(".");
                List columns = row.getColumnSlice().getColumns();
                for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
                    HColumn column = (HColumn) iterator.next();

                    System.out.print(column.getName() + ":" + column.getValue()
                            + "\t");
                    currentcount = (Long) column.getValue();
                }
                System.out.println("");
            }
        }

        return currentcount;
    }

    public static void main(String[] args) throws Exception {

        CassandraUpdater3 couUpdater = new CassandraUpdater3();
        couUpdater.createKeyspace();

        String ruleName = "rule2";

        List<String> counters = new ArrayList<String>();
        counters.add("sms");
        counters.add("ussd");

        // creating tables with counter|4n # for sms,ussd(2 tables rule2smscount, rule2ussdcount
        for (String s : counters) {
            couUpdater.createTable(ruleName + s + "count","l");
        }

        // creating table for 4n#|current count(sms)|current count(ussd) -(1
        // table=rule2current_count
        couUpdater.createTable(ruleName + "current_count","s");

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
            couUpdater.dataInserter(ruleName, s, maps.get(s));// cf,column
        }
    }

//    public void updateDatabaseTables(Rule businessRule, CassandraUpdater3 couUpdater) {
//
//        ArrayList<RecordMap> recordMaps = businessRule.getRecordMaps();
//
//        for (int i = 0; i < recordMaps.size(); i++) {
//            couUpdater.dataInserter(businessRule.getRuleName(), recordMaps.get(i).getType(), recordMaps.get(i).getDataMap());
//        }
//
//    }
}
