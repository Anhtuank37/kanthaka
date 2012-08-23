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
import java.io.BufferedReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

/**
 * @author Makumar
 */
public class CdrReadScheduledTask extends TimerTask {

    ArrayList<Rule> businessRules;
    CassandraUpdater couUpdater;
    TableCreater tableCreater;
    CdrRead cdr;
    final Logger logger = LoggerFactory.getLogger(CdrReadScheduledTask.class);

    /**
     * Constructor of CdrReadScheduledTask
     * @param businessRules
     * @return
     */
    public CdrReadScheduledTask(ArrayList<Rule> businessRules) {
        this.businessRules = businessRules;
        cdr = new CdrRead(businessRules);
    }

    /**
     * run method for the class
     */
    @Override
    public void run() {

        File files[];
        files = new File(Constant.CDR_URL).listFiles();

        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = new File(files[i].toString());
                readCdr(file);
                // file.delete();
//                  System.out.println("--- One File Done ----");
//                  System.out.println("");
                logger.info("--- One File Done ----");
            }
        } else {
            // System.out.println("--- No files ---");
            logger.info("--- No files ---");
        }

        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Read CDR files according to the given business rules
     * @param file
     */
    public void readCdr(File file) {
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(file));
            String tempRec;
            
            while ((tempRec = bufReader.readLine()) != null) {
                cdr.readCdrFile(tempRec);
            }
            printRuleRecordMaps();
            bufReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void printRuleRecordMaps() {
        for (int i = 0; i < businessRules.size(); i++) {
            Rule businessRule = businessRules.get(i);
//            System.out.println("Rule Name : " + businessRule.getRuleName());
            logger.debug("Rule Name : {}.", businessRule.getRuleName());

            for (int j = 0; j < businessRule.getConditionFields().size(); j++) {
                ArrayList<ConditionField> temp = businessRule.getConditionFields().get(j);
                for (int k = 0; k < temp.size(); k++) {
//                    System.out.println(temp.get(k).printDetails());
                    logger.debug("Checking Conditions : {}.", temp.get(k).printDetails());
                }
            }
            ArrayList<RecordMap> mapList = businessRule.getRecordMaps();
            for (int j = 0; j < mapList.size(); j++) {
                RecordMap record = mapList.get(j);
//                 System.out.println(record.getType() + "  -  " + record.getDataMap());
                logger.debug("{}. - {}.", record.getType(), record.getDataMap());
            }
//            System.out.println("");
            logger.debug("");
        }
    }
    
    public CdrRead getCdrRead() {
        return cdr;
    }
    
    
}
