/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author Johnnie Ho
 */
public class Config {
    
    private static String db;
    private static String database;
    private static String dbuser;
    private static String dbpassword;    
    
    public static void readConfig(){
    	Properties prop = new Properties();
    	InputStream input = null;
 
    	try { 
            String filename = "config.properties";
            input = Config.class.getClassLoader().getResourceAsStream(filename);
            if(input==null){
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            //load a properties file from class path, inside static method
            prop.load(input);

            //get the property value and print it out
            db = prop.getProperty("db");
            database = prop.getProperty("database");
            dbuser = prop.getProperty("dbuser");
            dbpassword = prop.getProperty("dbpassword");            
 
    	} catch (IOException ex) {
    		ex.printStackTrace();
        } finally{
            if(input!=null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String getDb() {
        return db;
    }
    
    public static String getDatabase() {
        return database;
    }

    public static String getDbuser() {
        return dbuser;
    }

    public static String getDbpassword() {
        return dbpassword;
    }    
}
