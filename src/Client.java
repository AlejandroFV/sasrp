/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 *
 * @author Alejandro
 */
/**
* Client: client application
*
*/
public class Client {
    public static void main(String[] args) {
        try {
            //Get reference to rmi registry server
            Registry registry = LocateRegistry.getRegistry("127.0.0.1");
            //Lookup server object
            IRemoteSeat rs = (IRemoteSeat) registry.lookup("Seat");
            IRemoteUser ru = (IRemoteUser) registry.lookup("User");
            //Save Seats and User
            Seat j11 = new Seat("J11", "in-process");
            Seat j12 = new Seat("J12", "in-process");
            User jd = new User("Jane", "Doe");
            //Save seats and user
            System.out.println("Saving data...");
            rs.saveSeat(j11);
            rs.saveSeat(j12);
            ru.saveUser(jd);
            //Update seat
            System.out.println("Update seat");
            Seat updatedJ12 = new Seat("J12", "reserved");
            int iRet = rs.updateSeat(updatedJ12);
            //Display all users
            System.out.println("Display all users");
            ArrayList <User> arrUser = ru.findAllUsers();
            for (User u : arrUser) {
                System.out.println(u.toString());
            }
//            //Delete Kampong Cham
//            System.out.println("Delete KPC");
//            rp.delete(kpc);
//            //Display province starts by "Kam"
//            System.out.println("Display province starts by \"Kam\"");
//            arrProv = rp.findByName("Kam");
//            for (Province p : arrProv) {
//                System.out.println(p.toString());
//            }
//            //Delete all provinces
//            System.out.println("Delete all provinces");
//            rp.deleteAll();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
