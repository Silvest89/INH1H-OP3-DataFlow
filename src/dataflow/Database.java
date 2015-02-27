/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Johnnie Ho
 */
public class Database {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    public void validateLogin(String username, String password) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] hash = md.digest();
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager
          .getConnection("jdbc:mysql://178.62.163.116/dataflow?"
              + "user=dataflow&password=test12@#");

            preparedStatement = connect
                    .prepareStatement("SELECT * from accounts where username = ? and password = ? LIMIT 1");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();

                account.setLoggedIn(true);
                account.setUserName(resultSet.getString("username"));
                DataFlow.account = account;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    // You need to close the resultSet

    private void close() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }

            if (statement != null) {
                statement.close();
            }

            if (connect != null) {
                connect.close();
            }
        } catch (Exception e) {

        }
    }
    public void putInDatabase(String id, String timeStamp, String location, String text) throws Exception{
        try {
        // This will load the MySQL driver, each DB has its own driver
        Class.forName("com.mysql.jdbc.Driver");
        // Setup the connection with the DB
        connect = DriverManager
          .getConnection("jdbc:mysql://178.62.163.116/dataflow?"
              + "user=dataflow&password=test12@#");

        preparedStatement = connect
                    .prepareStatement("INSERT INTO Tweets VALUES (?, ?, ?, ?)");
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, timeStamp);
        preparedStatement.setString(3, location);
        preparedStatement.setString(4, text);
        preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        
    }

}
