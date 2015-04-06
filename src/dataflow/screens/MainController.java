/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.Account;
import dataflow.DataFlow;
import dataflow.database.MySQLDb;
import dataflow.Utility;
import dataflow.feed.api.FacebookAPI;
import dataflow.feed.api.InstagramAPI;
import dataflow.feed.api.TwitterAPI;
import dataflow.feed.api.Weather;
import dataflow.screens.dialog.UserCreateDialogController;
import dataflow.screens.dialog.UserDeleteDialogController;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML MainController class
 * Contains all methods concerning the main screen
 * @author Johnnie Ho
 */
public class MainController extends ControlledScreen implements Initializable {
        
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
    
    @FXML
    private TabPane mainTabPane;
    
    @FXML
    private Tab mainTab1;
    
    @FXML
    private Tab mainTab2;
    
    @FXML
    private TextField pFirstName;
    
    @FXML
    private TextField pLastName;
    
    @FXML
    private TextField pEmail;
    
    @FXML
    private PasswordField pPassword;

    @FXML
    private ToggleButton pEditButton;
    
    @FXML
    private Button pConfirmButton;
    
    @FXML
    private Text adminStatusText;
    
    @FXML
    private ProgressBar commandProgress;
    
    @FXML
    private ChoiceBox commandChoice;
    
    @FXML
    private Button executeCommand;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setPersonalPageDisabled(true);
        ObservableList<String> commands = FXCollections.observableArrayList();
        commands.add("Fetch Weather");
        commands.add("Update Twitter");
        commands.add("Update Instagram");
        commands.add("Update Facebook");
        commandChoice.setItems(commands);
        
        if(Account.getAccessLevel() < Account.SUPERVISOR){
            addUser.setDisable(true);
            deleteUser.setDisable(true);
            mainTab2.setDisable(true);
        }
        else if(Account.getAccessLevel() == Account.SUPERVISOR){
            deleteUser.setDisable(true);
        }
        
        pFirstName.setText(Account.getFirstName());
        pLastName.setText(Account.getLastName());
        if(Account.getEmail() != null)
            pEmail.setText(Account.getEmail());
        else
            pEmail.setText("-No Email Found-");
        
    }

    /**
     * Method which prepares the screen based on the accessLevel of the logged in account
     */
    @Override
    public void prepare() {
        setWelcomeMessage(showUserName, showAccessLevel);
    }

    /**
     * Methods which opens an alert window for user addition
     * @param event
     * @return 
     */
    @FXML
    public boolean openAddUser(ActionEvent event) {
        try{
        // Load the fxml file and create a new stage for the popup dialog.
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(MainController.class.getResource("/dataflow/screens/dialog/UserCreateDialog.fxml"));
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

    /**
     * Method which opens an alert window for user deletion
     * @param event
     * @return 
     */
    @FXML
    public boolean openDeleteUser(ActionEvent event) {
        try{
            // Load the fxml file and create a new stage for the popup dialog.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainController.class.getResource("/dataflow/screens/dialog/UserDeleteDialog.fxml"));
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
    
    @FXML
    public void actionEditButton(ActionEvent event){        
        if(pEditButton.isSelected())
            setPersonalPageDisabled(false);
        else
            setPersonalPageDisabled(true);
    }
    
    public void setPersonalPageDisabled(boolean state){        
        pConfirmButton.setDisable(state);
        pFirstName.setDisable(state);
        pLastName.setDisable(state);
        pEmail.setDisable(state);
        pPassword.setDisable(state);
    }
    
    /**
     * Method which checks the input by passing the input texts to the regex validator in the Utility class
     * @param event
     * @return 
     */
    @FXML
    public boolean actionConfirmEdit(ActionEvent event){   
        boolean error = false;
        if(!Utility.nameValidation(pFirstName.getText()))
            error = true;
        
        if(!Utility.nameValidation(pLastName.getText()))
            error = true;        
        
        if(!Utility.EmailValidator(pEmail.getText()))
            error = true;               
        
        if(error){
            Utility.alertWindow(null, Alert.AlertType.ERROR, "Invalid Fields", "Please correct the invalid fields.", "Please recheck your fields.");
            return false;
        }
        // Create the custom dialog.
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Confirm Password");
        dialog.setHeaderText("Please confirm your password.");

        // Set the icon (must be included in the project).
        //dialog.setGraphic(new ImageView(this.getClass().getResource("login.png").toString()));

        // Set the button types.
        ButtonType loginButtonType = new ButtonType("Confirm", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields.
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        PasswordField password = new PasswordField();
        password.setPromptText("Password");

        grid.add(new Label("Password:"), 0, 0);
        grid.add(password, 1, 0);


        // Enable/Disable login button depending on whether a username was entered.
        Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
        loginButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            loginButton.setDisable(newValue.trim().isEmpty());
        });

        dialog.getDialogPane().setContent(grid);

        // Request focus on the username field by default.
        Platform.runLater(() -> password.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return password.getText();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            try{
                MySQLDb db = new MySQLDb();
                if(db.checkAccount(usernamePassword, Account.NORMAL)){                        
                    db.updateAccountDetails(pFirstName.getText(), pLastName.getText(), pEmail.getText(), pPassword.getText());    
                    pEditButton.setSelected(false);
                    setPersonalPageDisabled(true);
                }
            }
            catch(UnsupportedEncodingException | NoSuchAlgorithmException e){
                e.getMessage();
            }
        });         
        return true;
    }
    
    /**
     * Method which fetches the most recent weather information when a button is clicked
     * @param event 
     */
    @FXML
    public void fetchWeatherButton(ActionEvent event){
        executeCommand.setDisable(true);
        commandProgress.setProgress(0.25);
        
        Runnable task = () -> {
            switch(commandChoice.getValue().toString()){
                case "Fetch Weather":{
                    adminStatusText.setText("Fetching weather information...");
                    commandProgress.setProgress(0.5);
                    Weather.getWeather();                        
                    adminStatusText.setText("Successfully fetched the latest weather information.");
                    break;
                }
                case "Update Instagram":{
                    adminStatusText.setText("Grabbing latest Instagram feeds...");
                    InstagramAPI instagram = new InstagramAPI();
                    commandProgress.setProgress(0.5);
                    instagram.setKeyword("boijmans");
                    instagram.fetchFeed();
                    adminStatusText.setText("Successfully grabbed latest Instagram feeds...");
                    break;
                }    
                case "Update Twitter":{
                    adminStatusText.setText("Grabbing latest Twitter feeds...");
                    TwitterAPI twitter = new TwitterAPI();
                    commandProgress.setProgress(0.5);
                    twitter.setKeyword("boijmans");
                    twitter.fetchFeed();
                    adminStatusText.setText("Successfully grabbed latest Twitter feeds...");
                    break;
                }    
                case "Update Facebook":{
                    adminStatusText.setText("Grabbing latest Facebook posts...");
                    FacebookAPI facebook = new FacebookAPI();
                    commandProgress.setProgress(0.5);
                    facebook.fetchFeed();
                    adminStatusText.setText("Successfully grabbed latest Facebook posts...");
                    break;
                }                    
            }      
            commandProgress.setProgress(1);
            executeCommand.setDisable(false);  
            //Thread.currentThread().interrupt();
        };            
        
        Utility.executor.execute(task);                  
    }
}
