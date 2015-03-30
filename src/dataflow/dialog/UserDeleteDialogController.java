/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.dialog;

import dataflow.Account;
import dataflow.DataFlow;
import dataflow.Database;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class UserDeleteDialogController implements Initializable {

    @FXML
    private ChoiceBox deleteUserCb;
    
    @FXML
    private PasswordField passwordField;
    
    private final ObservableList<String> choices = FXCollections.observableArrayList();
    
    private Stage dialogStage;
    private boolean okClicked = false;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        deleteUserCb.setItems(choices);
        try{
            Database d = new Database();
            ArrayList<String> choicesAL = d.populateChoiceBox();
            for(String s : choicesAL){
                choices.add(s);
            }        
        }catch(Exception e){
            e.printStackTrace();
        }
    }    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }    
    public boolean isOkClicked() {
        return okClicked;
    }    
    
    @FXML
    private void handleOk() throws Exception {
        if (isInputValid()) {
            okClicked = true;
            Database db = new Database();
            if(db.checkAccount(passwordField.getText())){
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText("Confirm deletion");
                alert.setContentText("Are you sure you want to delete " + deleteUserCb.getValue().toString() + "?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    db.removeUser(deleteUserCb.getValue().toString(), passwordField.getText());
                } else {
                    // ... user chose CANCEL or closed the dialog
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
        
        if(deleteUserCb.getValue() != null && deleteUserCb.getValue().toString().equals(DataFlow.account.getUserName())) 
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