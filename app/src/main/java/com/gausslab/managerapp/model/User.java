package com.gausslab.managerapp.model;

public class User {
    private String phoneNumber;
    private String password;
    private String name;
    private String career;
    private String worksite;
    private boolean accidentHistory;
    private String memo;
    private boolean picture;

    public User() {
    }

    public User(String phoneNumber, String password, String userName, String career, String worksiteName, boolean accidentHistory, String memo, boolean picture) {
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.name = userName;
        this.career = career;
        this.worksite = worksiteName;
        this.accidentHistory = accidentHistory;
        this.memo = memo;
        this.picture = picture;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getWorksite() {
        return worksite;
    }

    public void setWorksite(String worksite) {
        this.worksite = worksite;
    }

    public void setAccidentHistory(boolean accidentHistory) {
        this.accidentHistory = accidentHistory;
    }

    public boolean isAccidentHistory() {
        return accidentHistory;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }
}
