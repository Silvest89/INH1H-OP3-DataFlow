/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.DataFlow;
import dataflow.database.MySQLDb;
import dataflow.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

/**
 * FXML Login Controller class
 * Contains all methods concerning login
 * @author Johnnie Ho
 */
public class LoginController extends ControlledScreen implements Initializable {
    
    @FXML
    private Label label;
    
    @FXML
    private TextField username;
    
    @FXML
    private PasswordField password;
    
    /**
     * Method which makes use the user can log in. Uses the validateLogin method from MySQLDb class
     * @param event 
     */
    @FXML
    private void login(ActionEvent event) {
        
        MySQLDb mySQL = new MySQLDb();  
        if(username.getLength() <= 0 || password.getLength() <= 0){
            label.setTextFill(Color.web("#9D1309"));
            label.setText("Username and/or password cannot be empty.");
            return;
        }
        
        try {
            mySQL.validateLogin(username.getText(), password.getText());
            if(Account.getAccount()){
                label.setTextFill(Color.web("#00AF33"));
                label.setText("Logging in.");
                DataFlow.mainContainer.unloadScreen("Login");
                DataFlow.setScreen("Main");    
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
    
    /**
     * Method which prepares the log in screen for further use (clears the text and password fields)
     */
    @Override
    public void prepare(){
        username.clear();
        password.clear();
    }
}
