/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;
import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.GeneralOperations;
/**
 *
 * @author Luka
 */
public class pl140170_GeneralOperations implements GeneralOperations{
    
   

     DB db;
    
    public pl140170_GeneralOperations(){
        db = new DB();
    }
    
     public void eraseAll(){
     
         Connection con = db.conn;
         Statement stmt = null;
         int value = -1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "Delete from Zahtev";
             stmt.executeUpdate(query);
             
             query = "Delete from Opstina";
             stmt.executeUpdate(query);
             
             query = "Delete from Grad";
             stmt.executeUpdate(query);
             
             query = "Delete from Ponuda";
             stmt.executeUpdate(query);
              
             query = "Delete from Voznja";
             stmt.executeUpdate(query);
             
             query = "Delete from Paket";
             stmt.executeUpdate(query);
             
             query = "Delete from KorisnikZahtev";
             stmt.executeUpdate(query);
             
             query = "Delete from Kurir";
             stmt.executeUpdate(query);
             
             query = "Delete from Korisnik";
             stmt.executeUpdate(query);
            
             query = "Delete from Administrator";
             stmt.executeUpdate(query);

             query = "Delete from Vozilo";
             stmt.executeUpdate(query);

         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }        
     }

}
