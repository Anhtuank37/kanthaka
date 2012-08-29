package com.uom.kanthaka.cassandra.updater;

import com.uom.kanthaka.preprocessor.cdrreader.RecordMap;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/14/12
 * Time: 5:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CassandraUpdater extends TimerTask {

    private Keyspace keyspace;
    private Cluster cluster = null;
    ArrayList<Rule> businessRules;
    private static final StringSerializer se = new StringSerializer();
    private static final LongSerializer le = new LongSerializer();
    static Logger logger = LoggerFactory.getLogger(CassandraUpdater.class.getName());

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public CassandraUpdater(ArrayList<Rule> businessRules) {
        this.businessRules = businessRules;
        this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
                BasicConf.CLUSTER_PORT);
        keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);
    }

    /**
     * Insert data in to Cassandra from hashmaps
     *
     * @param ruleName   rule name which is same as name as column family name
     * @param columnName column name of database column family
     * @param entries    HashMaps <phoneNumber|count> </>
     */
    public void dataInserter(String ruleName, String columnName, ConcurrentHashMap<String, Long> entries) {

		    System.out.print("ENtires to Casandra---------------"+entries.toString());
        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
        Set<String> keys = entries.keySet();
        for (String s : keys) {
            long currentValue = 0;
            try {

                currentValue = getValueInCell(ruleName, s, columnName);
                logger.debug("updated value from");
            } catch (NullPointerException ex) {
                logger.debug("new row column added");
            } catch (Exception e) {
                e.printStackTrace();
            }

            long newValue = currentValue + entries.get(s);
            mutator = mutator.addInsertion(s, ruleName, HFactory.createColumn(columnName, newValue, StringSerializer.get(), LongSerializer.get()));
            try {
                long l = this.getValueInCell(ruleName, s, "flag");

            } catch (Exception e) {
                mutator.addInsertion(s, ruleName, HFactory.createColumn("flag", 1L, se, le));
            }

        }
        mutator.execute();

    }

    /**
     * To get the current count in the databasecolumn family
     *
     * @param tableName name of column family
     * @param rowID     KEY value of the row
     * @param columnID  column name
     * @return current count value in database
     * @throws Exception
     */
    public Long getValueInCell(String tableName, String rowID,
            String columnID) throws Exception {
        Keyspace keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

        ColumnQuery<String, String, Long> cq = HFactory.createColumnQuery(keyspace, StringSerializer.get(),
                StringSerializer.get(), LongSerializer.get());

        cq.setColumnFamily(tableName).setKey(rowID).setName(columnID);
        return cq.execute().get().getValue();
    }

    /**
     *
     * @param businessRule
     */

    public synchronized void updateDatabaseTables(Rule businessRule) {
        ArrayList<RecordMap> recordMaps = businessRule.getRecordMaps();
        for (int i = 0; i < recordMaps.size(); i++) {
            RecordMap record = recordMaps.remove(0);
            System.out.println();
            System.out.println(" ------------------------- ");
            System.out.println("this is the hashmap for db "+ record.getDataMap());
            System.out.println(" --------------------------- ");
            dataInserter(businessRule.getRuleName(), record.getType(), record.getDataMap());
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
