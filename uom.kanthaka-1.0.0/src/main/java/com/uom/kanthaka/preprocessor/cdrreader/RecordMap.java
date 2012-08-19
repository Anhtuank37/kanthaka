/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import java.util.concurrent.ConcurrentHashMap;


/**
 * 
 * @author Makumar
 */

public class RecordMap {
    String type;
    ConcurrentHashMap<String, Long> DataMap;

    /**
     * RecordMap constructor to initilize object variables
     * @param type
     */
    public RecordMap(String type) {
        this.type = type;
        this.DataMap = new ConcurrentHashMap<String, Long>();
    }

    /**
     * RecordMap constructor to initilize object variables
     * @param type
     * @param DataMap
     */
    public RecordMap(String type, ConcurrentHashMap<String, Long> DataMap) {
        this.type = type;
        this.DataMap = DataMap;
    }
    
    /**
     * Return the object type
     * @return type of the object
     */
    public String getType() {
        return type;
    }

    /**
     * return the ConcurrentHashMap of the object
     * @return ConcurrentHashMap of the object
     */
    public ConcurrentHashMap<String, Long> getDataMap() {
        return DataMap;
    }

    /**
     * Set ConcurrentHashMap of the object
     * @param DataMap
     */
    public void setDataMap(ConcurrentHashMap<String, Long> DataMap) {
        this.DataMap = DataMap;
    }
    
    /**
     * Initialize the ConcurrentHashMap object
     * @return
     */
    public void initilizeMap(){
        DataMap = new ConcurrentHashMap<String, Long>();
    }

}
