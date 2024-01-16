/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package yumxpress.dao;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import yumxpress.dbutil.DBConnection;
import yumxpress.pojo.ProductPojo;

/**
 *
 * @author Shubham Rathore
 */
public class ProductDAO {
    public static String getNewId()throws SQLException
    {
        Connection conn=DBConnection.getConnetion();
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery("select max(product_id) from products");
        rs.next();
        String id=rs.getString(1);
        String proId="";
        if(id!=null){
            id=id.substring(4);
            proId= "PRD-"+(Integer.parseInt(id)+1);
        }
        else{
            proId= "PRD-101";
        }
        return proId;
    }
    
    public static boolean addProduct(ProductPojo product)throws SQLException,IOException
    {
        BufferedImage bufferImage=new BufferedImage(product.getProductImage().getWidth(null),
                product.getProductImage().getHeight(null),BufferedImage.TYPE_INT_RGB);
        
        Graphics gr=bufferImage.getGraphics();
        gr.drawImage(product.getProductImage(), 0, 0, null);
        
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        ImageIO.write(bufferImage, product.getProductImageType(), baos);
        
        byte[] imageData=baos.toByteArray();
        ByteArrayInputStream bais=new ByteArrayInputStream(imageData);
        
        Connection conn=DBConnection.getConnetion();
        PreparedStatement ps=conn.prepareStatement("insert into products values(?,?,?,?,?)");
        ps.setString(1, getNewId());
        ps.setString(2, product.getCompanyId());
        ps.setString(3, product.getProductName());
        ps.setDouble(4, product.getProductPrice());
        ps.setBinaryStream(5, bais,imageData.length);
        
        int result=ps.executeUpdate();
          return result>0;
        
        
    }
    
    public static Map<String,ProductPojo> getFoodDetailsByCompanyId(String CompanyId)throws SQLException,IOException
    {
        Map <String,ProductPojo> productDetails = new HashMap <>();
         Connection conn=DBConnection.getConnetion();
        PreparedStatement ps=conn.prepareStatement("select * from products where company_Id=?");
        ps.setString(1, CompanyId);
        ResultSet rs=ps.executeQuery();
        
        while(rs.next()){
            ProductPojo product=new ProductPojo();
            product.setProductName(rs.getString(3));
            product.setProductPrice(rs.getDouble(4));
            InputStream  inputStream=rs.getBinaryStream(5);
            
            BufferedImage bufferImage=ImageIO.read(inputStream);
            
            Image image=bufferImage;
            product.setProductImage(image);
            
            productDetails.put(product.getProductName(),product);
                       
        }
        
        return productDetails;
    }
    
   
    public static List<ProductPojo> getAllProductsByCompanyId(String companyId)throws SQLException,IOException{ 
        Connection conn = DBConnection.getConnetion(); 
        PreparedStatement ps; 
        if(companyId.equalsIgnoreCase("ALL")){ 
             ps = conn.prepareStatement("select * from products where company_id in(select company_id from companies where status='ACTIVE')"); 
        }else{ 
         
        ps= conn.prepareStatement("select * from products where company_id=?"); 
        ps.setString(1, companyId); 
        } 
        ResultSet rs = ps.executeQuery(); 
        List<ProductPojo> productDetails = new ArrayList<>(); 
        while (rs.next()) { 
            ProductPojo product = new ProductPojo(); 
            product.setProductId(rs.getString("product_id")); 
            product.setProductName(rs.getString("product_name")); 
            product.setProductPrice(rs.getDouble("product_price")); 
            product.setCompanyId(rs.getString("company_id")); 
            InputStream inputStream = rs.getBinaryStream("product_image"); 
 
            // Convert InputStream to BufferedImage 
            BufferedImage bufferedImage = ImageIO.read(inputStream); 
 
            // Convert BufferedImage to Image 
            Image image = bufferedImage; 
            product.setProductImage(image); 
            productDetails.add(product); 
 
        } 
        return productDetails; 
   }

}
