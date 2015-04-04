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
public final class Account {
    
    private static String userName = "";
    private static String firstName;
    private static String lastName;
    private static String email;
    private static int accessLevel;
    
    public static final int NORMAL = 1;
    public static final int SUPERVISOR = 2;
    public static final int ADMIN = 3;    
    
    public static boolean getAccount(){
        return !userName.equals("");
    }
    
    public static String getUserName(){
       return userName;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setFirstName(String firstName) {
        Account.firstName = firstName;
    }

    public static String getLastName() {
        return lastName;
    }

    public static void setLastName(String lastName) {
        Account.lastName = lastName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Account.email = email;
    }       
    
    public static int getAccessLevel(){
        return accessLevel;
    }
    
    public static void setUserName(String userName){
        Account.userName = userName;
    }        
    
    public static void setAccessLevel(int accessLevel){
        Account.accessLevel = accessLevel;
    }
    
    public static void resetAccount(){
        userName = "";
        firstName = "";
        lastName = "";
        email = "";
        accessLevel = NORMAL;          
    }
    
}
