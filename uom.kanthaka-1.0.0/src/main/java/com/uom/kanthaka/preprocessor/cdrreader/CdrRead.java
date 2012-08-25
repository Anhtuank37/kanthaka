/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import com.uom.kanthaka.preprocessor.Constant;
import com.uom.kanthaka.preprocessor.rulereader.ConditionField;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.pool.ObjectPool;

/**
 * 
 * @author Makumar
 */
public class CdrRead extends Thread {
    
    ArrayList<Rule> businessRules;
    String record;
    String timeStamp;
    String sourceAddress;
    String destinationAddress;
    String billingType;
    String sourceChannelType;
    CdrAttributeMapping cdrMap;
    final Logger logger = LoggerFactory.getLogger(CdrRead.class);
    private ObjectPool<CdrRead> pool;
    File file;

    /**
     * constructor of CdrRead which initiate object to given Rule object
     * 
     * @param buisinessRule
     */
    public CdrRead(ArrayList<Rule> rules, ObjectPool<CdrRead> pool) {
        cdrMap = new CdrAttributeMapping();
        businessRules = rules;
        this.pool = pool;
    }
    
        /**
     * Read CDR files according to the given business rules
     * @param file
     */
    public void readCdr() {
        CdrRead newCdrObject = null;
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(file));
            newCdrObject = pool.borrowObject();
            String tempRec;

