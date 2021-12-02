package com.hosp.oxygen.entry.ui.login;


import java.util.ArrayList;

public class LoginResponse {
    private String accessToken;
    private String refreshToken;
    private String userName;
    private int userId;
    private String email;
    private String roleName;
    private int roleId;
    private boolean isactive;
    private String message;
    private int hospital_id;
    private boolean is_basic_cert_applied;
    private String org_type;
    private boolean isfirsttime_login;

    public String getOrg_name() {
        return org_name;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }

    private String org_name;



    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    private String mobile;


    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getHospital_id() {
        return hospital_id;
    }

    public void setHospital_id(int hospital_id) {
        this.hospital_id = hospital_id;
    }

    public boolean isIs_basic_cert_applied() {
        return is_basic_cert_applied;
    }

    public void setIs_basic_cert_applied(boolean is_basic_cert_applied) {
        this.is_basic_cert_applied = is_basic_cert_applied;
    }

    public String getOrg_type() {
        return org_type;
    }

    public void setOrg_type(String org_type) {
        this.org_type = org_type;
    }

    public boolean isIsfirsttime_login() {
        return isfirsttime_login;
    }

    public void setIsfirsttime_login(boolean isfirsttime_login) {
        this.isfirsttime_login = isfirsttime_login;
    }


}
