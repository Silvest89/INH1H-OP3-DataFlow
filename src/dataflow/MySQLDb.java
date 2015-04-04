/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import dataflow.feed.Feed;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import dataflow.feed.FacebookFeed;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;

import javax.sql.DataSource;
import sun.misc.BASE64Encoder;

/**
 *
 * @author Johnnie Ho
 */
public class MySQLDb implements DatabaseInterface {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private static MysqlDataSource mysql = null;
        
    /**
     *
     */
    public MySQLDb() {
        // This will load the MySQL driver, each DB has its own driver     
        try {
            connect = mysql.getConnection();
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @return
     */
    public static DataSource getDataSource() {      
        //fis = new FileInputStream("src\\db.properties");
        //props.load(fis);
        mysql = new MysqlDataSource();
        mysql.setURL(Config.getDatabase());
        mysql.setUser(Config.getDbuser());
        mysql.setPassword(Config.getDbpassword());
        
        return mysql;
    }
    
    /**
     *
     * @param username
     * @param password
     * @return
     */
    @Override
    public boolean validateLogin(String username, String password) {
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
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return false;
    }
    
    /**
     * This method checks if a given user name/password combination is present
     * in the database with the appropriate access level
     *
     * @param password the password for confirmation
     * @param accessLevel the access level required
     * @return boolean based on whether the entered password is correct and has sufficient access
     * @throws UnsupportedEncodingException, NoSuchAlgorithmException
     * @throws java.security.NoSuchAlgorithmException
     */
    @Override
    public boolean checkAccount(String password, int accessLevel) throws UnsupportedEncodingException, NoSuchAlgorithmException{
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes("UTF-8"));
        byte[] hash = md.digest();
        try{
            preparedStatement = connect
                    .prepareStatement("SELECT username FROM accounts WHERE username = ? AND password = ? AND access_level >= ?");
            preparedStatement.setString(1, DataFlow.account.getUserName());
            preparedStatement.setString(2, new BASE64Encoder().encode(hash));
            preparedStatement.setInt(2, accessLevel);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     *
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean updateAccountDetails(String firstName, String lastName, String email, String password){
        if(!Utility.passwordValidation(password))
            return false;   
        
        return true;
    }

        /**
     * This method adds a new user into the database, if it does not exist yet
     *
     * @param username user name of the account added
     * @param password password of the account added
     * @param firstName
     * @param lastName
     * @param email
     * @param accessLevel
     * @return true or false whether the user is successfully added
     */
    @Override
    public boolean createUser(String username, String password, String firstName, String lastName, String email, int accessLevel){
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
     * This method removes a given account from the database
     *
     * @param name user name of the account deleted
     * @return 
     */
    @Override
    public boolean deleteUser(String name) {
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM accounts WHERE username = ?");
            preparedStatement.setString(1, name);
            if(preparedStatement.executeUpdate() > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return false;
    }    
    
    /**
     *
     * @param feedType
     * @param feedId
     * @param text
     * @param user
     * @param timeStamp
     * @param location
     * @return
     */
    @Override
    public int insertFeed(String feedType, String feedId, String text, String user, long timeStamp, String location) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
            String date = sdf.format(new Date(timeStamp * 1000L));
            
            preparedStatement = connect.prepareStatement("SELECT id FROM weather WHERE date = ?");
            preparedStatement.setString(1, date);
            resultSet = preparedStatement.executeQuery();
            
            preparedStatement = connect
                    .prepareStatement("INSERT INTO data_feed(feed_id, message, user, timestamp, location, feed_type) VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, feedId);
            preparedStatement.setString(2, text);
            preparedStatement.setString(3, user);
            preparedStatement.setLong(4, timeStamp);
            preparedStatement.setString(5, location);
            preparedStatement.setString(6, feedType);
            preparedStatement.executeUpdate();
            
            // adds the foreign key "feed_id" in the facebook_feed
            ResultSet rs = preparedStatement.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);
                return last_inserted_id;
            }
                
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
        return 0;
    }

    /**
     *
     * @param resultNumber
     * @param name
     */
    @Override
    public void insertFacebookLikes(int resultNumber, String name) {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO feed_facebook(feed_id, likes) VALUES (?, ?)");
            preparedStatement.setInt(1, resultNumber);
            preparedStatement.setString(2, name);
            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
    }
    
    /**
     * This method return an ArrayList of Feeds, used to populate the table in
     * the main screen
     *
     * @return ArrayList<> : ArrayList containing feeds from the database
     */
    @Override
    public ArrayList<Feed> retrieveFeeds() {
        try {
            ArrayList<Feed> tweetAL = new ArrayList<>();
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM data_feed");
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {
                Feed t = new Feed(resultSet.getLong("id"), resultSet.getLong("timestamp"), resultSet.getString("user"), resultSet.getString("location"), resultSet.getString("message"), resultSet.getString("feed_type"));
                tweetAL.add(t);
            }
            
            return tweetAL;
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    /**
     *
     * @param fbFeed
     * @return
     */
    @Override
    public ArrayList<String> retrieveFacebookLikes(FacebookFeed fbFeed){
        try{
            ArrayList<String> likeList = new ArrayList();
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM facebook_feed WHERE id = ?");
            preparedStatement.setLong(1, fbFeed.getId());
            resultSet = preparedStatement.executeQuery();  
            while(resultSet.next()){
                likeList.add(resultSet.getString("likes"));
            }
            return likeList;
        }
        catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }   
        return null;
    }

    /**
     * This method returns an ArrayList of Strings, used to populate the
     * choiceBox in the deletion menu
     *
     * @return ArrayList<> ArrayList containing user names from the
     * database
     */
    @Override
    public ArrayList<String> getAccountList() {
        ArrayList<String> choices = new ArrayList<>();
        try {
            preparedStatement = connect
                .prepareStatement("SELECT username FROM accounts");
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                choices.add(resultSet.getString(1));
            }            
        } 
        catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }        
        finally{
            close();
        }

        return choices;
    }
    
    /**
     *
     * @param feedType
     * @param date
     * @return
     * @throws Exception
     */
    @Override
    public int getFeedsPerDay(String feedType, Date date) throws Exception {
        ArrayList<String> results = new ArrayList<>();
        
        Date startOfDay = Utility.getStartOfDay(date);
        Date endOfDay = Utility.getEndOfDay(date);
        
        preparedStatement = connect
                .prepareStatement("SELECT COUNT(*) FROM data_feed WHERE feed_type = ? and timestamp >= ? AND timestamp <= ?");
        preparedStatement.setString(1, feedType);
        preparedStatement.setLong(2, startOfDay.getTime() / 1000L);
        preparedStatement.setLong(3, endOfDay.getTime() / 1000L);
        resultSet = preparedStatement.executeQuery();
        
        int amount = 0;
        while(resultSet.next()){
            amount = resultSet.getInt(1);
        }        
        
        return amount;
    }

    /**
     *
     * @param timeStamp
     * @param jsonWeather
     */
    @Override
    public void insertWeather(String timeStamp, JSONObject jsonWeather){
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
    @Override
    public Weather fetchWeatherByDate(long timeStamp) {
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

        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            close();
        }
        return null;
    }

    /**
     *
     * @param date
     * @return
     */
    @Override
    public double fetchWeatherTemperatureByDate(String date) {
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT * from weather where date = ? LIMIT 1");
            preparedStatement.setString(1, date);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double temp = Double.parseDouble(resultSet.getString("maxtemp"));
                return temp;
            }

        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            //close();
        }
        return 0;
    }
    
    /**
     *
     * @param minId
     */
    @Override
    public void insertInstagramId(String minId){
        try{
            preparedStatement = connect
                .prepareStatement("INSERT INTO feed_instagram(id, min_id) VALUES (1, ?)");
            preparedStatement.setString(1, minId);
            preparedStatement.executeUpdate();        
        }
        catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }     
    }
    
    /**
     *
     * @param minId
     * @param nextMinId
     */
    @Override
    public void updateInstagramId(String minId, String nextMinId) {
        try{
            preparedStatement = connect
                .prepareStatement("UPDATE feed_instagram SET min_id = ? WHERE min_id = ?");
            preparedStatement.setString(1, nextMinId);
            preparedStatement.setString(2, minId);
            preparedStatement.executeUpdate();        
        }
        catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }             
    }    
    
    /**
     *
     * @return
     */
    @Override
    public String getRecentInstagramId(){
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT * from feed_instagram LIMIT 1");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("min_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }        

    /**
     *
     */
    @Override
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
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}