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
public class ReservationObject extends UnicastRemoteObject implements IRemoteReservation {

    /**
    *
    */

    private static final long serialVersionUID = 11L;

    public ReservationObject() throws RemoteException {
        super();
    }

    public int saveReservation(Reservation r) {
        try {
            System.out.println("Invoke saveReservation from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ReservationRepository.save(r);
    }

    public int updateReservation(Reservation r) {
        try {
            System.out.println("Invoke updateReservation from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ReservationRepository.update(r);
    }

    public int deleteReservation(Reservation r) {
        try {
            System.out.println("Invoke deleteReservation from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ReservationRepository.delete(r);
    }

    public void deleteAllReservations() {
        try {
            System.out.println("Invoke deleteAllReservations from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        ReservationRepository.deleteAll();
    }

    public ArrayList findAllReservations() {
        try {
            System.out.println("Invoke findAllReservations from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ReservationRepository.findAll();
    }

    public ArrayList findReservationByUserID(int id) {
        try {
            System.out.println("Invoke findReservationByUserID from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ReservationRepository.findByUserID(id);
    }
    
    public ArrayList findReservationBySeatID(int id) {
        try {
            System.out.println("Invoke findReservationBySeatID from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ReservationRepository.findBySeatID(id);
    }
}
