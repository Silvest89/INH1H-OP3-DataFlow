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
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Johnnie Ho
 */
public class Weather {
    public void getWeather(){
        String jsonString = callURL("http://api.openweathermap.org/data/2.5/weather?id=2747891&units=metric&lang=nl");
		System.out.println("\n\njsonString: " + jsonString);
                
                // Replace this try catch block for all below subsequent examples
		try {  
			JSONObject jsonArray = new JSONObject(jsonString);
			System.out.println("\n\njsonArray: " + jsonArray);
                        //System.out.println(jsonArray.getJSONObject("coord"));
                        //System.out.println(jsonArray.getJSONArray("weather"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
    
	public static String callURL(String myURL) {
		System.out.println("Requested URL:" + myURL);
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
