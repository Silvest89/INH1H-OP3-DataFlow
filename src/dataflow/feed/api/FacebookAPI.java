package dataflow.feed.api;

import com.restfb.*;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import dataflow.database.MySQLDb;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class FacebookAPI extends FeedAPI{
    //This line of code holds the auth key to connect to facebook
    FacebookClient facebookClient = null;

    public String feedId;
    public long timeStamp;
    public String user;
    public String text;
    public String location;
    public String feedType;

    public FacebookAPI(){       
        connect();
    }
    /**
     * Constructor method constructing a facebookAPI object using the given access token
     * @param accessToken 
     */
    public FacebookAPI(String accessToken){
        facebookClient = new DefaultFacebookClient(accessToken);

    }
    
    @Override
    public void connect(){
        AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken("954169427935318", "4f7915b1abc5973dcbc9301a86bc33b5");
        String token=accessToken.getAccessToken();        
        facebookClient = new DefaultFacebookClient(token);
    }   

    /**
     * Method which retrieves posts from Facebook 
     */
    @Override
    public void fetchFeed() {

        MySQLDb db = new MySQLDb();
        //Fetches the feed on the boijmans museum page
        Connection<Post> messages = facebookClient.fetchConnection("boijmans/feed", Post.class, Parameter.with("until", "1426291200"/*"1427068800"*/), Parameter.with("since", /*"1424649600"*/ "1426118400"));

        //Loops through all posts and gets the useful information
        //Then saves it all in variables for adding it in the database later
        for (List<Post> connectionPage : messages) {
            for (Post p : connectionPage) {

                feedId = p.getId();
                Date createdTime = p.getCreatedTime();
                timeStamp = createdTime.getTime() / 1000;
                user = p.getFrom().getName();
                text = p.getMessage();
                location = "";
                feedType = "Facebook";
                Connection<NamedFacebookType> likesSection = facebookClient.fetchConnection(p.getId() + "/likes", NamedFacebookType.class);

                // makes a nameList, then loops through all the posts looking for likes
                // then it saves every person's name who liked into the nameList
                ArrayList<String> nameList = new ArrayList();
                for (List<NamedFacebookType> likesList : likesSection) {
                    for (NamedFacebookType person : likesList) {
                        nameList.add(person.getName());
                    }
                }

                try {
                    // returns the feed_id value and puts all the requested data into 2 feeds of the database
                    int resultNumber = db.insertFeed(feedType, feedId, text, user, timeStamp, location);
                    if (resultNumber > 0) {
                        for (int i = 0; i < nameList.size(); i++) {
                            db.insertFacebookLikes(resultNumber, nameList.get(i));
                        }
                    }
                } catch (Exception e) {
                    throw e;
                }
            }
        }
    }
}
