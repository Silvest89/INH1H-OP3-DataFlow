/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.feed;

import dataflow.MySQLDb;
import java.util.ArrayList;

/**
 *
 * @author Johnnie Ho
 */
public class FacebookFeed extends Feed {
    
    private ArrayList<String> likeList = new ArrayList();
    
    public FacebookFeed(long id, long timeStamp, String user, String location, String text, String feedType){
        super(id, timeStamp, user, location, text, feedType);        
    }

    public ArrayList<String> getLikeList() {
        if(likeList == null || likeList.isEmpty()){
            MySQLDb db = new MySQLDb();
            db.retrieveFacebookLikes(this);
        }
            
        return likeList;
    }

    public void setLikeList(ArrayList<String> likeList) {
        this.likeList = likeList;
    }
    
}
