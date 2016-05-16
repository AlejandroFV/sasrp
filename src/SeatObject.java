/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.server.*;
import java.rmi.*;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
/**
* Server object
*
*/
public class SeatObject extends UnicastRemoteObject implements IRemoteSeat {

    /**
    *
    */

    private static final long serialVersionUID = 11L;

    public SeatObject() throws RemoteException {
        super();
    }

    public int saveSeat(Seat s) {
        try {
            System.out.println("Invoke saveSeat from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return SeatRepository.save(s);
    }

    public int updateSeat(Seat s) {
        try {
            System.out.println("Invoke updateSeat from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return SeatRepository.update(s);
    }

    public ArrayList findAllSeats() {
        try {
            System.out.println("Invoke findAllSeats from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return SeatRepository.findAll();
    }

    public ArrayList findSeatByName(String criteria) {
        try {
            System.out.println("Invoke findSeatByName from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return SeatRepository.findByName(criteria);
    }
}
