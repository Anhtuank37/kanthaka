/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Makumar
 */

public class RecordMap {
  String type;
  ConcurrentHashMap<String, Long> DataMap;
  static Logger logger = LoggerFactory.getLogger(RecordMap.class.getName());

  /**
   * RecordMap constructor to initilize object variables
   * 
   * @param type
   */
  public RecordMap(String type) {
    this.type = type;
    this.DataMap = new ConcurrentHashMap<String, Long>();
  }

  /**
   * RecordMap constructor to initilize object variables
   * 
   * @param type
   * @param DataMap
   */
  public RecordMap(String type, ConcurrentHashMap<String, Long> DataMap) {
    this.type = type;
    this.DataMap = DataMap;
  }

  /**
   * Return the object type
   * 
   * @return type of the object
   */
  public String getType() {
    return type;
  }

  /**
   * return the ConcurrentHashMap of the object
   * 
   * @return ConcurrentHashMap of the object
   */
  public ConcurrentHashMap<String, Long> getDataMap() {
    return DataMap;
  }

  /**
   * Set ConcurrentHashMap of the object
   * 
   * @param DataMap
   */
  public void setDataMap(ConcurrentHashMap<String, Long> DataMap) {
    this.DataMap = DataMap;
  }

  /**
   * Initialize the ConcurrentHashMap object
   * 
   * @return
   */
  public void initilizeMap() {
    DataMap = new ConcurrentHashMap<String, Long>();
  }

}
