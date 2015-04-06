/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class for handline
 * @author Johnnie Ho
 */
public class Config {
    
    private static String db;
    private static String database;
    private static String dbuser;
    private static String dbpassword;    
    
    /**
     * Method which reads the config file
     */
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

    /**
     * Method which returns the name of the database, as stated in the config file
     * @return the name of the database
     */
    public static String getDb() {
        return db;
    }
    
    /**
     * Method which returns the link to the database, as stated in the config file
     * @return the link to the database
     */
    public static String getDatabase() {
        return database;
    }

    /**
     * Method which returns the user of the database
     * @return the userName which has access to the database
     */
    public static String getDbuser() {
        return dbuser;
    }

    /**
     * Method which returns the password of the user of the database
     * @return the password of the user of the database
     */
    public static String getDbpassword() {
        return dbpassword;
    }    
}
