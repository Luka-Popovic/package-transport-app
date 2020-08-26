/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.sun.istack.internal.NotNull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import operations.*;

/**
 *
 * @author Luka
 */
public class pl140170_UserOperations implements UserOperations {
        
    DB db;
    
    public pl140170_UserOperations(){
        db = new DB();
    }
    
    public boolean insertUser(@NotNull String string, @NotNull String string1, @NotNull String string2, @NotNull String string3){
        
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag = false;
         boolean firstNameCapital = false;
         boolean lastNameCapital = false;
         boolean correctUsername = false;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
             }
  
        String pattern = "^[A-Z]";
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(string1);
        if(m.find()){
            firstNameCapital = true;
        }
        
        m = p.matcher(string2);
        if(m.find()){
            lastNameCapital = true;
        }

        pattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$"; // OVAJ KORISTIM ZBOG JAVNOG TESTA 
        Pattern p1 = Pattern.compile(pattern);
        m = p1.matcher(string3);
        if(m.find()){
            correctUsername = true;
        }

        if(firstNameCapital == false || lastNameCapital == false || correctUsername  == false){
             return false;
        }
        
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Korisnik where Username = '"+string+"'";
             ResultSet rs =  stmt.executeQuery(query);
            
           
             if(rs.next()){ //username already exists
                 return false;
             }
            
            query = "insert into Korisnik(Username, Ime, Prezime, Password, BrojPoslatih) values ('"+string+"', '"+string1+"', '"+string2+"', '"+string3+"', "+0+")";
            int num = stmt.executeUpdate(query);
            
            if(num == 0){
                System.out.println("InserUser: nije uspeo insert, vraca false");
                return false;
            }else { flag = true; }
 
            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
        
            return flag;
    }
    

    public int declareAdmin(@NotNull String string){
    
         Connection con = db.conn;
         Statement stmt = null;
         int value = 2;
         int IdK = -1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
             }
     
         try {
             stmt = con.createStatement();
             
             String query = "Select IdK from Korisnik where Username = '"+string+"'";
             ResultSet rs =  stmt.executeQuery(query);
           
             if(!rs.next()){ //no such user error
                 return 2;
             }else{
                 IdK = rs.getInt("IdK");
             }
             
             query = "Select * from Administrator where IdK = "+IdK+"";
             rs =  stmt.executeQuery(query);
           
             if(rs.next()){ //already admin error
                 return 1;
             }
             else {
                   query = "insert into Administrator(IdK) values ("+IdK+")";
                   int num = stmt.executeUpdate(query);
                   if(num == 0){
                   System.out.println("declareAdmin: Nije uspeo insert, vraca 2");
                   return 2;
            }else { value = 0; }
                   
             }
             
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
        
            return value;
    
    }


    public Integer getSentPackages(@NotNull String[] strings){
        
         Connection con = db.conn;
         Statement stmt = null;

         Integer value = 0;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
             }
     
         try {
             stmt = con.createStatement();
             
             
             if(strings != null){
             if(strings.length == 0){
                 return null;
             }
                 
                 
             for(String s:strings){
             String query = "Select BrojPoslatih from Korisnik where Username = '"+s+"'";
             ResultSet rs =  stmt.executeQuery(query);    
             
              if(!rs.next()){ //no such user error
                   return null;
             }else{
              int num = rs.getInt("BrojPoslatih");
              value += num;
              }
             }
             }
             else{
                 return null;
             }
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
         
            return value;
    }

    
    public int deleteUsers(@NotNull String[] strings){
        
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
                  
             String query = "Delete from Korisnik where Username = '"+s+"'";
             value +=  stmt.executeUpdate(query);
             }
             }
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }          
         return value;
        
        
    }

    
    public List<String> getAllUsers(){
        
         List<String> userNames = new ArrayList<String>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select Username from Korisnik";
             ResultSet rs =  stmt.executeQuery(query);
           
   
             while(rs.next()){
             
                 String username = rs.getString("Username");
                 userNames.add(username);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         

        return userNames;
    
    }
        
}
