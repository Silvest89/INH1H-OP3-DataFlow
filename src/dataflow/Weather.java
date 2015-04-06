/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class which holds weather information
 * @author Johnnie Ho
 */
public class Weather {
    private int id;
    private String date;
    private String icon;
    private String clouds;
    private String minTemp;
    private String maxTemp;
    private String description;
        
    /**
     * Contructor class which constructs a weather object
     * @param id
     * @param date
     * @param icon
     * @param clouds
     * @param minTemp
     * @param maxTemp
     * @param description 
     */
    public Weather(int id, String date, String icon, String clouds, String minTemp, String maxTemp, String description){
        this.id = id;
        this.date = date;
        this.icon = icon;
        this.clouds = clouds;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;   
        this.description = description;
        
    }
    
    /**
     * Method which returns the icon of the current weather (sun icon, cloud icon etc.)
     * @return the icon of the current weather
     */
    public String getIcon(){
        return icon;
    }
    
    /**
     * Method which returns the min. temperature
     * @return the minimum temperature
     */
    public String getMinTemp(){
        return minTemp;
    }
    
    /**
     * Method which returns the max. temperature
     * @return the maximum temperature
     */
    public String getMaxTemp(){
        return maxTemp;
    }    
    
    /**
     * Method which returns the cloud percentage
     * @return the cloud percentage
     */
    public String getClouds(){
        return clouds;
    }
    
    /**
     * Method which returns the descrpition of the weather
     * @return the descrpition of the weather
     */
    public String getDescription(){
        return description;
    }
    
    /**
     * Method which connects to the weather api and retrieves the weather from the day before today
     */
    public static void getWeather(){
        String key = "c6aee37b80d801b44279ac16374db";
        Calendar fromTime = Calendar.getInstance();
        Date weatherDate = new Date();
        for(int i = 1; i < 6; i++){
            weatherDate.setTime(weatherDate.getTime() - (86400000));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(weatherDate);
            String jsonString = callURL("https://api.worldweatheronline.com/free/v2/past-weather.ashx?q=Rotterdam&format=json&tp=24&key=c6aee37b80d801b44279ac16374db&date=" + date);    
            try {  
                MySQLDb db = new MySQLDb();
                JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");
                System.out.println(jsonObject);
                db.insertWeather(date, jsonObject);           
            } catch (JSONException e) {
                throw e;
            }
        }           
    }
    
    /**
     * Method which builds the string of the URL to call
     * @param myURL the URL which must become a callURL
     * @return 
     */
    public static String callURL(String myURL) {
        //System.out.println("Requested URL:" + myURL);
        StringBuilder sb = new StringBuilder();
        URLConnection urlConn = null;
        InputStreamReader in = null;
        try {
                URL url = new URL(myURL);
                urlConn = url.openConnection();
                if (urlConn != null)
                        urlConn.setReadTimeout(60 * 1000);
                if (urlConn != null && urlConn.getInputStream() != null) {
                        in = new InputStreamReader(urlConn.getInputStream(),
                                        Charset.defaultCharset());
                        BufferedReader bufferedReader = new BufferedReader(in);
                        if (bufferedReader != null) {
                                int cp;
                                while ((cp = bufferedReader.read()) != -1) {
                                        sb.append((char) cp);
                                }
                                bufferedReader.close();
                        }
                }
        in.close();
        } catch (Exception e) {
                throw new RuntimeException("Exception while calling URL:"+ myURL, e);
        } 

        return sb.toString();
    }    
}
