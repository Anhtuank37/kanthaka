package com.uom.kanthaka.preprocessor.rulereader;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Makumar
 */
public class RecordMap {
    String type;
    ConcurrentHashMap<String, Long> DataMap;

    public RecordMap(String type) {
        this.type = type;
        this.DataMap = new ConcurrentHashMap<String, Long>();
    }

    public RecordMap(String type, ConcurrentHashMap<String, Long> DataMap) {
        this.type = type;
        this.DataMap = DataMap;
    }
    
    public String getType() {
        return type;
    }

    public ConcurrentHashMap<String, Long> getDataMap() {
        return DataMap;
    }

    public void setDataMap(ConcurrentHashMap<String, Long> DataMap) {
        this.DataMap = DataMap;
    }
    
    public void initilizeMap(){
        DataMap = new ConcurrentHashMap<String, Long>();
    }
    
    
}
