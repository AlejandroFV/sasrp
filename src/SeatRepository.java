/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.sql.*;
import java.util.*;

/**
 *
 * @author Alejandro
 */
/**
* SeatRepository: data accessor
*
*/
public class SeatRepository {
    public static int save(Seat s) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "INSERT INTO tbl_seat (name, status) values(?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, s.getName());
            pstmt.setString(2, s.getStatus());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    public static int update(Seat s) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "UPDATE tbl_seat SET status=? WHERE name=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, s.getStatus());
            pstmt.setString(2, s.getName());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    public static ArrayList findAll() {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_seat ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(QRY);
            while (rs.next()) {
                Seat s = new Seat();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setStatus(rs.getString("status"));
                s.setTimestamp(rs.getTimestamp("timestamp"));
                arr.add(s);
            }
            stmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
    
    public static ArrayList findByName(String name) {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_seat WHERE name LIKE(?) ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(QRY);
            pstmt.setString(1, name);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Seat s = new Seat();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setStatus(rs.getString("status"));
                s.setTimestamp(rs.getTimestamp("timestamp"));
                arr.add(s);
            }
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
}
