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
 *
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
        
    public Weather(int id, String date, String icon, String clouds, String minTemp, String maxTemp, String description){
        this.id = id;
        this.date = date;
        this.icon = icon;
        this.clouds = clouds;
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;   
        this.description = description;
        
    }
    
    public String getIcon(){
        return icon;
    }
    
    public String getMinTemp(){
        return minTemp;
    }
    
    public String getMaxTemp(){
        return maxTemp;
    }    
    
    public String getClouds(){
        return clouds;
    }
    
    public String getDescription(){
        return description;
    }
    
    public static void getWeather(){
        String key = "c6aee37b80d801b44279ac16374db";
        Calendar fromTime = Calendar.getInstance();
        Date weatherDate = new Date();
        for(int i = 1; i <= 6; i++){
            weatherDate.setTime(weatherDate.getTime() - (86400000));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String date = format.format(weatherDate);
            String jsonString = callURL("https://api.worldweatheronline.com/free/v2/past-weather.ashx?q=Rotterdam&format=json&tp=24&key=c6aee37b80d801b44279ac16374db&date=" + date);    
            try {  
                Database db = new Database();
                JSONObject jsonObject = new JSONObject(jsonString).getJSONObject("data");
                db.checkWeather(date, jsonObject);           
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }           
    }
    
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
