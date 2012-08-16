/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

//import DBUpdater.CounterCreater;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.log4j.Logger;

import com.uom.kanthaka.cassandra.updater.CassandraUpdater4;
import com.uom.kanthaka.preprocessor.CDRreader.Rule;
import com.uom.kanthaka.preprocessor.CDRreader.conditionField;

/**
 * 
 * @author Makumar
 */
public class CdrReadScheduledTask extends TimerTask {

  static String cdrUrl = "src/main/resources/CDR";
  // static String ruleUrl =
  // "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\Rules";
  // static String cdrUrl =
  // "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\CDR";
  static Logger _logger = Logger
      .getLogger(CdrReadScheduledTask.class.getName());
  ArrayList<Rule> businessRules;
  CassandraUpdater4 couUpdater;

  public CdrReadScheduledTask(ArrayList<Rule> businessRules) {
    this.businessRules = businessRules;

    // ******************************************************************************************
    couUpdater = new CassandraUpdater4(businessRules);
    couUpdater.createKeyspace();
    // ******************************************************************************************
  }

  @Override
  public void run() {

    File files[];
    files = new File(cdrUrl).listFiles();

    if (files.length > 0) {
      for (int i = 0; i < files.length; i++) {
        File file = new File(files[i].toString());
        readCdr(file);
        // file.delete();
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

      // ******************************************************************************
      // ******************************************************************************

      try {
        couUpdater.createTable(businessRule.getRuleName());
      } catch (Exception e) {
        _logger.error("columnfamily" + businessRule.getRuleName()
            + "not created");
        e.printStackTrace();

      }
      // creating tables with counter|4n # for sms,ussd(2 tables rule2smscount,
      // rule2ussdcount
      for (int j = 0; j < businessRule.getCounters().size(); j++) {
        couUpdater.indexColumn(businessRule.getCounters().get(j),
            businessRule.getRuleName(), "l");
      }

      couUpdater.indexColumn("flag", businessRule.getRuleName(), "l");
      couUpdater.indexColumn("phoneNumber", businessRule.getRuleName(), "s");
      // ******************************************************************************

      // System.out.println("Counter Condition Fields");
      // for (int j = 0; j < businessRule.getCounterConditionFields().size();
      // j++) {
      // ArrayList<counterConditionFields> temp =
      // businessRule.getCounterConditionFields().get(j);
      // for (int k = 0; k < temp.size(); k++) {
      // System.out.println(temp.get(k).printDetails());
      // }
      // }
      for (int j = 0; j < businessRule.getConditionFields().size(); j++) {
        ArrayList<conditionField> temp = businessRule.getConditionFields().get(
            j);
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

  public CassandraUpdater4 getCassandraUpdater() {
    return couUpdater;
  }
}
