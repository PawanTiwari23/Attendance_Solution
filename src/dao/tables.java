/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

//
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.Statement;



import javax.swing.JOptionPane;






/**
 *
 * @author Pashu Patinath
 */
public class tables {
    
    public static void main(String args[]){
       // Connection con = null;
        //Statement st = null;
        Connection con = ConnectionProvider.getCon();
        if (con == null)
        {
            System.out.println("Failed to establish databases connection");
            return ;
        }
        
        
        try {
            Statement st = con.createStatement();
            con =  ConnectionProvider.getCon();
           // st = con.createStatement();
            
            if(!tablesExists(st, "userdetails")) {
                
               // st.executeUpdate("CREATE TABLE userdetails (+ id INT AUTO_INCREMENT PRIMARY KEY, name VARCHAR(300) NOT NULL,"
                 //       + "gender VARCHAR(100) not null, email VARCHAR(300) not null, contact VARCHAR(300) not null,"
                   //     + "address VARCHAR(600) not null, state VARCHAR(200) not null, country VARCHAR(300) not null"
                     //   + "uniqueregid VARCHAR(100) not null, imagename VARCHAR(100));");
                     st.executeUpdate("CREATE TABLE userdetails ("
           + "id INT AUTO_INCREMENT PRIMARY KEY, "
           + "name VARCHAR(300) NOT NULL, "
           + "gender VARCHAR(100) NOT NULL, "
           + "email VARCHAR(300) NOT NULL, "
           + "contact VARCHAR(300) NOT NULL, "
           + "address VARCHAR(600) NOT NULL, "
           + "state VARCHAR(200) NOT NULL, "
           + "country VARCHAR(300) NOT NULL, "
           + "uniqueregid VARCHAR(100) NOT NULL, "
           + "imagename VARCHAR(100)"
           + ");");
                
                
            }

            
            if(!tablesExists(st, "userattendance")) {
                
                st.executeUpdate("CREATE TABLE userattendance (userid INT NOT NULL, date DATE NOT NULL, checkin DATETIME,checkout DATETIME, workduration VARCHAR(300));");
                
                
                
            }
            JOptionPane.showMessageDialog(  null, "Table are Checked/Created  Successfully");
            
            
            
            
        }catch(Exception ex) {
            JOptionPane.showMessageDialog(  null, ex);
            
        } finally {
           /* try {
                if(con!=null) {
                    con.close();
                }
                if(st!=null) {
                    st.close();
                }
                
            } catch (Exception ex) {
                ex.printStackTrace();
            }*/
           
            
        }
    }
    
    
    
    
    
    
    private static boolean tablesExists(Statement st, String tableName) throws Exception {
        ResultSet resultSet = st.executeQuery("SHOW TABLES LIKE '" + tableName + "'");
        return resultSet.next();
        
    }
    
    
    
}
