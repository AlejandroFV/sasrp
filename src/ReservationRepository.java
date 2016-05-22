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
* ReservationRepository: data accessor
*
*/
public class ReservationRepository {
    public static int save(Reservation r) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "INSERT INTO tbl_reservation (user_id, seat_id) values(?,?)";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, r.getUserID());
            pstmt.setInt(2, r.getSeatID());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }

    public static int update(Reservation r) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "UPDATE tbl_reservation SET user_id=?, seat_id=? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, r.getUserID());
            pstmt.setInt(2, r.getSeatID());
            pstmt.setInt(3, r.getId());
            iRet = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return iRet;
    }
    
    public static int delete(Reservation r) {
        int iRet = -1;
        try {
            Connection con = DBManager.getInstance().getConnection();
            String SQL = "DELETE FROM tbl_reservation WHERE user_id=? && seat_id=?";
            PreparedStatement pstmt = con.prepareStatement(SQL);
            pstmt.setInt(1, r.getUserID());
            pstmt.setInt(2, r.getSeatID());
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
            String SQL = "DELETE FROM tbl_reservation";
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
            String QRY = "SELECT * FROM tbl_reservation ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(QRY);
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setUserID(rs.getInt("user_id"));
                r.setSeatID(rs.getInt("seat_id"));
                arr.add(r);
            }
            stmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
    
    public static ArrayList findByUserID(int userID) {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_reservation WHERE user_id LIKE(?) ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(QRY);
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setUserID(rs.getInt("user_id"));
                r.setSeatID(rs.getInt("seat_id"));
                arr.add(r);
            }
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
    
    public static ArrayList findBySeatID(int seatID) {
        ArrayList arr = new ArrayList();
        try {
            String QRY = "SELECT * FROM tbl_reservation WHERE seat_id LIKE(?) ORDER BY id";
            Connection con = DBManager.getInstance().getConnection();
            PreparedStatement pstmt = con.prepareStatement(QRY);
            pstmt.setInt(1, seatID);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Reservation r = new Reservation();
                r.setId(rs.getInt("id"));
                r.setUserID(rs.getInt("user_id"));
                r.setSeatID(rs.getInt("seat_id"));
                arr.add(r);
            }
            pstmt.close();
        } catch (SQLException se) {
            System.out.println(se);
        }
        return arr;
    }
}
