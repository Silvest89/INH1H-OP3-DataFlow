/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.ScreensController;
import dataflow.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import org.apache.commons.lang3.text.WordUtils;

/**
 * Class which holds all methods concerning screen traversal
 * @author Johnnie Ho
 */
public class ControlledScreen {

    ScreensController myController;
    
     //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
     
    public void prepare(){
    }
    
    @FXML
    private void goToMain(ActionEvent event) {
        DataFlow.setScreen("Main");
    }
    
    @FXML
    private void goToStatistics(ActionEvent event) {
        DataFlow.setScreen("Statistics");
    }
    
    @FXML
    private void goToGraphs(ActionEvent event) {
        DataFlow.setScreen("Graphs");
    }
    
    @FXML
    private void logOut(ActionEvent event) {
        Account.resetAccount();
        DataFlow.setScreen("Login");
    }     
    
    @FXML
    private void aboutScreen(ActionEvent event){
        Utility.alertWindow(null, Alert.AlertType.INFORMATION, "About", "Data Flow Pro 2015\u00a9", "Jesse van Breda\nJohnnie Ho\nAnton Vu");
    }
    
    public void setWelcomeMessage(Label user, Label accessText){
        user.setText("Welcome, " + WordUtils.capitalizeFully(Account.getFirstName()) + " " + WordUtils.capitalizeFully(Account.getLastName()) + "!");
        String access = "";
        switch(Account.getAccessLevel()){
            case Account.NORMAL:
                access = "Normal Account";
                break;
            case Account.SUPERVISOR:
                access = "Supervisor Account";
                break;
            case Account.ADMIN:
                access = "Admin Account";
                break;                 
        }      
        accessText.setText(access);
    }
}
