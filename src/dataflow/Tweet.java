package dataflow;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tweet {
    
    private long id;
    private String timeStamp;
    private String user;
    private String location;
    private String text;
    private int weather;
    
    public Tweet(long id, Date timeStamp, String user, String location, String text, int weather){
        this.id = id;    
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm z");
        this.timeStamp = format.format(timeStamp);
        this.user = user;
        this.location = location;
        this.text = text;
        this.weather = weather;
    }
    
    public long getId(){
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
    
    public int getWeather(){
        return weather;
    }

}

