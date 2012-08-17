/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.rulereader;

/**
 *
 * @author Makumar
 */
//import com.mysql.jdbc.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import com.mysql.jdbc.Statement;

public class MysqlDatabaseUtil {

  static Connection connection;

  public static void main(String[] argv) {
    MysqlDatabaseUtil data = new MysqlDatabaseUtil();
    Connection conn = data.initiateDB();
    // data.insertUsersToDatabase(conn);
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public Connection initiateDB() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      System.out.println(" - MySQL JDBC Driver Not Found - ");
      e.printStackTrace();
    }

    try {
      connection = DriverManager.getConnection(
          "jdbc:mysql://localhost/kanthaka", "root", "kanthaka");
      // getRulesFromDatabase(connection);
    } catch (SQLException e) {
      System.out.println("Connection Failed !" + e);
      e.printStackTrace();
    }
    if (connection != null) {
      System.out.println("connection established !");
    } else {
      System.out.println("Failed to make connection!");
    }
    return connection;
  }

  /**
   * 
   * @param
   * @param
   * @return
   */
  public ArrayList<Rule> getRulesFromDatabase(Connection connection) {
    ArrayList<Rule> rules = new ArrayList<Rule>();
    try {
      Statement Stmt = (Statement) connection.createStatement();
      ResultSet RS = Stmt.executeQuery("SELECT name, rule "
          + " FROM promotions");

      while (RS.next()) {
        Rule tempRule = new Rule();
        tempRule.setRuleName(RS.getString(1).replaceAll(" ", "_"));
        System.out.println(tempRule.getRuleName());
        tempRule.setRuleString(RS.getString(2));
        rules.add(tempRule);
        System.out.print(RS.getString(1) + " - ");
        System.out.println(RS.getString(2));
      }
      System.out.println("");
      // connection.close();
      RS.close();
      Stmt.close();
    } catch (SQLException E) {
      System.out.println("SQLException: " + E.getMessage());
    }
    return rules;
  }

  public static Connection getConnection() {
    return connection;
  }

  public void insertUsersToDatabase(Connection connection, Rule businessRule) {

    ArrayList<Rule> rules = new ArrayList<Rule>();
    try {
      Statement Stmt = (Statement) connection.createStatement();
      HashSet<String> selectedList = businessRule.getSelectedList();
      for (String s : selectedList) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        Stmt.executeUpdate("INSERT into selectedusers values ('"
            + businessRule.getRuleName() + "','" + s + "','"
            + dateFormat.format(date) + "')");

      }
      // Stmt.executeUpdate("INSERT into selectedUsers (name, number) values (729729, 5)");

      // stmt.executeUpdate("insert into employee (First_Name ,Last_Name , Address , Salary ) values ( '"
      // + s1+ "','"+s2+ "','"+s3+ "',"+s4 + ")");

      System.out.println("");
      // connection.close();
      Stmt.close();
    } catch (SQLException E) {
      System.out.println("SQLException: " + E.getMessage());
    }
  }

}
