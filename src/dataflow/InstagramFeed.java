/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.util.List;
import org.jinstagram.Instagram;
import org.jinstagram.auth.InstagramAuthService;
import org.jinstagram.auth.model.Token;
import org.jinstagram.auth.oauth.InstagramService;
import org.jinstagram.entity.tags.TagMediaFeed;
import org.jinstagram.entity.users.feed.MediaFeed;
import org.jinstagram.entity.users.feed.MediaFeedData;
import org.jinstagram.exceptions.InstagramException;

/**
 *
 * @author Johnnie Ho
 */
public class InstagramFeed {
    private static final Token EMPTY_TOKEN = null;
    
    public InstagramFeed(){

    }
    
    public static void main(String[] args) throws InstagramException{
        InstagramService service = new InstagramAuthService()
                            .apiKey("3fc9480725944608aa51213d49cbb706")
                            .apiSecret("1a1e929561cb41dd80462d43ad242a6c")
                            .callback("http://localhost")     
                            .build();
                       
        Instagram instagram = new Instagram("3fc9480725944608aa51213d49cbb706");
        
        String tagName = "boijmans";
        TagMediaFeed mediaFeed = instagram.getRecentMediaTags(tagName);
        
        List<MediaFeedData> mediaFeeds = mediaFeed.getData(); 
        
        MediaFeed recentMediaNextPage = instagram.getRecentMediaNextPage(mediaFeed.getPagination());
        int counter = 0;
        while (recentMediaNextPage.getPagination() != null && counter <= 3) {
            mediaFeeds.addAll(recentMediaNextPage.getData());
            recentMediaNextPage = instagram.getRecentMediaNextPage(recentMediaNextPage.getPagination());
            counter++;
        }
        System.out.println(mediaFeeds.size());
        
        Database db = new Database();
        for (MediaFeedData mediaFeedData : mediaFeeds) {
            System.out.println(mediaFeedData.getId());
            //db.putInDatabase(counter, tagName, tagName, counter, tagName);
        }
    }
}