            while ((tempRec = bufReader.readLine()) != null) {
                newCdrObject.setRecord(tempRec);
                newCdrObject.readCdrFile();
//                newCdrObject.readCdrFile(tempRec);
            }
//  *****************************************************************          
            printRuleRecordMaps();
//  *****************************************************************          
            bufReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != newCdrObject) {
                    pool.returnObject(newCdrObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read the given CDR file and process entries
     * 
     * @param file
     */
//    public void readCdrFile(String record) {
    public void readCdrFile() {
        String ruleName[] = record.split(Constant.COMMA);
        for (int i = 0; i < Constant.cdrReadingFields.length; i++) {
            String ruleField = Constant.cdrReadingFields[i];
            if (ruleField.equalsIgnoreCase(Constant.TimeStamp)) {
                setTimeStamp(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
            } else if (ruleField.equalsIgnoreCase(Constant.SourceAddress)) {
                setSourceAddress(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
            } else if (ruleField.equalsIgnoreCase(Constant.DestinationAddress)) {
                setDestinationAddress(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
            } else if (ruleField.equalsIgnoreCase(Constant.BillingType)) {
                setBillingType(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
            } else if (ruleField.equalsIgnoreCase(Constant.SourceChannelType)) {
                setSourceChannelType(ruleName[cdrMap.getMappingNo(ruleField) - 2]);
            } else {
            }
        }
        testRulesWithCDR();
    }
    
    public void testRulesWithCDR() {
        for (int i = 0; i < businessRules.size(); i++) {
            compareCdrWithARule(businessRules.get(i));
        }
    }

    /**
     * Compare the CDR entries with the Rule object and process them
     */
    public void compareCdrWithARule(Rule rule) {
        ArrayList<ArrayList<ConditionField>> conditionComp = rule.getConditionFields();
        TempCDREntry tempEntries = rule.getTempEntries();
        boolean condition = true;
        for (int j = 0; j < conditionComp.size(); j++) {
            ArrayList<ConditionField> conditionOr = conditionComp.get(j);
            int count = 0;
            boolean innerCondition = false;
            for (int k = 0; k < conditionOr.size(); k++) {
                ConditionField conField = conditionOr.get(k);
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
                    && this.getSourceChannelType().equalsIgnoreCase(Constant.EventTypeCall)) {
                ConcurrentHashMap<String, Long> callCounter = tempEntries.getCallMap().getDataMap();
                if (callCounter.containsKey(this.getSourceAddress())) {
                    long currentCount = callCounter.get(this.getSourceAddress());
                    callCounter.replace(this.getSourceAddress(), currentCount,
                            currentCount + 1L);
                } else {
                    callCounter.put(this.getSourceAddress(), 1L);
                }
            } else if ((this.getSourceChannelType() != null)
                    && this.getSourceChannelType().equalsIgnoreCase(Constant.EventTypeSMS)) {
                ConcurrentHashMap<String, Long> smsCounter = tempEntries.getSmsMap().getDataMap();
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
    public boolean checkCdrAttribute(ConditionField conField, CdrRead cdr) {
        if ((conField.getConditionName()).equalsIgnoreCase(Constant.DestinationNumber)) {
            if (conField.getCondition().equalsIgnoreCase(Constant.Equals)) {
                return (conField.getValue()).equalsIgnoreCase(cdr.getDestinationAddress());
            } else if (conField.getCondition().equalsIgnoreCase(Constant.StartsWith)) {
                return (cdr.getDestinationAddress()).startsWith(conField.getValue());
            }
            return false;
        } else if ((conField.getConditionName()).equalsIgnoreCase(Constant.ConnectionType)) {
            return (conField.getValue()).equalsIgnoreCase(cdr.getBillingType());
        } else if ((conField.getConditionName()).equalsIgnoreCase(Constant.ChannelType)) {
            return (conField.getValue()).equalsIgnoreCase(cdr.getSourceChannelType());
        } else {
            return false;
        }
    }

    /**
     * Map CDR attribute with CDR properties
     * @param attribute
     * @return String
     */
    public String getMappingAttribute(String attribute) {
        if (attribute.equalsIgnoreCase(Constant.TimeStamp)) {
            return getTimeStamp();
        } else if (attribute.equalsIgnoreCase(Constant.SourceAddress)) {
            return getSourceAddress();
        } else if (attribute.equalsIgnoreCase(Constant.DestinationAddress)) {
            return getDestinationAddress();
        } else if (attribute.equalsIgnoreCase(Constant.SourceChannelType)) {
            return getSourceChannelType();
        } else if (attribute.equalsIgnoreCase(Constant.BillingType)) {
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
    public ArrayList<Rule> getRules() {
        return businessRules;
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

    public void setFile(File file) {
        this.file = file;
    }
    
    public void setRecord(String record) {
        this.record = record;
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
    
      public void printRuleRecordMaps() {
        CdrRead newCdrObject = null;
        try {
            newCdrObject = pool.borrowObject();
            ArrayList<Rule> businessRules = newCdrObject.getRules();
            for (int i = 0; i < businessRules.size(); i++) {
                Rule businessRule = businessRules.get(i);
                System.out.println("Rule Name : " + businessRule.getRuleName());
//            logger.debug("Rule Name : {}.", businessRule.getRuleName());

                for (int j = 0; j < businessRule.getConditionFields().size(); j++) {
                    ArrayList<ConditionField> temp = businessRule.getConditionFields().get(j);
                    for (int k = 0; k < temp.size(); k++) {
//                        System.out.println(temp.get(k).printDetails());
//                    logger.debug("Checking Conditions : {}.", temp.get(k).printDetails());
                    }
                }
                ArrayList<RecordMap> mapList = businessRule.getRecordMaps();
                for (int j = 0; j < mapList.size(); j++) {
                    RecordMap record = mapList.get(j);
                    System.out.println(record.getType() + "  -  " + record.getDataMap());
//                logger.debug("{}. - {}.", record.getType(), record.getDataMap());
                }
//                System.out.println("");
//            logger.debug("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != newCdrObject) {
                    pool.returnObject(newCdrObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
  
    public void initilizeFields() {
        setTimeStamp(null);
        setSourceAddress(null);
        setDestinationAddress(null);
        setBillingType(null);
        setSourceChannelType(null);
    }

    /**
     * 
     * @param
     * @param
     * @return
     */
    @Override
    public void run() {
        readCdr();
        Thread.yield();
    }
}
