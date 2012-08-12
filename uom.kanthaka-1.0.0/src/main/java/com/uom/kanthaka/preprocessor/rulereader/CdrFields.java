/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.uom.kanthaka.preprocessor.rulereader;

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
    
    /**
     * 
     * @param 
     * @param 
     * @return
     */
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

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    private String[] getCalled_No() {
        return Called_No;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    private String[] getSMSed_No() {
        return Smsed_No;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    private String[] getConnection_Type() {
        return Connection_Type;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    private String[] getNo_of_Calls() {
        return No_of_Calls;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    private String[] getNo_of_SMSs() {
        return No_of_SMSs;
    }
    
}
