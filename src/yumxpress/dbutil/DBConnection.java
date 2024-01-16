/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yumxpress.dbutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Shubham Rathore
 */
public class DBConnection {
    private static Connection conn;
     static 
     {
         try
         {
             conn=DriverManager.getConnection("jdbc:oracle:thin:@//shubham:1521/xe","yumxpress","123");
             JOptionPane.showMessageDialog(null,"connection opened successfully","Success",JOptionPane.INFORMATION_MESSAGE);
         }
         catch(SQLException ex)
         {
             JOptionPane.showMessageDialog(null,"DB Error","Error",JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
             System.exit(0);
         }
               
     }
     
     public static Connection getConnetion()
     {
         return conn;
     }
     
     public static void closeConnetion()
     {
         try
         {
             conn.close();
             JOptionPane.showMessageDialog(null,"connection Closed successfully","Success",JOptionPane.INFORMATION_MESSAGE);
             
         }
         catch(SQLException ex)
         {
             JOptionPane.showMessageDialog(null," Error in closing the Connetion","DB Error",JOptionPane.ERROR_MESSAGE);
             ex.printStackTrace();
            // System.exit(0);
         }
     }    

}
