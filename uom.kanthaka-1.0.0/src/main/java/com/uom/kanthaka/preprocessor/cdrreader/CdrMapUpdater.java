/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import java.util.ArrayList;
import java.util.TimerTask;

import com.uom.kanthaka.preprocessor.rulereader.Rule;

/**
 * 
 * @author Makumar
 */
public class CdrMapUpdater extends TimerTask {
  ArrayList<Rule> businessRules;

  public CdrMapUpdater(ArrayList<Rule> businessRules) {
    this.businessRules = businessRules;
  }

  /**
   * Update record maps of the business rule
   */
  public synchronized void updateRuleMaps() {
    for (int i = 0; i < businessRules.size(); i++) {
      Rule tempRule = businessRules.get(i);
      ArrayList<RecordMap> tempMaps = tempRule.getTempEntries().getMaps();

      while (tempMaps.size() > 0) {
        RecordMap tempRec = tempMaps.remove(0);
        // System.out.println("Updating Maps .....");
        // System.out.println(tempRec.getType() + "  -  " +
        // tempRec.getDataMap());
        // System.out.println("Updated .....");
        tempRule.getRecordMaps().add(
            new RecordMap(tempRec.getType(), tempRec.getDataMap()));
        // tempRec.initilizeMap();
      }
    }
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  @Override
  public void run() {
    updateRuleMaps();
    Thread.yield();
  }
}
