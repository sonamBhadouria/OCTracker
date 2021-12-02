package com.hosp.oxygen.entry.ui.airportuser;

public class AirportUserRequest {

    private String serial_no;
    private String capacity;
    private String manufacturer_name;
    private String qr_code_detail;
    private String doc_url;
    private String date_of_manufacturing;
    private String latitude;
    private String longitude;

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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
