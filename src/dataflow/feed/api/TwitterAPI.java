package dataflow.feed.api;

import dataflow.MySQLDb;
import java.util.ArrayList;
import java.util.Date;
import twitter4j.*;
import java.util.logging.Logger;
import twitter4j.conf.ConfigurationBuilder;

public final class TwitterAPI implements FeedAPI {
    
    Twitter twitter;
    String keyword;
    private final Logger logger = Logger.getLogger(TwitterAPI.class.getName());

    public TwitterAPI(){
        connect();
    }
    
    @Override
    public void connect(){
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("PhXinpF4eADYZCnfhM8BrmBOu")
                .setOAuthConsumerSecret("28u6vrEsyuPrbcz8wGFARuBktIIko42GNiK4HZREbRdo5lZxPo")
                .setOAuthAccessToken("3053907616-RVvOgW6oWNnPZwzFcESGAOuZIkfSKlLkTVopb3W")
                .setOAuthAccessTokenSecret("XBSPxErGDtV1l7o1mMLOm4VWtY8NJ57N70E1eAfCr4eAh");
        TwitterFactory tf = new TwitterFactory(cb.build());
        twitter = tf.getInstance();        
    }        
    
    @Override
    public String getKeyword(){
        return keyword;
    }
    
    @Override
    public void setKeyword(String keyword){
        this.keyword = keyword;
    }
    
    @Override
    public void fetchFeed(){
        MySQLDb db = new MySQLDb();
        // Here you can specify which keyword you want to search for
        Query query = new Query(keyword);
        
        // setCount: the maximum amount of tweets returned
        // setSince: as it suggests, retrieve tweets since the given date
        query.setCount(200);
        query.setSince("2015-03-01");
                
        // Prints out every tweet returned
        try {
            QueryResult result = twitter.search(query);
            
            for (Status status : result.getTweets()) {
                long id = status.getId();
                long timeStamp = status.getCreatedAt().getTime();
                String user = status.getUser().getName();
                String location = "" + status.getGeoLocation();
                String text = status.getText();
                
                db.insertFeed("Twitter", Long.toString(id), text, user, timeStamp, location);
                 
            }
        } catch (TwitterException e){
            e.printStackTrace();
        }        
    }   
}

