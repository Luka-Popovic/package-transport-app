/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.sun.istack.internal.NotNull;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CourierRequestOperation;

/**
 *
 * @author Luka
 */
public class pl140170_CourierRequestOperation implements CourierRequestOperation {
    
      DB db;
    
    public pl140170_CourierRequestOperation(){
        db = new DB();
    }
    
    
    public boolean insertCourierRequest(@NotNull String string, @NotNull String string1){
         
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
             
            query = "insert into KorisnikZahtev(IdK, IdV) values ("+IdK+", "+IdV+")";
             
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

    
    
    public boolean deleteCourierRequest(@NotNull String string){
    
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag = false;
         int IdK = -1;
         
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
             
            query = "Delete from KorisnikZahtev where IdK = "+IdK+"";
             
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
    
    
    public boolean changeVehicleInCourierRequest(@NotNull String string, @NotNull String string1){
    
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
             
            query = "update KorisnikZahtev set IdV = "+IdV+" where IdK = "+IdK+"";
             
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

    public List<String> getAllCourierRequests(){
         
        List<String> couriersRequestList = new ArrayList<String>();
        
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
            }
         try {
             stmt = con.createStatement();
             stmt1 = con.createStatement();
             
             String query = "Select Distinct IdK from KorisnikZahtev";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int IdK = rs.getInt("IdK");
                 String query1 = "select Username from Korisnik where IdK = "+IdK+"";
                 
                 ResultSet rs1 = stmt1.executeQuery(query1);
                 if(rs1.next()){
                   String username = rs1.getString("Username");
                   couriersRequestList.add(username);
                 }
                 
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return couriersRequestList;
    
    }

    public boolean grantRequest(@NotNull String string){
        
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
             
             query = "select * from KorisnikZahtev where IdK = "+IdK+"";
             ResultSet rs1 = stmt.executeQuery(query);
             
             if(!rs1.next()){// no such user error
                 return false;
             }
             else{
                 IdV = rs1.getInt("IdV");
             }

            CallableStatement cs = con.prepareCall("{call ProveraVozilaKurira(?, ?, ?)}");
               cs.setInt(1, IdK);
               cs.setInt(2, IdV);
               cs.registerOutParameter(3, Types.INTEGER);
               cs.execute();
            
            int OutputValue = cs.getInt(3);
            int num = 0;
            
            if(OutputValue == 0){
            query = "insert into Kurir(IdK, IdV, Profit, Status, BrojIsporucenih) values ("+IdK+", "+IdV+", "+0+", "+0+", "+0+")";
            stmt1 = con.createStatement();
            num = stmt1.executeUpdate(query);
            }
            
            
            if(num == 0){
                System.out.println("Nije uspeo insert, vraca -1");
                return false;
            }
            else {
                flag = true;
                query = "delete from KorisnikZahtev where IdK = "+IdK+"";
                int num1 = stmt.executeUpdate(query);
            }
            
               
            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
          
          return flag;
    
    }   
    
}
