package com.gausslab.managerapp.model;

public class AccidentHistory {
    private String description;
    private String place;
    private String date;
    private String time;
    private String userPhoneNumber;

    public AccidentHistory() {
    }

    public AccidentHistory(String description, String place, String date, String time, String userPhoneNumber) {
        this.description = description;
        this.place = place;
        this.date = date;
        this.time = time;
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }
}
