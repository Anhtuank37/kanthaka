/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.CDRreader;

//import DBUpdater.CounterCreater;
import com.uom.kanthaka.preprocessor.rulereader.CdrRead;
import com.uom.kanthaka.preprocessor.rulereader.RecordMap;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author Makumar
 */
public class BusinessRuleReadMain {

    static String ruleUrl = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\Rules";
    static String cdrUrl = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\CDR";

    public static void main(String[] args) {
        readBusinessRule readRules = new readBusinessRule();
        ArrayList<Rule> businessRules = readRules.readFilesOnPath();

//        CounterCreater counterCreater = new CounterCreater();
//        counterCreater.createKeyspace();

        for (int i = 0; i < businessRules.size(); i++) {
            Rule temp = businessRules.get(i);
//            temp.createCounterMaps(temp.getCounters());
            System.out.println("");
            System.out.println("Rule Name : " + temp.getRuleName());
//            System.out.println("Counters : " + temp.getCounters());
            System.out.println("");

            ////////////////////////////////////
//            counterCreater.createCounters(temp);
            ///////////////////////////////////

            CdrRead cdr = new CdrRead(temp);
            cdr.readCdrOnPath(new File(cdrUrl));
//            cdr.updateRuleMaps();

//            String[] list = {};
//            list = temp.getRuleMaps().keySet().toArray(list);
//            for (int j = 0; j < list.length; j++) {
//                String key = list[j];
//                System.out.println(key + "  -  " + temp.getRuleMaps().get(key));
//            }

            ArrayList<RecordMap> recordList = temp.getRecordMaps();
            while (recordList.size() > 0) {
                RecordMap key = recordList.remove(0);
                System.out.println(key.getType() + "  -  " + key.getDataMap());
            }

            System.out.println("");
            System.out.println("CDR Reading Elements : " + temp.getCdrReadingFields());

            ///////////////////////////////////
//            counterCreater.updateDatabaseTables(temp);
            //////////////////////////////////
        }
    }
}
