/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.uom.kanthaka.preprocessor.CDRreader.Rule;
import com.uom.kanthaka.preprocessor.CDRreader.conditionField;

/**
 * 
 * @author Makumar
 */
public class CdrRead extends TimerTask {

  Rule rule;
  String record;
  String timeStamp;
  String sourceAddress;
  String destinationAddress;
  String billingType;
  String sourceChannelType;
  ArrayList<RecordMap> maps;
  RecordMap callMap;
  RecordMap smsMap;

  /**
   * constructor of CdrRead which initiate object to given Rule object
   * 
   * @param buisinessRule
   */
  public CdrRead(Rule buisinessRule) {
    this.rule = buisinessRule;
    this.maps = new ArrayList<RecordMap>();
    callMap = new RecordMap("No_of_Calls");
    smsMap = new RecordMap("No_of_SMSs");
    maps.add(callMap);
    maps.add(smsMap);
  }

  /**
   * Read the given CDR file and process entries
   * 
   * @param file
   */
  public void readCdrFile(File file) {
    try {
      BufferedReader bufReader = new BufferedReader(new FileReader(file));
      String tempRec;

      while ((tempRec = bufReader.readLine()) != null) {
        CdrAttributeMapping cdrMap = new CdrAttributeMapping();
        record = tempRec;
        // System.out.println(record);
        String ruleName[] = tempRec.split(",");
        for (Iterator it = getRule().getCdrReadingFields().iterator(); it
            .hasNext();) {
          String ruleField = (String) it.next();
          if (ruleField.equalsIgnoreCase("timeStamp")) {
            setTimeStamp(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
          } else if (ruleField.equalsIgnoreCase("sourceAddress")) {
            setSourceAddress(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
          } else if (ruleField.equalsIgnoreCase("destinationAddress")) {
            setDestinationAddress(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
          } else if (ruleField.equalsIgnoreCase("billingType")) {
            setBillingType(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
          } else if (ruleField.equalsIgnoreCase("sourceChannelType")) {
            setSourceChannelType(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
          } else {
          }
          // System.out.print(ruleName[cdrMap.getMappingNo(ruleField) - 2] +
          // " - " + (cdrMap.getMappingNo(ruleField)) + ", ");
        }
        // System.out.println("");
        this.compareCdrAndRule();
      }
      bufReader.close();
      updateRuleMaps();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Update record maps of the business rule
   */
  public synchronized void updateRuleMaps() {
    while (maps.size() > 0) {
      RecordMap tempRec = maps.remove(0);
      // System.out.println("Updating Maps .....");
      // System.out.println(tempRec.getType() + "  -  " + tempRec.getDataMap());
      // System.out.println("Updated .....");
      getRule().getRecordMaps().add(
          new RecordMap(tempRec.getType(), tempRec.getDataMap()));
      // tempRec.initilizeMap();
    }
  }

  /**
   * Compare the CDR entries with the Rule object and process them
   */
  public void compareCdrAndRule() {
    ArrayList<ArrayList<conditionField>> conditionComp = getRule()
        .getConditionFields();
    boolean condition = true;
    for (int j = 0; j < conditionComp.size(); j++) {
      ArrayList<conditionField> conditionOr = conditionComp.get(j);
      int count = 0;
      boolean innerCondition = false;
      for (int k = 0; k < conditionOr.size(); k++) {
        conditionField conField = conditionOr.get(k);
        // System.out.print(conField.getConditionName() + ", " +
        // conField.getValue());
        if (conditionOr.size() > 1) {
          innerCondition = (innerCondition || checkCdrAttribute(conField, this));
        } else {
          innerCondition = checkCdrAttribute(conField, this);
        }
        count++;
        if (count > 0 && count < conditionOr.size()) {
          // System.out.print(" OR ");
        }
      }
      if ((j < conditionComp.size() - 1) && !(count == 0)) {
        // System.out.println("");
        // System.out.println(" AND ");
      }
      condition = (condition && innerCondition);
    }
    // System.out.println("");
    if (condition) {
      if ((this.getSourceChannelType() != null)
          && this.getSourceChannelType().equalsIgnoreCase("cas")) {
        ConcurrentHashMap<String, Long> callCounter = getCallMap().getDataMap();
        if (callCounter.containsKey(this.getSourceAddress())) {
          long currentCount = callCounter.get(this.getSourceAddress());
          callCounter.replace(this.getSourceAddress(), currentCount,
              currentCount + 1L);
        } else {
          callCounter.put(this.getSourceAddress(), 1L);
        }
      } else if ((this.getSourceChannelType() != null)
          && this.getSourceChannelType().equalsIgnoreCase("sms")) {
        ConcurrentHashMap<String, Long> smsCounter = getSmsMap().getDataMap();
        if (smsCounter.containsKey(this.getSourceAddress())) {
          long currentCount = smsCounter.get(this.getSourceAddress());
          smsCounter.replace(this.getSourceAddress(), currentCount,
              currentCount + 1L);
        } else {
          smsCounter.put(this.getSourceAddress(), 1L);
        }
      } else {
      }
    }
  }

  /**
   * Checks the CDR attribute with rule condition fields
   * 
   * @param conField
   * @param cdr
   * @return boolean value of success of operation
   */
  public boolean checkCdrAttribute(conditionField conField, CdrRead cdr) {
    // if ((conField.getValue()).equalsIgnoreCase(cdr.getSourceChannelType())) {
    if ((conField.getConditionName()).equalsIgnoreCase("Dest_No")) {
      if (conField.getCondition().equalsIgnoreCase("=")) {
        return (conField.getValue()).equalsIgnoreCase(cdr
            .getDestinationAddress());
      } else if (conField.getCondition().equalsIgnoreCase("Starts_With")) {
        return (cdr.getDestinationAddress()).startsWith(conField.getValue());
      }
      return false;
    } else if ((conField.getConditionName())
        .equalsIgnoreCase("Connection_Type")) {
      return (conField.getValue()).equalsIgnoreCase(cdr.getBillingType());
    } else if ((conField.getConditionName()).equalsIgnoreCase("Channel_Type")) {
      return (conField.getValue()).equalsIgnoreCase(cdr.getSourceChannelType());
    } else {
      return false;
    }
  }

  /**
   * Map CDR attribute with CDR properties
   * 
   * @param attribute
   * @return String
   */
  public String getMappingAttribute(String attribute) {
    if (attribute.equalsIgnoreCase("timeStamp")) {
      return getTimeStamp();
    } else if (attribute.equalsIgnoreCase("sourceAddress")) {
      return getSourceAddress();
    } else if (attribute.equalsIgnoreCase("destinationAddress")) {
      return getDestinationAddress();
    } else if (attribute.equalsIgnoreCase("sourceChannelType")) {
      return getSourceChannelType();
    } else if (attribute.equalsIgnoreCase("billingType")) {
      return getBillingType();
    } else {
      return null;
    }
  }

  /**
   * Getter method for Rule object
   * 
   * @return Rule
   */
  public Rule getRule() {
    return rule;
  }

  /**
   * Getter method for RecordMap object
   * 
   * @return RecordMap
   */
  private RecordMap getCallMap() {
    return callMap;
  }

  /**
   * Getter method for RecordMap object
   * 
   * @return RecordMap
   */
  private RecordMap getSmsMap() {
    return smsMap;
  }

  /**
   * Getter method for Record object
   * 
   * @return String
   */
  public String getRecord() {
    return record;
  }

  /**
   * Getter method for RecordMap object
   * 
   * @return RecordMap
   */
  public String getTimeStamp() {
    return timeStamp;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getSourceAddress() {
    return sourceAddress;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getDestinationAddress() {
    return destinationAddress;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getSourceChannelType() {
    return sourceChannelType;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getBillingType() {
    return billingType;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  private void setTimeStamp(String timeStamp) {
    this.timeStamp = timeStamp;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  private void setSourceAddress(String sourceAddress) {
    this.sourceAddress = sourceAddress;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  private void setDestinationAddress(String destinationAddress) {
    this.destinationAddress = destinationAddress;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  private void setSourceChannelType(String sourceChannelType) {
    this.sourceChannelType = sourceChannelType;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  private void setBillingType(String billingType) {
    this.billingType = billingType;
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
