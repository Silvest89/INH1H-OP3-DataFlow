/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.ReturnUser;
import dataflow.Utility;
import dataflow.database.MySQLDb;
import dataflow.feed.Feed;
import dataflow.feed.api.Weather;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 * FXML GraphsController class Contains all methods concerning the graphs screen
 *
 * @author Jesse
 */
public class GraphsController extends ControlledScreen implements Initializable {

    @FXML
    private Label label;
    @FXML
    private Label showUserName;
    @FXML
    private Label showPageName;
    @FXML
    private BarChart<String, Number> bcTweets;
    @FXML
    private LineChart<String, Number> lcWeather;
    @FXML
    private PieChart pcDistr;
    @FXML
    private ChoiceBox mediaChoiceDay, returnChoice, sentimentChoice;
    @FXML
    private Label twitterPie, facebookPie, instagramPie, sentimentFeed;
    @FXML
    private PieChart pnnTweets;
    @FXML
    private BarChart<String, Number> pnntweets2;
    @FXML 
    private TableView returningTable;
    @FXML
    private TableColumn<ReturnUser, String> returnUser;
    @FXML
    private TableColumn<ReturnUser, Integer> returnCount;    
    
    final ObservableList<ReturnUser> data = FXCollections.observableArrayList();

    private ArrayList<Date> days = new ArrayList<>();

    private int posFeeds = 0;
    private int negFeeds = 0;
    private int neutralFeeds = 0;

