package dataflow;

import twitter4j.conf.ConfigurationBuilder;
import twitter4j.*;

public class TwitterStream {

    Database d;
    String id;
    String timeStamp;
    String location;
    String text;

    public static void main(String[] args) {
        TwitterStream ts = new TwitterStream();
        ts.tweetStream();
    }

    /**
     * This method regulates the retrieval of tweets containing a certain (set of) keyword(s)
     */
    public void tweetStream() {
        
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
        
        RawStreamListener listener = new RawStreamListener() {
            @Override
            // This method takes a JSON (in String format) of the received tweet.
            // Then the String will be parsed to retrieve the useful data out of the string
            // After that, the tweet will be inserted into a table in the database
            public void onMessage(String rawJSON) {
                try {
                    d = new Database();
                    id = rawJSON.substring(rawJSON.indexOf("id") + 4, rawJSON.indexOf("id_str") - 3);
                    timeStamp = rawJSON.substring(rawJSON.indexOf("created_at") + 13, rawJSON.indexOf("id") - 3);
                    text = rawJSON.substring(rawJSON.indexOf("text") + 6, rawJSON.indexOf("source") - 3);
                    location = rawJSON.substring(rawJSON.indexOf("location") + 11, rawJSON.indexOf("url") - 3);

                    if (location.equals(" ") || location.equals("")) {
                        location = "unknown";
                    }
                    d.putInDatabase(id, timeStamp, location, text);

                    System.out.println("ID: " + id + ", " + "Time and Date: " + timeStamp + ", " + "Location: " + location + ", " + "Tweet: " + text);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
        };

        FilterQuery fq = new FilterQuery();

        //This array contains the keyword(s) which you want to look for
        String[] keywords = {"#testingTwitter"};

        //This line of code makes sure all incoming tweets are scanned for the given keyword(s)
        fq.track(keywords);

        twitterStream.addListener(listener);
        twitterStream.filter(fq);
    }

}
