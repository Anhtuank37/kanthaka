package org.kanthaka.cassandra.flowtest;



import java.util.ArrayList;
import java.util.Arrays;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.ThriftKsDef;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.ColumnSlice;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.ddl.ColumnFamilyDefinition;
import me.prettyprint.hector.api.ddl.ComparatorType;
import me.prettyprint.hector.api.ddl.KeyspaceDefinition;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.query.QueryResult;
import me.prettyprint.hector.api.query.RangeSlicesQuery;
import me.prettyprint.hector.api.query.SliceQuery;

public class TableCreater {

	private String keyspaceName;
	private String columnfamilyName;
	ArrayList<String> fields;

	private Cluster cluster;
	private ColumnFamilyDefinition columnFamilyDefinition;
	private KeyspaceDefinition newKeyspace;
	private Keyspace keyspace;

	void createTable(String query) {

		this.ReadQuery(query);

		cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
				BasicConf.CLUSTER_PORT);
		columnFamilyDefinition = HFactory.createColumnFamilyDefinition(
				this.keyspaceName, this.columnfamilyName,
				ComparatorType.BYTESTYPE);
		if (cluster.describeKeyspace(this.keyspaceName) == null) {
			newKeyspace = HFactory.createKeyspaceDefinition(this.keyspaceName,
					ThriftKsDef.DEF_STRATEGY_CLASS, 1,
					Arrays.asList(columnFamilyDefinition));
			cluster.addKeyspace(newKeyspace, true);
		}
		keyspace = HFactory.createKeyspace(this.keyspaceName, cluster);

	}

	void ReadQuery(String query) {

		keyspaceName = "CDRss";
		columnfamilyName = "Eventss";
		fields = new ArrayList<String>();
		fields.add("sourceAdd");
		fields.add("desAdd");
		fields.add("desChannelType");
		fields.add("billingType");
		fields.add("transStatus");

	}

	 public void createWorkflowIterator(String workflowID,String timestamp,String status) {

	        Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());

	        mutator.addInsertion(status,this.columnfamilyName,
	                     HFactory.createStringColumn(timestamp+"_"+(int)(Math.random() * 9999), workflowID));
	        mutator.execute();


	    }
	public String getKeyspaceName() {

		return keyspaceName;
	}

	public String getColumnfamilyName() {

		return columnfamilyName;
	}
}
