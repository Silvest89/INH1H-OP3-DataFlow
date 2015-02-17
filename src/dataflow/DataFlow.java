/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Johnnie Ho
 */
public class DataFlow extends Application {
    
    public static Account account = null;
    public static final String MAIN_SCREEN = "LoginController"; 
    public static final String MAIN_SCREEN_FXML = "LoginController.fxml"; 
     
    @Override
    public void start(Stage stage) throws Exception {
        
        ScreensController mainContainer = new ScreensController(); 
        mainContainer.loadScreen(DataFlow.MAIN_SCREEN, 
                            DataFlow.MAIN_SCREEN_FXML); 
       
        mainContainer.setScreen(DataFlow.MAIN_SCREEN); 
        
        Group root = new Group(); 
        root.getChildren().addAll(mainContainer); 
        Scene scene = new Scene(root); 
        stage.setScene(scene); 
        stage.show(); 
        //Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        /*Scene scene = new Scene(root);
        stage.setTitle("DreamTeam"); 
        stage.setScene(scene);
        stage.show();*/
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
