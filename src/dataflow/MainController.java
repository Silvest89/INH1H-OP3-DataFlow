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
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class MainController implements Initializable, ControlledScreen {

    ScreensController myController;
    
    final ObservableList<Tweet> data = FXCollections.observableArrayList();

    Account account = DataFlow.account;

    @FXML
    private Label label;
    
    @FXML
    private Label showUserName;
    
    @FXML
    private Label showPageName;

    @FXML
    private TableView TweetBox;


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
    
    private boolean userMenuIsOpened = false;
    
    @FXML
    private Button addUser;
    @FXML
    private TableColumn idCol;
    @FXML
    private TableColumn timeStampCol;
    @FXML
    private TableColumn userCol;
    @FXML
    private TableColumn locationCol;
    @FXML
    private TableColumn textCol;
    @FXML
    private Button createUser;
    @FXML
    private Label userCreation;
    @FXML
    private Button logOutButton;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new PropertyValueFactory<Tweet, String>("id"));
        timeStampCol.setCellValueFactory(new PropertyValueFactory<Tweet, String>("timeStamp"));
        userCol.setCellValueFactory(new PropertyValueFactory<Tweet, String>("user"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Tweet, String>("location"));
        textCol.setCellValueFactory(new PropertyValueFactory<Tweet, String>("text"));

        TweetBox.setItems(data);

    }

    public void prepare() {
        //label.setText(account.getUserName());
        showUserName.setText("Welcome, " + account.getUserName() + "!");
        showPageName.setText("Home page");
        userPane.setVisible(false);
    }

    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }

    @FXML
    public void openAddUser(ActionEvent event) {
        
        if (!userMenuIsOpened) {
        userMenuIsOpened = true;
        userPane.setVisible(true);
        createWarning.setVisible(false);
        } else {
            userMenuIsOpened = false;
            userPane.setVisible(false);
            createWarning.setVisible(true);
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
            } else {
                createWarning.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void removeUser(ActionEvent event) {

    }
   @FXML
   private void goToArchStatistics(ActionEvent event) {
        DataFlow.mainContainer.loadScreen(DataFlow.ASTATISTICS_SCREEN,
                DataFlow.ASTATISTICS_SCREEN_FXML);
        myController.setScreen(DataFlow.ASTATISTICS_SCREEN);
   }
   
   @FXML
   private void goToSentStatistics(ActionEvent event) {
        DataFlow.mainContainer.loadScreen(DataFlow.SSTATISTICS_SCREEN,
                DataFlow.SSTATISTICS_SCREEN_FXML);
        myController.setScreen(DataFlow.SSTATISTICS_SCREEN);
   }
   
   @FXML
   private void goToGeoStatistics(ActionEvent event) {
       DataFlow.mainContainer.loadScreen(DataFlow.GSTATISTICS_SCREEN,
               DataFlow.GSTATISTICS_SCREEN_FXML);
       myController.setScreen(DataFlow.GSTATISTICS_SCREEN);
   }
   
   @FXML
   private void logOut(ActionEvent event) {
       DataFlow.mainContainer.loadScreen(DataFlow.LOGIN_SCREEN, 
               DataFlow.LOGIN_SCREEN_FXML);
       myController.setScreen(DataFlow.LOGIN_SCREEN);    
   }
   
   @FXML
    private void retrieveData(ActionEvent event) throws Exception {
        try {
            Database d = new Database();
            ArrayList<Tweet> tweetAL = d.retrieveFromDatabase();
            data.clear();
            for(Tweet t : tweetAL){
                data.add(t);
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
