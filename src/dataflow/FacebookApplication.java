package dataflow;

import com.restfb.*;
import com.restfb.types.Post;
import java.util.List;

public class FacebookApplication {
    //This line of code holds the auth key to connect to facebook
    FacebookClient facebookClient = new DefaultFacebookClient("CAACEdEose0cBACQPsHWOvhNJQiWdUcgE4xNNfyqXgZBT7OgZCoMfjgxurwPMXH4XVHbjR3Wc2VJi6FqNgbC5gauhTkOaFm7IuTpQVHZAKsmIUMRrlZBVJcqs9pnG7sxR8IlaiA0rZC0K9st3oAMv3ItY6wVZCfFuZCVYJLsVxXaQ5vZBFB4U8itVcJlI8NMUIL6HofZC276zNLACqGI2ZCR6CQij7vQNFZALVwZD");

    public void fetch(){
        //Fetches the feed on the boijmans museum page
        Connection<Post> messages = facebookClient.fetchConnection("boijmans/feed", Post.class);
       
        //Loops through all posts and gets the useful information
        //And prints it to the screen
        for (List<Post> connectionPage : messages){
            for(Post p : connectionPage){
                System.out.println(p.getId() + ", " + p.getCreatedTime() + ", " + p.getFrom().getName() + ", " + p.getMessage());
            }
        }
    }
    
    public static void main(String[] args){
        FacebookApplication fb = new FacebookApplication();
        fb.fetch();
    }
}
