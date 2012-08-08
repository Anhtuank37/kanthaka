package com.uom.kanthaka.preprocessor.rulereader;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Makumar
 */
public class CdrFields {

//    String timeStamp;
//    String sourceAddress;
//    String destinationAddress;
//    final String billingType[] = {"prePaid", "postPain"};
//    final String destChannelType[] = {"sms", "cas", "wap-push", "ussd"};
//    final String duration = "Aggrigate";
    
//    final String Call_Duration[] = {};
    final String Called_No[] = {"destinationAddress"};
    final String Smsed_No[] = {"destinationAddress"};
    final String Connection_Type[] = {"billingType"};
    final String No_of_Calls[] = {"sourceChannelType"};
    final String No_of_SMSs[] = {"sourceChannelType"};
    
    public String[] getList(String type){
        if (type.equalsIgnoreCase("Called_No")) {
            return getCalled_No();
        } else if(type.equalsIgnoreCase("Smsed_No")){
            return getSMSed_No();
        } else if(type.equalsIgnoreCase("Connection_Type")){
            return getConnection_Type();
        } else if(type.equalsIgnoreCase("No_of_Calls")){
            return getNo_of_Calls();
        } else if(type.equalsIgnoreCase("No_of_SMSs")){
            return getNo_of_SMSs();
        }else {
            return null;
        }
    }

    private String[] getCalled_No() {
        return Called_No;
    }

    private String[] getSMSed_No() {
        return Smsed_No;
    }

    private String[] getConnection_Type() {
        return Connection_Type;
    }

    private String[] getNo_of_Calls() {
        return No_of_Calls;
    }

    private String[] getNo_of_SMSs() {
        return No_of_SMSs;
    }
    
}
