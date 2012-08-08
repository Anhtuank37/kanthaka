/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.CDRreader;

/**
 *
 * @author Makumar
 */
import com.mysql.jdbc.Statement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class JDBCExample {

    Connection connection;

    public static void main(String[] argv) {
    }

    public Connection initiateDB() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(" - MySQL JDBC Driver Not Found - ");
            e.printStackTrace();
        }

        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost/kanthaka", "user", "abc");
//            getRulesFromDatabase(connection);
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

    public ArrayList<Rule> getRulesFromDatabase(Connection connection) {
        ArrayList<Rule> rules = new ArrayList<Rule>();
        try {
            Statement Stmt = (Statement) connection.createStatement();
            ResultSet RS = Stmt.executeQuery("SELECT name, rule " + " FROM promotions");

            while (RS.next()) {
                Rule tempRule = new Rule();
                tempRule.setRuleName(RS.getString(1));
                tempRule.setRuleString(RS.getString(2));
                rules.add(tempRule);
                System.out.print(RS.getString(1) + " - ");
                System.out.println(RS.getString(2));
            }
            System.out.println("");
            connection.close();
            RS.close();
            Stmt.close();
        } catch (SQLException E) {
            System.out.println("SQLException: " + E.getMessage());
        }
        return rules;
    }
}
