/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

//import DBUpdater.CounterCreater;
import com.uom.kanthaka.cassandra.updater.CassandraUpdater3;
import com.uom.kanthaka.preprocessor.CDRreader.Rule;
import com.uom.kanthaka.preprocessor.CDRreader.conditionField;
import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 *
 * @author Makumar
 */
public class CdrReadScheduledTask extends TimerTask {

    static String ruleUrl = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\Rules";
    static String cdrUrl = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\CDR";
    ArrayList<Rule> businessRules;
    CassandraUpdater3 couUpdater;

    public CdrReadScheduledTask(ArrayList<Rule> businessRules) {
        this.businessRules = businessRules;

//      ******************************************************************************************
//        couUpdater = new CassandraUpdater3();
//        couUpdater.createKeyspace();
//      ******************************************************************************************
    }

    @Override
    public void run() {

        File files[];
        files = new File(cdrUrl).listFiles();

        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = new File(files[i].toString());
                readCdr(file);
                file.delete();
                System.out.println("--- One File Done ----");
            }
        } else {
            System.out.println("--- No files ---");
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    public void readCdr(File file) {
        for (int i = 0; i < businessRules.size(); i++) {
            Rule businessRule = businessRules.get(i);

//      ******************************************************************************
//      creating tables with counter|4n # for sms,ussd(2 tables rule2smscount, rule2ussdcount
//            for (int j = 0; j < businessRule.getCounters().size(); j++) {
//                couUpdater.createTable(businessRule.getRuleName() + businessRule.getCounters().get(j) + "count");
//            }
//      ******************************************************************************
//      creating table for 4n#|current count(sms)|current count(ussd) -(1
//      table=rule2current_count
//            couUpdater.createTable(businessRule.getRuleName() + "current_count");
//      ******************************************************************************

//            System.out.println("Counter Condition Fields");
//            for (int j = 0; j < businessRule.getCounterConditionFields().size(); j++) {
//                ArrayList<counterConditionFields> temp = businessRule.getCounterConditionFields().get(j);
//                for (int k = 0; k < temp.size(); k++) {
//                    System.out.println(temp.get(k).printDetails());
//                }
//            }
            for (int j = 0; j < businessRule.getConditionFields().size(); j++) {
                ArrayList<conditionField> temp = businessRule.getConditionFields().get(j);
                for (int k = 0; k < temp.size(); k++) {
                    System.out.println(temp.get(k).printDetails());
                }
            }

            CdrRead cdr = new CdrRead(businessRule);
            cdr.readCdrFile(file);
            System.out.println("Rule Name : " + businessRule.getRuleName());
            Timer time = new Timer();                                               // Instantiate Timer Object
            time.schedule(cdr, 0, 5000);                                              // Create Repetitively task for every 1 secs

            ArrayList<RecordMap> mapList = businessRule.getRecordMaps();

//      ******************************************************************************************
//      Comment this part cz this it for testing the buffer
//      it will remove the element after read
//      oyage counterCreater.updateDatabaseTables(businessRule); method eke 
//      elements db ekata dala remove karana part eka tiyenawa
//      methendi remove unoth db ekata danna element eka nati wenawa

//      ******************************************************************************************
            while (mapList.size() > 0) {
                RecordMap record = mapList.remove(0);
                System.out.println(record.getType() + "  -  " + record.getDataMap());
            }
//      ******************************************************************************************


//      ******************************************************************************************
//            updateDatabaseTables(businessRule);
//            Timer time1 = new Timer();                                               // Instantiate Timer Object
//            time1.schedule(couUpdater, 0, 5000);                                              // Create Repetitively task for every 1 secs
//      ******************************************************************************************
            System.out.println("");
        }
    }

    public void updateDatabaseTables(Rule businessRule) {

        ArrayList<RecordMap> recordMaps = businessRule.getRecordMaps();

        for (int i = 0; i < recordMaps.size(); i++) {
            RecordMap record = recordMaps.remove(0);
            couUpdater.dataInserter(businessRule.getRuleName(), record.getType(), record.getDataMap());
        }
    }
}
