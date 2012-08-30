/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import com.uom.kanthaka.preprocessor.Constant;

/**
 * 
 * @author Makumar
 */
public class CdrAttributeMapping {

  final int timeStamp = 3;              // Attributes taken form CDR's and their positions
  final int sourceAddress = 9;
  final int destinationAddress = 13;
  final int billingType = 19;
  final int sourceChannelType = 11;

  /**
     * Map the attribute name with the position in the CDR file record
     * the position value in the CDR will be returned as int value
     * @param attribute
     * @return int value of the mapping attribute
     */
    public int getMappingNo(String attribute) {
        if (attribute.equalsIgnoreCase(Constant.TimeStamp)) {
            return timeStamp;
        } else if (attribute.equalsIgnoreCase(Constant.SourceAddress)) {
            return sourceAddress;
        } else if (attribute.equalsIgnoreCase(Constant.DestinationAddress)) {
            return destinationAddress;
        } else if (attribute.equalsIgnoreCase(Constant.BillingType)) {
            return billingType;
        } else {
            return sourceChannelType;
        }
    }
}
