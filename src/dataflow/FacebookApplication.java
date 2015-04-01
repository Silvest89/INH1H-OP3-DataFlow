package dataflow;

import com.restfb.*;
import com.restfb.FacebookClient.AccessToken;
import com.restfb.types.NamedFacebookType;
import com.restfb.types.Post;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class FacebookApplication {
    //This line of code holds the auth key to connect to facebook
    FacebookClient facebookClient = null;

    public FacebookApplication(String accessToken){
        facebookClient = new DefaultFacebookClient(accessToken);
    }
    
    public void fetch(){
        //Fetches the feed on the boijmans museum page
        Connection<Post> messages = facebookClient.fetchConnection("boijmans/feed", Post.class, Parameter.with("since", "1424649600"), Parameter.with("until", "1427068800"), Parameter.with("date_format", "U"));
       
        //Loops through all posts and gets the useful information
        //And prints it to the screen
        for (List<Post> connectionPage : messages){
            for(Post p : connectionPage){
                System.out.println(p.getMessage());
                Connection<NamedFacebookType> messages2 = facebookClient.fetchConnection(p.getId() + "/likes", NamedFacebookType.class);
                int count = 0;
                for(List<NamedFacebookType> test : messages2){
                    for(NamedFacebookType p2 : test){
                        System.out.println(p2.getName());
                        count += 1;
                    }
                }
                System.out.println(count);
                //System.out.println(p.getId() + ", " + ((long)(p.getCreatedTime().getTime() / 1000L)) + ", " + p.getFrom().getName() + ", " + p.getMessage());
            }
        }
    }
    
    public static void main(String[] args){
        AccessToken accessToken = new DefaultFacebookClient().obtainAppAccessToken("954169427935318", "4f7915b1abc5973dcbc9301a86bc33b5");
        String token=accessToken.getAccessToken();
        System.out.println(token);
        FacebookApplication fb = new FacebookApplication(token);
        fb.fetch();
    }
}
