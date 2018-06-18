package com.mlab.contactorganizer.obj;

import java.util.Arrays;
import java.util.List;

public class SmsObj {

    private String fkUser;
    private String message;
    private List<String> receivers;
    private int id;

    public SmsObj(String message, String receivers, String fkUser, int id) {
        this.message = message;
        this.fkUser = fkUser;
        this.id = id;

        if (receivers != null) {
            this.receivers = Arrays.asList(receivers.split(","));
        }
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<String> receivers) {
        this.receivers = receivers;
    }

    public String getFkUser() {
        return fkUser;
    }

    public void setFkUser(String fkUser) {
        this.fkUser = fkUser;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
