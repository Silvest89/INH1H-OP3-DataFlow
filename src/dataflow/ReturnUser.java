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
public class ReturnUser {
    private String userName;
    private int count;
    
    public ReturnUser(String userName, int count){
        this.userName = userName;
        this.count = count;
    }

    public String getUserName() {
        return userName;
    }

    public int getCount() {
        return count;
    }
        
}
