/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class LoginController implements Initializable, ControlledScreen {

    ScreensController myController;
    
    @FXML
    private Label label;
    
    @FXML
    private TextField username;
    
    @FXML
    private TextField password;
    
    @FXML
    private void login(ActionEvent event) {
        
        Database mySQL = new Database();        
        if(username.getLength() <= 0 || password.getLength() <= 0){
            label.setTextFill(Color.web("#9D1309"));
            label.setText("Username and/or password cannot be empty.");
            return;
        }
        
        try {
            mySQL.validateLogin(username.getText(), password.getText());
            if(DataFlow.account != null){
                label.setTextFill(Color.web("#00AF33"));
                label.setText("Logging in.");
                myController.setScreen(DataFlow.MAIN_SCREEN);                     
            }
            else{
                label.setTextFill(Color.web("#9D1309"));
                label.setText("Username and/or password is incorrect.");
            }
        } catch (Exception ex) {
            label.setText("Error.");
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void setScreenParent(ScreensController screenParent){ 
        myController = screenParent; 
     } 
}
