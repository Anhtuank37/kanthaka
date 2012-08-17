/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.uom.kanthaka.preprocessor.cdrreader.CdrFields;

/**
 * 
 * @author Makumar
 */
public class ReadBusinessRule {

  Connection connection;
  ArrayList<Rule> rules;

  /**
   * Constructor to initiate class object
   */
  public ReadBusinessRule() {
    rules = new ArrayList<Rule>();
  }

  /**
   * 
   * @return ArrayList<Rule>
   */
  public ArrayList<Rule> readFilesOnPath() {
    MysqlDatabaseUtil databaseConnect = new MysqlDatabaseUtil();
    connection = databaseConnect.initiateDB();
    rules = databaseConnect.getRulesFromDatabase(connection);

    for (int i = 0; i < rules.size(); i++) {
      Rule tempRule = rules.get(i);
      processBusinessRule(tempRule);
    }
    return rules;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public void processBusinessRule(Rule tempRuleComp) {
    StringTokenizer strAND = new StringTokenizer(tempRuleComp.getRuleString(),
        "&&");
    // int ruleNumb = strAND.countTokens();
    while (strAND.hasMoreElements()) {
      String ruleComp = (String) strAND.nextElement();
      StringTokenizer strOR = new StringTokenizer(ruleComp, "||");
      if (strOR.countTokens() > 1) {
        ArrayList<ConditionField> conditionOrFields = new ArrayList<ConditionField>();
        ArrayList<CounterConditionFields> counterOrFields = new ArrayList<CounterConditionFields>();
        while (strOR.hasMoreElements()) {
          String ruleParts = (String) strOR.nextElement();
          StringTokenizer strSpace = new StringTokenizer(ruleParts);
          while (strSpace.hasMoreElements()) {
            String ruleComponent = ((String) strSpace.nextElement());
            if (ruleComponent.equalsIgnoreCase("Dest_No")) {
              String temp1 = (String) strSpace.nextElement();
              String temp2 = (String) strSpace.nextElement();
              ConditionField conField1 = new ConditionField(ruleComponent,
                  temp1, temp2);
              conditionOrFields.add(conField1);
              tempRuleComp.getFields().add(ruleComponent);
              getCdrFields(ruleComponent, tempRuleComp);
              // } else if (ruleComponent.equalsIgnoreCase("Smsed_No")) {
              // String temp1 = (String) strSpace.nextElement();
              // String temp2 = (String) strSpace.nextElement();
              // ConditionField conField1 = new ConditionField(ruleComponent,
              // temp2);
              // conditionOrFields.add(conField1);
              // tempRuleComp.getFields().add(ruleComponent);
              // getCdrFields(ruleComponent, tempRuleComp);
            } else if (ruleComponent.equalsIgnoreCase("Connection_Type")) {
              String temp1 = (String) strSpace.nextElement();
              String temp2 = (String) strSpace.nextElement();
              ConditionField conField = new ConditionField(ruleComponent,
                  temp1, temp2);
              conditionOrFields.add(conField);
              tempRuleComp.getFields().add(ruleComponent);
              getCdrFields(ruleComponent, tempRuleComp);
            } else if (ruleComponent.equalsIgnoreCase("No_of_Calls")) {
              String temp1 = (String) strSpace.nextElement();
              Long temp2 = Long.parseLong((String) strSpace.nextElement());
              ConditionField conField = new ConditionField("Channel_Type",
                  temp1, "cas");
              conditionOrFields.add(conField);
              counterOrFields.add(new CounterConditionFields("No_of_Calls",
                  temp1, temp2));
              tempRuleComp.getFields().add(ruleComponent);
              tempRuleComp.getCounters().add(ruleComponent);
              getCdrFields(ruleComponent, tempRuleComp);
            } else if (ruleComponent.equalsIgnoreCase("No_of_SMSs")) {
              String temp1 = (String) strSpace.nextElement();
              Long temp2 = Long.parseLong((String) strSpace.nextElement());
              ConditionField conField = new ConditionField("Channel_Type",
                  temp1, "sms");
              conditionOrFields.add(conField);
              counterOrFields.add(new CounterConditionFields("No_of_SMSs",
                  temp1, temp2));
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
        ArrayList<ConditionField> conditionOrFields = new ArrayList<ConditionField>();
        ArrayList<CounterConditionFields> counterOrFields = new ArrayList<CounterConditionFields>();
        StringTokenizer strSpace = new StringTokenizer(ruleComp);
        while (strSpace.hasMoreElements()) {
          String ruleComponent = ((String) strSpace.nextElement());
          if (ruleComponent.equalsIgnoreCase("Dest_No")) {
            String temp1 = (String) strSpace.nextElement();
            String temp2 = (String) strSpace.nextElement();
            ConditionField conField1 = new ConditionField(ruleComponent, temp1,
                temp2);
            conditionOrFields.add(conField1);
            tempRuleComp.getFields().add(ruleComponent);
            getCdrFields(ruleComponent, tempRuleComp);
            break;
            // } else if (ruleComponent.equalsIgnoreCase("Smsed_No")) {
            // String temp1 = (String) strSpace.nextElement();
            // String temp2 = (String) strSpace.nextElement();
            // ConditionField conField1 = new ConditionField(ruleComponent,
            // temp2);
            // conditionOrFields.add(conField1);
            // tempRuleComp.getFields().add(ruleComponent);
            // getCdrFields(ruleComponent, tempRuleComp);
            // break;
          } else if (ruleComponent.equalsIgnoreCase("Connection_Type")) {
            String temp1 = (String) strSpace.nextElement();
            String temp2 = (String) strSpace.nextElement();
            ConditionField conField = new ConditionField(ruleComponent, temp1,
                temp2);
            conditionOrFields.add(conField);
            tempRuleComp.getFields().add(ruleComponent);
            getCdrFields(ruleComponent, tempRuleComp);
            break;
          } else if (ruleComponent.equalsIgnoreCase("No_of_Calls")) {
            String temp1 = (String) strSpace.nextElement();
            Long temp2 = Long.parseLong((String) strSpace.nextElement());
            ConditionField conField = new ConditionField("Channel_Type", temp1,
                "cas");
            conditionOrFields.add(conField);
            counterOrFields.add(new CounterConditionFields("No_of_Calls",
                temp1, temp2));
            tempRuleComp.getFields().add(ruleComponent);
            tempRuleComp.getCounters().add(ruleComponent);
            getCdrFields(ruleComponent, tempRuleComp);
            break;
          } else if (ruleComponent.equalsIgnoreCase("No_of_SMSs")) {
            String temp1 = (String) strSpace.nextElement();
            Long temp2 = Long.parseLong((String) strSpace.nextElement());
            ConditionField conField = new ConditionField("Channel_Type", temp1,
                "sms");
            conditionOrFields.add(conField);
            counterOrFields.add(new CounterConditionFields("No_of_SMSs", temp1,
                temp2));
            tempRuleComp.getFields().add(ruleComponent);
            tempRuleComp.getCounters().add(ruleComponent);
            getCdrFields(ruleComponent, tempRuleComp);
            break;
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