    /**
     * Initializes the controller class and screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            MySQLDb d = new MySQLDb();
            ObservableList<String> mediaChoice = FXCollections.observableArrayList();
            ObservableList<String> returnChoiceOb = FXCollections.observableArrayList();
            Date date = new Date();
            SimpleDateFormat day = new SimpleDateFormat("dd-MM-yyyy");
            for (int i = 0; i < 7; i++) {
                date.setTime(date.getTime() - 86400000);
                mediaChoice.add(day.format(date));                
            }
            mediaChoiceDay.setItems(mediaChoice);
            
            day = new SimpleDateFormat("MM-yyyy");
            for (int i = 1; i < 4; i++) {
                Date date2 = Utility.getPreviousMonth(new Date(), i);                
                returnChoiceOb.add(day.format(date2));                
            }            
            returnChoice.setItems(returnChoiceOb);
            mediaChoiceDay.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                mediaChoiceChanged(newValue);
                //System.out.println("ListView Selection Changed (selected: " + newValue.toString() + ")");                
            });
            returnChoice.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                returnChoiceChanged(newValue);
                //System.out.println("ListView Selection Changed (selected: " + newValue.toString() + ")");                
            });            

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MM-YYYY");
            SimpleDateFormat sdfReverse = new SimpleDateFormat("YYYY-MM-dd");

            Date todayMin1 = new Date(System.currentTimeMillis() - (1 * 86400000));
            Date todayMin2 = new Date(System.currentTimeMillis() - (2 * 86400000));
            Date todayMin3 = new Date(System.currentTimeMillis() - (3 * 86400000));
            Date todayMin4 = new Date(System.currentTimeMillis() - (4 * 86400000));
            Date todayMin5 = new Date(System.currentTimeMillis() - (5 * 86400000));
            Date todayMin6 = new Date(System.currentTimeMillis() - (6 * 86400000));

            String todayMin1Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (1 * 86400000)));
            String todayMin2Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (2 * 86400000)));
            String todayMin3Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (3 * 86400000)));
            String todayMin4Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (4 * 86400000)));
            String todayMin5Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (5 * 86400000)));
            String todayMin6Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (6 * 86400000)));

            days.add(todayMin6);
            days.add(todayMin5);
            days.add(todayMin4);
            days.add(todayMin3);
            days.add(todayMin2);
            days.add(todayMin1);

            XYChart.Series bcSeries1 = new XYChart.Series();
            for (int i = 0; i < 5; i++) {
                // change color of bar if value of i is >5 than red if i>8 than blue
                final XYChart.Data<String, Number> data2 = new XYChart.Data(sdf.format(days.get(i)), d.getFeedsPerDay("Twin", days.get(i)));
                Weather w = d.fetchWeatherByDate(days.get(i).getTime() / 1000L);
                data2.nodeProperty().addListener(new ChangeListener<Node>() {
                    @Override
                    public void changed(ObservableValue<? extends Node> ov, Node oldNode, Node newNode) {
                        if (newNode != null) {
                            if (w != null) {
                                if (Utility.weatherChecker(w.getDescription()) == Utility.WEATHER_POSITIVE) {
                                    newNode.setStyle("-fx-bar-fill: green;");
                                } else {
                                    newNode.setStyle("-fx-bar-fill: red;");
                                }
                            }
                        }
                    }
                });
                bcSeries1.getData().add(data2);
            }

            bcTweets.getData().add(bcSeries1);

            XYChart.Series lcSeries = new XYChart.Series();
            lcSeries.getData().addAll(FXCollections.observableList(plot(
                    d.fetchWeatherTemperatureByDate(todayMin6Reverse),
                    d.fetchWeatherTemperatureByDate(todayMin5Reverse),
                    d.fetchWeatherTemperatureByDate(todayMin4Reverse),
                    d.fetchWeatherTemperatureByDate(todayMin3Reverse),
                    d.fetchWeatherTemperatureByDate(todayMin2Reverse),
                    d.fetchWeatherTemperatureByDate(todayMin1Reverse)
            )));

            lcWeather.getData().addAll(lcSeries);

            pcDistr.getData().addAll(d.getMediaDistribution());   
            
            returnUser.setCellValueFactory(new PropertyValueFactory<>("userName"));
            returnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
        
            ArrayList<Feed> feeds = d.retrieveFeeds();
            ArrayList<String> list = new ArrayList<>();            
            for (int i = 0; i < feeds.size(); i++) {
			list.add(feeds.get(i).getUser());
            }
            Set<String> uniqueSet = new HashSet<>(list);              
            for (String temp : uniqueSet) {
                data.add(new ReturnUser(temp, Collections.frequency(list, temp)));
            }
            returningTable.setItems(data);
            
            HashMap<Integer, Integer> map = d.retrieveSentiment(todayMin1);
            // sentiment tweets chart data
            XYChart.Series pnnSeries = new XYChart.Series();
            pnnSeries.setName("Positive");
            pnnSeries.getData().add(new XYChart.Data(sdf2.format(todayMin1), map.get(Utility.COMMENT_POSITIVE)));

            XYChart.Series pnnSeries2 = new XYChart.Series();
            pnnSeries2.setName("Neutral");
            pnnSeries2.getData().add(new XYChart.Data(sdf2.format(todayMin1), map.get(Utility.COMMENT_NEUTRAL)));

            XYChart.Series pnnSeries3 = new XYChart.Series();
            pnnSeries3.setName("Negative");
            pnnSeries3.getData().add(new XYChart.Data(sdf2.format(todayMin1), map.get(Utility.COMMENT_NEGATIVE)));

            pnntweets2.getData().addAll(pnnSeries, pnnSeries2, pnnSeries3);

            ObservableList<PieChart.Data> pieChartData
                    = FXCollections.observableArrayList(
                            new PieChart.Data("Positive", map.get(Utility.COMMENT_POSITIVE)),
                            new PieChart.Data("Neutral", map.get(Utility.COMMENT_NEUTRAL)),
                            new PieChart.Data("Negative", map.get(Utility.COMMENT_NEGATIVE)));
            sentimentFeed.setTextFill(Color.BLACK);
            sentimentFeed.setStyle("-fx-font: 16 arial;");

            pnnTweets.setData(pieChartData);

            for (final PieChart.Data data : pnnTweets.getData()) {
                data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED,
                        e -> {
                            double total = 0;
                            for (PieChart.Data ds : pnnTweets.getData()) {
                                total += ds.getPieValue();
                            }
                            String text = String.format("%.1f%%", 100 * data.getPieValue() / total);
                            sentimentFeed.setText(text);
                        }
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which creates text nodes on the weather chart, so the value of
     * each point can be viewed
     *
     * @param y
     * @return
     */
    public ObservableList<XYChart.Data<String, Number>> plot(double... y) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");
        final ObservableList<XYChart.Data<String, Number>> dataset = FXCollections.observableArrayList();
        int i = 0;

