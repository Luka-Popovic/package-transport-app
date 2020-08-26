/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import com.sun.istack.internal.NotNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.DistrictOperations;

/**
 *
 * @author Luka
 */
public class pl140170_DistrictOperations implements DistrictOperations{
    
     DB db;
    
    public pl140170_DistrictOperations(){
        db = new DB();
    }
    
    public int insertDistrict(String string, int i, int i1, int i2){
     
         Connection con = db.conn;
         Statement stmt = null;
         int value = -1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
            String query = "Select * from Grad where IdG = "+i+"";
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()){ // no such city error
                return -1;
            } 
             
            query = "insert into Opstina(Naziv, x_kord, y_kord, IdG) values ('"+string+"', "+i1+", "+i2+", "+i+")";
             
            int num = stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            if(num == 0){
                System.out.println("Nije uspeo insert, vraca -1");
                return -1;
            }
            
            SQLServerResultSet generatedKeys = (SQLServerResultSet)stmt.getGeneratedKeys();
            generatedKeys.next();
            
            value = generatedKeys.getInt(1);
                   
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         
           return value;
        
    }

    
    public int deleteDistricts(@NotNull String[] strings){
        
         Connection con = db.conn;
         Statement stmt = null;
         int value = 0;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             for(String s: strings){
             
             String query = "Delete from Opstina where Naziv = '"+s+"'";
             value +=  stmt.executeUpdate(query);
             }
             
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }          
         return value;    
    }

   
    public boolean deleteDistrict(int i){
    
         Connection con = db.conn;
         Statement stmt = null;
         int value = 0;
         boolean flag = false;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             //brisem Zahteve koji su vezani za odgovarajucu Opstinu
             String query = "Delete from Zahtev where IdOOd = "+i+" or IdODo = "+i+"";
             stmt.executeUpdate(query);
             
             query = "Delete from Opstina where IdO = "+i+"";
             value =  stmt.executeUpdate(query);
             if(value == 1  ) { flag = true; }
          
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
      
         return flag; 
        
    }

    
    public int deleteAllDistrictsFromCity(@NotNull String string){
         
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         int value = 0;
         int IdG = -1;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             stmt1 = con.createStatement();   
             
             String query = "Select IdG from Grad where Naziv = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){ //no such city error
                 return 0;
             }
             else{ 
                 IdG = rs.getInt("IdG");
             }
             
             query = "Delete from Opstina where IdG = "+IdG+"";
             value = stmt1.executeUpdate(query);
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
         return value; 
    }
  
    public List<Integer> getAllDistrictsFromCity(int i){
         List<Integer> districtIds = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Grad where IdG = "+i+"";
             ResultSet rs =  stmt.executeQuery(query);
             
             if(!rs.next()){ //no such city error
                 System.out.println("Ne postoji grad za koji hocemo da ispisemo opstine");
                 return null;
             }
             
             
             query = "Select IdO from Opstina where IdG = "+i+"";
             rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int id = rs.getInt("IdO");
                 districtIds.add(id);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return districtIds;
    
    }

    public List<Integer> getAllDistricts(){
        
         List<Integer> districtIds = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select IdO from Opstina";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int id = rs.getInt("IdO");
                 districtIds.add(id);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return districtIds;
    
    }
 
}
