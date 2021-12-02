package com.hosp.oxygen.entry.ui.hospuser;

public class HospUserFormResponse {

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
