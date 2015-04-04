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
 *
 * @author Johnnie Ho
 */
public class DataFlow extends Application {
    
    public static HashMap<String, String> screens = new HashMap<>();    
                                                    
    public static Stage stage;
    public static ScreensController mainContainer;    
    
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
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public static void setMainWindowSize(){
        stage.sizeToScene();
    }

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
    
    public static boolean validateLogin(String username, String password){
        MySQLDb db = new MySQLDb();
        if(db.validateLogin(username, password)){
            
            return true;
        }
        return false;
    }
}
