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
    public static final String LOGIN_SCREEN = "Login"; 
    public static final String LOGIN_SCREEN_FXML = "Login.fxml"; 
    public static final String MAIN_SCREEN = "Main"; 
    public static final String MAIN_SCREEN_FXML = "Main.fxml";
    public static final String STATISTICS_SCREEN = "Statistics";
    public static final String STATISTICS_SCREEN_FXML = "Statistics.fxml";
    public static final String GSTATISTICS_SCREEN = "GeographicStatistics";
    public static final String GSTATISTICS_SCREEN_FXML = "GeographicStatistics.fxml";
    public static final String SSTATISTICS_SCREEN = "SentimentStatistics";
    public static final String SSTATISTICS_SCREEN_FXML = "SentimentStatistics.fxml";
    public static final String ASTATISTICS_SCREEN = "ArchitectureStatistics.fxml";
    public static final String ASTATISTICS_SCREEN_FXML = "ArchitectureStatistics.fxml";
                                                    
    public static Stage stage;
    public static ScreensController mainContainer;
     
    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        
        mainContainer = new ScreensController(); 
        mainContainer.loadScreen(DataFlow.LOGIN_SCREEN, 
                            DataFlow.LOGIN_SCREEN_FXML);
        mainContainer.setScreen(DataFlow.LOGIN_SCREEN);       
        
        
        Group root = new Group(); 
        //root.getChildren().addAll(mainContainer); 
        Scene scene = new Scene(mainContainer);         
        stage.setScene(scene); 
        //stage.setWidth(320);
        //stage.setHeight(230);
        stage.setResizable(false);
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
    
    public static void setMainWindowSize(){
        stage.setWidth(820);    
        stage.setHeight(620);
    }
    
    public static void setLoginWindowSize() {
        stage.setWidth(320);
        stage.setHeight(240);
    }
}
