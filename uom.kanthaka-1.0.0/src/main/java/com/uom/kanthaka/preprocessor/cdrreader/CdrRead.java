/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import com.uom.kanthaka.cassandra.updater.CassandraUpdater;
import com.uom.kanthaka.preprocessor.Constant;
import com.uom.kanthaka.preprocessor.rulereader.ConditionField;
import com.uom.kanthaka.preprocessor.rulereader.Rule;
import org.apache.commons.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

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
     * and objectPool to be used
     * @param
     */
    public CdrRead(ArrayList<Rule> rules, ObjectPool<CdrRead> pool) {
        cdrMap = new CdrAttributeMapping();
        businessRules = rules;
        this.pool = pool;
    }

    /**
     * Read CDR files on multiple threads and process CDR record by record on corresponding
     * promotion rules. This will also update the record maps during the reading process
     * @param
     */
    public void readCdr() {
        CdrRead newCdrObject = null;
        BufferedReader bufReader;
        try {
            bufReader = new BufferedReader(new FileReader(file));
            newCdrObject = pool.borrowObject();         // borrow CdrRead pool object
            int counter = 0;                            // initialize counter
            String tempRec;

            while ((tempRec = bufReader.readLine()) != null) {
                newCdrObject.setRecord(tempRec);
                newCdrObject.readCdrFile();
                counter++;
                if (counter == Constant.CDR_Counter_Val) {      // Check wether counter value reached
                    updateRuleMaps();                   // update record maps
                    printRuleRecordMaps();
                    counter = 0;
                }
            }
            updateRuleMaps();                   // update record maps in EOF
//            printRuleRecordMaps();
            bufReader.close();
            logger.info("--- One File Done ----");
            file.delete();                      // delete the file after reading
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != newCdrObject) {
                    pool.returnObject(newCdrObject);    // return pool object
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update record maps of the business rule
     */
    public synchronized void updateRuleMaps() {
        for (int i = 0; i < businessRules.size(); i++) {    // iterate through  business rule objects
            Rule tempRule = businessRules.get(i);
            ArrayList<RecordMap> tempMaps = tempRule.getTempEntries().getMaps();

            while (tempMaps.size() > 0) {                   // Updating dataMaps using tempMaps 
                RecordMap tempRec = tempMaps.remove(0);     // removing temp map components and add them on data maps
                tempRule.getRecordMaps().add(
                        new RecordMap(tempRec.getType(), tempRec.getDataMap()));
            }
        }
    }

    /**
     * Printing the record maps to display(during tests)
     */
    public void printRuleRecordMaps() {
        CdrRead newCdrObject = null;
        try {
            newCdrObject = pool.borrowObject();                         // receive pool object
            ArrayList<Rule> businessRules = newCdrObject.getRules();
            for (int i = 0; i < businessRules.size(); i++) {                // iterate through business rules
                Rule businessRule = businessRules.get(i);
                System.out.println("Rule Name : " + businessRule.getRuleName());
                logger.debug("Rule Name : {}.", businessRule.getRuleName());        // print rule name

                ArrayList<RecordMap> mapList = businessRule.getRecordMaps();    // iterate through records maps on rules
                for (int j = 0; j < mapList.size(); j++) {
                    RecordMap record = mapList.get(j);
                    System.out.println(record.getType() + "  -  " + record.getDataMap());
                    logger.debug("{}. - {}.", record.getType(), record.getDataMap());    // print record map correspond to promotion
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != newCdrObject) {
                    pool.returnObject(newCdrObject);        // rerurn pool object
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Read the given CDR record and process the entry
     * read the required fields form CDR record and assign them to fields for check
     */
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

    /**
     * Compare the business promotions with the given CDR attributes
     */
    public void testRulesWithCDR() {
        for (int i = 0; i < businessRules.size(); i++) {        // iterate throgh the rules
            compareCdrWithARule(businessRules.get(i));
        }
    }

    /**
     * Compare the CDR entries with the Rule object and process them
     * compare conditonal attributes and counter attributes
     */
    public void compareCdrWithARule(Rule rule) {
        ArrayList<ArrayList<ConditionField>> conditionComp = rule.getConditionFields();
        TempCDREntry tempEntries = rule.getTempEntries();
        boolean condition = true;                           // boolean condition of given promotion
        for (int j = 0; j < conditionComp.size(); j++) {        // process business promotion rule by rule
            ArrayList<ConditionField> conditionOr = conditionComp.get(j);  // get the OR condition fields of a business promotion(1 rule)
            int count = 0;
            boolean innerCondition = false;                     // conditon for inner rule
            for (int k = 0; k < conditionOr.size(); k++) {      // check the boolean conditon of inner rule
                ConditionField conField = conditionOr.get(k);
                // System.out.print(conField.getConditionName() + ", " +
                // conField.getValue());
                if (conditionOr.size() > 1) {           // if more than one or condition exist
                    innerCondition = (innerCondition || checkCdrAttribute(conField, this));
                } else {                                // if only one or condition exist
                    innerCondition = checkCdrAttribute(conField, this);
                }
                count++;
//                if (count > 0 && count < conditionOr.size()) {
//                     System.out.print(" OR ");
//                }
            }
//            if ((j < conditionComp.size() - 1) && !(count == 0)) {
//                // System.out.println("");
//                // System.out.println(" AND ");
//            }
            condition = (condition && innerCondition);      // geting boolean condition among rules
        }
        if (condition) {                // if conditions get satisfied for the CDR record process the counters of the promotion
            if ((this.getSourceChannelType() != null)
                    && this.getSourceChannelType().equalsIgnoreCase(Constant.EventTypeCall)) {      // for event type Call
                ConcurrentHashMap<String, Long> callCounter = tempEntries.getCallMap().getDataMap();
                if (callCounter.containsKey(this.getSourceAddress())) {
                    long currentCount = callCounter.get(this.getSourceAddress());
                    callCounter.replace(this.getSourceAddress(), currentCount,          // increment the counters
                            currentCount + 1L);
                } else {
                    callCounter.put(this.getSourceAddress(), 1L);                       // create new counter field
                }
            } else if ((this.getSourceChannelType() != null)
                    && this.getSourceChannelType().equalsIgnoreCase(Constant.EventTypeSMS)) {       // for event type SMS
                ConcurrentHashMap<String, Long> smsCounter = tempEntries.getSmsMap().getDataMap();
                if (smsCounter.containsKey(this.getSourceAddress())) {
                    long currentCount = smsCounter.get(this.getSourceAddress());
                    smsCounter.replace(this.getSourceAddress(), currentCount,               // increment the counters
                            currentCount + 1L);
                } else {
                    smsCounter.put(this.getSourceAddress(), 1L);                        // create new counter field
                }
            } else {
            }
        }
    }

    /**
     * Checks the CDR attribute with promotion condition fields
     * 
     * @param conField
     * @param cdr
     * @return boolean value of success of operation
     */
    public boolean checkCdrAttribute(ConditionField conField, CdrRead cdr) {
        if ((conField.getConditionName()).equalsIgnoreCase(Constant.DestinationNumber)) {   // check two eqalities on sourse address
            if (conField.getCondition().equalsIgnoreCase(Constant.Equals)) {                // equals case
                return (conField.getValue()).equalsIgnoreCase(cdr.getDestinationAddress());
            } else if (conField.getCondition().equalsIgnoreCase(Constant.StartsWith)) {     // startsWith case
                return (cdr.getDestinationAddress()).startsWith(conField.getValue());
            }
            return false;
        } else if ((conField.getConditionName()).equalsIgnoreCase(Constant.ConnectionType)) {   // check ConnectionType (prepaid, postpaid)
            return (conField.getValue()).equalsIgnoreCase(cdr.getBillingType());
        } else if ((conField.getConditionName()).equalsIgnoreCase(Constant.ChannelType)) {      // check ChannelType(Sms, Call, Ussd)
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
    }
}
