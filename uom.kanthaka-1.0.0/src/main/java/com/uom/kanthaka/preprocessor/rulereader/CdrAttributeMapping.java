package com.uom.kanthaka.preprocessor.rulereader;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Makumar
 */
//      Called No           -   sourceAddress, sourceChannelType, destinationAddress
//	SMSed No            -   sourceAddress, sourceChannelType, destinationAddress 
//	No of Calls         -   sourceAddress, sourceChannelType
//	No of SMSs          -   sourceAddress, sourceChannelType 
//	Connection Type     -   sourceAddress, billingType
//	Call Duration
public class CdrAttributeMapping {

    final int timeStamp = 3;
    final int sourceAddress = 9;
    final int destinationAddress = 13;
    final int billingType = 19;
    final int sourceChannelType = 11;
//    int duration = ;

    public int getMappingNo(String attribute) {
        if (attribute.equalsIgnoreCase("timeStamp")) {
            return timeStamp;
        } else if (attribute.equalsIgnoreCase("sourceAddress")) {
            return sourceAddress;
        } else if (attribute.equalsIgnoreCase("destinationAddress")) {
            return destinationAddress;
        } else if (attribute.equalsIgnoreCase("billingType")) {
            return billingType;
        } else {
            return sourceChannelType;
        }
    }
}
