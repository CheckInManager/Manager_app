package com.gausslab.managerapp.model;

public class User {
    private String phoneNumber;
    private String password;
    private String userName;
    private String userImage;
    private String career;
    private String worksiteName;
    private String accidentHistory;
    private String memo;

    public User() {
    }

    public User(String phoneNumber, String password, String userName, String userImage, String career, String worksiteName, String accidentHistory, String memo) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.userName = userName;
        this.userImage = userImage;
        this.career = career;
        this.worksiteName = worksiteName;
        this.accidentHistory = accidentHistory;
        this.memo = memo;
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

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }

    public String getAccidentHistory() {
        return accidentHistory;
    }

    public void setAccidentHistory(String accidentHistory) {
        this.accidentHistory = accidentHistory;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
