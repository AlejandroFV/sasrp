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
public interface IRemoteUser extends Remote {
    public int saveUser(User u) throws RemoteException;

    public int updateUser(User u) throws RemoteException;

    public int deleteUser(User u) throws RemoteException;

    public void deleteAllUsers() throws RemoteException;

    public ArrayList findAllUsers() throws RemoteException;
    
    public ArrayList findUserByID(int id) throws RemoteException;
    
    public ArrayList findUserByName(String firstName, String lastName) throws RemoteException;
}
