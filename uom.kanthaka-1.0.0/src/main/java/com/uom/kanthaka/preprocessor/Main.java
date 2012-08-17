/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor;

import java.util.ArrayList;
import java.util.Timer;

import com.uom.kanthaka.cassandra.ruleExecuter.QueryRunner2;
import com.uom.kanthaka.cassandra.updater.CassandraUpdater4;
import com.uom.kanthaka.preprocessor.cdrreader.CdrReadScheduledTask;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import com.uom.kanthaka.preprocessor.rulereader.readBusinessRule;

/**
 * 
 * @author Makumar
 */

public class Main {

  public static void main(String args[]) throws InterruptedException {

    readBusinessRule readRules = new readBusinessRule();
    ArrayList<Rule> businessRules = readRules.readFilesOnPath();
    // ArrayList<Rule> businessRules = readRules.readFilesOnPath(new
    // File(ruleUrl));

    Timer time = new Timer(); // Instantiate Timer Object
    CdrReadScheduledTask st = new CdrReadScheduledTask(businessRules); // Instantiate
                                                                       // SheduledTask
                                                                       // class
    time.schedule(st, 0, 5000); // Create Repetitively task for every 1 secs

    Timer cassandraUpdateTimer = new Timer(); // Instantiate Timer Object
    CassandraUpdater4 cassUpdater = st.getCassandraUpdater();
    cassandraUpdateTimer.schedule(cassUpdater, 0, 2000);

    Timer cassandraQueryTimer = new Timer(); // Instantiate Timer Object
    QueryRunner2 queryRun = new QueryRunner2(businessRules);
    cassandraQueryTimer.schedule(queryRun, 0, 5000);
  }
}
