/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class MainController implements Initializable, ControlledScreen {

    ScreensController myController;
    
    Account account = DataFlow.account;
    
    @FXML
    private Label label;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO        
    }    
    
    public void prepare(){
        label.setText(account.getUserName());
    }
    
    public void setScreenParent(ScreensController screenParent){ 
        myController = screenParent;         
     }     
}
