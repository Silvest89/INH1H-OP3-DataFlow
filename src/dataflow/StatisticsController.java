package dataflow;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;


/**
 * FXML Controller class
 *
 * @author ninc__000
 */
public class StatisticsController extends ControlledScreen implements Initializable {
    
    String pageName = "Statistics";
    
    Account account = DataFlow.account;
    
    final ObservableList<Tweet> data = FXCollections.observableArrayList();
    final ObservableList<Tweet> searchResult = FXCollections.observableArrayList();
    
    @FXML
    private Label label;
    
    @FXML
    private Label showPageName;
    
    @FXML
    private Label showUserName;
    @FXML
    private TableView<Tweet> tweetBox;
    @FXML
    private TableColumn<Tweet, String> idCol;
    @FXML
    private TableColumn<Tweet, String> timeStampCol;
    @FXML
    private TableColumn<Tweet, String> userCol;
    @FXML
    private TableColumn<Tweet, String> locationCol;
    @FXML
    private TableColumn<Tweet, String> textCol;
    @FXML
    private TextField searchText;
    
    @FXML
    private ImageView weatherIcon1;
    
    @FXML
    private ImageView weatherIcon2;    
    
    @FXML
    private Text minTemp;
    
    @FXML
    private Text maxTemp;
    
    @FXML
    private Text clouds;    
    
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
        
        // Listen for selection changes and show the person details when changed.
        tweetBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> getWeather(newValue));
    }   
    
    @Override
    public void prepare () {
        showUserName.setText("Welcome, " + account.getUserName() + "!");
        showPageName.setText(pageName); 
    }
    
    @FXML
    public void goToMain (ActionEvent event) {
        DataFlow.setScreen("Main");
    }

    @FXML
    private void retrieveData(ActionEvent event) throws Exception {
        try {
            Database d = new Database();
            ArrayList<Tweet> tweetAL = d.retrieveFromDatabase();
            data.clear();
            searchResult.clear();
            for (Tweet t : tweetAL) {
                data.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tweetBox.setItems(data);
        System.out.println(data);
    }

    @FXML
    private void searchTweet(ActionEvent event) {
        try {
            TwitterSearch ts = new TwitterSearch();
            ArrayList<Tweet> tweetAL = ts.retrieve(searchText.getText());
            data.clear();
            searchResult.clear();
            for (Tweet t : tweetAL) {
                searchResult.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        tweetBox.setItems(searchResult);
    }
    
    @FXML
    private void goToStatistics(ActionEvent event) {
        
    }

    private void getWeather(Tweet tweet) {
        Database db = new Database();        
        if(tweet.getWeather() > 0){
            Weather weather = db.fetchWeather(tweet.getWeather());      
            try {

                String fullUrlPath = weather.getIcon();
                URL url = new URL(fullUrlPath);
                BufferedImage img = ImageIO.read(url);
                Image image = SwingFXUtils.toFXImage(img, null);
                weatherIcon1.setImage(image);
            } catch (IOException ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }           
            minTemp.setText(weather.getMinTemp() + "°C");
            maxTemp.setText(weather.getMaxTemp() + "°C");
            clouds.setText(weather.getClouds() + "%");
        }
    }
}