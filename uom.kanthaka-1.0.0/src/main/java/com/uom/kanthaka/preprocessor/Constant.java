package com.uom.kanthaka.preprocessor;

public interface Constant {

    public static final int EVENT_SMS = 0;
    public static final int EVENT_USSD = 1;
    public static final int TYPE_VOICE_CALL = 2;
    public static final int TYPE_DESCHTYPE = 3;
    public static final int TYPE_DEST_ADDRESS = 4;
    //public static final int TYPE_SMS = 5;
    
    
    static String CDR_URL = "src/main/resources/CDR";
//    static String CDR_URL = "C:\\Users\\Makumar\\Desktop\\New Folder\\CDR";
    //    static String CDR_URL = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\CDR";
   
    //  Rule format strings
    public static final String DestinationNumber = "Dest_No";
    public static final String StartsWith = "Starts_With";
    public static final String ConnectionType = "Connection_Type";
    public static final String ChannelType = "Channel_Type";
    public static final String NumOfCalls = "No_of_SMSs"; 
    public static final String NumOfSMSs = "No_of_Calls"; 
    public static final String Equals = "=";
    public static final String COMMA = ",";
    public static final String AND = "&&";
    public static final String OR = "||";

   
    //  CDR attribute names
    public static final String TimeStamp = "timeStamp";
    public static final String SourceAddress = "sourceAddress";
    public static final String DestinationAddress = "destinationAddress";
    public static final String BillingType = "billingType";
    public static final String SourceChannelType = "sourceChannelType";
   
    //  Counter RecordMap names
    public static final String CallCounterName = "No_of_Calls";
    public static final String SMSCounterName = "No_of_SMSs";
    
    public static final String EventTypeCall = "cas";
    public static final String EventTypeSMS = "sms";
    public static final String EventTypeUSSD = "ussd";
    
    //  MySQL configurations
    public static final String DatabaseDriver = "com.mysql.jdbc.Driver";
    public static final String DatabaseURL = "jdbc:mysql://localhost/kanthaka";
    public static final String DatabaseUserName = "root";
    public static final String DatabasePassword = "kanthaka";
    
}