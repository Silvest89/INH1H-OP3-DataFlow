package dataflow;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;


/**
 * FXML Controller class
 *
 * @author ninc__000
 */
public class SentimentStatisticsController implements Initializable, ControlledScreen {

    String pageName = "Sentiment Statistics";
    
    ScreensController myController;
    
    Account account = DataFlow.account;
    
    @FXML
    private Label label;
    
    @FXML
    private Label showPageName;
    
    @FXML
    private Label showUserName;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    public void prepare () {
        showUserName.setText("Welcome, " + account.getUserName() + "!");
        showPageName.setText(pageName);
    }

    @Override
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
    
    public void goToMain (ActionEvent event) {
        DataFlow.mainContainer.loadScreen(DataFlow.MAIN_SCREEN, DataFlow.MAIN_SCREEN_FXML);
        DataFlow.mainContainer.setScreen(DataFlow.MAIN_SCREEN);
    }
    
      public void goToArchStatistics(ActionEvent event) {
        DataFlow.mainContainer.loadScreen(DataFlow.ASTATISTICS_SCREEN,
                DataFlow.ASTATISTICS_SCREEN_FXML);
        myController.setScreen(DataFlow.ASTATISTICS_SCREEN);
   }

   
   public void goToGeoStatistics(ActionEvent event) {
       DataFlow.mainContainer.loadScreen(DataFlow.GSTATISTICS_SCREEN,
               DataFlow.GSTATISTICS_SCREEN_FXML);
       myController.setScreen(DataFlow.GSTATISTICS_SCREEN);
   }
}
