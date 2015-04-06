
package dataflow.database;

import dataflow.feed.Feed;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import dataflow.Account;
import dataflow.Config;
import dataflow.Utility;
import dataflow.feed.api.Weather;
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
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import org.json.JSONObject;

import javax.sql.DataSource;

/**
 * Class which regulates all methods that have something to de which the database
 * @author Johnnie Ho
 */
public class MySQLDb implements DatabaseInterface {

    private Connection connect = null;
    private Statement statement = null;
    private PreparedStatement preparedStatement = null;
    private ResultSet resultSet = null;

    private static MysqlDataSource mysql = null;
        
    /**
     * Contructor method for the database
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
     * Method which reads the config file and sets up the connection
     * @return the Datasource of the connection
     */
    public static DataSource getDataSource() {      
        mysql = new MysqlDataSource();
        mysql.setURL(Config.getDatabase());
        mysql.setUser(Config.getDbuser());
        mysql.setPassword(Config.getDbpassword());
        
        return mysql;
    }
    
    /**
     * Method which validates the login by checking if the account exists in the database
     * @param username
     * @param password
     * @return true or false, based on the fact if the account exists or not
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
            preparedStatement.setString(2, Base64.getEncoder().encodeToString(hash));
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Account.setUserName(resultSet.getString("username"));
                Account.setFirstName(resultSet.getString("first_name"));
                Account.setLastName(resultSet.getString("last_name"));
                Account.setEmail(resultSet.getString("email"));
                Account.setAccessLevel(resultSet.getInt("access_level"));                
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
            preparedStatement.setString(1, Account.getUserName());
            preparedStatement.setString(2, Base64.getEncoder().encodeToString(hash));
            preparedStatement.setInt(3, accessLevel);
            resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    /**
     * Method which can be used to update account information
     * @param firstName
     * @param lastName
     * @param email
     * @param password
     * @return
     */
    @Override
    public boolean updateAccountDetails(String firstName, String lastName, String email, String password){      
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes("UTF-8"));
            byte[] hash = md.digest();
            
