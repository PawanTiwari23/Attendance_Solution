/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

//import com.sun.jdi.connect.spi.Connection;


import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager; 
import java.sql.*;


/**
 *
 * @author Pashu Patinath
 */
public class ConnectionProvider {
    private static final String DB_NAME = "attedanceJframebd";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/"+ DB_NAME;

    private static final String DB_USERNAME = "root";
    private static final String DB_PASSWORD = "1234";
    
    
    
    public static Connection getCon() {
        
        try {
            
            
            
            Class.forName("com.mysql.cj.jdbc.Driver");
            
          
            Connection con = DriverManager.getConnection(DB_URL + "?useSSL=false&allowPublicKeyRetrieval=true", DB_USERNAME,DB_PASSWORD);
            if(!databaseExists(con, DB_NAME)){
                createDatabase(con, DB_NAME);
            }
            
            con = DriverManager.getConnection(DB_URL + "?useSSL=false&allowPublicKeyRetrieval=true", DB_USERNAME,DB_PASSWORD);
            return con;
            
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    
    private static boolean databaseExists(Connection con, String dbName) throws Exception {
        Statement stmt = con.createStatement();
        return stmt.executeQuery("SHOW DATABASES LIKE '" + dbName + "'").next();
    }
    
    
        private static void createdatabase(Connection con, String dbName) throws Exception {
        Statement stmt = con.createStatement();
        stmt.executeUpdate("CREATE DATABASE" +dbName);
            System.out.println("Database '" + dbName + "'created successfully");
    }

    private static void createDatabase(Connection con, String DB_NAME) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    
    
    
}
