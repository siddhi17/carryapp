package com.carryapp.Classes;

/**
 * Created by siddhi jambhale on 6/12/2017.
 */

public class Notifications {

    String nt_id,nt_message,nt_status;

    public Notifications(){}

    public Notifications(String id,String message,String status)
    {
        this.nt_id = id;
        this.nt_message = message;
        this.nt_status = status;
    }

    public String getNt_id() {
        return nt_id;
    }

    public String getNt_message() {
        return nt_message;
    }

    public String getNt_status() {
        return nt_status;
    }

    public void setNt_id(String nt_id) {
        this.nt_id = nt_id;
    }

    public void setNt_message(String nt_message) {
        this.nt_message = nt_message;
    }

    public void setNt_status(String nt_status) {
        this.nt_status = nt_status;
    }
}

