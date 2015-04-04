/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.Database;
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
 * FXML Controller class
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

    ArrayList<String> days = new ArrayList<>();
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Database d = new Database();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");

            SimpleDateFormat sdfReverse = new SimpleDateFormat("YYYY-MM-dd");

            String todayMin1 = sdf.format(new Date(System.currentTimeMillis() - (1 * 86400000)));
            String todayMin2 = sdf.format(new Date(System.currentTimeMillis() - (2 * 86400000)));
            String todayMin3 = sdf.format(new Date(System.currentTimeMillis() - (3 * 86400000)));
            String todayMin4 = sdf.format(new Date(System.currentTimeMillis() - (4 * 86400000)));
            String todayMin5 = sdf.format(new Date(System.currentTimeMillis() - (5 * 86400000)));
            String todayMin6 = sdf.format(new Date(System.currentTimeMillis() - (6 * 86400000)));

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
            bcSeries1.getData().add(new XYChart.Data(todayMin6, d.countTweets(todayMin6)));
            bcSeries1.getData().add(new XYChart.Data(todayMin5, d.countTweets(todayMin5)));
            bcSeries1.getData().add(new XYChart.Data(todayMin4, d.countTweets(todayMin4)));
            bcSeries1.getData().add(new XYChart.Data(todayMin3, d.countTweets(todayMin3)));
            bcSeries1.getData().add(new XYChart.Data(todayMin2, d.countTweets(todayMin2)));
            bcSeries1.getData().add(new XYChart.Data(todayMin1, d.countTweets(todayMin1)));

            bcTweets.getData().add(bcSeries1);
            
            XYChart.Series lcSeries = new XYChart.Series();
            lcSeries.getData().addAll(FXCollections.observableList(plot(
                    d.fetchWeatherByDouble(todayMin6Reverse), 
                    d.fetchWeatherByDouble(todayMin5Reverse), 
                    d.fetchWeatherByDouble(todayMin4Reverse), 
                    d.fetchWeatherByDouble(todayMin3Reverse), 
                    d.fetchWeatherByDouble(todayMin2Reverse), 
                    d.fetchWeatherByDouble(todayMin1Reverse)
            )));

            lcWeather.getData().addAll(lcSeries);
            
            pcDistr.getData().addAll(d.getMediaDistribution());
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @FXML
    private void goToMain(ActionEvent event) {
    }
}

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

    private Label createDataLabel(double value) {
        final Label label = new Label(value + "");
        label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
        label.setStyle("-fx-font-size: 12");

        label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
        return label;
    }
}