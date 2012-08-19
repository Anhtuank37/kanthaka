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
        ArrayList<Rule> businessRules = readRules.readFilesOnPath();
        // ArrayList<Rule> businessRules = readRules.readFilesOnPath(new
        // File(ruleUrl));

        Timer time = new Timer(); // Instantiate Timer Object
        CdrReadScheduledTask st = new CdrReadScheduledTask(businessRules); // Instantiate
        // SheduledTask
        // class
        time.schedule(st, 0, 5000); // Create Repetitively task for every 1 secs

        Timer cassandraUpdateTimer = new Timer(); // Instantiate Timer Object
        CassandraUpdater cassUpdater = st.getCassandraUpdater();
        cassandraUpdateTimer.schedule(cassUpdater, 0, 2000);

        Timer cassandraQueryTimer = new Timer(); // Instantiate Timer Object
        QueryRunner queryRun = new QueryRunner(businessRules);
        cassandraQueryTimer.schedule(queryRun, 0, 5000);
    }
}
