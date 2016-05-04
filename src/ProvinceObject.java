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
public class ProvinceObject extends UnicastRemoteObject implements IRemoteProvince {

    /**
    *
    */

    private static final long serialVersionUID = 11L;

    public ProvinceObject() throws RemoteException {
        super();
    }

    public int save(Province p) {
        try {
            System.out.println("Invoke save from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ProvinceRepository.save(p);
    }

    public int update(Province p) {
        try {
            System.out.println("Invoke update from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ProvinceRepository.update(p);
    }

    public int delete(Province p) {
        try {
            System.out.println("Invoke delete from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ProvinceRepository.delete(p);
    }

    public void deleteAll() {
        try {
            System.out.println("Invoke deleteAll from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        ProvinceRepository.deleteAll();
    }

    public ArrayList findAll() {
        try {
            System.out.println("Invoke findAll from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ProvinceRepository.findAll();
    }

    public ArrayList findByName(String criteria) {
        try {
            System.out.println("Invoke findByName from " + getClientHost());
        } catch (ServerNotActiveException snae) {
            snae.printStackTrace();
        }
        return ProvinceRepository.findByName(criteria);
    }
}
