package com.uom.kanthaka.preprocessor;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;

import com.uom.kanthaka.preprocessor.CDRreader.Rule;
import com.uom.kanthaka.preprocessor.CDRreader.readBusinessRule;
import com.uom.kanthaka.preprocessor.rulereader.CdrReadScheduledTask;

/**
 *
 * @author Makumar
 */


public class Main {

    static String ruleUrl = "/resources/Rules";

    public static void main(String args[]) throws InterruptedException {

        readBusinessRule readRules = new readBusinessRule();
        ArrayList<Rule> businessRules = readRules.readFilesOnPath(new File(ruleUrl));

        Timer time = new Timer();                                              // Instantiate Timer Object
        CdrReadScheduledTask st = new CdrReadScheduledTask(businessRules);     // Instantiate SheduledTask class
        time.schedule(st, 0, 5000);                                            // Create Repetitively task for every 1 secs

    }
}
