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
    * Connection to MySQL Database
    */
    private static Connection getMySQLConnection() {
        Connection con = null;
        try {
            // Change this data according to your own machine's configuration
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
            // Here you will write the directory where you place the db file
            con=DriverManager.getConnection(
                    "jdbc:ucanaccess://C:/Users/Alejandro/Desktop/Province.mdb");
        } catch (Exception se) {
            System.out.println(se);
        }
        return con;
    }
}
