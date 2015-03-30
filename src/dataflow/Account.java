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
    
    private int accessLevel;
    
    public static final int NORMAL = 1;
    public static final int SUPERVISOR = 2;
    public static final int ADMIN = 3;
    
    public Account(){
        userName = "";
        accessLevel = NORMAL;
    }
    
    public String getUserName(){
       return userName;
    }
    
    public int getAccessLevel(){
        return accessLevel;
    }
    
    public void setUserName(String userName){
        this.userName = userName;
    }        
    
    public void setAccessLevel(int accessLevel){
        this.accessLevel = accessLevel;
    }
    
}
