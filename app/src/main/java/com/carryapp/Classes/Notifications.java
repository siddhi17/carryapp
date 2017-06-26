package com.carryapp.Classes;

/**
 * Created by siddhi jambhale on 6/12/2017.
 */

public class Notifications {

    String nt_id,nt_message,nt_status,pt_id,sender_id;
    public boolean notification;

    public Notifications(){}

    public Notifications(String id,String message,String status,String pt_id,String sender_id)
    {
        this.nt_id = id;
        this.nt_message = message;
        this.nt_status = status;
        this.pt_id = pt_id;
        this.sender_id = sender_id;
    }

    public Notifications(String id,String message,String status,String pt_id,String sender_id,Boolean notification)
    {
        this.nt_id = id;
        this.nt_message = message;
        this.nt_status = status;
        this.pt_id = pt_id;
        this.sender_id = sender_id;
        this.notification = notification;
    }



    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public Boolean getNotification() {
        return notification;
    }

    public String getPt_id() {
        return pt_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setPt_id(String pt_id) {
        this.pt_id = pt_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
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

