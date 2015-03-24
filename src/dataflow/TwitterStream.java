package dataflow;

import java.util.Date;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

public class TwitterStream {

    Database d;
    
    long id;
    long timeStamp;
    String user;
    String location;
    String text;

    public static void main(String[] args) throws Exception {
        TwitterStream ts = new TwitterStream();
        ts.tweetStream();
    }

    /**
     * This method regulates the retrieval of tweets containing a certain (set
     * of) keyword(s)
     */
    public void tweetStream() {
        d = new Database();
        // Sets the configuration of the streaming application
        // A twitter application is required if you want to use the api.
        // IMPORTANT NOTE: do not share the keys and tokens as other people can/will use our application
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
                .setOAuthConsumerKey("PhXinpF4eADYZCnfhM8BrmBOu")
                .setOAuthConsumerSecret("28u6vrEsyuPrbcz8wGFARuBktIIko42GNiK4HZREbRdo5lZxPo")
                .setOAuthAccessToken("3053907616-RVvOgW6oWNnPZwzFcESGAOuZIkfSKlLkTVopb3W")
                .setOAuthAccessTokenSecret("XBSPxErGDtV1l7o1mMLOm4VWtY8NJ57N70E1eAfCr4eAh");
        twitter4j.TwitterStream twitterStream = new twitter4j.TwitterStreamFactory(cb.build()).getInstance();

        StatusListener listener = new StatusListener() {
            
            @Override
            public void onScrubGeo(long a, long b){}
            @Override
            public void onStallWarning(StallWarning warning){}
            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            
            @Override
            public void onStatus(Status status) {
                id = status.getId();
                timeStamp = status.getCreatedAt().getTime();
                user = "" + status.getUser().getName();
                location = "" + status.getGeoLocation();
                text = "" + status.getText();

                try {
                    d.putInDatabase(id, timeStamp, user, location, text);
                } catch(Exception e){
                    e.printStackTrace();
                }
                
                System.out.println(id + ", " + timeStamp + ", " + user + ", " + location + ", " + text);
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }

        };

        FilterQuery fq = new FilterQuery();

        //This array contains the keyword(s) which you want to look for
        String[] keywords = {"the"};

        //This line of code makes sure all incoming tweets are scanned for the given keyword(s)
        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);
    }

}


