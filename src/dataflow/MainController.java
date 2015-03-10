/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author Johnnie Ho
 */
public class MainController implements Initializable, ControlledScreen {

    ScreensController myController;
    
    Account account = DataFlow.account;
    
    @FXML
    private Label label;
    
    @FXML
    private TableView TweetBox;
    
    @FXML
    private TableColumn TestCol;
    
    @FXML
    private Pane userPane;
    
    @FXML
    private TextField createName;
    
    @FXML
    private TextField createPass;
    
    @FXML
    private TextField createConfirm;
    
    @FXML
    private Text createWarning;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public void prepare(){
        label.setText(account.getUserName());
        userPane.setVisible(false);
    }
    
    public void setScreenParent(ScreensController screenParent){ 
        myController = screenParent;         
     }   
    
    public void openAddUser(ActionEvent event){
        userPane.setVisible(true);
        createWarning.setVisible(false);
    }
    
    public void createUser(ActionEvent event) throws Exception {
        String name = createName.getText();
        String pass = createPass.getText();
        String confirm = createConfirm.getText();
        try {
            if (pass.equals(confirm)) {
                Database d = new Database();
                d.addUser(name, pass);
                userPane.setVisible(false);
            } else {
                createWarning.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    public void removeUser(ActionEvent event){
        
    }
}
