/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.feed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Superclass for all the feeds
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
    
    /**
     * Constructor class contructing a feed object
     * @param id
     * @param timeStamp
     * @param user
     * @param location
     * @param text
     * @param feedType 
     */
    public Feed(long id, long timeStamp, String user, String location, String text, String feedType){
        this.id = id;    
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm z");
        this.timeString = format.format(timeStamp * 1000L);
        this.timeStamp = timeStamp;
        this.user = user;
        this.location = location;
        this.text = text;        
        
        this.feedType = feedType;    
    }

    /**
     * Method which returns the id
     * @return id of the feed
     */
    public final long getId() {
        return id;
    }

    /**
     * Method which sets the id
     * @param id new id of the feed
     */
    public final void setId(long id) {
        this.id = id;
    }

    /**
     * Method which returns the timeStamp
     * @return timeStamp of the feed
     */
    public final long getTimeStamp() {
        return timeStamp;
    }

    /**
     * Method which sets the timeStamp
     * @param timeStamp new timeStamp of the feed
     */
    public final void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    /**
     * Method which returns the user
     * @return user/author of the feed
     */
    public final String getUser() {
        return user;
    }

    /**
     * Method which sets the user
     * @param user new user of the feed
     */
    public final void setUser(String user) {
        this.user = user;
    }

    /**
     * Method which returns the location
     * @return location of the feed
     */
    public final String getLocation() {
        return location;
    }

    /**
     * Method which sets the location
     * @param location new location of the feed
     */
    public final void setLocation(String location) {
        this.location = location;
    }

    /**
     * Method which returns the text
     * @return text of the feed/actual message
     */
    public final String getText() {
        return text;
    }

    /**
     * Method which sets the text
     * @param text new text of the feed
     */
    public final void setText(String text) {
        this.text = text;
    }

    /**
     * Method which returns the time String
     * @return time string of the feed
     */
    public final String getTimeString() {
        return timeString;
    }

    /**
     * Method which sets the time string
     * @param timeString new time string of the feed
     */
    public final void setTimeString(String timeString) {
        this.timeString = timeString;
    }            
}
