/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.uom.kanthaka.preprocessor.CDRreader;

/**
 *
 * @author Makumar
 */
public class conditionField {

    String conditionName;
    String condition;
    String value;

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public conditionField(String conditionName, String value) {
        this.conditionName = conditionName;
        this.value = value;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public conditionField(String conditionName, String condition, String value) {
        this.conditionName = conditionName;
        this.condition = condition;
        this.value = value;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public String printDetails() {
        return conditionName + " - " + value;
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
