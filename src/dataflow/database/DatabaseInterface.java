/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.database;

import dataflow.feed.api.Weather;
import dataflow.feed.FacebookFeed;
import dataflow.feed.Feed;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import javax.sql.DataSource;
import org.json.JSONObject;

/**
 * Interface Class for databases. If new Databases are created, they can implement this interface
 * @author Johnnie Ho
 */
public interface DatabaseInterface {
    
    public boolean validateLogin(String username, String password);     
    public boolean checkAccount(String password, int accessLevel) throws UnsupportedEncodingException, NoSuchAlgorithmException;
    public boolean updateAccountDetails(String firstName, String lastName, String email, String password);
    
    public boolean createUser(String username, String password, String firstName, String lastName, String email, int accessLevel);
    public boolean deleteUser(String name);    
    
    public int insertFeed(String feedType, String feedId, String text, String user, long timeStamp, String location);
    public void insertFacebookLikes(int resultNumber, String name);
        
    public ArrayList<Feed> retrieveFeeds();        
    public ArrayList<String> retrieveFacebookLikes(FacebookFeed fbFeed);   
    public ArrayList<String> getAccountList();
    public int getFeedsPerDay(String feedType, Date date) throws Exception;
    
    public void insertWeather(String timeStamp, JSONObject jsonWeather);
    public Weather fetchWeatherByDate(long timeStamp);
    public double fetchWeatherTemperatureByDate(String date);
    
    public void insertInstagramId(String minId);    
    public void updateInstagramId(String minId, String nextMinId);    
    public String getRecentInstagramId();
    
    public void close();        
}
