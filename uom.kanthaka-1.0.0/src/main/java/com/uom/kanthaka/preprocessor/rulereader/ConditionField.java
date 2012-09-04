/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

/**
 * 
 * @author Makumar
 */

public class ConditionField {

  String conditionName;
  String condition;
  String value;

  /**
   * Constructor for to set the condition field
   * 
   * @param conditionName
   * @param condition
   * @param value
   */
  public ConditionField(String conditionName, String condition, String value) {
    this.conditionName = conditionName;
    this.condition = condition;
    this.value = value;
  }

  /**
   * Print the details in the condition field
   * 
   * @return string value of condition field
   */
  public String printDetails() {
    return conditionName + " " + condition + " " + value;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getConditionName() {
    return conditionName;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getCondition() {
    return condition;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public String getValue() {
    return value;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public void setConditionName(String conditionName) {
    this.conditionName = conditionName;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public void setCondition(String condition) {
    this.condition = condition;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public void setValue(String value) {
    this.value = value;
  }
}
