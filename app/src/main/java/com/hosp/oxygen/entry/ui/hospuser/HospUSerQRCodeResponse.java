package com.hosp.oxygen.entry.ui.hospuser;

public class HospUSerQRCodeResponse {

     private int id;
     private String serial_no;
     private String capacity;
     private String manufacturer_name;
     private String qr_code_detail;
     private String doc_url;
     private String date_of_manufacturing;
     private String stage_id;
     private boolean isactive;
     private String updated_date;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public String getManufacturer_name() {
        return manufacturer_name;
    }

    public void setManufacturer_name(String manufacturer_name) {
        this.manufacturer_name = manufacturer_name;
    }

    public String getQr_code_detail() {
        return qr_code_detail;
    }

    public void setQr_code_detail(String qr_code_detail) {
        this.qr_code_detail = qr_code_detail;
    }

    public String getDoc_url() {
        return doc_url;
    }

    public void setDoc_url(String doc_url) {
        this.doc_url = doc_url;
    }

    public String getDate_of_manufacturing() {
        return date_of_manufacturing;
    }

    public void setDate_of_manufacturing(String date_of_manufacturing) {
        this.date_of_manufacturing = date_of_manufacturing;
    }

    public String getStage_id() {
        return stage_id;
    }

    public void setStage_id(String stage_id) {
        this.stage_id = stage_id;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getUpdated_date() {
        return updated_date;
    }

    public void setUpdated_date(String updated_date) {
        this.updated_date = updated_date;
    }
}
