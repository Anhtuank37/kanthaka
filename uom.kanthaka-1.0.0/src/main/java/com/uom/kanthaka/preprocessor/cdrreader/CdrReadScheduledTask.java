/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

//import DBUpdater.CounterCreater;

import com.uom.kanthaka.cassandra.updater.CassandraUpdater;
import com.uom.kanthaka.cassandra.updater.TableCreater;
import com.uom.kanthaka.preprocessor.Constant;
import com.uom.kanthaka.preprocessor.rulereader.ConditionField;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Makumar
 */
public class CdrReadScheduledTask extends TimerTask {

    static String cdrUrl = "src/main/resources/CDR";
    ArrayList<Rule> businessRules;
    CassandraUpdater couUpdater;
    TableCreater tableCreater;
    final Logger logger = LoggerFactory.getLogger(CdrReadScheduledTask.class);


    public CdrReadScheduledTask(ArrayList<Rule> businessRules) {
        this.businessRules = businessRules;

        // ******************************************************************************************
        couUpdater = new CassandraUpdater(businessRules);

        tableCreater = new TableCreater(businessRules);
        tableCreater.createKeyspace();


        // ******************************************************************************************
    }

    @Override
    public void run() {

        File files[];
        files = new File(Constant.CDR_URL).listFiles();

        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = new File(files[i].toString());
                readCdr(file);
                // file.delete();
                logger.info("--- One File Done ----");
            }
        } else {
            logger.info("--- No files ---");
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

            // ******************************************************************************
            // ******************************************************************************

            try {
                //   couUpdater.createTable(businessRule.getRuleName());
                tableCreater.createTable(businessRule.getRuleName());
            } catch (Exception e) {
                logger.error("columnfamily" + businessRule.getRuleName()
                        + "not created");
                e.printStackTrace();

            }
            for (int j = 0; j < businessRule.getCounters().size(); j++) {
                tableCreater.indexColumn(businessRule.getCounters().get(j),
                        businessRule.getRuleName());
            }

            tableCreater.indexColumn("flag", businessRule.getRuleName());

            // ******************************************************************************


            for (int j = 0; j < businessRule.getConditionFields().size(); j++) {
                ArrayList<ConditionField> temp = businessRule.getConditionFields().get(j);
                for (int k = 0; k < temp.size(); k++) {
                    System.out.println(temp.get(k).printDetails());
                }
            }

            CdrRead cdr = new CdrRead(businessRule);
            cdr.readCdrFile(file);
            System.out.println("Rule Name : " + businessRule.getRuleName());
            Timer time = new Timer(); // Instantiate Timer Object
            time.schedule(cdr, 0, 5000); // Create Repetitively task for every 1 secs

            // ******************************************************************************************
            ArrayList<RecordMap> mapList = businessRule.getRecordMaps();
            for (int j = 0; j < mapList.size(); j++) {
                RecordMap record = mapList.get(j);
                System.out.println(record.getType() + "  -  " + record.getDataMap());
            }
            // ******************************************************************************************

            // ******************************************************************************************
            // updateDatabaseTables(businessRule);
            // Timer time1 = new Timer(); // Instantiate Timer Object
            // time1.schedule(couUpdater, 0, 5000); // Create Repetitively task for
            // every 1 secs
            // ******************************************************************************************
            System.out.println("");
        }
    }

    // public void updateDatabaseTables(Rule businessRule) {
    //
    // ArrayList<RecordMap> recordMaps = businessRule.getRecordMaps();
    //
    // for (int i = 0; i < recordMaps.size(); i++) {
    // RecordMap record = recordMaps.remove(0);
    // couUpdater.dataInserter(businessRule.getRuleName(), record.getType(),
    // record.getDataMap());
    // }
    // }

    public CassandraUpdater getCassandraUpdater() {
        return couUpdater;
    }
}
