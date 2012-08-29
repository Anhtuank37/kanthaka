package com.uom.kanthaka.preprocessor.rulereader;

import com.uom.kanthaka.preprocessor.cdrreader.RecordMap;
import com.uom.kanthaka.preprocessor.cdrreader.TempCDREntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Rule {

  private String ruleName;
  private String ruleString;
  private String channelType;
  private Set<String> cdrReadingFields;
  private ArrayList<String> fields;
  private ArrayList<ArrayList<ConditionField>> conditionFields;
  private ArrayList<ArrayList<CounterConditionFields>> counterConditionFields;
  ArrayList<ArrayList<HashSet<String>>> counterResultSet;
  private ArrayList<String> counters;
  ArrayList<RecordMap> recordMaps;
  ArrayList<HashSet<Long>> DoORresultset;
  TempCDREntry tempEntries;
    HashMap<String,String> selectedUsers;
  /**
     * 
     * @param 
     * @param 
     * @return
     */
    public Rule() {
        super();
        cdrReadingFields = new HashSet<String>();
        cdrReadingFields.add("sourceAddress");
        fields = new ArrayList<String>();
        conditionFields = new ArrayList<ArrayList<ConditionField>>();
        counterConditionFields = new ArrayList<ArrayList<CounterConditionFields>>();
        counterResultSet = new ArrayList<ArrayList<HashSet<String>>>();
        counters = new ArrayList<String>();
        recordMaps = new ArrayList<RecordMap>();
        selectedUsers=new HashMap<String, String>();
        DoORresultset = new ArrayList<HashSet<Long>>();
        tempEntries = new TempCDREntry();
    }

//    public void createCounterMaps(ArrayList<String> counters){
//        for (int i = 0; i < counters.size(); i++) {
//            ruleMaps.put(counters.get(i), new ConcurrentHashMap<String, Long>());
//        }
//    }
//    public void insertToCounterMap(ConcurrentHashMap<String, Long> counterMap){
//            ruleMaps.put(counters.get(i), new ConcurrentHashMap<String, Long>());
//    }
//    public HashMap<String, ConcurrentHashMap<String, Long>> getRuleMaps() {
//        return this.ruleMaps;
//    }
    public ArrayList<RecordMap> getRecordMaps() {
        return recordMaps;
    }

    public String getChannelType() {
        return channelType;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public String getRuleName() {
        return ruleName;
    }


    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public String getRuleString() {
        return ruleString;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public Set<String> getCdrReadingFields() {
        return cdrReadingFields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<ArrayList<ConditionField>> getConditionFields() {
        return conditionFields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<ArrayList<CounterConditionFields>> getCounterConditionFields() {
        return counterConditionFields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<String> getCounters() {
        return counters;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<String> getFields() {
        return fields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setCdrReadingFields(Set<String> cdrReadingFields) {
        this.cdrReadingFields = cdrReadingFields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setConditionFields(ArrayList<ArrayList<ConditionField>> conditionFields) {
        this.conditionFields = conditionFields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setRecordMaps(ArrayList<RecordMap> recordMaps) {
        this.recordMaps = recordMaps;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setcounterConditionFields(ArrayList<ArrayList<CounterConditionFields>> counterConditionFd) {
        this.counterConditionFields = counterConditionFd;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setCounters(ArrayList<String> counters) {
        this.counters = counters;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setRuleString(String ruleString) {
        this.ruleString = ruleString;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setFields(ArrayList<String> conditionFields) {
        this.fields = conditionFields;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<ArrayList<HashSet<String>>> getCounterResultSet() {
        return counterResultSet;
    }

    public void setCounterResultSet(ArrayList<ArrayList<HashSet<String>>> resultset) {
        this.counterResultSet=resultset;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<HashSet<Long>> getDoORresultset() {
        return DoORresultset;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setDoORresultset(ArrayList<HashSet<Long>> doORresultset) {
        DoORresultset = doORresultset;
    }



    public TempCDREntry getTempEntries() {
        return tempEntries;
    }
    public HashMap<String, String> getSelectedUsers() {
        return selectedUsers;
    }

    public void setSelectedUsers(HashMap<String, String> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }
    
}