/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;

/**
 *
 * @author Alejandro
 */
/**
* DBManager: Singleton pattern
* *
**/
public final class DBManager {
    private static DBManager _instance = null;
    private Connection _con = null;

    public DBManager() {
        //Connect to DB
        // _con = getMsAccessConnection();
        _con = getMySQLConnection();
        //_con = getFirebirdDBConnection();
    }

    //Thread safe instatiate method
    public static synchronized DBManager getInstance() {
        if (_instance == null) {
            _instance = new DBManager();
        }
        return _instance;
    }

    public Connection getConnection() {
        return _con;
    }

    /**
    * Connection to SQLServer Database
    */
    private static Connection getSQLServerConnection() {
        Connection con = null;
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String URL =
            "jdbc:sqlserver://localhost;databaseName=NID;user=sa;password=123;";
            con = DriverManager.getConnection(URL);
        } catch (Exception e) {
            System.out.println(e);
        }
        return con;
    }

    /**
    * Connection to MySQL Database
    */
    private static Connection getMySQLConnection() {
        Connection con = null;
        try {
            String DB_URL = "jdbc:mysql://127.0.0.1/Province";
            String USER = "root";
            String PASS = "";
            con = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (SQLException se) {
            System.out.println(se);
        } catch(Exception se){
            //Handle errors for Class.forName
            se.printStackTrace();
        }
        return con;
    }

    /**
    * Connection to Microsoft Access
    */
    private static Connection getMsAccessConnection() {
        Connection con = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            con=DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Alejandro/Desktop/Province.mdb");
        } catch (Exception se) {
            System.out.println(se);
        }
        return con;
    }

    private static Connection getFirebirdDBConnection() {
        Connection con = null;
        try {
            Class.forName("org.firebirdsql.jdbc.FBDriver");
            con = DriverManager.getConnection("jdbc:firebirdsql://localhost:3050/D:/Programacion/mars/RMIServerSide/PROVINCE.FDB", "SYSDBA", "masterkey");
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                con.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return con;
    }
}
