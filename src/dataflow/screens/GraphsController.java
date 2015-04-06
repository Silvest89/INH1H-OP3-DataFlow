/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.MySQLDb;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * FXML GraphsController class
 * Contains all methods concerning the graphs screen
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

    ArrayList<String> days = new ArrayList<>();
    

    /**
     * Initializes the controller class and screen.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            MySQLDb d = new MySQLDb();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");

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
            
            days.add(sdf.format(todayMin6));
            days.add(sdf.format(todayMin5));
            days.add(sdf.format(todayMin4));
            days.add(sdf.format(todayMin3));
            days.add(sdf.format(todayMin2));
            days.add(sdf.format(todayMin1));

            XYChart.Series bcSeries1 = new XYChart.Series();
            bcSeries1.getData().add(new XYChart.Data(sdf.format(todayMin6), d.getFeedsPerDay("Twitter", todayMin6)));
            bcSeries1.getData().add(new XYChart.Data(sdf.format(todayMin5), d.getFeedsPerDay("Twitter", todayMin5)));
            bcSeries1.getData().add(new XYChart.Data(sdf.format(todayMin4), d.getFeedsPerDay("Twitter", todayMin4)));
            bcSeries1.getData().add(new XYChart.Data(sdf.format(todayMin3), d.getFeedsPerDay("Twitter", todayMin3)));
            bcSeries1.getData().add(new XYChart.Data(sdf.format(todayMin2), d.getFeedsPerDay("Twitter", todayMin2)));
            bcSeries1.getData().add(new XYChart.Data(sdf.format(todayMin1), d.getFeedsPerDay("Twitter", todayMin1)));

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
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Method which creates text nodes on the weather chart, so the value of each point can be viewed
     * @param y
     * @return 
     */
    public ObservableList<XYChart.Data<String, Number>> plot(double... y) {
        final ObservableList<XYChart.Data<String, Number>> dataset = FXCollections.observableArrayList();
        int i = 0;

        while (i < y.length) {
            final XYChart.Data<String, Number> data = new XYChart.Data<>(days.get(i), y[i]);
            data.setNode(
                    new HoveredNode(y[i]));


            dataset.add(data);
            i++;
        }

        return dataset;
    }
}

/**
 * Class for the node to be created on the weather line chart
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
