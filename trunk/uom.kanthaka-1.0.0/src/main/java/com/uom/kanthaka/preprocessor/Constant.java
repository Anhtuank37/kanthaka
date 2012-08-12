package main.java.com.uom.kanthaka.preprocessor;
public interface Constant {

	  
    public static final int EVENT_SMS = 0;
    public static final int EVENT_USSD = 1;
    public static final int TYPE_VOICE_CALL = 2;
    
    public static final int TYPE_DESCHTYPE = 3;
    public static final int TYPE_DEST_ADDRESS = 4;
    //public static final int TYPE_SMS = 5;
    
    public static final String STRING_TYPE_SMS = "sms";
    public static final String STRING_TYPE_USSD = "ussd";
    
    static String  RULE_URL = "resources/Rules";
    static String CDR_URL = "resources/CDR";
    
//    static String RULE_URL = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\Rules";
//    static String CDR_URL = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\CDR";

}
