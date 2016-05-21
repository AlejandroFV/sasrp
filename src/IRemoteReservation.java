/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
public interface IRemoteReservation extends Remote {
    public int saveReservation(Reservation r) throws RemoteException;

    public int updateReservation(Reservation r) throws RemoteException;

    public int deleteReservation(Reservation r) throws RemoteException;

    public void deleteAllReservations() throws RemoteException;

    public ArrayList findAllReservations() throws RemoteException;
    
    public ArrayList findReservationByUserID(int id) throws RemoteException;
    
    public ArrayList findReservationBySeatID(int id) throws RemoteException;
}
