/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.util.HashMap;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * 'Main' Class which controls the application
 * @author Johnnie Ho
 */
public class DataFlow extends Application {
    
    public static HashMap<String, String> screens = new HashMap<>();    
                                                    
    public static Stage stage;
    public static ScreensController mainContainer;    
    
    /**
     * Method which starts the application by reading the config file andloads the screens
     * @param stage
     * @throws Exception 
     */
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        Config.readConfig();
        MySQLDb.getDataSource();   

        screens.put("Login", "screens/Login.fxml");
        screens.put("Main", "screens/Main.fxml");
        screens.put("Statistics", "screens/Statistics.fxml");
        screens.put("Graphs", "screens/Graphs.fxml");
        
        mainContainer = new ScreensController(); 
        mainContainer.loadScreen("Login", 
            screens.get("Login"));
        mainContainer.setScreen("Login");       
        
        Scene scene = new Scene(mainContainer);         
        stage.setScene(scene); 
        stage.setResizable(false);
        stage.setTitle("Data Flow"); 
        stage.show(); 
    }

    /**
     * Method which launches the application
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Method which sets the main window size of the application
     */
    public static void setMainWindowSize(){
        stage.sizeToScene();
    }

    /**
     * Method which sets the screen to the given screen in the parameter
     * @param screen screen which has to be set
     */
    public static void setScreen(String screen){
        if(screens.get(screen) != null){            
            //iterating over keys only
            for (String key : screens.keySet()) {
                mainContainer.unloadScreen(key);
            }   
            
            if(mainContainer.getScreen(screen) == null)
                mainContainer.loadScreen(screen, screens.get(screen));

            mainContainer.setScreen(screen);      
            
            stage.setTitle(screen + " Page");
        }
    }           
    
    /**
     * Method which validates the log in, when a member tries to log into the system
     * @param username
     * @param password
     * @return true or false, based on the fact if the userName/password combination is present in the database
     */
    public static boolean validateLogin(String username, String password){
        MySQLDb db = new MySQLDb();
        if(db.validateLogin(username, password)){
            
            return true;
        }
        return false;
    }
}
