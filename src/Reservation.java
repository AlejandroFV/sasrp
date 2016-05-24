
import java.io.Serializable;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Alejandro
 */
public class Reservation implements Serializable {
    private int id;
    private int userID;
    private int seatID;

    public Reservation() {
    }

    public Reservation(int id, int userID, int seatID) {
        this.id = id;
        this.userID = userID;
        this.seatID = seatID;
    }

    public Reservation(int userID, int seatID) {
        this.userID = userID;
        this.seatID = seatID;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the userID
     */
    public int getUserID() {
        return userID;
    }

    /**
     * @param userID the userID to set
     */
    public void setUserID(int userID) {
        this.userID = userID;
    }

    /**
     * @return the seatID
     */
    public int getSeatID() {
        return seatID;
    }

    /**
     * @param seatID the seatID to set
     */
    public void setSeatID(int seatID) {
        this.seatID = seatID;
    }

    @Override
    public String toString() {
        return "Reservation{" + "id=" + id + ", userID=" + userID + ", seatID=" + seatID + '}';
    }
}
