package com.uom.kanthaka.cassandra.ruleExecuter;

import com.uom.kanthaka.cassandra.updater.BasicConf;
import com.uom.kanthaka.preprocessor.CDRreader.Rule;
import com.uom.kanthaka.preprocessor.CDRreader.counterConditionFields;
import me.prettyprint.cassandra.model.IndexedSlicesQuery;
import me.prettyprint.cassandra.serializers.LongSerializer;
import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.beans.OrderedRows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.query.QueryResult;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import static me.prettyprint.hector.api.factory.HFactory.createKeyspace;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/11/12
 * Time: 1:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class QueryDB {
    private Keyspace keyspace;
    private Cluster cluster = null;
    private final static String KEYSPACE = "Keyspace1";
    private static final StringSerializer se = new StringSerializer();
    private static final LongSerializer le = new LongSerializer();


    public QueryDB() {

        this.cluster = HFactory.getOrCreateCluster(BasicConf.CASSANDRA_CLUSTER,
                BasicConf.CLUSTER_PORT);
        keyspace = createKeyspace(KEYSPACE, cluster);
    }

    ArrayList<HashSet<Long>> queryCreater(Rule rule){

        ArrayList<ArrayList<counterConditionFields>> rulesSet = rule.getCounterConditionFields();//OR((and list))
        ArrayList<HashSet<Long>> ORlist = rule.getDoORresultset();

        for (ArrayList<counterConditionFields> list : rulesSet) {
            IndexedSlicesQuery<String, String, Long> ANDquery= new IndexedSlicesQuery<String, String, Long>(keyspace, se, se, le);
          //  IndexedSlicesQuery<String, String, String> indexedSlicesQuery = HFactory.createIndexedSlicesQuery(keyspace, se, se, se);
            for (com.uom.kanthaka.preprocessor.CDRreader.counterConditionFields counterConditionFields : list) {
                // take ands between each record
                String conditionName=counterConditionFields.getConditionName();
                String condition=counterConditionFields.getCondition();
                long value=counterConditionFields.getValue();

                 if(condition.equals("<") ){
                     ANDquery.addLteExpression(conditionName,value);
                 }else if(condition.equals(">")){
                     ANDquery.addGteExpression(conditionName,value);
                 }else if(condition.equals("=")){
                     ANDquery.addEqualsExpression(conditionName,value) ;
                 }

            }
            ANDquery.addEqualsExpression("flag",1L);
            ANDquery.setColumnFamily(rule.getRuleName());
            ANDquery.setColumnNames("phoneNumber")  ;
            ANDquery.setStartKey("");
            QueryResult<OrderedRows<String, String, Long>> result=ANDquery.execute();
             HashSet<Long> doORset= this.printResult(result);
            System.out.println(doORset);
            ORlist.add(doORset);

        }
        this.getSelected(ORlist);
        return ORlist;
    }


    HashSet<Long> printResult(QueryResult<OrderedRows<String, String, Long>> result){

        HashSet<Long> returnSet = new HashSet<Long>();
        if (result != null && result.get() != null) {
            List<Row<String, String, Long>> list = result.get().getList();
            for (Row row : list) {
                System.out.println(".");
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
                    if("phoneNumber".equals(s)){
                        returnSet.add(column.getValueBytes().getLong());
                       // System.out.print(column.getValue());
                        //System.out.println(column.getValueBytes().getLong());
                    }
                }
                System.out.println("");
            }
        }

        return returnSet;
    }

     HashSet<Long> getSelected(ArrayList<HashSet<Long>> list){

         HashSet<Long> selectedList=new HashSet<Long>();
          for(HashSet<Long> hs:list){
              for(Long l:hs){
                  selectedList.add(l);
              }

          }
         System.out.println(selectedList);
         return selectedList;
     }

    public static void main(String[] args) {

        //(a&b)
        counterConditionFields conditionFieldsAND1 = new counterConditionFields("No_of_SMSs", ">", 2L);
        counterConditionFields conditionFieldsAND2 = new counterConditionFields("No_of_Calls", "<", 4L);
        ArrayList<counterConditionFields> conditionFieldsAND1Arr = new ArrayList<counterConditionFields>();// a|b
        conditionFieldsAND1Arr.add(conditionFieldsAND1);
        conditionFieldsAND1Arr.add(conditionFieldsAND2);

        counterConditionFields conditionFieldsAND11 = new counterConditionFields("No_of_SMSs", ">", 4L);
        ArrayList<counterConditionFields> conditionFieldsAND2Arr = new ArrayList<counterConditionFields>();//(a)
        conditionFieldsAND2Arr.add(conditionFieldsAND11);

        //(a|b)&(a)
        ArrayList<ArrayList<counterConditionFields>> counterConditionFd = new ArrayList<ArrayList<counterConditionFields>>();
        counterConditionFd.add(conditionFieldsAND1Arr);
        counterConditionFd.add(conditionFieldsAND2Arr);

        Rule rule = new Rule();
        rule.setcounterConditionFields(counterConditionFd);
        rule.setRuleName("rule02");

        QueryDB q =new QueryDB() ;
        q.queryCreater(rule);


    }

}
