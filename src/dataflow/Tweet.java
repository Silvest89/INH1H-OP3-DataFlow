package dataflow;

public class Tweet {
    
    private String id;
    private String timeStamp;
    private String user;
    private String location;
    private String text;
    
    public Tweet(String id, String timeStamp, String user, String location, String text){
        this.id = id;
        this.timeStamp = timeStamp;
        this.user = user;
        this.location = location;
        this.text = text;
    }
    
    public String getId(){
        return id;
    }
    
    public String getTimeStamp(){
        return timeStamp;
    }
    
    public String getUser(){
        return user;
    }
    
    public String getLocation(){
        return location;
    }
    
    public String getText(){
        return text;
    }

}

