package com.narek.ami;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hello world!
 *
 */
public class App 
{
    private static Logger logger = Logger.getLogger(App.class.getName());

    public static Properties getProperties () {
        Properties appProps = new Properties();
        try {
            appProps.load(new FileInputStream("config.properties"));
        } catch (FileNotFoundException e) {
            logger.log(Level.SEVERE, "Config not found\n " + e.toString(), e);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "IOException", e);
        }

        return appProps;
    }
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
