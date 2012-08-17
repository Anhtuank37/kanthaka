/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader2;

/**
 * 
 * @author Makumar
 */
public class counterConditionFields {

  String conditionName;
  String condition;
  Long value;

  public counterConditionFields(String conditionName, String condition,
      Long value) {
    this.conditionName = conditionName;
    this.condition = condition;
    this.value = value;
  }

  public String printDetails() {
    return conditionName + " " + condition + " " + value;
  }

  public String getConditionName() {
    return conditionName;
  }

  public String getCondition() {
    return condition;
  }

  public Long getValue() {
    return value;
  }

  public void setConditionName(String conditionName) {
    this.conditionName = conditionName;
  }

  public void setCondition(String condition) {
    this.condition = condition;
  }

  public void setValue(Long value) {
    this.value = value;
  }
}
