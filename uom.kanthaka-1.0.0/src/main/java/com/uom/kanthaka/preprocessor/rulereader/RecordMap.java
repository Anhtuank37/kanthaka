/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.uom.kanthaka.preprocessor.rulereader;

import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Makumar
 */
public class RecordMap {
    String type;
    ConcurrentHashMap<String, Long> DataMap;

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public RecordMap(String type) {
        this.type = type;
        this.DataMap = new ConcurrentHashMap<String, Long>();
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public RecordMap(String type, ConcurrentHashMap<String, Long> DataMap) {
        this.type = type;
        this.DataMap = DataMap;
    }
    
    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ConcurrentHashMap<String, Long> getDataMap() {
        return DataMap;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void setDataMap(ConcurrentHashMap<String, Long> DataMap) {
        this.DataMap = DataMap;
    }
    
    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void initilizeMap(){
        DataMap = new ConcurrentHashMap<String, Long>();
    }
    
    
}
