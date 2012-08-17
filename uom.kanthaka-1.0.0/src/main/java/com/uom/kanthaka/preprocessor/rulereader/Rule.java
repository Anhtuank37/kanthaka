package com.uom.kanthaka.preprocessor.rulereader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.uom.kanthaka.preprocessor.cdrreader.RecordMap;

public class Rule {

  private String ruleName;
  private String ruleString;
  private String channelType;
  // private StringBuffer query;
  private Set<String> cdrReadingFields;
  private ArrayList<String> fields;
  private ArrayList<ArrayList<ConditionField>> conditionFields;
  private ArrayList<ArrayList<CounterConditionFields>> counterConditionFields;
  ArrayList<ArrayList<HashSet<String>>> counterResultSet;
  private ArrayList<String> counters;
  HashSet<String> selectedList;// counter fields
  // private HashMap<String, ConcurrentHashMap<String, Long>> ruleMaps;
  ArrayList<RecordMap> recordMaps;

  ArrayList<HashSet<Long>> DoORresultset;

  public Rule() {
    super();
    // query = new StringBuffer();
    cdrReadingFields = new HashSet<String>();
    cdrReadingFields.add("sourceAddress");
    fields = new ArrayList<String>();
    conditionFields = new ArrayList<ArrayList<ConditionField>>();
    counterConditionFields = new ArrayList<ArrayList<CounterConditionFields>>();
    counterResultSet = new ArrayList<ArrayList<HashSet<String>>>();
    counters = new ArrayList<String>();
    recordMaps = new ArrayList<RecordMap>();
    selectedList = new HashSet<String>();
    DoORresultset = new ArrayList<HashSet<Long>>();
    // ruleMaps = new HashMap<String, ConcurrentHashMap<String, Long>>();
  }

  // public void createCounterMaps(ArrayList<String> counters){
  // for (int i = 0; i < counters.size(); i++) {
  // ruleMaps.put(counters.get(i), new ConcurrentHashMap<String, Long>());
  // }
  // }

  // public void insertToCounterMap(ConcurrentHashMap<String, Long> counterMap){
  // ruleMaps.put(counters.get(i), new ConcurrentHashMap<String, Long>());
  // }

  // public HashMap<String, ConcurrentHashMap<String, Long>> getRuleMaps() {
  // return this.ruleMaps;
  // }

  public ArrayList<RecordMap> getRecordMaps() {
    return recordMaps;
  }

  public HashSet<String> getSelectedList() {
    return selectedList;
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

  public ArrayList<ArrayList<ConditionField>> getConditionFields() {
    return conditionFields;
  }

  public ArrayList<ArrayList<CounterConditionFields>> getCounterConditionFields() {
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

  public void setConditionFields(
      ArrayList<ArrayList<ConditionField>> conditionFields) {
    this.conditionFields = conditionFields;
  }

  public void setRecordMaps(ArrayList<RecordMap> recordMaps) {
    this.recordMaps = recordMaps;
  }

  public void setcounterConditionFields(
      ArrayList<ArrayList<CounterConditionFields>> counterConditionFd) {
    this.counterConditionFields = counterConditionFd;
  }

  public void setCounters(ArrayList<String> counters) {
    this.counters = counters;
  }

  public void setRuleString(String ruleString) {
    this.ruleString = ruleString;
  }

  public void setFields(ArrayList<String> conditionFields) {
    this.fields = conditionFields;
  }

  public ArrayList<ArrayList<HashSet<String>>> getCounterResultSet() {
    return counterResultSet;
  }

  public ArrayList<HashSet<Long>> getDoORresultset() {
    return DoORresultset;
  }

  public void setDoORresultset(ArrayList<HashSet<Long>> doORresultset) {
    DoORresultset = doORresultset;
  }

  public void setSelectedList(HashSet<String> selectedList) {
    this.selectedList = selectedList;
  }

}
