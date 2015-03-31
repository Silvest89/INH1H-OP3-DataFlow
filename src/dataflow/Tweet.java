package dataflow;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tweet {
    
    private long id;
    private long timeStamp;
    private String user;
    private String location;
    private String text;
    private String timeString;
    
    public Tweet(long id, long timeStamp, String user, String location, String text){
        this.id = id;    
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm z");
        this.timeString = format.format(timeStamp * 1000L);
        this.timeStamp = timeStamp;
        this.user = user;
        this.location = location;
        this.text = text;
    }
    
    public long getId(){
        return id;
    }
    
    public long getTimeStamp(){
        return timeStamp;
    }
    
    public String getTimeString(){
        return timeString;
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

