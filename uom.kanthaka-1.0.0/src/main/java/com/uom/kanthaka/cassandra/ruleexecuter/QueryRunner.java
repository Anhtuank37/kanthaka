/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.cassandra.ruleexecuter;

import java.util.ArrayList;
import java.util.TimerTask;

import com.uom.kanthaka.preprocessor.rulereader.Rule;

public class QueryRunner extends TimerTask {
  ArrayList<Rule> businessRules;

  public QueryRunner(ArrayList<Rule> businessRules) {
    this.businessRules = businessRules;
  }

  public ArrayList<Rule> getBusinessRules() {
    return businessRules;
  }

  @Override
  public void run() {
    for (int i = 0; i < getBusinessRules().size(); i++) {
      Rule tempRule = getBusinessRules().get(i);
      QueryRunUtill queryRun = new QueryRunUtill(tempRule);
      queryRun.start();
    }

    Thread.yield();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace(); // To change body of catch statement use File |
                           // Settings | File Templates.
    }

  }
}
