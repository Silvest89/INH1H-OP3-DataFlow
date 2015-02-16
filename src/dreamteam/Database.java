/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreamteam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

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
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");
      // Setup the connection with the DB
      connect = DriverManager
          .getConnection("jdbc:mysql://195.238.74.60/silven1q_dreamteam?"
              + "user=silven1q_dream&password=test123");

      preparedStatement = connect
          .prepareStatement("SELECT * from accounts where username = ? and password = ? LIMIT 1");
      preparedStatement.setString(1, username);
      preparedStatement.setString(2, password);
      resultSet = preparedStatement.executeQuery();      
      if(resultSet.next())
      {
          Account account = new Account();
          
          account.setLoggedIn(true);
          account.setUserName(resultSet.getString("username"));
          DreamTeam.account = account;
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
}
