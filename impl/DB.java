package student;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Drazen
 */ 


public class DB {

    static final String connectionString =
            "jdbc:sqlserver://localhost;" +
            "databaseName=pl140170;";// +
           // "integratedSecurity=true;";

    public Connection conn;

    public DB() {
        conn = null;

        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(connectionString,"sa","123");

        } catch(Exception e) {
            System.out.println(e);
        }
    }
}