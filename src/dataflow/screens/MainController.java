/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.Account;
import dataflow.DataFlow;
import dataflow.dialog.UserCreateDialogController;
import dataflow.dialog.UserDeleteDialogController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.text.WordUtils;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class MainController extends ControlledScreen implements Initializable {

    Account account = DataFlow.account;
        
    @FXML
    private Label label;

    @FXML
    private Label showUserName;

    @FXML
    private Label showPageName;
    
    @FXML
    private Label showAccessLevel;

    @FXML
    private Button logOutButton;
    
    @FXML
    private Button addUser;
    
    @FXML 
    private Button deleteUser;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if(DataFlow.account.getAccessLevel() < Account.SUPERVISOR){
            addUser.setDisable(true);
            deleteUser.setDisable(true);
        }
        else if(DataFlow.account.getAccessLevel() == Account.SUPERVISOR){
            deleteUser.setDisable(true);
        }
        
        
    }

    @Override
    public void prepare() {
            showUserName.setText("Welcome, " + WordUtils.capitalizeFully(account.getFirstName()) + " " + WordUtils.capitalizeFully(account.getLastName()) + "!");
            String access = "";
            switch(DataFlow.account.getAccessLevel()){
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
            showAccessLevel.setText(access);
    }

    @FXML
    public boolean openAddUser(ActionEvent event) {
        try{
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("/dataflow/dialog/UserCreateDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create User");
        dialogStage.setResizable(false);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(DataFlow.stage);
        Scene scene = new Scene(page);
        dialogStage.setScene(scene);

        // Set the person into the controller.
        UserCreateDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        

        // Show the dialog and wait until the user closes it
        dialogStage.showAndWait();

        return controller.isOkClicked();            
        }
        catch (IOException e) {
            e.printStackTrace();
        }        
        return false;
    }

    @FXML
    public boolean openDeleteUser(ActionEvent event) {
        try{
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("/dataflow/dialog/UserDeleteDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Delete User");
            dialogStage.setResizable(false);
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(DataFlow.stage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller.
            UserDeleteDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);


            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isOkClicked();            
        }
        catch (IOException e) {
            e.printStackTrace();
        }        
        return false;        
    }
}