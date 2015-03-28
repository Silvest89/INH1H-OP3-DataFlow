/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Johnnie Ho
 */
public class DataFlow extends Application {
    
    public static Account account = null;
    public static Map<String, String> screens = new HashMap<String, String>();    
                                                    
    public static Stage stage;
    public static ScreensController mainContainer;    
    
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        screens.put("Login", "Login.fxml");
        screens.put("Main", "Main.fxml");
        screens.put("Statistics", "Statistics.fxml");
        screens.put("Graphs", "Graphs.fxml");
        
        Calendar fromTime = Calendar.getInstance();
        fromTime.set(2015, 2, 23, 8, 0);
        Calendar toTime = Calendar.getInstance();
        toTime.set(2015, 2, 23, 5, 0);
        Date date = toTime.getTime();
        SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
        System.out.println(format.format(date));
        //long unixTime = (long)calendar.getTimeInMillis()/1000L;
        //Date date =  calendar.getTime();
        //System.out.println(unixTime);
        
        Weather.getWeather();       

        mainContainer = new ScreensController(); 
        mainContainer.loadScreen("Login", 
            screens.get("Login"));
        mainContainer.setScreen("Login");       
        

        //Group root = new Group(); 
        //root.getChildren().addAll(mainContainer); 
        Scene scene = new Scene(mainContainer);         
        stage.setScene(scene); 
        //stage.setWidth(320);
        //stage.setHeight(230);
        stage.setResizable(false);
        stage.setTitle("Data Flow"); 
        stage.show(); 
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        /*Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();*/
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void setMainWindowSize(){
        stage.setWidth(820);    
        stage.setHeight(620);
    }
    
    public static void setLoginWindowSize() {
        stage.setWidth(320);
        stage.setHeight(240);
    }
    
    public static void setScreen(String screen){
        if(screens.get(screen) != null){
            System.out.println(screens.get(screen));
            if(mainContainer.getScreen(screen) == null)
                mainContainer.loadScreen(screen, screens.get(screen));
            mainContainer.setScreen(screen);
        }
    }           
}
