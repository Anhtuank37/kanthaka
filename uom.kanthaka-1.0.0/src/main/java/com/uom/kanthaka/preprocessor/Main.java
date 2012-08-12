/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.uom.kanthaka.preprocessor;

import java.util.ArrayList;
import java.util.Timer;
import main.java.com.uom.kanthaka.preprocessor.CDRreader.Rule;
import main.java.com.uom.kanthaka.preprocessor.CDRreader.readBusinessRule;
import main.java.com.uom.kanthaka.preprocessor.rulereader.CdrReadScheduledTask;

/**
 *
 * @author Makumar
 */

/**
     * 
     * @param 
     * @param 
     * @return
     */
public class Main {

    public static void main(String args[]) throws InterruptedException {

        readBusinessRule readRules = new readBusinessRule();
        ArrayList<Rule> businessRules = readRules.readFilesOnPath();
//        ArrayList<Rule> businessRules = readRules.readFilesOnPath(new File(ruleUrl));

        Timer time = new Timer();                                               // Instantiate Timer Object
        CdrReadScheduledTask st = new CdrReadScheduledTask(businessRules);     // Instantiate SheduledTask class
        time.schedule(st, 0, 5000);                                              // Create Repetitively task for every 1 secs

    }
}
