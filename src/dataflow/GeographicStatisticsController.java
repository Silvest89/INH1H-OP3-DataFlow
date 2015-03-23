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
public class GeographicStatisticsController extends ControlledScreen implements Initializable {

    String pageName = "Geographic Statistics";
    
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
    
    @Override
    public void prepare () {
        showUserName.setText("Welcome, " + account.getUserName() + "!");
        showPageName.setText(pageName);
    }
    
    public void goToMain (ActionEvent event) {
        DataFlow.setScreen("Main");
    }
    
      public void goToArchStatistics(ActionEvent event) {
        DataFlow.setScreen("ArchitectureStatistics");
   }
   public void goToSentStatistics(ActionEvent event) {
        DataFlow.setScreen("Statistics");
   }
   
}
