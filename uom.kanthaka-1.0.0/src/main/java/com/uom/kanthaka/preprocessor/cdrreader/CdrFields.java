/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import com.uom.kanthaka.preprocessor.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Makumar
 */
public class CdrFields {

    final String Dest_No[] = {"destinationAddress"};
    final String Connection_Type[] = {"billingType"};
    final String No_of_Calls[] = {"sourceChannelType"};
    final String No_of_SMSs[] = {"sourceChannelType"};
    final Logger logger = LoggerFactory.getLogger(CdrFields.class);

    /**
     * return the String[] which mapping to given input type
     *
     * @param type
     * @return String[] which maps to the given input type
     */
    public String[] getList(String type) {
        if (type.equalsIgnoreCase(Constant.DestinationNumber)) {
            return getDest_No();
        } else if (type.equalsIgnoreCase(Constant.ConnectionType)) {
            return getConnection_Type();
        } else if (type.equalsIgnoreCase(Constant.NumOfCalls)) {
            return getNo_of_Calls();
        } else if (type.equalsIgnoreCase(Constant.NumOfSMSs)) {
            return getNo_of_SMSs();
        } else {
            return null;
        }
    }

    /**
     * Return Called_No array
     *
     * @return String[] of Called_No
     */
    private String[] getDest_No() {
        return Dest_No;
    }

    /**
     * Return Connection_Type array
     *
     * @return String[] of Connection_Type
     */
    private String[] getConnection_Type() {
        return Connection_Type;
    }

    /**
     * Return No_of_Calls array
     *
     * @return String[] of No_of_Calls
     */
    private String[] getNo_of_Calls() {
        return No_of_Calls;
    }

    /**
     * Return No_of_SMSs array
     *
     * @return String[] of No_of_SMSs
     */
    private String[] getNo_of_SMSs() {
        return No_of_SMSs;
    }

}
