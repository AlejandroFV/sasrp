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
public interface IRemoteProvince extends Remote {
    public int save(Province p) throws RemoteException;

    public int update(Province p) throws RemoteException;

    public int delete(Province p) throws RemoteException;

    public void deleteAll() throws RemoteException;

    public ArrayList findAll() throws RemoteException;
    
    public ArrayList findByName(String criteria) throws RemoteException;
}
