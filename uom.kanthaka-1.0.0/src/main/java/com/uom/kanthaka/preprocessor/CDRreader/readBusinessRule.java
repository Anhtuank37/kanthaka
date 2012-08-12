/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.com.uom.kanthaka.preprocessor.CDRreader;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;
import main.java.com.uom.kanthaka.preprocessor.rulereader.CdrFields;

/**
 *
 * @author Makumar
 */
public class readBusinessRule {

    Connection connection;
    ArrayList<Rule> rules;
    //String url = "C:\\Users\\Makumar\\Documents\\NetBeansProjects\\XML Read\\Rules";

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public readBusinessRule() {
        rules = new ArrayList<Rule>();
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public ArrayList<Rule> readFilesOnPath() {
        MysqlDatabaseUtil databaseConnect = new MysqlDatabaseUtil();
        connection = databaseConnect.initiateDB();
        rules = databaseConnect.getRulesFromDatabase(connection);

        for (int i = 0; i < rules.size(); i++) {
            Rule tempRule = rules.get(i);
            createQueryForRuleFile(tempRule);
        }
//        File files[];
//        files = path.listFiles();
//
//        for (int i = 0, n = files.length; i < n; i++) {
//            Rule tempRule = new Rule();
//            String file = files[i].toString();
////            tempRule.createCounterMaps(tempRule.getCounters());
//            rules.add(createQueryForRuleFile(file, tempRule));
//        }
        return rules;
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void createQueryForRuleFile(Rule tempRuleComp) {
//        String inputRule = "";
//        try {
//            BufferedReader bufReader = new BufferedReader(new FileReader(path));
//            inputRule = bufReader.readLine();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        String ruleName = (inputRule.split(" "))[0];
//        tempRuleComp.setRuleName(ruleName);
//        inputRule = inputRule.substring(ruleName.length());

        StringTokenizer strAND = new StringTokenizer(tempRuleComp.getRuleString(), "&&");
//        int ruleNumb = strAND.countTokens();
        while (strAND.hasMoreElements()) {
            String ruleComp = (String) strAND.nextElement();
            StringTokenizer strOR = new StringTokenizer(ruleComp, "||");
            if (strOR.countTokens() > 1) {
                ArrayList<conditionField> conditionOrFields = new ArrayList<conditionField>();
                ArrayList<counterConditionFields> counterOrFields = new ArrayList<counterConditionFields>();
                while (strOR.hasMoreElements()) {
                    String ruleParts = (String) strOR.nextElement();
                    StringTokenizer strSpace = new StringTokenizer(ruleParts);
                    while (strSpace.hasMoreElements()) {
                        String ruleComponent = ((String) strSpace.nextElement());
                        if (ruleComponent.equalsIgnoreCase("Called_No")) {
                            String temp1 = (String) strSpace.nextElement();
                            String temp2 = (String) strSpace.nextElement();
                            conditionField conField1 = new conditionField(ruleComponent, temp2);
                            conditionOrFields.add(conField1);
                            tempRuleComp.getFields().add(ruleComponent);
                            getCdrFields(ruleComponent, tempRuleComp);
                        } else if (ruleComponent.equalsIgnoreCase("Smsed_No")) {
                            String temp1 = (String) strSpace.nextElement();
                            String temp2 = (String) strSpace.nextElement();
                            conditionField conField1 = new conditionField(ruleComponent, temp2);
                            conditionOrFields.add(conField1);
                            tempRuleComp.getFields().add(ruleComponent);
                            getCdrFields(ruleComponent, tempRuleComp);
                        } else if (ruleComponent.equalsIgnoreCase("Connection_Type")) {
                            String temp1 = (String) strSpace.nextElement();
                            String temp2 = (String) strSpace.nextElement();
                            conditionField conField = new conditionField(ruleComponent, temp2);
                            conditionOrFields.add(conField);
                            tempRuleComp.getFields().add(ruleComponent);
                            getCdrFields(ruleComponent, tempRuleComp);
                        } else if (ruleComponent.equalsIgnoreCase("No_of_Calls")) {
                            String temp1 = (String) strSpace.nextElement();
                            Long temp2 = Long.parseLong((String) strSpace.nextElement());
                            conditionField conField = new conditionField("Channel_Type", "cas");
                            conditionOrFields.add(conField);
                            counterOrFields.add(new counterConditionFields("No_of_Calls", temp1, temp2));
                            tempRuleComp.getFields().add(ruleComponent);
                            tempRuleComp.getCounters().add(ruleComponent);
                            getCdrFields(ruleComponent, tempRuleComp);
                        } else if (ruleComponent.equalsIgnoreCase("No_of_SMSs")) {
                            String temp1 = (String) strSpace.nextElement();
                            Long temp2 = Long.parseLong((String) strSpace.nextElement());
                            conditionField conField = new conditionField("Channel_Type", "sms");
                            conditionOrFields.add(conField);
                            counterOrFields.add(new counterConditionFields("No_of_SMSs", temp1, temp2));
                            tempRuleComp.getFields().add(ruleComponent);
                            tempRuleComp.getCounters().add(ruleComponent);
                            getCdrFields(ruleComponent, tempRuleComp);
                        } else {
                        }
                    }
                }
                tempRuleComp.getConditionFields().add(conditionOrFields);
                if (counterOrFields.size() > 0) {
                    tempRuleComp.getCounterConditionFields().add(counterOrFields);
                }
            } else {
                ArrayList<conditionField> conditionOrFields = new ArrayList<conditionField>();
                ArrayList<counterConditionFields> counterOrFields = new ArrayList<counterConditionFields>();
                StringTokenizer strSpace = new StringTokenizer(ruleComp);
                while (strSpace.hasMoreElements()) {
                    String ruleComponent = ((String) strSpace.nextElement());
                    if (ruleComponent.equalsIgnoreCase("Called_No")) {
                        String temp1 = (String) strSpace.nextElement();
                        String temp2 = (String) strSpace.nextElement();
                        conditionField conField1 = new conditionField(ruleComponent, temp2);
                        conditionOrFields.add(conField1);
                        tempRuleComp.getFields().add(ruleComponent);
                        getCdrFields(ruleComponent, tempRuleComp);
                        //break;
                    } else if (ruleComponent.equalsIgnoreCase("Smsed_No")) {
                        String temp1 = (String) strSpace.nextElement();
                        String temp2 = (String) strSpace.nextElement();
                        conditionField conField1 = new conditionField(ruleComponent, temp2);
                        conditionOrFields.add(conField1);
                        tempRuleComp.getFields().add(ruleComponent);
                        getCdrFields(ruleComponent, tempRuleComp);
                        //break;
                    } else if (ruleComponent.equalsIgnoreCase("Connection_Type")) {
                        String temp1 = (String) strSpace.nextElement();
                        String temp2 = (String) strSpace.nextElement();
                        conditionField conField = new conditionField(ruleComponent, temp2);
                        conditionOrFields.add(conField);
                        tempRuleComp.getFields().add(ruleComponent);
                        getCdrFields(ruleComponent, tempRuleComp);
                        //break;
                    } else if (ruleComponent.equalsIgnoreCase("No_of_Calls")) {
                        String temp1 = (String) strSpace.nextElement();
                        Long temp2 = Long.parseLong((String) strSpace.nextElement());
                        conditionField conField = new conditionField("Channel_Type", "sms");
                        conditionOrFields.add(conField);
                        counterOrFields.add(new counterConditionFields("No_of_Calls", temp1, temp2));
                        tempRuleComp.getFields().add(ruleComponent);
                        tempRuleComp.getCounters().add(ruleComponent);
                        getCdrFields(ruleComponent, tempRuleComp);
                        //break;
                    } else if (ruleComponent.equalsIgnoreCase("No_of_SMSs")) {
                        String temp1 = (String) strSpace.nextElement();
                        Long temp2 = Long.parseLong((String) strSpace.nextElement());
                        conditionField conField = new conditionField("Channel_Type", "sms");
                        conditionOrFields.add(conField);
                        counterOrFields.add(new counterConditionFields("No_of_SMSs", temp1, temp2));
                        tempRuleComp.getFields().add(ruleComponent);
                        tempRuleComp.getCounters().add(ruleComponent);
                        getCdrFields(ruleComponent, tempRuleComp);
                        //break;
                    } else {
                    }
                }
                tempRuleComp.getConditionFields().add(conditionOrFields);
                if (counterOrFields.size() > 0) {
                    tempRuleComp.getCounterConditionFields().add(counterOrFields);
                }
            }

        }
    }

    /**
     * 
     * @param 
     * @param 
     * @return
     */
    public void getCdrFields(String type, Rule tempRuleComp) {
        CdrFields cdr = new CdrFields();
        String temp[] = cdr.getList(type);
        for (int i = 0; i < temp.length; i++) {
            tempRuleComp.getCdrReadingFields().add(temp[i]);
        }
    }
}
