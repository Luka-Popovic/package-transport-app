/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.sun.istack.internal.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierOperations;

/**
 *
 * @author Luka
 */
public class pl140170_CourierOperations implements CourierOperations{
    
    DB db;
    
    public pl140170_CourierOperations(){
        db = new DB();
    }
    
    public boolean insertCourier(@NotNull String string, @NotNull String string1){
    
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag = false;
         int IdK = -1;
         int IdV = -1;
         
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Korisnik where Username = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such user error
                 return false;
             }
             else{
                 IdK = rs.getInt("IdK");
             }
             
             query = "select * from Vozilo where RegBroj = '"+string1+"'";
             ResultSet rs1 = stmt.executeQuery(query);
             
             if(!rs1.next()){// no such vehicle error
                 return false;
             }
             else{
                 IdV = rs1.getInt("IdV");
             }
             
             query = "select * from Kurir where IdK = "+IdK+" or IdV = "+IdV+"";
             rs1 = stmt.executeQuery(query);
             if(rs1.next()){ //can not exist two couriers with the same vehicle or one courier with two vehicles
                 return false;
             }
             
            query = "insert into Kurir(IdK, IdV, Profit, Status, BrojIsporucenih) values ("+IdK+", "+IdV+", "+0+", "+0+", "+0+")";
             
            int num = stmt.executeUpdate(query);
            if(num == 0){
                System.out.println("Nije uspeo insert, vraca -1");
                return false;
            }
            else {
                flag = true;
            }
            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
          
          return flag;
    
    }

    
    public boolean deleteCourier(@NotNull String string){
        
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         boolean flag = false;
         int IdK = -1;
         int IdV = -1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Korisnik where Username = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such user error
                 return false;
             }
             else{
                 IdK = rs.getInt("IdK");
             }

            query = "Select * from Kurir where IdK = "+IdK+""; 
            rs = stmt.executeQuery(query);
            if(!rs.next()){ // no such courier error
                return false;
            }else{
                int status = rs.getInt("Status");
                if(status == 1){ //can not delete courier that drives vehicle
                    return false; 
                }
            
            }

            query = "Delete from Kurir where IdK = '"+IdK+"'";
             
            int num = stmt.executeUpdate(query);
            if(num == 0){
                System.out.println("Nije uspeo delete, vraca -1");
                return false;
            }
            else {
                flag = true;
            }
            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
          
          return flag;
    
    }

 
    public List<String> getCouriersWithStatus(int i){
        
         List<String> couriersList = new ArrayList<String>();
    
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
            }
         try {
             stmt = con.createStatement();
             stmt1 = con.createStatement();
             
             String query = "Select IdK from Kurir where Status = "+i+"";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int IdK = rs.getInt("IdK");
                 String query1 = "Select Username from Korisnik where IdK = "+IdK+"";
                 ResultSet rs1 = stmt1.executeQuery(query1);
                 if(rs1.next()){
                     
                     String username = rs1.getString("Username");
                     couriersList.add(username);
                 
                 }
                 
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return couriersList;

    }
    
   
    public List<String> getAllCouriers(){
    
         List<String> couriersList = new ArrayList<String>();
    
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
            }
         try {
             stmt = con.createStatement();
             stmt1 = con.createStatement();
             
             String query = "Select * from Kurir Order by Profit DESC";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int IdK = rs.getInt("IdK");
                 
                 String query1 = "Select Username from Korisnik where IdK = "+IdK+"";
                 
                 ResultSet rs1 = stmt1.executeQuery(query1);
                 
                 if(!rs1.next()){
                     return null;
                 }
                 
                 String username = rs1.getString("Username");
                 couriersList.add(username);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return couriersList;
        
    }

    
     
    public BigDecimal getAverageCourierProfit(int i){
    
         Connection con = db.conn;
         Statement stmt = null;
         BigDecimal bd = new BigDecimal(0).setScale(3, RoundingMode.CEILING);
         
          if (con == null) {
              System.out.println("Nema konekcije");       
            }
         try {
             stmt = con.createStatement();
             
             String query = "Select AVG(Profit) as SrednjaV from Kurir where BrojIsporucenih >= "+i+"";
             ResultSet rs =  stmt.executeQuery(query);
           
             if(rs.next()){
             
                 bd = rs.getBigDecimal("SrednjaV");
           
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           if(bd == null) {
               bd = new BigDecimal(0).setScale(3, RoundingMode.CEILING);
           }
           
        return bd.setScale(3, RoundingMode.CEILING);
        
    }
    
}
