/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor;

import com.uom.kanthaka.cassandra.ruleexecuter.QueryRunner;
import com.uom.kanthaka.cassandra.updater.CassandraUpdater;
import com.uom.kanthaka.preprocessor.cdrreader.CdrReadScheduledTask;
import com.uom.kanthaka.preprocessor.rulereader.ReadBusinessRule;
import com.uom.kanthaka.preprocessor.rulereader.Rule;

import java.util.ArrayList;
import java.util.Timer;

/**
 * @author Makumar
 */
public class Main {

    public static void main(String args[]) throws InterruptedException {

        ReadBusinessRule readRules = new ReadBusinessRule();
        ArrayList<Rule> businessRules = readRules.readRulesFromDatabase();
        
        Timer cdrReadingTimer = new Timer(); // Instantiate Timer Object
        CdrReadScheduledTask scheduleTask = new CdrReadScheduledTask(businessRules); // Instantiate SheduledTask class
        cdrReadingTimer.schedule(scheduleTask, 0, 5000); // Create Repetitively task for every 5 secs

        Timer mapUpdatingTimer = new Timer(); // Instantiate Timer Object
        mapUpdatingTimer.schedule(scheduleTask.getCdrRead(), 1000, 2000); // Create Repetitively task for every 1 secs


        Timer cassandraUpdateTimer = new Timer(); // Instantiate Timer Object
        cassandraUpdateTimer.schedule(readRules.getCassandraUpdater(), 1500, 2000);

        Timer cassandraQueryTimer = new Timer(); // Instantiate Timer Object
        QueryRunner queryRun = new QueryRunner(businessRules);
        cassandraQueryTimer.schedule(queryRun, 4000, 5000);
    }
}
