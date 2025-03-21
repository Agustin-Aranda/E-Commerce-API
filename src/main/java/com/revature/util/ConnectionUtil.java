package com.revature.util;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionUtil {
    //Private Static instance
    private static Connection conn = null;
    //Private constructor
    private  ConnectionUtil(){
        // Having this be private means NOBODY can make an instance of this class at all
    }

    //public static getInstance method

    public static  Connection getConnection(){
        //If a connection already exists. We return it
        try {
            if(conn != null && !conn.isClosed()){
                return conn;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        //Otherwise we create a new connection
        //To create the connection we need
        //Database connection
        //Database UserName
        //Database password

        String url = "";
        String username = "";
        String password = "";

        Properties props = new Properties();

        try {
            props.load(new FileReader("src/main/resources/application.properties"));
            url = props.getProperty("url");
            username = props.getProperty("username");
            password = props.getProperty("password");

            conn = DriverManager.getConnection(url,username, password);

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Could not connect to the database");

        }catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Could not connect to the database");

        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Could not connect to the database");

        }

        return conn;

    }
}
