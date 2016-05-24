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
* UserRepository: data accessor
*
*/
public class UserRepository {
    public static int save(User u) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "INSERT INTO tbl_user (first_name, last_name) values(?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, u.getFirstName());
            pstmt.setString(2, u.getLastName());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    public static int update(User u) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "UPDATE tbl_user SET first_name=?, last_name=? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setString(1, u.getFirstName());
            pstmt.setString(2, u.getLastName());
            pstmt.setInt(3, u.getId());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }
    
    public static int delete(User u) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "DELETE FROM tbl_user WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, u.getId());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    public static void deleteAll() {
        Connection con = DBManager.getInstance().getConnection();
        try {
            con.setAutoCommit(false);
            String SQL = "DELETE FROM tbl_user";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.executeUpdate();
            con.commit();
        } catch (SQLException se) {
            try {
                con.rollback();
            } catch (SQLException ise) {
            }
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException fse) {
            }
        }
    }

    public static ArrayList findAll() {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_user ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(QRY);
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                arr.add(u);
            }
            stmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
    
    public static ArrayList findByID(int id) {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_user WHERE id = ?";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(QRY);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                arr.add(u);
            }
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
    
    public static ArrayList findByName(String firstName, String lastName) {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_user WHERE first_name LIKE(?) && last_name LIKE(?) ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(QRY);
            pstmt.setString(1, "%" + firstName + "%");
            pstmt.setString(2, "%" + lastName + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setFirstName(rs.getString("first_name"));
                u.setLastName(rs.getString("last_name"));
                arr.add(u);
            }
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
}
