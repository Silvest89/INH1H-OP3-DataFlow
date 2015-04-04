/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens.dialog;

import dataflow.Account;
import dataflow.MySQLDb;
import dataflow.Utility;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class UserDeleteDialogController extends Dialog implements Initializable {

    @FXML
    private ChoiceBox deleteUserCb;
    
    @FXML
    private PasswordField passwordField;
    
    private final ObservableList<String> choices = FXCollections.observableArrayList();
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        deleteUserCb.setItems(choices);
        try{
            MySQLDb db = new MySQLDb();
            ArrayList<String> choicesAL = db.getAccountList();
            for(String s : choicesAL){
                choices.add(s);
            }        
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    
    @FXML
    private void handleOk() throws Exception {
        if (isInputValid()) {
            okClicked = true;
            MySQLDb db = new MySQLDb();
            if(db.checkAccount(passwordField.getText(), Account.ADMIN)){
                Optional<ButtonType> result = Utility.confirmationWindow(dialogStage, "Confirmation Dialog", "Confirm deletion", "Are you sure you want to delete " + deleteUserCb.getValue().toString() + "?");

                if (result.get() == ButtonType.OK){
                    db.deleteUser(deleteUserCb.getValue().toString());
                } else {
                    Utility.alertWindow(dialogStage, AlertType.INFORMATION, "Success.", null, "The account has been successfully deleted.");
                }      
                dialogStage.close();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred, please try again.");
                alert.setContentText("Your password seems to be invalid.");

                alert.showAndWait();                
            }
            
        }
    }
    
    private boolean isInputValid() {
        String errorMessage = "";     
        
        if(deleteUserCb.getValue() == null)
            errorMessage += "You need to select a user to delete!\n"; 
        
        if(deleteUserCb.getValue() != null && deleteUserCb.getValue().toString().equals(Account.getUserName())) 
            errorMessage += "You cannot delete yourself!\n"; 
        
        if(passwordField.getText() == null || passwordField.getLength() == 0 || !passwordField.getText().matches("^[a-zA-Z0-9_]*$"))
            errorMessage += "No valid password!\n";      
        
        if(errorMessage.length() == 0)
            return true;
        else{
            // Show the error message.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Error");
            alert.setHeaderText("An error occurred, please try again.");
            alert.setContentText(errorMessage);

            alert.showAndWait();

            return false;
        }
    }        
}
