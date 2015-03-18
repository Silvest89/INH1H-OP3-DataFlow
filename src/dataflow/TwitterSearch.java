package dataflow;

import twitter4j.*;
import java.util.logging.Logger;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterSearch {
    private final Logger logger = Logger.getLogger(TwitterSearch.class.getName());

    public static void main(String[] args) {
        new TwitterSearch().retrieve();
    }
    
    public void retrieve(){
        logger.info("Retrieving Tweets....");
        // Same configuration as the twitterStream
        // Again, do not share these keys and tokens
        
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("PhXinpF4eADYZCnfhM8BrmBOu")
                .setOAuthConsumerSecret("28u6vrEsyuPrbcz8wGFARuBktIIko42GNiK4HZREbRdo5lZxPo")
                .setOAuthAccessToken("3053907616-RVvOgW6oWNnPZwzFcESGAOuZIkfSKlLkTVopb3W")
                .setOAuthAccessTokenSecret("XBSPxErGDtV1l7o1mMLOm4VWtY8NJ57N70E1eAfCr4eAh");
        TwitterFactory tf = new TwitterFactory(cb.build());
        Twitter twitter = tf.getInstance();
        
        // Here you can specify which keyword you want to search for
        String search = "#boijmans";
        Query query = new Query(search);
        
        // setCount: the maximum amount of tweets returned
        // setSince: as it suggests, retrieve tweets since the given date
        query.setCount(10000);
        query.setSince("2015-03-01");
                
        // Prints out every tweet returned
        try {
            QueryResult result = twitter.search(query);
            System.out.println("Count : " + result.getTweets().size());
            for (Status status : result.getTweets()) {
                System.out.println("Search term: " + search);
                System.out.println("User : " + status.getUser().getName());
                System.out.println("Time stamp: " + status.getCreatedAt()); 
                System.out.println("Text : " + status.getText());
                System.out.println("");
            }
        } catch (TwitterException e){
            e.printStackTrace();
        }
        logger.info("done! ");
        
    }

}

