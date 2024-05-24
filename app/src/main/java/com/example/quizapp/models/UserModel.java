package com.example.quizapp.models;

public class UserModel {

    private  String userID;
    private String name;
    private String phone;
    private String email;
    private String role;

    public UserModel() {

    }

    public UserModel(String userID, String name, String phone, String email, String role) {
        this.userID = userID;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}

