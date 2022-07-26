package com.gausslab.managerapp.model;

public class User {
    private String phoneNumber;
    private String password;
    private String userName;
    private String userImage;
    private String career;

    public User(String phoneNumber, String password, String userName, String userImage, String career) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userName = userName;
        this.userImage = userImage;
        this.career = career;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }
}
