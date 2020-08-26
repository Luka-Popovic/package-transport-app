/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import com.sun.istack.internal.NotNull;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.CityOperations;

/**
 *
 * @author Luka
 */
public class pl140170_CityOperations implements CityOperations{
    
    DB db;
    
    public pl140170_CityOperations(){
        db = new DB();
    }
    
     public int insertCity(String string, String string1){
     
         Connection con = db.conn;
         Statement stmt = null;
         int value = -1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Grad where Naziv = '"+string+"' or Post_broj = '" + string1 +"'";
             ResultSet rs =  stmt.executeQuery(query);
            if(rs == null) {/*System.out.println(rs.getString("Error"));*/}
           
             if(rs.next()){ //town already exists error 
                 return -1;
             }
            
            query = "insert into Grad(Naziv, Post_broj) values ('"+string+"', '"+string1+"')";
            int num = stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
             
            if(num == 0){
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
    
     
     public int deleteCity(@NotNull String[] strings){
     
         Connection con = db.conn;
         Statement stmt = null;
         int value = 0;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
          }
         try {
             stmt = con.createStatement();
             
             if(strings != null){
             
                 for(String s: strings){
             
             // prvo brisemo sve Opstine koje se nalaze u tom gradu        
             int idG = 0;
             String query = "Select * from Grad where Naziv = '"+s+"'";        
             ResultSet rs = stmt.executeQuery(query);
             if(rs.next()){
                 idG = rs.getInt("IdG");
             }
             
             query = "Delete from Opstina where IdG = "+idG+"";        
             stmt.executeUpdate(query);
             
             query = "Delete from Grad where Naziv = '"+s+"'";
             value +=  stmt.executeUpdate(query);
             }
            }
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }          
         return value;    
     }

    public boolean deleteCity(int i){
    
         Connection con = db.conn;
         Statement stmt = null;
         int value = 0;
         boolean flag = false;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
                            
             String query = "Delete from Grad where IdG = "+i+"";
             value =  stmt.executeUpdate(query);
             if(value == 1) { flag = true; }
          
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         return flag;
        }

    public List<Integer> getAllCities(){

         List<Integer> cityIds = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select IdG from Grad";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int id = rs.getInt("IdG");
                 cityIds.add(id);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return cityIds;
    }

}
