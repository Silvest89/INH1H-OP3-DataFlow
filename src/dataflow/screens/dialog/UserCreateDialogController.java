/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens.dialog;

import dataflow.screens.dialog.Dialog;
import dataflow.*;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class UserCreateDialogController extends Dialog implements Initializable {

    @FXML
    private TextField userNameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField firstNameField;
    
    @FXML
    private TextField lastNameField;    
    
    @FXML
    private TextField emailField;    
    
    @FXML
    private ChoiceBox accessChoiceBox;
       
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO                
        accessChoiceBox.getItems().add("User");
        if(DataFlow.account.getAccessLevel() == Account.ADMIN){
            accessChoiceBox.getItems().add("Supervisor");        
            accessChoiceBox.getItems().add("Admin");
        }
        accessChoiceBox.setValue("User");
    }        
    
    @FXML
    private void handleOk() throws Exception {
        if (isInputValid()) {
            okClicked = true;
            MySQLDb db = new MySQLDb();
            int accessLevel = Account.NORMAL;
            switch(accessChoiceBox.getValue().toString()){
                case "Supervisor":
                    accessLevel = Account.SUPERVISOR;
                    break;
                case "Admin":
                    accessLevel = Account.ADMIN;
                    break;                                                            
            }
            System.out.println("test");
            if(!db.createUser(userNameField.getText(), passwordField.getText(), firstNameField.getText(), lastNameField.getText(), emailField.getText(), accessLevel)){
                Utility.alertWindow(dialogStage, AlertType.ERROR, "Error", "An error occurred during account creation.", "This username may already be in use.");       
            }
            else{
                Utility.alertWindow(dialogStage, AlertType.INFORMATION, "Success.", null, "The account has been successfully created.");
            }               
            dialogStage.close();
        }
    }
    
    private boolean isInputValid() {
        String errorMessage = "";
        if(firstNameField.getText() == null || firstNameField.getLength() == 0 || !firstNameField.getText().matches("[a-zA-Z ]*"))
            errorMessage += "No valid first name!\n"; 
                
        if(lastNameField.getText() == null || lastNameField.getLength() == 0 || !lastNameField.getText().matches("[a-zA-Z ]*"))
            errorMessage += "No valid last name!\n";     
                
        if(emailField.getText() == null || emailField.getLength() == 0 || !Utility.EmailValidator(emailField.getText()))
            errorMessage += "No valid email!\n";     
        
        if(userNameField.getText() == null || userNameField.getLength() == 0 || !userNameField.getText().matches("^[a-zA-Z0-9_]*$"))
            errorMessage += "No valid username!\n";     
        
        if(passwordField.getText() == null || passwordField.getLength() == 0 || !passwordField.getText().matches("^[a-zA-Z0-9_]*$"))
            errorMessage += "No valid password!\n";             
        
        if(errorMessage.length() == 0)
            return true;
        else{
            Utility.alertWindow(dialogStage, AlertType.ERROR, "Invalid Fields", "Please correct the invalid fields.", errorMessage);
            return false;
        }
    }    
}
