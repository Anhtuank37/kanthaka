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
// Called No - sourceAddress, sourceChannelType, destinationAddress
// SMSed No - sourceAddress, sourceChannelType, destinationAddress
// No of Calls - sourceAddress, sourceChannelType
// No of SMSs - sourceAddress, sourceChannelType
// Connection Type - sourceAddress, billingType
// Call Duration
public class CdrAttributeMapping {

  final int timeStamp = 3;
  final int sourceAddress = 9;
  final int destinationAddress = 13;
  final int billingType = 19;
  final int sourceChannelType = 11;

  // int duration = ;

  /**
     * Map the attribute name with the position in the CDR file record
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
