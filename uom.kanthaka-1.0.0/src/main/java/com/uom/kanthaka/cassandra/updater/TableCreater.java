package com.uom.kanthaka.cassandra.updater;

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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/19/12
 * Time: 2:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableCreater {

    private Keyspace keyspace;
    private Cluster cluster = null;
    private static final StringSerializer se = new StringSerializer();
    private static final LongSerializer le = new LongSerializer();
    static Logger logger = LoggerFactory.getLogger(TableCreater.class.getName());

    /**
     *
     */

    public TableCreater() {
        this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
                BasicConf.CLUSTER_PORT);
    }

    /**
     * to create the keyspace for the application
     */
    public void createKeyspace() {

        KeyspaceDefinition newKeyspaceDef = HFactory.createKeyspaceDefinition(BasicConf.KEYSPACE);
        if ((cluster.describeKeyspace(BasicConf.KEYSPACE)) == null) {
            cluster.addKeyspace(newKeyspaceDef, true);
        }
        keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);
    }

    /**
     * to create column families
     *
     * @param tableName
     * @throws Exception
     */
    public void createTable(String tableName) throws Exception {
        Cluster cluster = HFactory.getOrCreateCluster(
                BasicConf.CASSANDRA_CLUSTER, BasicConf.CLUSTER_PORT);

        ColumnFamilyDefinition cfDef = HFactory.createColumnFamilyDefinition(
                BasicConf.KEYSPACE, tableName, ComparatorType.UTF8TYPE);
        cfDef.setKeyValidationClass("org.apache.cassandra.db.marshal.UTF8Type");


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
            System.out.println("column Family" + tableName + "created");
        }
    }

    /**
     * create secondary indexes for columns
     *
     * @param idxColumnName
     * @param cfName
     */
    public void indexColumn(String idxColumnName, String cfName) {

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
        bcdf.setIndexName(cfName + idxColumnName + "index");
        bcdf.setIndexType(ColumnIndexType.KEYS);


        bcdf.setValidationClass(ComparatorType.LONGTYPE.getClassName());


        columnFamilyDefinition.addColumnDefinition(bcdf);
        cluster.updateColumnFamily(new ThriftCfDef(columnFamilyDefinition));


    }
}
