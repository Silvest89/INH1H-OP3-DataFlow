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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONObject;
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
                account.setAccessLevel(resultSet.getInt("access_type"));
                DataFlow.account = account;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            //close();
        }
    }

    public void close() {
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

    public void putInDatabase(String feedType, String feedId, String text, String user, long timeStamp, String location) throws Exception {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            String date = sdf.format(new Date(timeStamp * 1000L));
            
            preparedStatement = connect.prepareStatement("SELECT id FROM weather WHERE date = ?");
            preparedStatement.setString(1, date);
            resultSet = preparedStatement.executeQuery();
            
            preparedStatement = connect
                    .prepareStatement("INSERT INTO data_feed(feed_id, message, user, timestamp, location, feed_type) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, feedId);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, user);
            preparedStatement.setLong(4, timeStamp);
            preparedStatement.setString(5, location);
            preparedStatement.setString(6, feedType);
            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
    }

    /**
     * This method return an ArrayList of Tweets, used to populate the table in
     * the main screen
     *
     * @return ArrayList<Tweet> : ArrayList containing tweets from the database
     * @throws Exception
     */
    public ArrayList<Tweet> retrieveFromDatabase() throws Exception {
        ArrayList<Tweet> tweetAL = new ArrayList<>();
        preparedStatement = connect
                .prepareStatement("SELECT * FROM data_feed");
        resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            Tweet t = new Tweet(resultSet.getLong("feed_id"), new Date(resultSet.getLong("timestamp") * 1000L), resultSet.getString("user"), resultSet.getString("location"), resultSet.getString("message"), resultSet.getInt("weather_id"));
            tweetAL.add(t);
        }

        return tweetAL;
    }

    /**
     * This method adds a new user into the database, iff it does not exist yet
     *
     * @param name user name of the account added
     * @param passWord password of the account added
     * @throws Exception
     */
    public boolean addUser(String username, String password, String firstName, String lastName, String email, int accessLevel) throws Exception {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();
            preparedStatement = connect
                    .prepareStatement("SELECT username FROM accounts WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return false;
            }
            preparedStatement = connect
                    .prepareStatement("INSERT INTO accounts(username, password, firstname, lastname, email, access_level) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, new BASE64Encoder().encode(hash));
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);
            preparedStatement.setInt(6, accessLevel);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw e;
        } finally {
            close();
        }
        return true;
    }

    /**
     * This method returns an ArrayList of Strings, used to populate the
     * choiceBox in the deletion menu
     *
     * @return ArrayList<String> ArrayList containing user names from the
     * database
     * @throws Exception
     */
    public ArrayList<String> populateChoiceBox() throws Exception {
        ArrayList<String> choices = new ArrayList<>();
        preparedStatement = connect
                .prepareStatement("SELECT username FROM accounts");
        resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            choices.add(resultSet.getString(1));
        }

        return choices;
    }

    /**
     * This method checks if a given user name/password combination is present
     * in the database
     *
     * @param name the user name of the account checked
     * @param passWord the password of the account checked
     * @return boolean based on whether the account is present or not
     * @throws Exception
     */
    public boolean checkAccount(String passWord) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(passWord.getBytes("UTF-8"));
        byte[] hash = md.digest();
        preparedStatement = connect
                .prepareStatement("SELECT username FROM accounts WHERE username = ? AND password = ? AND access_level=3");
        preparedStatement.setString(1, DataFlow.account.getUserName());
        preparedStatement.setString(2, new BASE64Encoder().encode(hash));
        resultSet = preparedStatement.executeQuery();

        return resultSet.next();

    }

    /**
     * This method removes a given account from the database
     *
     * @param name user name of the account deleted
     * @param passWord password of the account deleted
     */
    public void removeUser(String name, String passWord) {
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM accounts WHERE username = ?");
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
    }
    
    public int countTweets(String date) throws Exception {
        ArrayList<String> results = new ArrayList<>();
        preparedStatement = connect
                .prepareStatement("SELECT timestamp FROM data_feed WHERE feed_type = 'Twitter'");
        resultSet = preparedStatement.executeQuery();

        while(resultSet.next()){
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");
            results.add(sdf.format(new Date(resultSet.getLong("timeStamp") * 1000L)));
        }
        
        int i = 0;
        for (String s : results){
            if(s.equals(date)){
                i++;
            }
        }
        
        return i;
    }

    public void checkWeather(String timeStamp, JSONObject jsonWeather){
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT * from weather where date = ? LIMIT 1");
            preparedStatement.setString(1, timeStamp);
            resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {                
                JSONObject weather = jsonWeather.getJSONArray("weather").getJSONObject(0);
                String date = weather.getString("date");
                String mintemp = weather.getString("mintempC");
                String maxtemp = weather.getString("maxtempC");
                JSONObject weather2 = weather.getJSONArray("hourly").getJSONObject(0);
                String cloudCover = weather2.getString("cloudcover");
                String weatherDesc = weather2.getJSONArray("weatherDesc").getJSONObject(0).getString("value");
                String weatherIcon = weather2.getJSONArray("weatherIconUrl").getJSONObject(0).getString("value");
                
                preparedStatement = connect
                    .prepareStatement("INSERT INTO weather(date, icon, clouds, mintemp, maxtemp, description) VALUES (?, ?, ?, ?, ?, ?)");
                preparedStatement.setString(1, date);
                preparedStatement.setString(2, weatherIcon);
                
                preparedStatement.setString(3, cloudCover);
                preparedStatement.setString(4, mintemp);
                preparedStatement.setString(5, maxtemp);
                preparedStatement.setString(6, weatherDesc);
                preparedStatement.executeUpdate();
               
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }   
    }

    public Weather fetchWeather(int id) {
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT * from weather where id = ? LIMIT 1");
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Weather weather = new Weather(resultSet.getInt("id"), resultSet.getString("date"), resultSet.getString("icon1"), resultSet.getString("icon2"), resultSet.getString("clouds"), resultSet.getString("mintemp"), resultSet.getString("maxtemp"));
                return weather;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }
    
    public void insertInstagramId(String minId) throws SQLException{
        try{
            preparedStatement = connect
                .prepareStatement("INSERT INTO feed_instagram(id, min_id) VALUES (1, ?)");
            preparedStatement.setString(1, minId);
            preparedStatement.executeUpdate();        
        }
        catch (Exception e) {
            e.printStackTrace();
        }     
    }
    
    public void updateInstagramId(String minId, String nextMinId) throws SQLException{
        try{
            preparedStatement = connect
                .prepareStatement("UPDATE feed_instagram SET min_id = ? WHERE min_id = ?");
            preparedStatement.setString(1, nextMinId);
            preparedStatement.setString(2, minId);
            preparedStatement.executeUpdate();        
        }
        catch (Exception e) {
            e.printStackTrace();
        }             
    }    
    
    public String getRecentInstagramId(){
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT * from feed_instagram LIMIT 1");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("min_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public double fetchWeather(String date) {
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT * from weather where date = ? LIMIT 1");
            preparedStatement.setString(1, date);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double temp = Double.parseDouble(resultSet.getString("maxtemp"));
                return temp;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return 0;
    }

}
