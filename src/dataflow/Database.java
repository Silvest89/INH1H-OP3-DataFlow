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
import java.sql.Statement;
import java.util.ArrayList;
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

    public Database() {
        // This will load the MySQL driver, each DB has its own driver
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // Setup the connection with the DB
            connect = DriverManager
                    .getConnection("jdbc:mysql://178.62.163.116/dataflow?"
                            + "user=dataflow&password=test12@#");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void validateLogin(String username, String password) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();

            preparedStatement = connect
                    .prepareStatement("SELECT * from accounts where username = ? and password = ? LIMIT 1");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, new BASE64Encoder().encode(hash));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Account account = new Account();
                account.setUserName(resultSet.getString("username"));
                account.setAccessLevel(resultSet.getInt("access_level"));
                DataFlow.account = account;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

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

    public void putInDatabase(String id, String timeStamp, String user, String location, String text) throws Exception {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO Tweets VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, timeStamp);
            preparedStatement.setString(3, user);
            preparedStatement.setString(4, location);
            preparedStatement.setString(5, text);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            //close();
        }
    }
    
    public ArrayList<Tweet> retrieveFromDatabase() throws Exception {
        ArrayList<Tweet> tweetAL = new ArrayList<>();
        MainController mc = new MainController();
        preparedStatement = connect
                    .prepareStatement("SELECT * FROM Tweets");
        resultSet = preparedStatement.executeQuery();
        
        while(resultSet.next()){
            Tweet t = new Tweet(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));
            tweetAL.add(t);
        }
        
        return tweetAL;
    }



    public void addUser(String name, String passWord) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(passWord.getBytes("UTF-8"));
            byte[] hash = md.digest();
            preparedStatement = connect
                    .prepareStatement("SELECT username FROM accounts WHERE username = ?");
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return;
            }
            preparedStatement = connect
                    .prepareStatement("INSERT INTO accounts(username,password, access_level) VALUES (?, ?, ?)");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, new BASE64Encoder().encode(hash));
            preparedStatement.setString(3, "0");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
    }

    

}
