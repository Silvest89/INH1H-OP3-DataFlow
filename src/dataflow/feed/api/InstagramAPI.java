/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.feed.api;

import dataflow.database.MySQLDb;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

/**
 * Class for Instagram message retrieval
 * @author Johnnie Ho
 */
public final class InstagramAPI extends FeedAPI {
    
    private Instagram instagram;
    private String keyword;
    
    public InstagramAPI(){
        connect();
    }    
    
    /**
     * Method which connects to Instagram
     */
    @Override
    public void connect(){
        InstagramService service = new InstagramAuthService()
                    .apiKey("3fc9480725944608aa51213d49cbb706")
                    .apiSecret("1a1e929561cb41dd80462d43ad242a6c")
                    .callback("http://localhost")     
                    .build();     

        instagram = new Instagram("3fc9480725944608aa51213d49cbb706");                
    }
    
    /**
     * Method which gets the keyword
     * @return keyword which is used to search posts
     */
    public String getKeyword(){
        return keyword;
    }
    
    /**
     * Method which sets the keywords
     * @param keyword the new keyword
     */
    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
    
    /**
     * Method which fetches posts
     */
    @Override
    public void fetchFeed(){        
        
        MySQLDb db = new MySQLDb();
        TagMediaFeed mediaFeed = null;
        String minId = db.getRecentInstagramId();
        try {
            if(minId != null){
                    mediaFeed = instagram.getRecentMediaTags(keyword, minId, "");
                    String nextMinId = mediaFeed.getPagination().getMinTagId();
                    if(nextMinId == null)
                        return;
                    db.updateInstagramId(minId, nextMinId);   
            }
            else{
                mediaFeed = instagram.getRecentMediaTags(keyword);
                minId = mediaFeed.getPagination().getMinTagId();
                db.insertInstagramId(minId);
            }                

            if(mediaFeed.getPagination().getMinTagId() != null){
                List<MediaFeedData> mediaFeeds = mediaFeed.getData(); 

                MediaFeed recentMediaNextPage = instagram.getRecentMediaNextPage(mediaFeed.getPagination());
                int counter = 0;
                while (recentMediaNextPage.getPagination() != null && counter < 10) {
                    mediaFeeds.addAll(recentMediaNextPage.getData());
                    recentMediaNextPage = instagram.getRecentMediaNextPage(recentMediaNextPage.getPagination());
                    counter++;
                }

                String location;
                for (MediaFeedData mediaFeedData : mediaFeeds) {
                    location = "";
                    if(mediaFeedData.getLocation() != null)
                        location = mediaFeedData.getLocation().getName();                    

                    if(location == null)
                        location = "";

                    db.insertFeed("Instagram", mediaFeedData.getId(), mediaFeedData.getCaption().getText(), mediaFeedData.getUser().getUserName(), Long.parseLong(mediaFeedData.getCreatedTime(), 10), location);
                }
            }
        }
        catch (InstagramException ex) {
                Logger.getLogger(InstagramAPI.class.getName()).log(Level.SEVERE, null, ex);
        }  
        db.close();
    }
}