            if(password.length() > 0 || !password.equals("")){
                preparedStatement = connect
                    .prepareStatement("UPDATE accounts SET first_name = ?, last_name = ?, email = ?, password = ? WHERE username = ?");                
            }
            else
                preparedStatement = connect
                    .prepareStatement("UPDATE accounts SET first_name = ?, last_name = ?, email = ? WHERE username = ?");                
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, email);    
            if(password.length() > 0 || !password.equals("")){
                preparedStatement.setString(4, Base64.getEncoder().encodeToString(hash));
                preparedStatement.setString(5, Account.getUserName());  
            }
            else
                preparedStatement.setString(4, Account.getUserName());
            
            preparedStatement.executeUpdate();        
        }
        catch (SQLException | UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }          
        
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
            preparedStatement.setString(2, Base64.getEncoder().encodeToString(hash));
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
     * Method which insert a feed into the database, with the given parameters
     * @param feedType type of the feed. Can be "Twitter", "Facebook" or "Instagram"
     * @param feedId the id of the feed
     * @param text the actual message
     * @param user the user which posted the message
     * @param timeStamp the time at which the user posted the message
     * @param location the location from which the user posted
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
     * Method which inserts the number of likes into the database
     * @param resultNumber the number of likes
     * @param name the name of the post
     */
    @Override
    public void insertFacebookLikes(int resultNumber, int count) {
        try {
            preparedStatement = connect
                    .prepareStatement("INSERT INTO feed_facebook(feed_id, likes) VALUES (?, ?)");
            preparedStatement.setInt(1, resultNumber);
            preparedStatement.setInt(2, count);
            preparedStatement.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //close();
        }
    }
    
    @Override
    public boolean removeFeed(Feed feed){
        try {
            preparedStatement = connect
                    .prepareStatement("DELETE FROM data_feed WHERE id = ?");
            preparedStatement.setLong(1, feed.getId());
            if(preparedStatement.executeUpdate() > 0)
                return true;

        } catch (Exception e) {
            e.getMessage();
        } finally {
            close();
        }       
        return false;
    }
    
    @Override
    public ArrayList<Feed> searchFeed(String searchText){
        try {
            ArrayList<Feed> tweetAL = new ArrayList<>();
            preparedStatement = connect
                    .prepareStatement("SELECT * FROM data_feed WHERE message LIKE ?");
            preparedStatement.setString(1, "%"+searchText+"%");
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
     * Method which gets the number of likes of the given facebook post
     * @param fbFeed the post of which the number of likes has to be retrieved from
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
     * Method which calculates the number of feeds per day
     * @param feedType The feed type (Facebook, twitter, instagram)
     * @param date The date of which the number of feeds has to be calculated
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
     * Method which inserts the weather information of the given time into the database
     * @param timeStamp the current time
     * @param jsonWeather a JSON object containing weather information
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
     * Method which gets the weather based on a given timeStamp
     * @param timeStamp the time of which you want to know the weather (unix format)
     * @return a weather Object containing weather information
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
     * Method which returns the temperature on a given date
     * @param date the date of which the temperature has to be retrieved
     * @return double containing the temperature of that date
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
    
    public long getRecentFacebookPost(){
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT timestamp from data_feed WHERE feed_type = 'Facebook' ORDER BY timestamp DESC LIMIT 1");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong("timestamp");
            }

        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return 0;        
    }
    
    @Override
    public String getRecentTwitterId(){
        try {
            preparedStatement = connect
                    .prepareStatement("SELECT feed_id from data_feed WHERE feed_type = 'Twitter' ORDER BY timestamp DESC LIMIT 1");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("feed_id");
            }

        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;        
    }
    
    /**
     * Method which inserts an Instagram ID into the database
     * @param minId the ID of the post
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
     * Method which updates a given ID to a new ID
     * @param minId the old ID
     * @param nextMinId the new ID
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
     * Method which gets the most recent instagram ID
     * @return the most recent instagram ID
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
     * Method which calculates the number of messages per social media
     * @return an observable list containing the number of messages per social media: (#TwitterPosts, #FacebookPosts, #InstagramPosts)
     * @throws Exception 
     */
    @Override
    public ObservableList<PieChart.Data> getMediaDistribution() throws Exception {
        final ObservableList<PieChart.Data> counts = FXCollections.observableArrayList();
        preparedStatement = connect.
                prepareStatement("SELECT COUNT(*) from data_feed WHERE feed_type = ?");
        preparedStatement.setString(1, "Twitter");
        resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            counts.add(new PieChart.Data("Twitter", resultSet.getInt(1)));
        }
        
        preparedStatement = connect.
                prepareStatement("SELECT COUNT(*) from data_feed WHERE feed_type = ?");
        preparedStatement.setString(1, "Facebook");
        resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            counts.add(new PieChart.Data("Facebook", resultSet.getInt(1)));
        }
        
        preparedStatement = connect.
                prepareStatement("SELECT COUNT(*) from data_feed WHERE feed_type = ?");
        preparedStatement.setString(1, "Instagram");
        resultSet = preparedStatement.executeQuery();
        
        if(resultSet.next()){
            counts.add(new PieChart.Data("Instagram", resultSet.getInt(1)));
        }
        
        return counts;
    }
    
    @Override
    public ObservableList<PieChart.Data> getMediaDistributionPerDay(long startDay, long endDay){
        try {
            final ObservableList<PieChart.Data> counts = FXCollections.observableArrayList();
            preparedStatement = connect.
                    prepareStatement("SELECT COUNT(*) from data_feed WHERE feed_type = ? AND timestamp >= ? AND timestamp <= ?");
            preparedStatement.setString(1, "Twitter");
            preparedStatement.setLong(2, startDay);
            preparedStatement.setLong(3, endDay);
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                counts.add(new PieChart.Data("Twitter", resultSet.getInt(1)));
            }
            else
                counts.add(new PieChart.Data("Twitter", 0));

            preparedStatement = connect.
                    prepareStatement("SELECT COUNT(*) from data_feed WHERE feed_type = ? AND timestamp >= ? AND timestamp <= ?");
            preparedStatement.setString(1, "Facebook");
            preparedStatement.setLong(2, startDay);
            preparedStatement.setLong(3, endDay);            
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                counts.add(new PieChart.Data("Facebook", resultSet.getInt(1)));
            }
            else
                counts.add(new PieChart.Data("Facebook", 0));

            preparedStatement = connect.
                    prepareStatement("SELECT COUNT(*) from data_feed WHERE feed_type = ? AND timestamp >= ? AND timestamp <= ?");
            preparedStatement.setString(1, "Instagram");
            preparedStatement.setLong(2, startDay);
            preparedStatement.setLong(3, endDay);            
            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                counts.add(new PieChart.Data("Instagram", resultSet.getInt(1)));
            }
            else
                counts.add(new PieChart.Data("Instagram", 0));
            
            return counts;
        } catch (SQLException ex) {
            Logger.getLogger(MySQLDb.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }    
    
    /**
     * Method which closes the database connection
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
