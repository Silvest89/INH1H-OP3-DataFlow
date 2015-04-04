/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.screens.dialog;

import javafx.stage.Stage;

/**
 *
 * @author Johnnie Ho
 */
public class Dialog {
    protected Stage dialogStage;
    protected boolean okClicked = false;
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }    
    public boolean isOkClicked() {
        return okClicked;
    }    
    
    
}
