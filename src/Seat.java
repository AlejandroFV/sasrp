/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;

/**
 *
 * @author Alejandro
 */
public class Seat implements Serializable {
    private int id;
    private String name;
    private String status;

    public Seat() {
    }

    public Seat(int id, String name, String status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public Seat(String name, String status) {
        this.name = name;
        this.status = status;
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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Seat{" + "id=" + id + ", name=" + name + ", status=" + status + '}';
    }
}
