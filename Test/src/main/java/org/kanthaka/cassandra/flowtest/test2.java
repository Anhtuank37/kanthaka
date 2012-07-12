package org.kanthaka.cassandra.flowtest;

public class test2 {

	public static void main(String[] args) throws Exception {
		String query = "SELECT 'sourceAdd' from 'Events' where desAdd='91222222222' AND desChannelType='sms' AND transStatus='success' AND billingType='prepaid'";
		TableCreater tableCreater = new TableCreater();
		tableCreater.createTable(query);
		SStableCreater sStableCreater=new SStableCreater(tableCreater);
		sStableCreater.createSStables("/home/amila/FYP/cdr_001.csv");
		String[] s ={"/home/amila/workspace/CASFF/CDRss/Eventss"};
//		BulkDataLoader bulkDataLoader = new BulkDataLoader("localhost", 7199);
		JmxBulkLoader jmxBulkLoader=new JmxBulkLoader("localhost", 7199);
	//	bulkDataLoader.runLoader(s,bulkDataLoader);
		jmxBulkLoader.main(s,jmxBulkLoader);

	}
}
