/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens;

import dataflow.DataFlow;
import dataflow.Database;
import dataflow.screens.ControlledScreen;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Database d = new Database();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-YY");
            
            SimpleDateFormat sdfReverse = new SimpleDateFormat("YYYY-MM-dd");
            String today = sdf.format(new Date(System.currentTimeMillis()));
            String todayMin1 = sdf.format(new Date(System.currentTimeMillis() - (1 * 86400000)));
            String todayMin2 = sdf.format(new Date(System.currentTimeMillis() - (2 * 86400000)));
            String todayMin3 = sdf.format(new Date(System.currentTimeMillis() - (3 * 86400000)));
            String todayMin4 = sdf.format(new Date(System.currentTimeMillis() - (4 * 86400000)));
            String todayMin5 = sdf.format(new Date(System.currentTimeMillis() - (5 * 86400000)));
            String todayMin6 = sdf.format(new Date(System.currentTimeMillis() - (6 * 86400000)));
            
            String todayReverse = sdfReverse.format(new Date(System.currentTimeMillis()));
            String todayMin1Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (1 * 86400000)));
            String todayMin2Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (2 * 86400000)));
            String todayMin3Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (3 * 86400000)));
            String todayMin4Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (4 * 86400000)));
            String todayMin5Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (5 * 86400000)));
            String todayMin6Reverse = sdfReverse.format(new Date(System.currentTimeMillis() - (6 * 86400000)));

            XYChart.Series bcSeries1 = new XYChart.Series();
            bcSeries1.getData().add(new XYChart.Data(todayMin6, d.countTweets(todayMin6)));
            bcSeries1.getData().add(new XYChart.Data(todayMin5, d.countTweets(todayMin5)));
            bcSeries1.getData().add(new XYChart.Data(todayMin4, d.countTweets(todayMin4)));
            bcSeries1.getData().add(new XYChart.Data(todayMin3, d.countTweets(todayMin3)));
            bcSeries1.getData().add(new XYChart.Data(todayMin2, d.countTweets(todayMin2)));
            bcSeries1.getData().add(new XYChart.Data(todayMin1, d.countTweets(todayMin1)));

            bcTweets.getData().add(bcSeries1);

            XYChart.Series lcSeries = new XYChart.Series();
            lcSeries.setName("Min. temp");
            lcSeries.getData().add(new XYChart.Data(todayMin6, d.fetchWeather(todayMin6Reverse)));
            lcSeries.getData().add(new XYChart.Data(todayMin5, d.fetchWeather(todayMin5Reverse)));
            lcSeries.getData().add(new XYChart.Data(todayMin4, d.fetchWeather(todayMin4Reverse)));
            lcSeries.getData().add(new XYChart.Data(todayMin3, d.fetchWeather(todayMin3Reverse)));
            lcSeries.getData().add(new XYChart.Data(todayMin2, d.fetchWeather(todayMin2Reverse)));
            lcSeries.getData().add(new XYChart.Data(todayMin1, d.fetchWeather(todayMin1Reverse)));
            
            lcWeather.getData().addAll(lcSeries);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

  
    @FXML
    private void goToMain(ActionEvent event) {
        DataFlow.setScreen("Main");
    }

    @FXML
    private void goToStatistics(ActionEvent event) {
        DataFlow.setScreen("Statistics");
    }

}
