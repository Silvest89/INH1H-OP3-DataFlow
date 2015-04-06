/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import dataflow.*;
import dataflow.screens.ControlledScreen;
import java.util.HashMap;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Class which holds all the methods concerning screens
 * @author Johnnie Ho
 */
public class ScreensController  extends StackPane {
    //Holds the screens to be displayed

    private HashMap<String, Node> screens = new HashMap<>();
    
    public ScreensController() {
        super();
    }

    /**
     * Method whichh adds the screen to the collection
     **/
    public void addScreen(String name, Node screen) {
        screens.put(name, screen);
    }

    /**
     * Method which returns the Node with the appropriate name
     * @param name name of the screen
     * @return Node which belongs to the given name
     */
    public Node getScreen(String name) {
        return screens.get(name);
    }

    /**
     * Method which loads the fxml file, add the screen to the screens collection and finally injects the 
     * screenPane to the controller.
     * @param name name of the screen to be loaded
     * @param resource FXML file of the screen to be loaded
     * @return 
     */
    public boolean loadScreen(String name, String resource) {
        try {
            FXMLLoader myLoader = new FXMLLoader(getClass().getResource(resource));
            Parent loadScreen = (Parent) myLoader.load();
            ControlledScreen myScreenController = ((ControlledScreen) myLoader.getController());
            myScreenController.setScreenParent(this);
            myScreenController.prepare();
            addScreen(name, loadScreen);
            // System.out.println(name + resource);
            return true;
            
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    /**
     * Method which tries to displayed the screen with a predefined name.
     * First it makes sure the screen has been already loaded.  Then if there is more than
     * one screen the new screen is been added second, and then the current screen is removed.
     * If there isn't any screen being displayed, the new screen is just added to the root.
     * @param name name of the screen
     * @return 
     */
    public boolean setScreen(final String name) {       
        if (screens.get(name) != null) {   //screen loaded
            final DoubleProperty opacity = opacityProperty();

            if (!getChildren().isEmpty()) {    //if there is more than one screen
                Timeline fade = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 1.0)),
                        new KeyFrame(new Duration(500), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        getChildren().remove(0);                    //remove the displayed screen
                        getChildren().add(0, screens.get(name));     //add the screen
                        Timeline fadeIn = new Timeline(
                                new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                                new KeyFrame(new Duration(300), new KeyValue(opacity, 1.0)));
                        fadeIn.play();                               
                        DataFlow.setMainWindowSize();
                    }
                }, new KeyValue(opacity, 0.0)));
                fade.play();

            } else {
                setOpacity(0.0);
                getChildren().add(screens.get(name));       //no one else been displayed, then just show
                Timeline fadeIn = new Timeline(
                        new KeyFrame(Duration.ZERO, new KeyValue(opacity, 0.0)),
                        new KeyFrame(new Duration(1500), new KeyValue(opacity, 1.0)));
                fadeIn.play();
            }
            return true;
        } else {
            System.out.println("screen hasn't been loaded!\n");
            return false;
        }
    }

    /**
     * Method which will remove the screen with the given name from the collection of screens
     * @param name name of the screen to be deleted
     * @return 
     */
    public boolean unloadScreen(String name) {
        if (screens.remove(name) == null) {
            //System.out.println("Screen didn't exist");
            return false;
        } else {
            return true;
        }
    }
}