package dataflow;

import java.text.SimpleDateFormat;
import java.util.Date;
import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

public class TwitterStream {

    Database d;
    
    String id;
    long timeStamp;
    String user;
    String location;
    String text;

    public static void main(String[] args) throws Exception {
        TwitterStream ts = new TwitterStream();
        ts.tweetStream();
    }
     //this method filters the incoming twitterstream and gives the comments a value of negative, positive or neutral
    
   public String commentChecker(String text){
           if(text.matches(".*(mooi|goed|leuk|fantastisch|prachtig).*")) //you can change the words in here to change what the filter thinks is positive
           {return "comment is positief";}
            
            
            else if(text.matches(".*(lelijk|stom|saai|kut|verschrikkelijk).*")) //you can change the words in here to change what the filer thinks is negative
                    {return "comment is negatief";}
            
            else return "comment is neutraal"; //if the comment is not positve or negative the method will automatically assign it the neutral value
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
                id = Long.toString(status.getId());
                timeStamp = status.getCreatedAt().getTime();
                user = "" + status.getUser().getName();
                location = "" + status.getGeoLocation();
                text = "" + status.getText();
                    
                try {                    
                    d.putInDatabase("Twitter", id, text, user, timeStamp, location);
                } catch(Exception e){
                    e.printStackTrace();
                }
                commentChecker(text);
                System.out.println(id + ", " + timeStamp + ", " + user + ", " + location + ", " + text);
                System.out.println(commentChecker(text));
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
        d.close();
    }

}


