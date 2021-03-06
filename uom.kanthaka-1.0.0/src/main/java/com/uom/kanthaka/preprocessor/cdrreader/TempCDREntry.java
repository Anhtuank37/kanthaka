/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import java.util.ArrayList;

import com.uom.kanthaka.preprocessor.Constant;

/**
 * 
 * @author Makumar
 */
public class TempCDREntry {
  ArrayList<RecordMap> maps;
  RecordMap callMap;
  RecordMap smsMap;

  public TempCDREntry() {
    this.maps = new ArrayList<RecordMap>();
    callMap = new RecordMap(Constant.CallCounterName);
    smsMap = new RecordMap(Constant.SMSCounterName);
    maps.add(callMap);
    maps.add(smsMap);
  }

  public ArrayList<RecordMap> getMaps() {
    return maps;
  }

  public void setMaps(ArrayList<RecordMap> maps) {
    this.maps = maps;
  }

  public RecordMap getCallMap() {
    return callMap;
  }

  public void setCallMap(RecordMap callMap) {
    this.callMap = callMap;
  }

  public RecordMap getSmsMap() {
    return smsMap;
  }

  public void setSmsMap(RecordMap smsMap) {
    this.smsMap = smsMap;
  }

}
