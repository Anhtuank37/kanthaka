/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.CDRreader;

/**
 *
 * @author Makumar
 */
public class conditionField {

    String conditionName;
    String condition;
    String value;

    public conditionField(String conditionName, String value) {
        this.conditionName = conditionName;
        this.value = value;
    }

    public conditionField(String conditionName, String condition, String value) {
        this.conditionName = conditionName;
        this.condition = condition;
        this.value = value;
    }

    public String printDetails() {
        return conditionName + " - " + value;
    }

    public String getConditionName() {
        return conditionName;
    }

    public String getCondition() {
        return condition;
    }

    public String getValue() {
        return value;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
