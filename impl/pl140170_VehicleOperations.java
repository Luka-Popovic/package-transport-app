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
import operations.VehicleOperations;

/**
 *
 * @author Luka
 */
public class pl140170_VehicleOperations implements VehicleOperations{
    
     DB db;
    
    public pl140170_VehicleOperations(){
        db = new DB();
    }
    
    public boolean insertVehicle(@NotNull String string, int i, BigDecimal bd){
        
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag = false;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Vozilo where RegBroj = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(rs.next()){//not unique registration number error
                 return false;
             }
             
             if(i<0 || i>2){ //incorrect fuel type error
                 return false;
                         }
             
             if(bd.compareTo(BigDecimal.ZERO) == -1 || bd.compareTo(BigDecimal.ZERO) == 0){ //fuel consumption can not be negative
                 return false;
             }
             
             query = "insert into Vozilo(RegBroj, TipGoriva, Potrosnja) values ('"+string+"', "+i+", "+bd.setScale(3,RoundingMode.CEILING)+")";
                     
            int num = stmt.executeUpdate(query);
            if(num == 0){
                System.out.println("Nije uspeo insert, vraca -1");
                return false;
            }
            else{
                flag = true;
            }
            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }

          return flag;
    
    }

    
    public int deleteVehicles(@NotNull String[] strings){
    
         Connection con = db.conn;
         Statement stmt = null;
         int value = 0;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             if(strings != null){
             if(strings.length == 0){ return 0; }
             for(String s: strings){
             
             String query1 = "Select * from Vozilo where RegBroj = '"+s+"'"; 
             ResultSet rs1 = stmt.executeQuery(query1);
               if(rs1.next()){

                int IdV = rs1.getInt("IdV");
                String query2 = "Select * from Kurir where IdV= "+IdV+""; 
                ResultSet rs2 = stmt.executeQuery(query2);
                if(rs2.next()){ //ako je vozilo koristi kurir onda moram da proverim da li je vozilo u toku voznje 
                    int Status = rs2.getInt("Status");
                    if(Status != 1){
                    String query = "Delete from Vozilo where RegBroj = '"+s+"'";
                    value +=  stmt.executeUpdate(query);
                    
                    }
                }else{ // ako vozilo nije dato nijednom kuriru na koriscenje, vozilo se moze obrisati
                    String query = "Delete from Vozilo where RegBroj = '"+s+"'";
                    value +=  stmt.executeUpdate(query);
                }  
              }
          
             }
             }
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }          
         return value;  
    
    }

    
    public List<String> getAllVehichles(){
    
        List<String> vehiclesPlateNumbers = new ArrayList<String>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
            }
         try {
             stmt = con.createStatement();
             
             String query = "Select RegBroj from Vozilo";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 String regBr = rs.getString("RegBroj");
                 vehiclesPlateNumbers.add(regBr);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return vehiclesPlateNumbers;
        
    }

    public boolean changeFuelType(@NotNull String string, int i){
        
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag = false;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Vozilo where RegBroj = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// vehicle does not exist error
                 return false;
             }
             
             if(i<0 || i>2){ //incorrect fuel type error
                 return false;
                         }

               int num = -1;
               int IdV = rs.getInt("IdV");
                
                String query2 = "Select * from Kurir where IdV= "+IdV+""; 
                ResultSet rs2 = stmt.executeQuery(query2);
                if(rs2.next()){ // vozilo je dodeljeno kuriru i moze se promeniti tip goriva samo ako vozilo nije u toku voznje
                    int Status = rs2.getInt("Status");
                    if(Status != 1){ 
                    String query3 = "update Vozilo set TipGoriva = "+i+" where IdV = "+IdV+"";
                    num =  stmt.executeUpdate(query3);
                    
                    }
                }else{ // vozilo nije dodeljeno nijednom kuriru, moze se sigurno promeniti tip goriva
                    String query3 = "update Vozilo set TipGoriva = "+i+" where IdV = "+IdV+"";
                    num =  stmt.executeUpdate(query3);
                }

            if(num == 0){
                System.out.println("Nije uspeo update, vraca -1");
                return false;
            }
            else{
                flag = true;
            }
                       
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         
          return flag;
    }

    public boolean changeConsumption(@NotNull String string, BigDecimal bd){
    
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag = false;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Vozilo where RegBroj = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// vehicle does not exist error
                 return false;
             }

               int num = -1;
               int IdV = rs.getInt("IdV");
                
                String query2 = "Select * from Kurir where IdV= "+IdV+""; 
                ResultSet rs2 = stmt.executeQuery(query2);
                if(rs2.next()){ // vozilo je dodeljeno kuriru i moze se promeniti potrosnja samo ako vozilo nije u toku voznje
                    int Status = rs2.getInt("Status");
                    if(Status != 1){
                    String query3 = "update Vozilo set Potrosnja = "+bd.setScale(3,RoundingMode.CEILING)+"";
                    num =  stmt.executeUpdate(query3);
                    }
                }else{ // vozilo nije dodeljeno nijednom kuriru, moze se sigurno promeniti potrosnja
                    String query3 = "update Vozilo set Potrosnja = "+bd.setScale(3,RoundingMode.CEILING)+"";
                    num =  stmt.executeUpdate(query3);
                }
            
            if(num == 0){
                System.out.println("Nije uspeo update, vraca -1");
                return false;
            }
            else{
                flag = true;
            }

         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
          
          return flag;
    }
    
}
