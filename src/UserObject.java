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
public class UserObject extends UnicastRemoteObject implements IRemoteUser {

    /**
    *
    */

    private static final long serialVersionUID = 11L;

    public UserObject() throws RemoteException {
        super();
    }

    public int saveUser(User u) {
        try {
            System.out.println("Invoke saveUser from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return UserRepository.save(u);
    }

    public int updateUser(User u) {
        try {
            System.out.println("Invoke updateUser from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return UserRepository.update(u);
    }

    public int deleteUser(User u) {
        try {
            System.out.println("Invoke deleteUser from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return UserRepository.delete(u);
    }

    public void deleteAllUsers() {
        try {
            System.out.println("Invoke deleteAllUsers from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        UserRepository.deleteAll();
    }

    public ArrayList findAllUsers() {
        try {
            System.out.println("Invoke findAllUsers from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return UserRepository.findAll();
    }

    public ArrayList findUserByName(String firstName, String lastName) {
        try {
            System.out.println("Invoke findUserByName from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return UserRepository.findByName(firstName, lastName);
    }
}
