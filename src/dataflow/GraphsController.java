/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

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
            
            String today = sdf.format(new Date(System.currentTimeMillis()));
            String todayMin1 = sdf.format(new Date(System.currentTimeMillis() - (1 * 86400000)));
            String todayMin2 = sdf.format(new Date(System.currentTimeMillis() - (2 * 86400000)));
            String todayMin3 = sdf.format(new Date(System.currentTimeMillis() - (3 * 86400000)));
            String todayMin4 = sdf.format(new Date(System.currentTimeMillis() - (4 * 86400000)));
            String todayMin5 = sdf.format(new Date(System.currentTimeMillis() - (5 * 86400000)));
            String todayMin6 = sdf.format(new Date(System.currentTimeMillis() - (6 * 86400000)));

            XYChart.Series bcSeries1 = new XYChart.Series();
            bcSeries1.getData().add(new XYChart.Data(todayMin6, d.countTweets(todayMin6)));
            bcSeries1.getData().add(new XYChart.Data(todayMin5, d.countTweets(todayMin5)));
            bcSeries1.getData().add(new XYChart.Data(todayMin4, d.countTweets(todayMin4)));
            bcSeries1.getData().add(new XYChart.Data(todayMin3, d.countTweets(todayMin3)));
            bcSeries1.getData().add(new XYChart.Data(todayMin2, d.countTweets(todayMin2)));
            bcSeries1.getData().add(new XYChart.Data(todayMin1, d.countTweets(todayMin1)));
            bcSeries1.getData().add(new XYChart.Data(today, d.countTweets(today)));

            bcTweets.getData().add(bcSeries1);

            XYChart.Series lcSeries = new XYChart.Series();
            lcSeries.getData().add(new XYChart.Data("22 Mar", 8.2));
            lcSeries.getData().add(new XYChart.Data("23 Mar", 9.3));
            lcSeries.getData().add(new XYChart.Data("24 Mar", 15.1));
            lcSeries.getData().add(new XYChart.Data("25 Mar", 8.4));
            lcSeries.getData().add(new XYChart.Data("26 Mar", 9.6));
            lcSeries.getData().add(new XYChart.Data("27 Mar", 10.0));

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
