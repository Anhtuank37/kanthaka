package com.uom.kanthaka.preprocessor;

public interface Constant {

  // CDR counter value for read records
  public static final int CDR_Counter_Val = 10000;

  static String CDR_URL = "/home/pushpalanka/HomeCluster/CDR";

  // Rule format strings
  public static final String DestinationNumber = "Dest_No";
  public static final String StartsWith = "Starts_With";
  public static final String ConnectionType = "Connection_Type";
  public static final String ChannelType = "Channel_Type";
  public static final String NumOfCalls = "No_of_Calls";
  public static final String NumOfSMSs = "No_of_SMSs";
  public static final String Equals = "=";
  public static final String COMMA = ",";
  public static final String AND = "&&";
  public static final String OR = "||";

  // CDR attribute names
  public static final String TimeStamp = "timeStamp";
  public static final String SourceAddress = "sourceAddress";
  public static final String DestinationAddress = "destinationAddress";
  public static final String BillingType = "billingType";
  public static final String SourceChannelType = "sourceChannelType";

  // Counter RecordMap names
  public static final String CallCounterName = "No_of_Calls";
  public static final String SMSCounterName = "No_of_SMSs";

  public static final String EventTypeCall = "cas";
  public static final String EventTypeSMS = "sms";
  public static final String EventTypeUSSD = "ussd";

  // MySQL configurations
  public static final String DatabaseDriver = "com.mysql.jdbc.Driver";
  public static final String DatabaseURL = "jdbc:mysql://localhost/kanthaka";
  public static final String DatabaseUserName = "root";
  public static final String DatabasePassword = "kanthaka";
  // public static final String DatabasePassword = "abc";

  public static final String[] cdrReadingFields = { "timeStamp",
      "sourceAddress", "destinationAddress", "billingType", "sourceChannelType" };
}
