/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.*;
import java.sql.Timestamp;

/**
 *
 * @author Alejandro
 */
public class Seat implements Serializable {
    private int id;
    private String name;
    private String status;
    private Timestamp timestamp;

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

    public Seat(int id, String name, String status, Timestamp timestamp) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.timestamp = timestamp;
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

    /**
     * @return the timestamp
     */
    public Timestamp getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp to set
     */
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Seat{" + "id=" + id + ", name=" + name + ", status=" + status + ", timestamp=" + timestamp + '}';
    }
}
