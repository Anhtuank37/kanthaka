package com.uom.kanthaka.cassandra.ruleExecuter;

import com.uom.kanthaka.cassandra.updater.BasicConf;
import com.uom.kanthaka.preprocessor.CDRreader.Rule;
import com.uom.kanthaka.preprocessor.CDRreader.counterConditionFields;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class QueryRunner {

	
	
	
	 //private ArrayList<ArrayList<counterConditionFields>> counterConditionFields;
	
	HashSet<String> queryCompiler(String query){
		
		
		
		Cluster cluster = HFactory.getOrCreateCluster(
				BasicConf.CASSANDRA_CLUSTER, BasicConf.CLUSTER_PORT);
		Keyspace keyspace = HFactory
				.createKeyspace(BasicConf.KEYSPACE, cluster);

		CqlQuery<String, String, Long> cqlQuery = new CqlQuery<String, String, Long>(
				HFactory.createKeyspace(BasicConf.KEYSPACE, cluster), StringSerializer.get(), StringSerializer.get(),LongSerializer.get());
		
		HashSet<String> returnSet=new HashSet<String>();
	//	cqlQuery.setQuery("SELECT * FROM rule01 WHERE No_of_SMSs=32");
		cqlQuery.setQuery(query);
	
		QueryResult<CqlRows<String, String, Long>> result = cqlQuery
				.execute();
		if (result != null && result.get() != null) {
			List<Row<String, String, Long>> list = result.get().getList();
			for (Row row : list) {
				System.out.println("");
				List columns = row.getColumnSlice().getColumns();
				for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
					HColumn column = (HColumn) iterator.next();
					
//					if(column.getName().equals("No_of_SMSs")){
//						
//						System.out.print(column.getName() + ":" + column.getValueBytes().getLong()
//								+ "\t");
//					}else
					System.out.print(column.getName() + ":" + column.getValue()
							+ "\t");
                    String s=(String)column.getName();
                    if(!"KEY".equals(s)){
                        returnSet.add(s);
                    }

			}
			System.out.println("");
			}
		}
		
		return returnSet;
	}
	
	// to run all the queries related to a rule and return a list of eligible subscribers
	void runRuleQueries(Rule rule){
		ArrayList<ArrayList<counterConditionFields>> rulesSet=rule.getCounterConditionFields();
		ArrayList<ArrayList<HashSet<String>>> ANDlist=new ArrayList<ArrayList<HashSet<String>>>();
		
		for(ArrayList<counterConditionFields> list:rulesSet){
			ArrayList<HashSet<String>> ORlist=new ArrayList<HashSet<String>>();
			for(counterConditionFields counterConditionFields:list){
				String query=this.createQuery(counterConditionFields, rule.getRuleName());
				HashSet<String> resultset=queryCompiler(query);
				ORlist.add(resultset);
				
			}
			ANDlist.add(ORlist);
		}
		
		processResultSet processResultSet=new processResultSet();
		processResultSet.compareResultSet(ANDlist);
	}
	
	String createQuery(counterConditionFields c,String ruleName){
		
		String query="SELECT * FROM "+ ruleName+c.getConditionName()+"count WHERE KEY" +c.getCondition()+c.getValue();
		return query;
	}
	public static void main(String[] args) {
		
		//(a|b)
		counterConditionFields conditionFieldsOr1=new counterConditionFields("No_of_SMSs", ">", 5L);
		counterConditionFields conditionFieldsOr2=new counterConditionFields("No_of_Calls", ">", 5L);
		ArrayList<counterConditionFields> conditionFieldsOR1Arr= new ArrayList<counterConditionFields>();// a|b
		conditionFieldsOR1Arr.add(conditionFieldsOr1);
		conditionFieldsOR1Arr.add(conditionFieldsOr2);
		
		counterConditionFields conditionFieldsOr11=new counterConditionFields("No_of_SMSs", ">", 3L);
		ArrayList<counterConditionFields> conditionFieldsOR2Arr= new ArrayList<counterConditionFields>();//(a)
		conditionFieldsOR2Arr.add(conditionFieldsOr11);
		
		//(a|b)&(a)
		ArrayList<ArrayList<counterConditionFields>> counterConditionFd=new ArrayList<ArrayList<counterConditionFields>>();
		counterConditionFd.add(conditionFieldsOR1Arr);
		counterConditionFd.add(conditionFieldsOR2Arr);
		
		Rule rule=  new Rule();
		rule.setcounterConditionFields(counterConditionFd);
		rule.setRuleName("rule02");
		
		QueryRunner queryRunner=new QueryRunner();
		queryRunner.runRuleQueries(rule);
		
		
		
		
	}
	
}
