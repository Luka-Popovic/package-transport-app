/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student;

import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import com.sun.istack.internal.NotNull;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import operations.PackageOperations;
import operations.PackageOperations.Pair;

/**
 *
 * @author Luka
 */
public class pl140170_PackageOperations implements PackageOperations{
 
      DB db;
    
    public pl140170_PackageOperations(){
        db = new DB();
    }
    
    private List<Integer> IdPs = new ArrayList<Integer>();
    
    private class pl140170_Pair implements PackageOperations.Pair<Integer, BigDecimal>{
    
        private Integer a;
        private BigDecimal b;
     
        public pl140170_Pair(Integer objA, BigDecimal objB){
            a = objA;
            b = objB;
        }
        
        
        public Integer getFirstParam(){
            return a;
        }

        public BigDecimal getSecondParam(){
            return b;
        }

        public  boolean equals(Pair a, Pair b) {
           if (a.getFirstParam().equals(b.getFirstParam()) && a.getSecondParam().equals(b.getSecondParam())){
                return true;
            }
            else {
                return false;
            }
        }
    }
    
    
    public int insertPackage(int i, int i1, @NotNull String string, int i2, BigDecimal bd){
        
         Connection con = db.conn;
         Statement stmt = null;
         int value  = -1;
         int IdSender = -1;
         int initPrice = 0;
         int weightFactor = 0;
         int kgPrice = 1;
         int x1_kord = 0;
         int y1_kord = 0;
         int x2_kord = 0;
         int y2_kord = 0;
         double distance = 0;
         
         
         
         if(i2 < 0 || i2 > 2){ // no such package type error
             return -1;
         }
         
         BigDecimal z = new BigDecimal(0);
         if(bd.compareTo(z) < 0){ // weight can not be negative
             return -1;
         }
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Opstina where IdO = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such district error
                 return -1;
             }
             else{
                 x1_kord = rs.getInt("x_kord");
                 y1_kord = rs.getInt("y_kord");
             }

             query = "select * from Opstina where IdO = "+i1+"";
             ResultSet rs1 = stmt.executeQuery(query);
             
             if(!rs1.next()){// no such district error
                 return -1;
             }
             else{
                 x2_kord = rs1.getInt("x_kord");
                 y2_kord = rs1.getInt("y_kord");
             }
            
             query = "select IdK from Korisnik where Username = '"+string+"'";
             ResultSet rs2 = stmt.executeQuery(query);
             
             if(!rs2.next()){// no such user error
                 return -1;
             }else{
                 IdSender = rs2.getInt("IdK");
             }   
             
             distance = Math.sqrt(Math.pow(x1_kord - x2_kord, 2) + Math.pow(y1_kord - y2_kord, 2));
             
            switch (i2) {
                case 0:
                    initPrice = 10;
                    weightFactor = 0;
                    kgPrice = 1;
                    break;
                case 1:
                    initPrice = 25;
                    weightFactor = 1;
                    kgPrice = 100;
                    break;
                case 2:
                    initPrice = 75;
                    weightFactor = 2;
                    kgPrice = 300;
                    break;
                default:
                    break;
            }
             
             BigDecimal price = bd.multiply(new BigDecimal(weightFactor)).setScale(3, RoundingMode.CEILING).multiply(new BigDecimal(kgPrice)).setScale(3, RoundingMode.CEILING).add(new BigDecimal(initPrice)).setScale(3, RoundingMode.CEILING).multiply(new BigDecimal(distance)).setScale(3, RoundingMode.CEILING);
             price.setScale(3, RoundingMode.CEILING);
             
             query = "Insert into Paket(Status, Cena) values ("+0+", "+price+")";
             
             int num = stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            if(num == 0){
                System.out.println("insertPackage: Nije uspeo insert, vraca -1");
                return -1;
            }
            
            SQLServerResultSet generatedKeys = (SQLServerResultSet)stmt.getGeneratedKeys();
            generatedKeys.next();
            
            value = generatedKeys.getInt(1);
            
            bd.setScale(3, RoundingMode.CEILING);
            BigDecimal razdaljina = BigDecimal.valueOf(distance);
            razdaljina.setScale(3, RoundingMode.CEILING);
            query = "Insert into Zahtev(TipPaketa, TezinaPaketa, IdOOd, IdODo, IdK, IdP, Distance) values ("+i2+", "+bd+", "+i+", "+i1+", "+IdSender+","+value+", "+razdaljina+")";
            num = stmt.executeUpdate(query);
            
