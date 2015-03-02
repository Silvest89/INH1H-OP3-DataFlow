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
public class Account {
    
    private String userName;
    
    public Account(){
        userName = "";
    }
    
    public String getUserName(){
       return userName; 
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }        
    
}
