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
public interface IRemoteSeat extends Remote {
    public int saveSeat(Seat s) throws RemoteException;

    public int updateSeat(Seat s) throws RemoteException;

    public ArrayList findAllSeats() throws RemoteException;
    
    public ArrayList findSeatByName(String criteria) throws RemoteException;
}
