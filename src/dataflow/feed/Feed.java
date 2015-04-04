/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.feed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Johnnie Ho
 */
public class Feed {
    
    private long id;
    private long timeStamp;
    private String user;
    private String location;
    private String text;
    private String timeString;
    private String feedType;
    
    private static ArrayList<Feed> feedList;    
    
    public Feed(long id, long timeStamp, String user, String location, String text, String feedType){
        this.id = id;    
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm z");
        this.timeString = format.format(timeStamp * 1000L);
        this.timeStamp = timeStamp;
        this.user = user;
        this.location = location;
        this.text = text;        
        
        this.feedType = feedType;
        feedList.add(this);        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTimeString() {
        return timeString;
    }

    public void setTimeString(String timeString) {
        this.timeString = timeString;
    }            
}
