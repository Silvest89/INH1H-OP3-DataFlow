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
    public ArrayList<String> positiveCommentList = new ArrayList<String>(); 
    public ArrayList<String> negativeCommentList = new ArrayList<String>(); // manier om te printen in andere class = system.out.println(new Database.positiveCommentList.size());
    public ArrayList<String> neutralCommentList = new ArrayList<String>(); 
    /**
     *
     */
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

    /**
     *
     * @param username
     * @param password
     * @throws Exception
     */
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
                account.setFirstName(resultSet.getString("first_name"));
                account.setLastName(resultSet.getString("last_name"));
                account.setEmail(resultSet.getString("email"));
                account.setAccessLevel(resultSet.getInt("access_level"));                
                DataFlow.account = account;
            }

        } catch (Exception e) {
            throw e;
        } finally {
            //close();
        }
    }
    
    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     */
    public void updateAccountDetails(String firstName, String lastName, String email, String password){
        if(!Utility.passwordValidation(password))
            return;
        
        
    }

    /**
     *
     */
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

    /**
     *
     * @param feedType
     * @param feedId
     * @param text
     * @param user
     * @param timeStamp
     * @param location
     * @throws Exception
     */
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
            Tweet t = new Tweet(resultSet.getLong("id"), resultSet.getLong("timestamp"), resultSet.getString("user"), resultSet.getString("location"), resultSet.getString("message"));
            tweetAL.add(t);
            commentChecker(resultSet.getString("message")); 
        }
        System.out.println(positiveCommentList.size());
        System.out.println(negativeCommentList.size());
        System.out.println(neutralCommentList.size());
        return tweetAL;
    }
    
    //this method filters the incoming twitterstream and gives the comments a value of negative, positive or neutral

   public String commentChecker(String text){
           if(text.matches(".*(mooi|goed|leuk|fantastisch|prachtig|#boijmans|het).*")) //you can change the words in here to change what the filter thinks is positive
           {
               positiveCommentList.add(text);
               return "comment is positive";
           }
            
            
           else if(text.matches(".*(lelijk|stom|saai|kut|verschrikkelijk).*")) //you can change the words in here to change what the filer thinks is negative
           {
               negativeCommentList.add(text);
               return "comment is negative";
           }
           else
           {
               neutralCommentList.add(text);
               return "comment is neutral";
           } //if the comment is not positve or negative the method will automatically assign it the neutral value
   }
    /**
     * This method adds a new user into the database, iff it does not exist yet
     *
     * @param username user name of the account added
     * @param password password of the account added
     * @param firstName
     * @param email
     * @param lastName
     * @param accessLevel
     * @return 
     */
    public boolean addUser(String username, String password, String firstName, String lastName, String email, int accessLevel){
        System.out.println("test2");
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();
            preparedStatement = connect
                    .prepareStatement("SELECT username FROM accounts WHERE username = ?");
            preparedStatement.setString(1, username);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                System.out.println("test3");
                return false;
            }
            System.out.println("test4");
            preparedStatement = connect
                    .prepareStatement("INSERT INTO accounts(username, password, first_name, last_name, email, access_level) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, new BASE64Encoder().encode(hash));
            preparedStatement.setString(3, firstName);
            preparedStatement.setString(4, lastName);
            preparedStatement.setString(5, email);
            preparedStatement.setInt(6, accessLevel);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
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
     * @param password
     * @return boolean based on whether the account is present or not
     * @throws Exception
     */
    public boolean checkAccount(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] hash = md.digest();
        try{
            preparedStatement = connect
                    .prepareStatement("SELECT username FROM accounts WHERE username = ? AND password = ? AND access_level=3");
            preparedStatement.setString(1, DataFlow.account.getUserName());
            preparedStatement.setString(2, new BASE64Encoder().encode(hash));
            resultSet = preparedStatement.executeQuery();
        }catch (Exception e) {
            e.printStackTrace();
        }

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
    
    /**
     *
     * @param date
     * @return
     * @throws Exception
     */
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

    /**
     *
     * @param timeStamp
     * @param jsonWeather
     */
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

    /**
     *
     * @param timeStamp
     * @return
     */
    public Weather fetchWeather(long timeStamp) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {            
            preparedStatement = connect
                    .prepareStatement("SELECT * from weather where date = ? LIMIT 1");
            preparedStatement.setString(1, format.format(timeStamp * 1000L));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Weather weather = new Weather(resultSet.getInt("id"), resultSet.getString("date"), resultSet.getString("icon"), resultSet.getString("clouds"), resultSet.getString("mintemp"), resultSet.getString("maxtemp"), resultSet.getString("description"));
                return weather;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }
    
    /**
     *
     * @param minId
     * @throws SQLException
     */
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
    
    /**
     *
     * @param minId
     * @param nextMinId
     * @throws SQLException
     */
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
    
    /**
     *
     * @return
     */
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
    
    /**
     *
     * @param date
     * @return
     */
    public double fetchWeatherByDouble(String date) {
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
