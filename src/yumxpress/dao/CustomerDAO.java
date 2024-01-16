/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yumxpress.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static yumxpress.dao.CompanyDAO.getNewId;
import yumxpress.dbutil.DBConnection;
import yumxpress.pojo.CompanyPojo;
import yumxpress.pojo.CustomerPojo;

/**
 *
 * @author Shubham Rathore
 */
public class CustomerDAO {
    
     public static String getNewId()throws SQLException
    {
        Connection conn=DBConnection.getConnetion();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select max(customer_id) from customers");
        rs.next();
        String id=rs.getString(1);
        String cusId="";
        if(id!=null){
            id=id.substring(4);
            cusId= "CUS-"+(Integer.parseInt(id)+1);
        }
        else{
            cusId= "CUS-101";
        }
        return cusId;
    }
    
    public static boolean addCustomer(CustomerPojo customer)throws SQLException
    {
         Connection conn=DBConnection.getConnetion();
       PreparedStatement ps=conn.prepareStatement("insert into customers values(?,?,?,?,?,?)");
       customer.setCustomerId(getNewId());
       ps.setString(1, customer.getCustomerId());
       ps.setString(2, customer.getCustomerName());
       ps.setString(3, customer.getCustomerEmailId());
       ps.setString(4, customer.getCustomerPassword());
 
       ps.setString(5, customer.getCustomerMobileNo());
       ps.setString(6,customer.getCustomerAddress());
       
       return  ps.executeUpdate()==1;     
    }
    
    public static CustomerPojo validate(String costEmail,String password) throws SQLException
    {
        Connection conn=DBConnection.getConnetion();
       PreparedStatement ps=conn.prepareStatement("select * from customers where email_id=? and password=?");
        ps.setString(1, costEmail);
        ps.setString(2, password);
        
       ResultSet rs=ps.executeQuery();
        
        CustomerPojo cust=null;
        
        if(rs.next()){
            cust=new CustomerPojo();
            cust.setCustomerId(rs.getString(1));
            cust.setCustomerName(rs.getString(2));
            cust.setCustomerEmailId(costEmail);
        }
        return cust;
    }

}
