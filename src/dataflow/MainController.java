/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import dataflow.dialog.UserCreateDialogController;
import dataflow.dialog.UserDeleteDialogController;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
    private Button logOutButton;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @Override
    public void prepare() {
            //label.setText(account.getUserName());
            showUserName.setText("Welcome, " + account.getUserName() + "!");
            showPageName.setText("Home page");
    }

    @FXML
    public boolean openAddUser(ActionEvent event) {
        try{
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("dialog/UserCreateDialog.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        // Create the dialog Stage.
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Create User");
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
            loader.setLocation(MainController.class.getResource("dialog/UserDeleteDialog.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            // Create the dialog Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Delete User");
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