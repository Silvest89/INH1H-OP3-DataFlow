package dataflow.screens;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dataflow.Account;
import dataflow.DataFlow;
import dataflow.database.MySQLDb;
import dataflow.feed.Feed;
import dataflow.feed.api.Weather;
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
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javax.imageio.ImageIO;


/**
 * FXML Controller class
 *
 * @author ninc__000
 */
public class StatisticsController extends ControlledScreen implements Initializable {
    
    String pageName = "Statistics";
    
    final ObservableList<Feed> data = FXCollections.observableArrayList();
    final ObservableList<Feed> searchResult = FXCollections.observableArrayList();
    
    @FXML
    private Label label;
    
    @FXML
    private Label showPageName;
    
    @FXML 
    private Label showAccessLevel;
    
    @FXML
    private Label showUserName;
    @FXML
    private TableView<Feed> tweetBox;
    @FXML
    private TableColumn<Feed, String> idCol;
    @FXML
    private TableColumn<Feed, String> timeStampCol;
    @FXML
    private TableColumn<Feed, String> userCol;
    @FXML
    private TableColumn<Feed, String> locationCol;
    @FXML
    private TableColumn<Feed, String> textCol;
    @FXML
    private TextField searchText;
    
    @FXML
    private ImageView weatherIcon1;
    
    @FXML
    private Text weatherDescription;    
    
    @FXML
    private Text minTemp;
    
    @FXML
    private Text maxTemp;
    
    @FXML
    private Text clouds;    
    @FXML
    private Region weatherRegion;
    @FXML
    private Label showPageName1;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        idCol.setCellValueFactory(new PropertyValueFactory<Feed, String>("id"));
        timeStampCol.setCellValueFactory(new PropertyValueFactory<Feed, String>("timeString"));
        userCol.setCellValueFactory(new PropertyValueFactory<Feed, String>("user"));
        locationCol.setCellValueFactory(new PropertyValueFactory<Feed, String>("location"));
        textCol.setCellValueFactory(new PropertyValueFactory<Feed, String>("text"));
        
        // Listen for selection changes and show the person details when changed.
        tweetBox.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> getWeather(newValue));
    }   
    
    /**
     * Method which prepares the screen based on the accessLevel of the current account
     */
    @Override
    public void prepare () {
        //setWelcomeMessage(showUserName, showAccessLevel);
        //showUserName.setText("Welcome, " + Account.getUserName() + "!");
        showPageName.setText(pageName); 
    }
    
    @FXML
    public void goToMain (ActionEvent event) {
        DataFlow.setScreen("Main");
    }

    /**
     * Method which retrieves feeds from the database to be displayed in a table
     * @param event
     * @throws Exception 
     */
    @FXML
    private void retrieveData(ActionEvent event) throws Exception {
        try {
            MySQLDb d = new MySQLDb();
            ArrayList<Feed> tweetAL = d.retrieveFeeds();
            data.clear();
            searchResult.clear();
            for (Feed t : tweetAL) {
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
       /* if(searchText.getText() == null || searchText.getText().length() == 0)
            return;
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
        
        tweetBox.setItems(searchResult);*/
    }

    /**
     * Method which retrieves the weather information at the time the given feed was send
     * @param tweet tweet to retrieve weather information from
     */
    private void getWeather(Feed tweet) {
        MySQLDb db = new MySQLDb();        
        Weather weather = db.fetchWeatherByDate(tweet.getTimeStamp()); 
        if(weather == null){
            minTemp.setText("");
            maxTemp.setText("");
            clouds.setText("");
            weatherDescription.setText("No weather data found.");            
            return;
        }
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
        weatherDescription.setText(weather.getDescription());
    }

}
