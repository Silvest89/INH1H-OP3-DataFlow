/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class MainController extends ControlledScreen implements Initializable {

    private final ObservableList<Tweet> data = FXCollections.observableArrayList();
    private final ObservableList<String> choices = FXCollections.observableArrayList();

    Account account = DataFlow.account;
        
    @FXML
    private Label label;

    @FXML
    private Label showUserName;

    @FXML
    private Label showPageName;

    @FXML
    private Pane userPane;

    @FXML
    private TextField createName;

    @FXML
    private TextField createPass;

    @FXML
    private TextField createConfirm;

    @FXML
    private Text createWarning;

    private boolean userCreateOpened = false;
    private boolean userDeleteOpened = false;

    @FXML
    private Button addUser;
    @FXML
    private Button createUser;
    @FXML
    private Label userCreation;
    @FXML
    private Button logOutButton;
    @FXML
    private Pane userDeletePane;
    @FXML
    private Label userDeletion;
    @FXML
    private Text deleteWarning;
    @FXML
    private ChoiceBox namesChoice;
    @FXML
    private PasswordField deletePassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        namesChoice.setItems(choices);

    }

    @Override
    public void prepare() {
            //label.setText(account.getUserName());
            showUserName.setText("Welcome, " + account.getUserName() + "!");
            showPageName.setText("Home page");
            userPane.setVisible(false);
            userDeletePane.setVisible(false);
    }

    @FXML
    public void openAddUser(ActionEvent event) {

        if (!userCreateOpened && !userDeleteOpened) {
            userCreateOpened = true;
            userPane.setVisible(true);
            createWarning.setVisible(false);
        } else {
            userCreateOpened = false;
            userPane.setVisible(false);
            createWarning.setVisible(true);
            createName.clear();
            createPass.clear();
            createConfirm.clear();
        }
    }

    @FXML
    public void openDeleteUser(ActionEvent event) {
        if (!userDeleteOpened && !userCreateOpened) {
            userDeleteOpened = true;
            userDeletePane.setVisible(true);
            deleteWarning.setVisible(false);
            try {
                choices.remove(0, choices.size());
                Database d = new Database();
                ArrayList<String> choicesAL = d.populateChoiceBox();
                for(String s : choicesAL){
                    choices.add(s);
                }
            
            } catch (Exception e){
            
            }
            
        } else {
            userDeleteOpened = false;
            userDeletePane.setVisible(false);
            deleteWarning.setVisible(true);
            namesChoice.setValue(null);
            deletePassword.clear();
        }
    }

    @FXML
    public void createUser(ActionEvent event) throws Exception {
        String name = createName.getText();
        String pass = createPass.getText();
        String confirm = createConfirm.getText();
        try {
            if (pass.equals(confirm)) {
                Database d = new Database();
                d.addUser(name, pass);
                userPane.setVisible(false);
                userCreateOpened = false;
            } else {
                createWarning.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @FXML
    public void deleteUser(ActionEvent event) {
        String name = "" + namesChoice.getValue();
        String passWord = deletePassword.getText();
        
        try {
            Database d = new Database();
            if(d.checkAccount(name, passWord)){
                d.removeUser(name, passWord);
                userDeletePane.setVisible(false);  
                userDeleteOpened = false;
            }
            
            else {
                deleteWarning.setVisible(true);
            }
            
            
        } catch (Exception e){
            e.printStackTrace();
        }    
    }

    @FXML
    private void goToStatistics(ActionEvent event) {
        DataFlow.setScreen("Statistics");
    }

    @FXML
    private void logOut(ActionEvent event) {
        DataFlow.setScreen("Login");
    } 
}