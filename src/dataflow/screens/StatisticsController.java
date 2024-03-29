package dataflow.screens;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import dataflow.database.MySQLDb;
import dataflow.feed.Feed;
import dataflow.feed.api.Weather;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
    private Button updateWeatherButton;

    
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
    }

    /**
     * Method which retrieves feeds from the database to be displayed in a table
     * @param event
     * @throws Exception 
     */
    @FXML
    private void retrieveData(ActionEvent event) {
        try {
            MySQLDb d = new MySQLDb();
            ArrayList<Feed> tweetAL = d.retrieveFeeds();
            data.clear();
            searchResult.clear();
            for (Feed t : tweetAL) {
                data.add(t);
            }

        } catch (Exception e) {
            e.getMessage();
        }
        
        tweetBox.setItems(data);
    }

    @FXML
    private void searchTweet(ActionEvent event) {
        MySQLDb db = new MySQLDb();
        if(searchText.getText() == null || searchText.getText().length() == 0)
            return;
        try {
            ArrayList<Feed> tweetAL = db.searchFeed(searchText.getText());
            data.clear();
            searchResult.clear();
            for (Feed t : tweetAL) {
                searchResult.add(t);
            }

        } catch (Exception e) {
            e.getMessage();
        }
        
        tweetBox.setItems(searchResult);
    }

    @FXML
    private void deleteFeed(ActionEvent event){
        Feed feed = tweetBox.getSelectionModel().getSelectedItem();
        if(feed == null)
            return;
        
        MySQLDb db = new MySQLDb();
        db.removeFeed(feed);
        data.remove(feed);  
    }
    
    /**
     * Method which retrieves the weather information at the time the given feed was send
     * @param tweet tweet to retrieve weather information from
     */
    private void getWeather(Feed tweet) {
        MySQLDb db = new MySQLDb();        
        Weather weather = db.fetchWeatherByDate(tweet.getTimeStamp()); 
        if(weather == null){
            updateWeatherInfo(null);
            updateWeatherButton.setVisible(true);
            return;
        }
        try {
            updateWeatherButton.setVisible(false);
            String fullUrlPath = weather.getIcon();
            URL url = new URL(fullUrlPath);
            BufferedImage img = ImageIO.read(url);
            Image image = SwingFXUtils.toFXImage(img, null);
            weatherIcon1.setImage(image);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }           
        updateWeatherInfo(weather);
    }
    
    @FXML
    public void updateWeather(ActionEvent event){        
        Feed feed = tweetBox.getSelectionModel().selectedItemProperty().get();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if(format.format(new Date()).equals(format.format(feed.getTimeStamp() * 1000L)))
            return;
        Weather.getWeatherByDay(format.format(feed.getTimeStamp()));
        MySQLDb db = new MySQLDb();        
        Weather weather = db.fetchWeatherByDate(feed.getTimeStamp());     
        updateWeatherInfo(weather);
        updateWeatherButton.setVisible(false);
    }
    
    public void updateWeatherInfo(Weather weather){
        if(weather == null){
            minTemp.setText("");
            maxTemp.setText("");
            clouds.setText("");
            weatherDescription.setText("No weather data found.");    
        }
        else{
            minTemp.setText(weather.getMinTemp() + "°C");
            maxTemp.setText(weather.getMaxTemp() + "°C");
            clouds.setText(weather.getClouds() + "%");
            weatherDescription.setText(weather.getDescription());    
        }
    }

}