             if(num == 0){// insert error
                 return -1;
             }
             
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }
          
          return value;
        
    }

    public int insertTransportOffer(@NotNull String string, int i, BigDecimal bd){
          
         Connection con = db.conn;
         Statement stmt = null;
         int value  = -1;     
         int IdK = -1;
         BigDecimal percentage;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select IdK from Korisnik where Username = '"+string+"'";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such user error
                 System.out.println("insertTransportOffer: nema takvog korisnika error");
                 return -1;
             }
             else{
                 IdK = rs.getInt("IdK");
             }

             query = "select * from Kurir where IdK = "+IdK+"";
             ResultSet rs1 = stmt.executeQuery(query);
             
             
             if(!rs1.next()){// no such courier error
                 System.out.println("insertTransportOffer: nema takvog kurira error");
                 return -1;
             }
           
            int courierStatus = -1;
            courierStatus = rs1.getInt("Status");
            if(courierStatus != 0) {// only couriers that do not drive can send offers
                  System.out.println("insertTransportOffer: Kurir vozi pa ne moze da salje ponudu");
                return -1;
            }
             
             
             query = "select * from Paket where IdP = "+i+"";
             ResultSet rs2 = stmt.executeQuery(query);
             
             if(!rs2.next()){// no such package error
                 System.out.println("insertTransportOffer: nema takav paket error");
                 return -1;
             }   
             
             if(bd == null){
                 Random r = new Random();
                 int min = -10;
                 int max = 10;
                 int seed = r.nextInt(max - min) + min;
                 percentage = new BigDecimal(seed);
             }else{
                 percentage = bd;
             }
             
             percentage.setScale(3, RoundingMode.CEILING);
           
             query = "Insert into Ponuda(Procenat, IdK, IdP) values ("+percentage+", "+IdK+", "+i+")";
             
            int num = stmt.executeUpdate(query,Statement.RETURN_GENERATED_KEYS);
            if(num == 0){
                System.out.println("insertTransportOffer: Nije uspeo insert u insertOffer, vraca -1");
                return -1;
            }
            
            SQLServerResultSet generatedKeys = (SQLServerResultSet)stmt.getGeneratedKeys();
            generatedKeys.next();
            
            value = generatedKeys.getInt(1);
              
                            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             System.out.println("insertOffer: Desio se exception");
             return -1;
         }
          return value;
   
    }


    public boolean acceptAnOffer(int i){
        
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag  = false;     
         int IdK = -1;
         int IdV = -1;
         int IdP = -1;
         BigDecimal newPrice;
         BigDecimal procenat;
         int driveStatus = -1;
         boolean statusFurtherCheck = true;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Ponuda where IdPonuda = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such offer error
                 System.out.println("acceptAnOffer: Nema takve ponude");
                 return false;
             }
             else{
                 IdK = rs.getInt("IdK");
                 IdP = rs.getInt("IdP");
                 procenat = rs.getBigDecimal("Procenat");
             }

             query = "select * from Kurir where IdK = "+IdK+"";
             ResultSet rs1 = stmt.executeQuery(query);
             
             if(!rs1.next()){// no such courier error
                 System.out.println("acceptAnOffer: Nema takvog kurira");
                 return false;
             }
             else{
                IdV = rs1.getInt("IdV");
             }
             
             query = "Insert into Voznja(IdP, IdV, StatusVoznje) values ("+IdP+", "+IdV+", "+0+")";
             
            int num = stmt.executeUpdate(query);
            if(num == 0){
                System.out.println("acceptAnOffer: Nije uspeo insert, vraca -1");
                return false;
            }
            else {
                flag = true;
            }
             
            
            query = "select * from Paket where IdP = "+IdP+"";
            ResultSet rs2 = stmt.executeQuery(query); 
             if(!rs2.next()){// no such package error
                 System.out.println("acceptAnOffer: Nema takvog paketa");
                 return false;
             }
             else{
                BigDecimal cena = rs2.getBigDecimal("Cena");
                newPrice = ((procenat.add(new BigDecimal(100))).divide(new BigDecimal(100))).setScale(3, RoundingMode.CEILING).multiply(cena);
                newPrice.setScale(3, RoundingMode.CEILING);
             }
             query = "Update Paket set Cena = "+newPrice+", IdK ="+IdK+", Status = "+1+" where IdP ="+IdP+"";
             
             num = stmt.executeUpdate(query);

             if(num == 0){
                 return false;
             }
             
             CallableStatement cs = con.prepareCall("{call UpdateTime(?)}");
             cs.setInt(1, IdP);
             cs.execute();
             
             //uvecanja broja poslatih paketa Korisnika, paket smatram poslatim tek kada bude prihvacen Kurirov zahtev
             query = "Select * from Zahtev where IdP = "+IdP+"";
             ResultSet rs3 = stmt.executeQuery(query);
             int IdKorisnika = 0;
             if(rs3.next()){
                 IdKorisnika = rs3.getInt("IdK");
             }
             
             query = "Update Korisnik set BrojPoslatih = BrojPoslatih + 1 where IdK = "+IdKorisnika+"";
             stmt.executeUpdate(query);
                 
                 
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
          
          return flag;
   
    }
    
    
    
    public List<Integer> getAllOffers(){
         
         List<Integer> offersIds = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select IdPonuda from Ponuda";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int id = rs.getInt("IdPonuda");
                 offersIds.add(id);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return offersIds;
    }

      public List<PackageOperations.Pair<Integer, BigDecimal>> getAllOffersForPackage(int i){
        
        ArrayList<PackageOperations.Pair<Integer, BigDecimal>> offersIds = new ArrayList<PackageOperations.Pair<Integer, BigDecimal>>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Ponuda where IdP = "+i+"";
             ResultSet rs =  stmt.executeQuery(query);
           
             while(rs.next()){
             
                 int idPonuda = rs.getInt("IdPonuda");
                 BigDecimal procenat = rs.getBigDecimal("Procenat");
                 pl140170_Pair list = new pl140170_Pair(idPonuda, procenat);
                 offersIds.add(list);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         

        return offersIds;
    }

   
    public boolean deletePackage(int i){
        
         Connection con = db.conn;
         Statement stmt = null;
         int value = 0;
         boolean flag = false;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
                            
             String query = "Select * from Paket where IdP = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             if(rs.next()){
                 int status = rs.getInt("Status");
                 if(status != 0){ // paket za koji je prihvacenja ponuda se ne moze brisati
                 return false;
             }
             }             
             
             query = "Delete from Paket where IdP = "+i+"";
             value =  stmt.executeUpdate(query);
             if(value == 1  ) { flag = true; }
          
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }         
         return flag;
    
    }

     
    public boolean changeWeight(int i, @NotNull BigDecimal bd){
        
         if(bd == null) { return false; }
         if(bd.compareTo(new BigDecimal(0)) == -1 || bd.compareTo(new BigDecimal(0)) == 0) { return false; }
         
         
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag  = false;     
         int tipPaketa = -1;
         int initPrice = 0;
         int weightFactor = 0;
         int kgPrice = 1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Ponuda where IdP = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(rs.next()){// package has offer, its weight can not be changed
                 return false;
             }
            
             query = "select * from Voznja where IdP = "+i+"";
             rs = stmt.executeQuery(query);
             
             if(rs.next()){// offer has been accepted for package, its weight can not be changed
                 return false;
             }
             
             
             bd.setScale(3, RoundingMode.CEILING);
             query = "Update Zahtev set TezinaPaketa = "+bd+" where IdP = "+i+"";
             
            int num = stmt.executeUpdate(query);
            if(num == 0){
                System.out.println("changeWeight: Nije uspeo insert, vraca -1");
                return false;
            }
            else {
                flag = true;
            }
                           
            // Moramo promeniti inicijalnu cenu paketa jer smo joj promenili tezinu
            query = "Select * from Zahtev where IdP = "+i+"";
            rs = stmt.executeQuery(query);
            if(rs.next()){
                tipPaketa = rs.getInt("TipPaketa");
                
                switch (tipPaketa) {
                case 0:
                    initPrice = 10;
                    weightFactor = 0;
                    kgPrice = 1;
                    break;
                case 1:
                    initPrice = 25;
                    weightFactor = 1;
                    kgPrice = 100;
                    break;
                case 2:
                    initPrice = 75;
                    weightFactor = 2;
                    kgPrice = 300;
                    break;
                default:
                    break;
            }
            BigDecimal distance = rs.getBigDecimal("Distance");
            BigDecimal price = bd.multiply(new BigDecimal(weightFactor)).setScale(3, RoundingMode.CEILING).multiply(new BigDecimal(kgPrice)).setScale(3, RoundingMode.CEILING).add(new BigDecimal(initPrice)).setScale(3, RoundingMode.CEILING).multiply(distance).setScale(3, RoundingMode.CEILING);
            price.setScale(3, RoundingMode.CEILING);
            
            query = "Update Paket set Cena = "+price+" where IdP = "+i+"";
            stmt.executeUpdate(query);
            
            }
            
            
            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
          
          return flag;
    
    }

    
    public boolean changeType(int i, int i1){
    
         Connection con = db.conn;
         Statement stmt = null;
         boolean flag  = false;     
         int initPrice = 0;
         int weightFactor = 0;
         int kgPrice = 1;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             if(i1 < 0 || i1 > 2){ // wrong new package type error
                 return false;
             }

             String query = "select * from Ponuda where IdP = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(rs.next()){// package has offer, its type can not be changed
                 return false;
             }
            
             query = "select * from Voznja where IdP = "+i+"";
             rs = stmt.executeQuery(query);
             
             if(rs.next()){// offer has been accepted for package, its type can not be changed
                 return false;
             }
             
             query = "Update Zahtev set TipPaketa = "+i1+" where IdP = "+i+"";
             
            int num = stmt.executeUpdate(query);
            if(num == 0){
                System.out.println("changeType: Nije uspeo insert, vraca -1");
                return false;
            }
            else {
                flag = true;
            }
                
            // Moramo promeniti inicijalnu cenu paketa jer smo mu promenili i tip
                
            query = "Select * from Zahtev where IdP = "+i+"";
            rs = stmt.executeQuery(query);
            if(rs.next()){
                
                switch (i1) {
                case 0:
                    initPrice = 10;
                    weightFactor = 0;
                    kgPrice = 1;
                    break;
                case 1:
                    initPrice = 25;
                    weightFactor = 1;
                    kgPrice = 100;
                    break;
                case 2:
                    initPrice = 75;
                    weightFactor = 2;
                    kgPrice = 300;
                    break;
                default:
                    break;
            }
            BigDecimal distance = rs.getBigDecimal("Distance");
            BigDecimal bd = rs.getBigDecimal("TezinaPaketa");
            BigDecimal price = bd.multiply(new BigDecimal(weightFactor)).setScale(3, RoundingMode.CEILING).multiply(new BigDecimal(kgPrice)).setScale(3, RoundingMode.CEILING).add(new BigDecimal(initPrice)).setScale(3, RoundingMode.CEILING).multiply(distance).setScale(3, RoundingMode.CEILING);
            price.setScale(3, RoundingMode.CEILING);
            
            query = "Update Paket set Cena = "+price+" where IdP = "+i+"";
            stmt.executeUpdate(query);
            
            }


            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return false;
         }
          
          return flag;
    
    }

    
   
    public Integer getDeliveryStatus(int i){
    
         Connection con = db.conn;
         Statement stmt = null;
         Integer value  = null;     
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Paket where IdP = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such package error
                 return null;
             }else{
                 value = rs.getInt("Status");
             }
            
                            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return null;
         }
          
          return value;
    
    }

    
     
    public BigDecimal getPriceOfDelivery(int i){
        
         Connection con = db.conn;
         Statement stmt = null;
         BigDecimal value  = null;     
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Paket where IdP = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such package error
                 return null;
             }else{
                 int status = rs.getInt("Status");
                 if(status == 0){
                     return null;
                 }

                 value = rs.getBigDecimal("Cena");
             }
            
                            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return null;
         }
          
          return value;
        
    }

    
    public Date getAcceptanceTime(int i){
    
         Connection con = db.conn;
         Statement stmt = null;
         Date date  = null;     
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement();
             
             String query = "select * from Paket where IdP = "+i+"";
             ResultSet rs = stmt.executeQuery(query);
             
             if(!rs.next()){// no such package error
                 return null;
             }else{
                 date = rs.getDate("VremePrihvatanja");
                 Timestamp ts = rs.getTimestamp("VremePrihvatanja");
                 if(ts != null){
                 date = new Date(ts.getTime());
                 }
                }
            
                            
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return null;
         }
          
          return date;
    }

    
    public List<Integer> getAllPackagesWithSpecificType(int i){
         
        List<Integer> packageList = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select IdP from Zahtev where TipPaketa = "+i+"";
             ResultSet rs =  stmt.executeQuery(query);
             
             while(rs.next()){
             
                 int idP = rs.getInt("IdP");
                 packageList.add(idP);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return packageList;
    }

 
    public List<Integer> getAllPackages(){
        
         List<Integer> packageList = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select IdP from Paket";
             ResultSet rs =  stmt.executeQuery(query);
             
             while(rs.next()){
             
                 int idP = rs.getInt("IdP");
                 packageList.add(idP);
             
             }
         
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return packageList;
    
    }

       
    public List<Integer> getDrive(String string){
            
        List<Integer> packageDriveList = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         int IdK = -1;
         int IdV = -1;
         int IdP = -1;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stmt1 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             
             String query = "Select IdK from Korisnik where Username = '"+string+"'";
             ResultSet rs =  stmt.executeQuery(query);
             
             if(!rs.next()){// no such user error
                 return null;
             }else{
                IdK = rs.getInt("IdK");
             }
             
             query = "Select * from Kurir where IdK = "+IdK+"";
             ResultSet rs1 =  stmt.executeQuery(query);
             
             if(!rs1.next()){// no such courier error
                 return null;
             }else{
                 IdV = rs1.getInt("IdV");
                 
             }
             
             query = "Select P.IdP as IdP from Voznja as V, Paket as P where V.IdV = "+IdV+" and V.IdP = P.IdP and P.Status = "+2+"";//get all packages for that vehicle and 
             ResultSet rs2 =  stmt.executeQuery(query);                                                                  //that are in process of delivery
             
             if(!rs2.next()){// no drives for that vehicle error
                 return null;
             }else{
              
                 rs2.beforeFirst();
                 
                 while(rs2.next()){
                 
                 IdP = rs2.getInt("IdP");
                 packageDriveList.add(IdP);
                 }

             }
 
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
        return packageDriveList;
    
    }

    
    public int driveNextPackage(@NotNull String string){
        
         if(string == null) { return -2; }
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         Statement stmt2 = null;
         boolean flag  = false;     
         int IdK = -1;
         int IdV = -1;
         int IdP = -1;
         int IdPaket = -1;
         int courierStatus = 0;
         BigDecimal newPrice;
         BigDecimal procenat;
         int driveStatus = -1;
         boolean statusFurtherCheck = true;
         
            if (con == null) {
              System.out.println("Nema konekcije");   
      }
     
         try {
             stmt = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             stmt1 = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
             
             String query = "Select IdK from Korisnik where Username = '"+string+"'";
             ResultSet rs =  stmt.executeQuery(query);
             
             if(!rs.next()){// no such user error
                 return -2;
             }else{
                IdK = rs.getInt("IdK");
             }
             
             query = "Select * from Kurir where IdK = "+IdK+"";
             ResultSet rs1 =  stmt.executeQuery(query);
             
             if(!rs1.next()){// no such courier error
                 return -2;
             }else{
                 IdV = rs1.getInt("IdV");
                 courierStatus = rs1.getInt("Status");
             }

             //Provera da li ima paketa za voznju ili da li je voznja u toku, to je slucaj kada postoje paketi u Voznji sa Statusom 1 ili 2
             query = "Select P.IdP as IdP from Voznja as V, Paket as P where V.IdV = "+IdV+" and V.IdP = P.IdP and P.Status != "+3+" and P.Status != "+0+"";
             rs1 =  stmt.executeQuery(query);
             if(!rs1.next()){ //there is nothing to drive for the given courier
                 return -1;
             }
             
             if(courierStatus == 0){ // courier is not driving, make driving plan, collect packages and start driving first package
                  query = "Select V.IdP as IdP from Voznja as V, Paket as P where V.IdV = "+IdV+" and V.IdP = P.IdP and P.Status = "+1+" order by V.IdP asc";
                  rs1 =  stmt.executeQuery(query);
                  
                  query = "Update Kurir set Status = "+1+" where IdK = "+IdK+";";
                  stmt1.executeUpdate(query);
                  
                  rs1.beforeFirst();
                  while(rs1.next()){
                  int IdPaketa = rs1.getInt("IdP");
                  update_Status(IdPaketa, 2);
                  //postavljanje StatusaVoznje paketa koji su u trenutnoj voznji zbog racunanja profita na kraju voznje
                  query = "Update Voznja set StatusVoznje = "+1+" where IdP = "+IdPaketa+";";
                  stmt1.executeUpdate(query);
                  }
                  
                  int IdPrvogPaketa = 0;
                  rs1.beforeFirst();
                  if(rs1.next()){
                    
                      IdPrvogPaketa = rs1.getInt("IdP"); 
                  
                  } 
                  update_Status(IdPrvogPaketa, 3);

                  if(!rs1.next()){ //there are no more packages for this drive, courirer should get in Status 0, and Profit should be counted
                      query = "Update Kurir set Status = "+0+" where IdK = "+IdK+";";
                      stmt1.executeUpdate(query);
                      countProfit(IdV, 1);
                
                      
                      query = "Select * from Kurir where IdK= "+IdK+";";
                      ResultSet rsss = stmt1.executeQuery(query);
                      if(rsss.next()){
                      }
                      
                      
                 }

                  return IdPrvogPaketa;
                  
             }else{ // courier is already driving, there are packages that have Status 2
             
                 int IdPaketa = -1;
                 
                  query = "Select V.IdP as IdP from Voznja as V, Paket as P where V.IdV = "+IdV+" and V.IdP = P.IdP and P.Status = "+2+" order by V.IdP asc";
                  rs1 =  stmt.executeQuery(query);
             
                 if(rs1.next()){
                     
                  IdPaketa = rs1.getInt("IdP");
                  update_Status(IdPaketa, 3);
                  
                 }
                 if(!rs1.next()){ //there are no more packages for this drive, courirer should get in Status 0, and Profit should be counted
                      query = "Update Kurir set Status = "+0+" where IdK = "+IdK+";";
                      stmt1.executeUpdate(query);
                      countProfit(IdV, 1);
                
                      
                      query = "Select * from Kurir where IdK= "+IdK+";";
                      ResultSet rsss = stmt1.executeQuery(query);

                      
                 }
                 return IdPaketa;
                 
             }

         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
             return 1;
         }
          
    }
    

    private void countProfit(int IdV, int Status){
             
         Connection con = db.conn;
         Statement stmt = null;
         Statement stmt1 = null;
         Statement stmt2 = null;
         Statement stmt3 = null;
         int IdK = -1;
         int IdP = -1;
         BigDecimal totalDistance = new BigDecimal(0);
         BigDecimal totalExpenses = new BigDecimal(0);
         BigDecimal totalCena = new BigDecimal(0);
         BigDecimal potrosnja = new BigDecimal(0);
         int x_a = -1;
         int y_a = -1;
         int x_b = -1;
         int y_b = -1;
         int x_prev = -1;
         int y_prev = -1;
         int IdOOd = -1;
         int IdODo = -1;
         int TipGoriva = -1;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             stmt1 = con.createStatement();
             stmt2 = con.createStatement();
             stmt3 = con.createStatement();
                     

             String query = "Select IdP from Voznja where IdV = "+IdV+" and StatusVoznje = "+Status+" order by IdP asc";
             ResultSet rs =  stmt.executeQuery(query);
             
             while(rs.next()){
                IdP = rs.getInt("IdP");
                
                //dohvatanje podataka iz paketa
                String query1 = "Select * from Paket where IdP = "+IdP+""; 
                ResultSet rs1 = stmt1.executeQuery(query1);
                
                // dohvatanje podataka iz zahteva
                String query2 = "Select * from Zahtev where IdP = "+IdP+""; 
                ResultSet rs2 = stmt2.executeQuery(query2);
                
                
                //dohvatanje cene isporuke paketa bez troskova prevoza
                if(rs1.next()){
                
                    BigDecimal cena = rs1.getBigDecimal("Cena");
                    totalCena = totalCena.add(cena).setScale(3, RoundingMode.CEILING);
                    IdK = rs1.getInt("IdK");
              
                } 
             
                //dohvatanje Id Opstina i racunanje razdaljine
                if(rs2.next()){
               
                    IdOOd = rs2.getInt("IdOOd");
                    IdODo = rs2.getInt("IdODo");
               
                  String query3 = "Select * from Opstina where IdO = "+IdOOd+"";  
                  ResultSet rs3 = stmt3.executeQuery(query3);
                
                  if(rs3.next()){
                  x_a = rs3.getInt("x_kord");
                  y_a = rs3.getInt("y_kord");   
                  }
                
                  query3 = "Select * from Opstina where IdO = "+IdODo+"";  
                  rs3 = stmt3.executeQuery(query3);
                
                  if(rs3.next()){
                  x_b = rs3.getInt("x_kord");
                  y_b = rs3.getInt("y_kord"); 
                  }
                  
                  if(x_prev != -1 && y_prev != -1){ //ne krecemo od prve voznje, postoji prethodna tacka na putu
                      double distance = Math.sqrt(Math.pow(x_prev - x_a, 2) + Math.pow(y_prev - y_a, 2));
                      totalDistance = totalDistance.add(new BigDecimal(distance)).setScale(3, RoundingMode.CEILING);
                  }
                  
                      double distance = Math.sqrt(Math.pow(x_a - x_b, 2) + Math.pow(y_a - y_b, 2));
                      totalDistance = totalDistance.add(new BigDecimal(distance)).setScale(3, RoundingMode.CEILING);
                      
                      x_prev = x_b;
                      y_prev = y_b;
                  
                } 
             }// izracunate distance prevoza svih paketa i cena po paketu bez troskova
             
             
             query = "Select * from Vozilo where IdV = "+IdV+"";
             ResultSet rs1 =  stmt.executeQuery(query);
             
             if(rs1.next()){
                 
                 TipGoriva = rs1.getInt("TipGoriva");
                 potrosnja = rs1.getBigDecimal("Potrosnja");
                 
             }
             
             switch (TipGoriva) {
                 case 0:
                     totalExpenses = totalDistance.multiply(new BigDecimal(15)).multiply(potrosnja).setScale(3, RoundingMode.CEILING);
                     break;
                 case 1:
                     totalExpenses = totalDistance.multiply(new BigDecimal(32)).multiply(potrosnja).setScale(3, RoundingMode.CEILING);
                     break;
                 case 2:
                     totalExpenses = totalDistance.multiply(new BigDecimal(36)).multiply(potrosnja).setScale(3, RoundingMode.CEILING);
                     break;
                 default:
                     break;
             }
             
             totalCena = totalCena.subtract(totalExpenses).setScale(3, RoundingMode.CEILING);
             
             
             query = "Update Kurir set Profit = "+totalCena+", Status = "+0+" where IdK = "+IdK+"";
             int statusKurira = stmt.executeUpdate(query);                                

             query = "Select * from Kurir where IdK = "+IdK+"";
             ResultSet rs2 = stmt.executeQuery(query);

             query = "Delete from Voznja where IdV = "+IdV+" and StatusVoznje = "+Status+"";
             stmt.executeUpdate(query);

         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
           
    
    }
    
    
        private void set_Package_Status(int IdV, int StatusVoznje, int StatusPaketa){
         
         List<Integer> packageList = new ArrayList<Integer>();
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Select * from Voznja where IdV = "+IdV+" and StatusVoznje = "+StatusVoznje+"";
             ResultSet rs =  stmt.executeQuery(query);
             
             while(rs.next()){
             
                 int idP = rs.getInt("IdP");
                 packageList.add(idP);
             
             }
         
             for(Integer i:packageList){
             
                 query = "Update Paket set Status = "+StatusPaketa+" where IdP = "+i+"";
                 stmt.executeUpdate(query);
             }
             
             
             
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }         
         
         }
    
    
        private void update_Status(int IdP, int Status){
          
    
         Connection con = db.conn;
         Statement stmt = null;
         
          if (con == null) {
              System.out.println("Nema konekcije");       
      }
         try {
             stmt = con.createStatement();
             
             String query = "Update Paket set Status = "+Status+" where IdP = "+IdP+"";
             stmt.executeUpdate(query);
             
         } catch (SQLException ex) {
             Logger.getLogger(pl140170_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
         }   
        
        
        }
    
}
