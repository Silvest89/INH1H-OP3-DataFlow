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

/**
 *
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
    private void logOut(ActionEvent event) {
        DataFlow.account = null;        
        DataFlow.setScreen("Login");
    }     
}
