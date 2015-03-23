/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

/**
 *
 * @author Johnnie Ho
 */
public class ControlledScreen {

    ScreensController myController;
    
     //This method will allow the injection of the Parent ScreenPane
    public void setScreenParent(ScreensController screenParent) {
        myController = screenParent;
    }
     
    public void prepare(){
    }
}
