package com.uom.kanthaka.preprocessor.rulereader;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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

//    public static void main(String[] args) {
//        String url = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\CDR";
//        Rule businessRule = new Rule();
//        businessRule.getCdrReadingFields().add("sourceChannelType");
//        businessRule.getCdrReadingFields().add("destinationAddress");
//        businessRule.getCdrReadingFields().add("billingType");
//        CdrRead cdr = new CdrRead();
//        cdr.readCdrOnPath(new File(url), businessRule);
//    }
    public CdrRead(Rule buisinessRule) {
        this.rule = buisinessRule;
        this.maps = new ArrayList<RecordMap>();
        callMap = new RecordMap("No_of_Calls");
        smsMap = new RecordMap("No_of_SMSs");
        maps.add(callMap);
        maps.add(smsMap);
    }

    public boolean readCdrOnPath(File path) {
        File files[];
        files = path.listFiles();

        if (files.length > 0) {
            for (int i = 0; i< files.length; i++) {
                File file = new File(files[i].toString());
                readCdrFile(file);
                file.delete();
                System.out.println("--- One File Done ----");
                return true;
            }
        }else
            System.out.println("--- No files ---");
        return false;
    }

    public void readCdrFile(File file) {
        try {
            BufferedReader bufReader = new BufferedReader(new FileReader(file));
            String tempRec;

            while ((tempRec = bufReader.readLine()) != null) {
                CdrAttributeMapping cdrMap = new CdrAttributeMapping();
                record = tempRec;
//                System.out.println(record);
                String ruleName[] = tempRec.split(",");
                for (Iterator it = getRule().getCdrReadingFields().iterator(); it.hasNext();) {
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
//                    System.out.print(ruleName[cdrMap.getMappingNo(ruleField) - 2] + " - " + (cdrMap.getMappingNo(ruleField)) + ", ");
                }
//                System.out.println("");
                this.compareCdrAndRule();
            }
            bufReader.close();
            updateRuleMaps();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void updateRuleMaps() {
        while (maps.size() > 0) {
            RecordMap tempRec = maps.remove(0);
//            System.out.println("Updating Maps .....");
//            System.out.println(tempRec.getType() + "  -  " + tempRec.getDataMap());
//            System.out.println("Updated .....");
            getRule().getRecordMaps().add(new RecordMap(tempRec.getType(), tempRec.getDataMap()));
            //tempRec.initilizeMap();
        }
    }

    public void compareCdrAndRule() {
        ArrayList<ArrayList<conditionField>> conditionComp = getRule().getConditionFields();
        boolean condition = true;
        for (int j = 0; j < conditionComp.size(); j++) {
            ArrayList<conditionField> conditionOr = conditionComp.get(j);
            int count = 0;
            boolean innerCondition = false;
            for (int k = 0; k < conditionOr.size(); k++) {
                conditionField conField = conditionOr.get(k);
//                System.out.print(conField.getConditionName() + ", " + conField.getValue());
                if (conditionOr.size() > 1) {
                    innerCondition = (innerCondition || checkCdrAttribute(conField, this));
                } else {
                    innerCondition = checkCdrAttribute(conField, this);
                }
                count++;
                if (count > 0 && count < conditionOr.size()) {
//                    System.out.print(" OR ");
                }
            }
            if ((j < conditionComp.size() - 1) && !(count == 0)) {
//                System.out.println("");
//                System.out.println(" AND ");
            }
            condition = (condition && innerCondition);
        }
//        System.out.println("");
        if (condition) {
//            System.out.println(" ------------------------- ");
//            System.out.println(" ----- Satisfy Rule ------ " + tempRuleComp.getRuleName());
//            System.out.println(" ------------------------- ");
//            System.out.println("");
            if ((this.getSourceChannelType() != null) && this.getSourceChannelType().equalsIgnoreCase("cas")) {
                ConcurrentHashMap<String, Long> callCounter = getCallMap().getDataMap();
                if (callCounter.containsKey(this.getSourceAddress())) {
                    long currentCount = callCounter.get(this.getSourceAddress());
                    callCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
                } else {
                    callCounter.put(this.getSourceAddress(), 1L);
                }
//                ConcurrentHashMap<String, Long> callCounter = getRule().getRuleMaps().get("No_of_Calls");
//                if (callCounter.containsKey(this.getSourceAddress())) {
//                    long currentCount = callCounter.get(this.getSourceAddress());
//                    callCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
////                    callCounter.remove(this.getSourceAddress());
////                    callCounter.put(this.getSourceAddress(), currentCount + 1L);
//                } else {
//                    callCounter.put(this.getSourceAddress(), 1L);
//                }
            } else if ((this.getSourceChannelType() != null) && this.getSourceChannelType().equalsIgnoreCase("sms")) {
                ConcurrentHashMap<String, Long> smsCounter = getSmsMap().getDataMap();
                if (smsCounter.containsKey(this.getSourceAddress())) {
                    long currentCount = smsCounter.get(this.getSourceAddress());
                    smsCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
                } else {
                    smsCounter.put(this.getSourceAddress(), 1L);
                }
//                ConcurrentHashMap<String, Long> smsCounter = getRule().getRuleMaps().get("No_of_SMSs");
//                if (smsCounter.containsKey(this.getSourceAddress())) {
//                    long currentCount = smsCounter.get(this.getSourceAddress());
//                    smsCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
////                    smsCounter.put(this.getSourceAddress(), currentCount + 1L);
//                } else {
//                    smsCounter.put(this.getSourceAddress(), 1L);
//                }
            } else {
            }
        }
    }

    public static boolean checkCdrAttribute(conditionField conField, CdrRead cdr) {
//        if ((conField.getValue()).equalsIgnoreCase(cdr.getSourceChannelType())) {
        if ((conField.getConditionName()).equalsIgnoreCase("Called_No") || (conField.getConditionName()).equalsIgnoreCase("Smsed_No")) {
            return (conField.getValue()).equalsIgnoreCase(cdr.getDestinationAddress());
//        } else if ((conField.getConditionName()).equalsIgnoreCase("No_of_Calls")||(conField.getConditionName()).equalsIgnoreCase("No_of_SMSs")) {
//            return (conField.getValue()).equalsIgnoreCase(cdr.getSourceChannelType());
        } else if ((conField.getConditionName()).equalsIgnoreCase("Connection_Type")) {
            return (conField.getValue()).equalsIgnoreCase(cdr.getBillingType());
        } else if ((conField.getConditionName()).equalsIgnoreCase("Channel_Type")) {
            return (conField.getValue()).equalsIgnoreCase(cdr.getSourceChannelType());
        } else {
            return false;
        }
    }

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

    public Rule getRule() {
        return rule;
    }

    private RecordMap getCallMap() {
        return callMap;
    }

    private RecordMap getSmsMap() {
        return smsMap;
    }

    public String getRecord() {
        return record;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public String getSourceAddress() {
        return sourceAddress;
    }

    public String getDestinationAddress() {
        return destinationAddress;
    }

    public String getSourceChannelType() {
        return sourceChannelType;
    }

    public String getBillingType() {
        return billingType;
    }

    private void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    private void setSourceAddress(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    private void setDestinationAddress(String destinationAddress) {
        this.destinationAddress = destinationAddress;
    }

    private void setSourceChannelType(String sourceChannelType) {
        this.sourceChannelType = sourceChannelType;
    }

    private void setBillingType(String billingType) {
        this.billingType = billingType;
    }

    @Override
    public void run() {
        updateRuleMaps();

//        try {
        Thread.yield();
//        } catch (InterruptedException ex) {
//            ex.printStackTrace();
//        }

    }
}
