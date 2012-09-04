package com.uom.kanthaka.cassandra.updater;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uom.kanthaka.preprocessor.rulereader.Rule;

import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.factory.HFactory;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/28/12
 * Time: 6:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class CassandraUpdaterTest {

    private CassandraUpdater cassandraUpdater;
    private  TableCreater tableCreater;
    private Cluster cluster;
    private Keyspace keyspace;
    ArrayList<Rule> businessRules;
    String ruleName="testRule";
    String columnName="testColumn";
    String rowID="0779747398";
    @Before
    public void setUp() throws Exception {

        cluster= HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
                BasicConf.CLUSTER_PORT);
        keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

        // creating business rules
        Rule rule=new Rule();
        rule.setRuleName(ruleName);
        businessRules.add(rule);
        cassandraUpdater=new CassandraUpdater(businessRules);
        tableCreater=new TableCreater();
        tableCreater.createTable(ruleName);
        tableCreater.indexColumn(columnName, ruleName);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetValueInCell() throws Exception {
        long expected=200L;
        this.testdataInserter();
        long getValue=cassandraUpdater.getValueInCell(ruleName,rowID,columnName);
        assertEquals(expected, getValue);

    }

    @Test
    public void testUpdateDatabaseTables() throws Exception {


    }

    @Test
    public void testdataInserter(){

        ConcurrentHashMap<String, Long> entries=new ConcurrentHashMap<String, Long>();
        entries.put(rowID,200L);
//        cassandraUpdater.dataInserter(ruleName, columnName, entries);


    }
    @Test
    public void testRun() throws Exception {

    }


    @Test
    public void testCancel() throws Exception {

    }

    @Test
    public void testScheduledExecutionTime() throws Exception {

    }
}
