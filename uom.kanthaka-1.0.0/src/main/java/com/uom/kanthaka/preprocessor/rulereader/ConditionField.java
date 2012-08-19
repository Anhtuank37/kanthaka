/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Makumar
 */

public class ConditionField {

    String conditionName;
    String condition;
    String value;
    final Logger logger = LoggerFactory.getLogger(ConditionField.class);

    /**
     *
     * @param
     * @param
     * @return
     */
//    public conditionField(String conditionName, String value) {
//        this.conditionName = conditionName;
//        this.value = value;
//    }

    /**
     * @param
     * @param
     * @return
     */
    public ConditionField(String conditionName, String condition, String value) {
        this.conditionName = conditionName;
        this.condition = condition;
        this.value = value;
    }

    /**
     * @param
     * @param
     * @return
     */
    public String printDetails() {
        return conditionName + " " + condition + " " + value;
    }

    /**
     * @param
     * @param
     * @return
     */
    public String getConditionName() {
        return conditionName;
    }

    /**
     * @param
     * @param
     * @return
     */
    public String getCondition() {
        return condition;
    }

    /**
     * @param
     * @param
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @param
     * @param
     * @return
     */
    public void setConditionName(String conditionName) {
        this.conditionName = conditionName;
    }

    /**
     * @param
     * @param
     * @return
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @param
     * @param
     * @return
     */
    public void setValue(String value) {
        this.value = value;
    }
}
