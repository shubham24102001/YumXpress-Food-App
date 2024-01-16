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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import yumxpress.dbutil.DBConnection;
import yumxpress.pojo.StaffPojo;

/**
 *
 * @author Shubham Rathore
 */
public class StaffDAO {
    
     public static String getNewId()throws SQLException
    {
        Connection conn=DBConnection.getConnetion();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select max(staff_id) from staff");
        rs.next();
        String id=rs.getString(1);
        String staffId="";
        if(id!=null){
            id=id.substring(4);
            staffId= "STF-"+(Integer.parseInt(id)+1);
        }
        else{
            staffId= "STF-101";
        }
        return staffId;
    }
     
    public static String addStaff(StaffPojo staff)throws SQLException
    {
        Connection conn=DBConnection.getConnetion();
        PreparedStatement ps=conn.prepareStatement("insert into staff values(?,?,?,?,?)");
        staff.setStaffId(getNewId());
        ps.setString(1, staff.getStaffId());
        ps.setString(2,staff.getCompanyId());
        ps.setString(3, staff.getStaffEmailId());
        ps.setString(4,staff.getStaffPassword());
        ps.setString(5,staff.getStaffName());
        return (ps.executeUpdate()==1 ? staff.getStaffId():null);
        
    }
    
    public static List<String> getAllStaffIdByCompanyId(String companyId)throws SQLException
    {
         Connection conn=DBConnection.getConnetion();
        PreparedStatement ps=conn.prepareStatement("select staff_id from staff where company_id=?");
        ps.setString(1, companyId);
        ResultSet rs=ps.executeQuery();
         List<String> staffList=staffList=new ArrayList<>();
        while(rs.next()){
            staffList.add(rs.getString(1));
        }
        return staffList;
    }
    
    public static StaffPojo getStaffDetailsbyId(String staffId)throws SQLException
    {
         Connection conn=DBConnection.getConnetion();
        PreparedStatement ps=conn.prepareStatement("select * from staff where staff_id=?");
        ps.setString(1, staffId);
        ResultSet rs=ps.executeQuery();
        StaffPojo staff=new StaffPojo();
        rs.next();
            staff.setStaffName(rs.getString(5));
            staff.setStaffEmailId(rs.getString(3));
        return staff;
      
    }
     public static String getRandomStaffIdFromCompany(String compId)throws SQLException{
        List<String>staffList=StaffDAO.getAllStaffIdByCompanyId(compId);
        Random rand=new Random();
        int index=rand.nextInt(staffList.size());
        return staffList.get(index);
    }
     
      public static StaffPojo validate(String staffEmail,String password) throws SQLException
    {
        Connection conn=DBConnection.getConnetion();
       PreparedStatement ps=conn.prepareStatement("select * from staff where email_id=? and password=?");
        ps.setString(1, staffEmail);
        ps.setString(2, password);
        
       ResultSet rs=ps.executeQuery();
        
        StaffPojo stf=null;
        
        if(rs.next()){
            stf=new StaffPojo();
            stf.setStaffId(rs.getString(1));
            stf.setStaffName(rs.getString(5));
            stf.setStaffEmailId(staffEmail);
        }
        return stf;
    }
}
