package com.uom.kanthaka.preprocessor.rulereader;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import com.uom.kanthaka.preprocessor.CDRreader.counterConditionFields;

/**
 *
 * @author Makumar
 */
public class processResultSet {

    ArrayList<String> setOne;
    ArrayList<String> setTwo;
    ArrayList<String> setThree;
    ArrayList<String> setFour;
    ArrayList<ArrayList<counterConditionFields>> counterConditionFields;

    public processResultSet() {
        counterConditionFields = new ArrayList<ArrayList<counterConditionFields>>();
        ArrayList<counterConditionFields> list1 = new ArrayList<counterConditionFields>();
        ArrayList<counterConditionFields> list2 = new ArrayList<counterConditionFields>();
        ArrayList<counterConditionFields> list3 = new ArrayList<counterConditionFields>();
        list1.add(new counterConditionFields("Tel", ">", "5"));

        list2.add(new counterConditionFields("Tel", ">", "5"));
        list2.add(new counterConditionFields("Tel", ">", "5"));

        list3.add(new counterConditionFields("Tel", ">", "5"));

        this.setOne = new ArrayList<String>();
        setOne.add("1111");
        setOne.add("1112");
        setOne.add("1113");
        setOne.add("1114");
        setOne.add("1115");

        this.setTwo = new ArrayList<String>();
        setTwo.add("1112");
        setTwo.add("1113");
        setTwo.add("1114");

        this.setThree = new ArrayList<String>();
        setThree.add("1112");
        setThree.add("1115");
        setThree.add("1116");
        setThree.add("1117");

        this.setFour = new ArrayList<String>();
        setThree.add("1112");
        setThree.add("1115");
        setThree.add("1116");
        setThree.add("1117");
    }

    public static void main(String args[]) {
    }

//    public void compareCdrAndRule() {
//        //ArrayList<ArrayList<conditionField>> conditionComp = getRule().getConditionFields();
//        boolean condition = true;
//        for (int i = 0; i < counterConditionFields.size(); i++) {
//            ArrayList<counterConditionFields> conditionOr = counterConditionFields.get(i);
//            int count = 0;
//            Set<String> innerCondition;
//            for (int k = 0; k < conditionOr.size(); k++) {
//                innerCondition = union(innerCondition, innerCondition);
////                counterConditionFields conField = conditionOr.get(k);
//////                System.out.print(conField.getConditionName() + ", " + conField.getValue());
////                if (conditionOr.size() > 1) {
////                    innerCondition = (innerCondition || checkCdrAttribute(conField, this));
////                } else {
////                    innerCondition = checkCdrAttribute(conField, this);
////                }
////                count++;
////                if (count > 0 && count < conditionOr.size()) {
//////                    System.out.print(" OR ");
////                }
//            }
//            if ((j < conditionComp.size() - 1) && !(count == 0)) {
////                System.out.println("");
////                System.out.println(" AND ");
//            }
//            condition = (condition && innerCondition);
//        }
////        System.out.println("");
//        if (condition) {
////            System.out.println(" ------------------------- ");
////            System.out.println(" ----- Satisfy Rule ------ " + tempRuleComp.getRuleName());
////            System.out.println(" ------------------------- ");
////            System.out.println("");
//            if ((this.getSourceChannelType() != null) && this.getSourceChannelType().equalsIgnoreCase("cas")) {
//                ConcurrentHashMap<String, Long> callCounter = getCallMap().getDataMap();
//                if (callCounter.containsKey(this.getSourceAddress())) {
//                    long currentCount = callCounter.get(this.getSourceAddress());
//                    callCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
//                } else {
//                    callCounter.put(this.getSourceAddress(), 1L);
//                }
////                ConcurrentHashMap<String, Long> callCounter = getRule().getRuleMaps().get("No_of_Calls");
////                if (callCounter.containsKey(this.getSourceAddress())) {
////                    long currentCount = callCounter.get(this.getSourceAddress());
////                    callCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
//////                    callCounter.remove(this.getSourceAddress());
//////                    callCounter.put(this.getSourceAddress(), currentCount + 1L);
////                } else {
////                    callCounter.put(this.getSourceAddress(), 1L);
////                }
//            } else if ((this.getSourceChannelType() != null) && this.getSourceChannelType().equalsIgnoreCase("sms")) {
//                ConcurrentHashMap<String, Long> smsCounter = getSmsMap().getDataMap();
//                if (smsCounter.containsKey(this.getSourceAddress())) {
//                    long currentCount = smsCounter.get(this.getSourceAddress());
//                    smsCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
//                } else {
//                    smsCounter.put(this.getSourceAddress(), 1L);
//                }
////                ConcurrentHashMap<String, Long> smsCounter = getRule().getRuleMaps().get("No_of_SMSs");
////                if (smsCounter.containsKey(this.getSourceAddress())) {
////                    long currentCount = smsCounter.get(this.getSourceAddress());
////                    smsCounter.replace(this.getSourceAddress(), currentCount, currentCount + 1L);
//////                    smsCounter.put(this.getSourceAddress(), currentCount + 1L);
////                } else {
////                    smsCounter.put(this.getSourceAddress(), 1L);
////                }
//            } else {
//            }
//        }
//    }

    public static Set<String> union(Set<String> setA, Set<String> setB) {
        Set<String> tmp = new TreeSet<String>(setA);
        tmp.addAll(setB);
        return tmp;
    }

    public static Set<String> intersection(Set<String> setA, Set<String> setB) {
        Set<String> tmp = new TreeSet<String>();
        for (String x : setA) {
            if (setB.contains(x)) {
                tmp.add(x);
            }
        }
        return tmp;
    }

    public static Set<String> difference(Set<String> setA, Set<String> setB) {
        Set<String> tmp = new TreeSet<String>(setA);
        tmp.removeAll(setB);
        return tmp;
    }

    public static Set<String> symDifference(Set<String> setA, Set<String> setB) {
        Set<String> tmpA;
        Set<String> tmpB;

        tmpA = union(setA, setB);
        tmpB = intersection(setA, setB);
        return difference(tmpA, tmpB);
    }
}
