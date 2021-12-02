package com.hosp.oxygen.entry.ui.airportuser;

public class AirportUserResponse {
    private int id;
    private boolean isSuccess;
    private String  message;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
