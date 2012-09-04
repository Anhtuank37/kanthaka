/*
 * Developed as a final year project in Computer Science & Engineering Department of
 * University of Moratuwa. All the content of project is owned by the University of
 * Moratuwa.
 */
package com.uom.kanthaka.cassandra.ruleexecuter;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.uom.kanthaka.preprocessor.rulereader.MysqlDatabaseUtil;
import com.uom.kanthaka.preprocessor.rulereader.Rule;

public class ProcessResultSet {

  Connection conn;
  MysqlDatabaseUtil mySqlMethod;
  final Logger logger = LoggerFactory.getLogger(ProcessResultSet.class);

  public ProcessResultSet() {

    this.mySqlMethod = new MysqlDatabaseUtil();
    this.conn = MysqlDatabaseUtil.getConnection();
  }

  /**
   * compare the String records in the ArrayList<ArrayList<HashSet<String>>>
   * data structure to select matching records to select eligible users
   * 
   * @param
   */
  public HashSet<String> compareResultSet(Rule businessRule,
      QueryRunUtill queryRunner) {
    ArrayList<ArrayList<HashSet<String>>> resultSet = businessRule
        .getCounterResultSet();
    Map<String, String> selectedUsers = businessRule.getSelectedUsers();

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
    }

    for (String selectedOne : outerCondition) {

      if (!selectedUsers.containsKey(selectedOne)) {
        selectedUsers.put(selectedOne, "selected");
      }
    }
    mySqlMethod.insertUsersToDatabase(conn, businessRule);

    if (!outerCondition.isEmpty()) {
      logger.debug("The final set for promotion {}: {}",
          businessRule.getRuleName(), outerCondition);

    }
    try {
      queryRunner.removeUser(outerCondition, businessRule);
    } catch (Exception e) {

    }
    return outerCondition;
  }

  /**
   * Compute the union of given records in HashSet<HashSet<String>>
   * 
   * @param setList
   * @return HashSet<String> of the union result
   */
  public HashSet<String> union(HashSet<HashSet<String>> setList) {
    HashSet<String> tempSet = new HashSet<String>();
    for (Iterator<HashSet<String>> it = setList.iterator(); it.hasNext();) {
      // HashSet<String> set = it.next();
      tempSet.addAll(it.next());
    }
    return tempSet;
  }

  /**
   * Compute the union of given two HashSet<String> hashSets
   * 
   * @param setA
   * @param setB
   * @return HashSet<String> of the union result
   */
  public HashSet<String> union(HashSet<String> setA, HashSet<String> setB) {
    HashSet<String> tempSet = new HashSet<String>(setA);
    tempSet.addAll(setB);
    return tempSet;
  }

  /**
   * Compute the intersection of given two HashSet<String> hashSets
   * 
   * @param setA
   * @param setB
   * @return HashSet<String> of the intersection result
   */
  public HashSet<String> intersection(HashSet<String> setA, HashSet<String> setB) {
    HashSet<String> tmp = new HashSet<String>();
    for (String x : setA) {
      if (setB.contains(x)) {
        tmp.add(x);
      }
    }
    return tmp;
  }

  /**
   * Compute the difference of given two HashSet<String> hashSets
   * 
   * @param setA
   * @param setB
   * @return HashSet<String> of the difference result
   */
  public HashSet<String> difference(HashSet<String> setA, HashSet<String> setB) {
    HashSet<String> tmp = new HashSet<String>(setA);
    tmp.removeAll(setB);
    return tmp;
  }

  /**
   * Compute the symDifference of given two HashSet<String> hashSets
   * 
   * @param setA
   * @param setB
   * @return HashSet<String> of the symDifference result
   */
  public HashSet<String> symDifference(HashSet<String> setA,
      HashSet<String> setB) {
    HashSet<String> tmpA;
    HashSet<String> tmpB;

    tmpA = union(setA, setB);
    tmpB = intersection(setA, setB);
    return difference(tmpA, tmpB);
  }
}
