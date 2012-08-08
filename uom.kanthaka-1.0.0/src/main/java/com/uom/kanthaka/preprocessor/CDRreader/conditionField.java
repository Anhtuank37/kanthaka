package com.uom.kanthaka.preprocessor.CDRreader;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Makumar
 */
public class conditionField {
    String conditionName;
    String value;

    public conditionField(String conditionName, String value) {
        this.conditionName = conditionName;
        this.value = value;
    }
    
    public String printDetails() {
        return conditionName + " - " + value;
    }

    public String getConditionName() {
        return conditionName;
    }

    public String getValue() {
        return value;
    }

    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
