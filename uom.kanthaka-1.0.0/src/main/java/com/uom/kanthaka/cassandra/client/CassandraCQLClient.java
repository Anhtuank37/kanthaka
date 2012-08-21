package com.uom.kanthaka.cassandra.client;

import java.util.Iterator;
import java.util.List;

//import org.apache.log4j.Logger;

import com.uom.kanthaka.cassandra.updater.BasicConf;

import me.prettyprint.cassandra.model.CqlQuery;
import me.prettyprint.cassandra.model.CqlRows;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.HCounterColumn;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.CounterQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.SliceQuery;

public class CassandraCQLClient {

//  static Logger _logger = Logger.getLogger(CassandraCQLClient.class.getName());

  private final static StringSerializer se = StringSerializer.get();

  public static void main(String[] args) {

    Cluster cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
    Keyspace keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

    CqlQuery<String, String, Long> cqlQuery = new CqlQuery<String, String, Long>(
        HFactory.createKeyspace(BasicConf.KEYSPACE, cluster), se, se,
        LongSerializer.get());

    // cqlQuery.setQuery("SELECT * FROM rule01 WHERE No_of_SMSs=32");
    cqlQuery.setQuery("select * from rule2ussdcount where KEY IN (6,8)");

    QueryResult<CqlRows<String, String, Long>> result = cqlQuery.execute();
    if (result != null && result.get() != null) {
      List<Row<String, String, Long>> list = result.get().getList();
      for (Row row : list) {
        System.out.println("");
        List columns = row.getColumnSlice().getColumns();
        for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
          HColumn column = (HColumn) iterator.next();

          // if(column.getName().equals("No_of_SMSs")){
          //
          // System.out.print(column.getName() + ":" +
          // column.getValueBytes().getLong()
          // + "\t");
          // }else
          System.out.print(column.getName() + ":" + column.getValue() + "\t");
        }
        System.out.println("");
      }
    }
  }

  public String getLastColumnValue(String columnFamily, String rowKey) {
    Cluster cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
    Keyspace keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);
    String lastColumnValue = null;

    SliceQuery<String, String, String> query = HFactory.createSliceQuery(
        keyspace, StringSerializer.get(), StringSerializer.get(),
        StringSerializer.get());
    query.setColumnFamily(columnFamily).setKey(rowKey)
        .setRange("", "", true, 1);
    QueryResult<ColumnSlice<String, String>> result = query.execute();
    for (HColumn<String, String> column : result.get().getColumns()) {
      lastColumnValue = column.getValue();
    }

    return lastColumnValue;

  }

  void Hcl() {
    Cluster cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
        BasicConf.CLUSTER_PORT);
    Keyspace keyspace = HFactory.createKeyspace(BasicConf.KEYSPACE, cluster);

    CounterQuery<String, String> query = HFactory.createCounterColumnQuery(
        keyspace, se, se);
    query.setColumnFamily("rule01").setKey("localhost").setName("home");
    HCounterColumn<String> counter = query.execute().get();
    System.out.println("Count:" + counter.getValue());
  }
}
