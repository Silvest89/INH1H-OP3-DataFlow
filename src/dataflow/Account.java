/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

/**
 * Account class which holds the information of the currently logged in account
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
    
    /**
     * Method which checks is the username is not equals to an empty string
     * @return boolean containing true or false depending on the userName
     */
    public static boolean getAccount(){
        return !userName.equals("");
    }
    
    /**
     * Method which returns the userName of the account
     * @return userName of the account
     */
    public static String getUserName(){
       return userName;
    }

    /**
     * Method which returns the first name of the account
     * @return first name of the owner of the account
     */
    public static String getFirstName() {
        return firstName;
    }

    /**
     * Method which sets the first name of the account to the given string
     * @param firstName the new first name of the owner of the account
     */
    public static void setFirstName(String firstName) {
        Account.firstName = firstName;
    }

    /**
     * Method which returns the last name of the account   
     * @return last name of the owner of the account
     */
    public static String getLastName() {
        return lastName;
    }

    /**
     * Method which sets the last name of the account to the given string
     * @param lastName the new last name of the owner of the account
     */
    public static void setLastName(String lastName) {
        Account.lastName = lastName;
    }

    /**
     * Method which returns the email address of the account
     * @return email address of the owner of the account
     */
    public static String getEmail() {
        return email;
    }

    /**
     * Method which sets the email to the given string
     * @param email the new email of the owner of the account
     */
    public static void setEmail(String email) {
        Account.email = email;
    }       
    
    /**
     * Method which return the accessLevel (admin/0, supervisor/1, normal/2)
     * @return accessLevel of the current account
     */
    public static int getAccessLevel(){
        return accessLevel;
    }
    
    /**
     * Method which sets the user Name of the account to the given string
     * @param userName the new userName of the account
     */
    public static void setUserName(String userName){
        Account.userName = userName;
    }        
    
    /**
     * Method which sets the accessLevel for the account to a given int
     * @param accessLevel the new accessLevel for the account
     */
    public static void setAccessLevel(int accessLevel){
        Account.accessLevel = accessLevel;
    }
    
    /**
     * Method which resets the account, i.e. sets all attributes to null/empty
     */
    public static void resetAccount(){
        userName = "";
        firstName = "";
        lastName = "";
        email = "";
        accessLevel = NORMAL;          
    }
    
}
