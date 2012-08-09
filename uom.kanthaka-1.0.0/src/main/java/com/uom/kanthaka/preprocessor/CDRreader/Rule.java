package com.uom.kanthaka.preprocessor.CDRreader;

import com.uom.kanthaka.preprocessor.rulereader.RecordMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Rule {

    private String ruleName;
    private String ruleString;
    private String channelType;
//    private StringBuffer query;
    private Set<String> cdrReadingFields;
    private ArrayList<String> fields;
    private ArrayList<ArrayList<conditionField>> conditionFields;
    private ArrayList<ArrayList<counterConditionFields>> counterConditionFields;
    private ArrayList<String> counters;            //   counter fields
    //private HashMap<String, ConcurrentHashMap<String, Long>> ruleMaps;
    ArrayList<RecordMap> recordMaps;

    public Rule() {
        super();
//        query = new StringBuffer();
        cdrReadingFields = new HashSet<String>();
        cdrReadingFields.add("sourceAddress");
        fields = new ArrayList<String>();
        conditionFields = new ArrayList<ArrayList<conditionField>>();
        counterConditionFields = new ArrayList<ArrayList<counterConditionFields>>();
        counters = new ArrayList<String>();
        recordMaps = new  ArrayList<RecordMap>();
        //ruleMaps = new HashMap<String, ConcurrentHashMap<String, Long>>();
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
    
    public String getRuleName() {
        return ruleName;
    }

    public String getRuleString() {
        return ruleString;
    }
    
    public Set<String> getCdrReadingFields() {
        return cdrReadingFields;
    }

    public ArrayList<ArrayList<conditionField>> getConditionFields() {
        return conditionFields;
    }

    public ArrayList<ArrayList<counterConditionFields>> getCounterConditionFields() {
        return counterConditionFields;
    }
    
    public ArrayList<String> getCounters() {
        return counters;
    }
    
    public ArrayList<String> getFields() {
        return fields;
    }
    
    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
    
    public void setCdrReadingFields(Set<String> cdrReadingFields) {
        this.cdrReadingFields = cdrReadingFields;
    }

    public void setConditionFields(ArrayList<ArrayList<conditionField>> conditionFields) {
        this.conditionFields = conditionFields;
    }

    public void setRecordMaps(ArrayList<RecordMap> recordMaps) {
        this.recordMaps = recordMaps;
    }
    
    public void setcounterConditionFields(ArrayList<ArrayList<counterConditionFields>> counterConditionFd) {
        this.counterConditionFields = counterConditionFd;
    }

    public void setCounters(ArrayList<String> counters) {
        this.counters = counters;
    }

    public void setRuleString(String ruleString) {
        this.ruleString = ruleString;
    }
    
//    public void setRuleMaps(HashMap<String, ConcurrentHashMap<String, Long>> ruleMaps) {
//        this.ruleMaps = ruleMaps;
//    }
    
    public void setFields(ArrayList<String> conditionFields) {
        this.fields = conditionFields;
    }

    
}
