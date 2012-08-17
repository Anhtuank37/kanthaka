/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;


/**
 * 
 * @author Makumar
 */
public class processResultSet {

  HashSet<String> setOne;
  HashSet<String> setTwo;
  HashSet<String> setThree;
  HashSet<String> setFour;
  ArrayList<ArrayList<HashSet<String>>> result;

  public processResultSet() {

    this.setOne = new HashSet<String>();
    setOne.add("1111");
    setOne.add("1112");
    setOne.add("1113");
    setOne.add("1114");
    setOne.add("1115");

    this.setTwo = new HashSet<String>();
    setTwo.add("1112");
    setTwo.add("1113");
    setTwo.add("1114");

    this.setThree = new HashSet<String>();
    setThree.add("1112");
    setThree.add("1115");
    setThree.add("1116");
    setThree.add("1117");

    this.setFour = new HashSet<String>();
    setFour.add("1112");
    setFour.add("1115");
    setFour.add("1116");
    setFour.add("1117");

    ArrayList<HashSet<String>> orOne = new ArrayList<HashSet<String>>();
    orOne.add(setOne);
    orOne.add(setTwo);
    ArrayList<HashSet<String>> orTwo = new ArrayList<HashSet<String>>();
    orTwo.add(setTwo);
    orTwo.add(setThree);
    ArrayList<HashSet<String>> orThree = new ArrayList<HashSet<String>>();
    orThree.add(setFour);

    result = new ArrayList<ArrayList<HashSet<String>>>();
    result.add(orOne);
    result.add(orTwo);
    result.add(orThree);

  }

  public static void main(String args[]) {
    processResultSet process = new processResultSet();
    process.compareResultSet(process.result);
  }

  public void compareResultSet(ArrayList<ArrayList<HashSet<String>>> resultSet) {
    HashSet<String> outerCondition = new HashSet<String>();
    int initializer = 0;
    for (int i = 0; i < resultSet.size(); i++) {
      ArrayList<HashSet<String>> orOperationList = resultSet.get(i);
      if ((initializer == 0) && (orOperationList.size() == 1)) {
        outerCondition.addAll(orOperationList.get(i));
        initializer++;
      }
      HashSet<String> innerCondition = new HashSet<String>();
      for (int j = 0; j < orOperationList.size(); j++) {
        innerCondition = union(innerCondition, orOperationList.get(j));
      }
      if ((initializer == 0)) {
        outerCondition.addAll(innerCondition);
        initializer++;
      } else {
        outerCondition = intersection(outerCondition, innerCondition);
      }
      // if (initializer != 0) {
      // outerCondition = intersection(outerCondition, innerCondition);
      // }
      System.out.println("Set : " + outerCondition);
    }
    System.out.println("Final Set : " + outerCondition);
  }

  public HashSet<String> union(HashSet<HashSet<String>> setList) {
    HashSet<String> tempSet = new HashSet<String>();
    for (Iterator<HashSet<String>> it = setList.iterator(); it.hasNext();) {
      // HashSet<String> set = it.next();
      tempSet.addAll(it.next());
    }
    return tempSet;
  }

  public HashSet<String> union(HashSet<String> setA, HashSet<String> setB) {
    HashSet<String> tempSet = new HashSet<String>(setA);
    tempSet.addAll(setB);
    return tempSet;
  }

  public HashSet<String> intersection(HashSet<String> setA, HashSet<String> setB) {
    HashSet<String> tmp = new HashSet<String>();
    for (String x : setA) {
      if (setB.contains(x)) {
        tmp.add(x);
      }
    }
    return tmp;
  }

  public HashSet<String> difference(HashSet<String> setA, HashSet<String> setB) {
    HashSet<String> tmp = new HashSet<String>(setA);
    tmp.removeAll(setB);
    return tmp;
  }

  public HashSet<String> symDifference(HashSet<String> setA,
      HashSet<String> setB) {
    HashSet<String> tmpA;
    HashSet<String> tmpB;

    tmpA = union(setA, setB);
    tmpB = intersection(setA, setB);
    return difference(tmpA, tmpB);
  }
}