        while (i < y.length) {
            final XYChart.Data<String, Number> data = new XYChart.Data<>(sdf.format(days.get(i)), y[i]);
            data.setNode(
                    new HoveredNode(y[i]));

            dataset.add(data);
            i++;
        }

        return dataset;
    }

    private void mediaChoiceChanged(Object value) {
        MySQLDb db = new MySQLDb();
        pcDistr.getData().clear();
        SimpleDateFormat day = new SimpleDateFormat("dd-MM-yyyy");
        Date d1 = null;
        try {
            d1 = day.parse(value.toString());
        } catch (ParseException ex) {
            Logger.getLogger(GraphsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        Date startDay = Utility.getStartOfDay(d1);
        Date endDay = Utility.getEndOfDay(d1);
        ObservableList<PieChart.Data> test = db.getMediaDistributionPerDay(startDay.getTime() / 1000L, endDay.getTime() / 1000L);
        double totalCount = test.get(0).getPieValue() + test.get(1).getPieValue() + test.get(2).getPieValue();

        twitterPie.setText(String.format("%.2f", (test.get(0).getPieValue() / totalCount) * 100) + "%");
        facebookPie.setText(String.format("%.2f", (test.get(1).getPieValue() / totalCount) * 100) + "%");
        instagramPie.setText(String.format("%.2f", (test.get(2).getPieValue() / totalCount) * 100) + "%");

        pcDistr.getData().addAll(test);
    }
    private void returnChoiceChanged(Object value) {
        data.clear();
        MySQLDb db = new MySQLDb();
        SimpleDateFormat day = new SimpleDateFormat("MM-yyyy");
        Date d1 = null;
        try {
            d1 = day.parse(value.toString());
            System.out.println(d1.getTime());
        } catch (ParseException ex) {
            Logger.getLogger(GraphsController.class.getName()).log(Level.SEVERE, null, ex);
        }        
        Date startDay = Utility.getStartOfMonth(d1);
        Date endDay = Utility.getEndOfMonth(d1);
        ArrayList<Feed> feeds = db.retrieveFeedsPerMonth(startDay.getTime() / 1000L, endDay.getTime() / 1000L);
            ArrayList<String> list = new ArrayList<>();            
            for (int i = 0; i < feeds.size(); i++) {
			list.add(feeds.get(i).getUser());
            }
            Set<String> uniqueSet = new HashSet<>(list);              
            for (String temp : uniqueSet) {
                data.add(new ReturnUser(temp, Collections.frequency(list, temp)));
            }
            returningTable.setItems(data);
        
    }    
}

/**
 * Class for the node to be created on the weather line chart
 *
 * @author Jesse
 */
class HoveredNode extends StackPane {

    HoveredNode(double value) {
        setPrefSize(10, 10);

        final Label label = createDataLabel(value);

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getChildren().setAll(label);
                setCursor(Cursor.NONE);
                toFront();
            }
        });
        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                getChildren().clear();
                setCursor(Cursor.CROSSHAIR);
            }
        });
    }

    /**
     * Method which actually created the node
     *
     * @param value the value of the node
     * @return the node to be showed
     */
    private Label createDataLabel(double value) {
        final Label label = new Label(value + "");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 12");

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }

}
