/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import com.uom.kanthaka.preprocessor.Constant;
import com.uom.kanthaka.preprocessor.rulereader.ConditionField;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * @author Makumar
 */
public class CdrRead extends TimerTask {

    ArrayList<Rule> businessRules;
    String record;
    String timeStamp;
    String sourceAddress;
    String destinationAddress;
    String billingType;
    String sourceChannelType;
    CdrAttributeMapping cdrMap;
//    ArrayList<RecordMap> maps;
//    RecordMap callMap;
//    RecordMap smsMap;
//    TempCDREntry tempEntries;
    final Logger logger = LoggerFactory.getLogger(CdrRead.class);

    /**
     * constructor of CdrRead which initiate object to given Rule object
     * 
     * @param buisinessRule
     */
    public CdrRead(ArrayList<Rule> rules) {
        cdrMap = new CdrAttributeMapping();
        businessRules = rules;
    }

    /**
     * Read the given CDR file and process entries
     * 
     * @param file
     */
    public void readCdrFile(String record) {
        this.record = record;
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
     * Update record maps of the business rule
     */
    public synchronized void updateRuleMaps() {
        for (int i = 0; i < businessRules.size(); i++) {
            Rule tempRule = businessRules.get(i);
            ArrayList<RecordMap> tempMaps = tempRule.getTempEntries().getMaps();

            while (tempMaps.size() > 0) {
                RecordMap tempRec = tempMaps.remove(0);
                // System.out.println("Updating Maps .....");
                // System.out.println(tempRec.getType() + "  -  " + tempRec.getDataMap());
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
