/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.preprocessor;

import java.util.ArrayList;
import java.util.Timer;

import org.apache.commons.pool.impl.StackObjectPool;

import com.uom.kanthaka.cassandra.ruleexecuter.QueryRunner;
import com.uom.kanthaka.preprocessor.cdrreader.CdrRead;
import com.uom.kanthaka.preprocessor.cdrreader.CdrReadFactory;
import com.uom.kanthaka.preprocessor.cdrreader.CdrReadScheduledTask;
import com.uom.kanthaka.preprocessor.rulereader.ReadBusinessRule;
import com.uom.kanthaka.preprocessor.rulereader.Rule;

/**
 * @author Makumar
 */
public class Main {

  public static void main(String args[]) throws InterruptedException {

    ReadBusinessRule readRules = new ReadBusinessRule();
    ArrayList<Rule> businessRules = readRules.readRulesFromDatabase();

    Timer cdrReadingTimer = new Timer(); // Instantiate Timer Object
    CdrReadScheduledTask scheduleTask = new CdrReadScheduledTask(
        new StackObjectPool<CdrRead>(new CdrReadFactory(businessRules))); // Instantiate
                                                                          // SheduledTask
                                                                          // class
    cdrReadingTimer.schedule(scheduleTask, 0, 15000); // Create Repetitively
                                                      // task for every 15 secs

    Timer cassandraUpdateTimer = new Timer(); // Instantiate Timer Object
    cassandraUpdateTimer.schedule(readRules.getCassandraUpdater(), 5000, 5000);

    Timer cassandraQueryTimer = new Timer(); // Instantiate Timer Object
    QueryRunner queryRun = new QueryRunner(businessRules);
    cassandraQueryTimer.schedule(queryRun, 5020, 5020);
  }
}
