/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.cassandra.ruleexecuter;

import com.uom.kanthaka.preprocessor.rulereader.MysqlDatabaseUtil;
import com.uom.kanthaka.preprocessor.rulereader.Rule;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Makumar
 */
public class ProcessResultSet {

    HashSet<String> setOne;
    HashSet<String> setTwo;
    HashSet<String> setThree;
    HashSet<String> setFour;
    ArrayList<ArrayList<HashSet<String>>> result;
    Connection conn;
    MysqlDatabaseUtil mySqlMethod;
    final Logger logger = LoggerFactory.getLogger(ProcessResultSet.class);


    public ProcessResultSet() {

        this.mySqlMethod = new MysqlDatabaseUtil();
        this.conn = MysqlDatabaseUtil.getConnection();
    }


/**
     * compare the String records in the ArrayList<ArrayList<HashSet<String>>> data structure
     * to select matching records to select eligible users
     * @param resultSet
     */
    public void compareResultSet(Rule businessRule,QueryRunner queryRunner) {
        ArrayList<ArrayList<HashSet<String>>> resultSet =  businessRule.getCounterResultSet();
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
        mySqlMethod.insertUsersToDatabase(conn, businessRule);
        businessRule.setSelectedList(outerCondition);

        System.out.println("Final Set : " + outerCondition);
        logger.debug("Final set {}.",outerCondition);
        queryRunner.removeUser(outerCondition,businessRule);
    }

    /**
     * Compute the union of given records in HashSet<HashSet<String>>
     * @param setList
     * @return HashSet<String> of the union result
     */
    public HashSet<String> union(HashSet<HashSet<String>> setList) {
        HashSet<String> tempSet = new HashSet<String>();
        for (Iterator<HashSet<String>> it = setList.iterator(); it.hasNext();) {
//            HashSet<String> set = it.next();
            tempSet.addAll(it.next());
        }
        return tempSet;
    }

    /**
     * Compute the union of given two HashSet<String> hashSets
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
     * @param setA
     * @param setB
     * @return HashSet<String> of the symDifference result
     */
    public HashSet<String> symDifference(HashSet<String> setA, HashSet<String> setB) {
        HashSet<String> tmpA;
        HashSet<String> tmpB;

        tmpA = union(setA, setB);
        tmpB = intersection(setA, setB);
        return difference(tmpA, tmpB);
    }
}