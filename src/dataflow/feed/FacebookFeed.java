/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.feed;

import dataflow.database.MySQLDb;
import java.util.ArrayList;

/**
 * Class for facebook feeds
 * @author Johnnie Ho
 */
public class FacebookFeed extends Feed {
    
    private ArrayList<String> likeList = new ArrayList();
    
    /**
     * Constructor class which created a facebook feed object
     * @param id
     * @param timeStamp
     * @param user
     * @param location
     * @param text
     * @param feedType 
     */
    public FacebookFeed(long id, long timeStamp, String user, String location, String text, String feedType){
        super(id, timeStamp, user, location, text, feedType);        
    }

    /**
     * Method which returns the number of likes per post
     * @return an arraylist containing the number of likes 
     */
    public ArrayList<String> getLikeList() {
        if(likeList == null || likeList.isEmpty()){
            MySQLDb db = new MySQLDb();
            db.retrieveFacebookLikes(this);
        }
            
        return likeList;
    }

    /**
     * Method which let you set an arraylist containing likes per post
     * @param likeList the new arraylist of likes
     */
    public void setLikeList(ArrayList<String> likeList) {
        this.likeList = likeList;
    }
    
}
