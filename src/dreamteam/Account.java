/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dreamteam;

/**
 *
 * @author Johnnie Ho
 */
public class Account {
    private boolean loggedIn;
    private String userName;
    
    public Account(){
        loggedIn = false;
        userName = "";
    }
    public void setLoggedIn(boolean test){
        this.loggedIn = test;
    }
    public boolean isLoggedIn(){
        return loggedIn;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }
    
}
