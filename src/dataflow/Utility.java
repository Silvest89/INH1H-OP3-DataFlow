/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 * Class containing handy methods which can be used throughout the application
 * @author Johnnie Ho
 */
public class Utility {
    //private static final int MYTHREADS = 5;
    public static final ExecutorService executor = Executors.newCachedThreadPool();
    public static final int COMMENT_POSITIVE = 0;
    public static final int COMMENT_NEGATIVE = 1;
    public static final int COMMENT_NEUTRAL = 2;
    
    public static final int WEATHER_POSITIVE = 0;
    public static final int WEATHER_NEGATIVE = 1;
  

    /**
     * Method which shows an alert window with the given parameters
     * @param stage the stage on which the alert has to be opened
     * @param alertType the type of the alert
     * @param title the title of the alert
     * @param header the header of the alert
     * @param content the content of the alert
     */
    public static void alertWindow(Stage stage, AlertType alertType, String title, String header, String content){
        Alert alert = new Alert(alertType);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        alert.showAndWait();
    }
    
    /**
     * Method which shows a confirmation alert with the given parameters (same as 'alertWindow')
     * @param stage
     * @param title
     * @param header
     * @param content
     * @return 
     */
    public static Optional<ButtonType> confirmationWindow(Stage stage, String title, String header, String content){
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initOwner(stage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);

        Optional<ButtonType> result = alert.showAndWait();
        return result;  
    }        
 
    /**
     * Method which uses regex to check if a given string is really a valid email address
     * @param email email which has to be validated
     * @return true or false, based on the fact if the email is valid
     */
    public static boolean EmailValidator(String email) {
        String EMAIL_PATTERN = 
		"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
		+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";        
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }    
    
    /**
     * Method which uses regex to check if a name is valid
     * @param name the name to be validated
     * @return true or false, based on the fact if the name is valid
     */
    public static boolean nameValidation(String name){
        if(name == null || name.length() == 0 || !name.matches("[a-zA-Z ]*"))
            return false;
        
        return true;
    }
    
    /**
     * Method which uses regex to check is a password is valid
     * @param password password to be validated
     * @return true or false, based on the fact if the password is valid
     */
    public static boolean passwordValidation(String password){
        if(password == null || password.length() == 0 || !password.matches("^[a-zA-Z0-9_]*$"))
            return false;   
        
        return true;
    }    
    
    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }    
    
    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }    
    
    public static Date getStartOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }    
    
    public static Date getEndOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }        
    
    public static Date getPreviousMonth(Date date, int amount) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.roll(Calendar.MONTH, -amount);
        return calendar.getTime();
    }            
    
    public static int commentChecker(String text){
        text = text.toLowerCase();
        if(text.matches(".*(aanrader|mooi|uitstekend|goed|leuk|fantastisch|prachtig|krachtig|inspirerend|sympathiek|gezellig|tip|terug|ideaal|moeite waard|enjoy|sprakeloos|ontroer).*")){
            return COMMENT_POSITIVE;
        }            
        else if(text.matches(".*(lelijk|stom|saai|kut|verschrikkelijk|slecht|belediging|fake|gaar).*")){
            return COMMENT_NEGATIVE;
        }
        else{
            //if the comment is not positve or negative the method will automatically assign it the neutral value
            return COMMENT_NEUTRAL;
        }
    }  
    
    public static int weatherChecker(String description){
        description = description.toLowerCase();
        if(description.matches(".*(sunny|cloudy|overcast|light drizzle).*")){
            return WEATHER_POSITIVE;
        }            
        else {
            return WEATHER_NEGATIVE;
        }
    }
}
