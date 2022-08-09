package com.gausslab.managerapp.model;

public class AccidentHistory {
    private String Description;
    private String place;
    private String date;
    private String time;

    public AccidentHistory(String description, String place, String date, String time) {
        Description = description;
        this.place = place;
        this.date = date;
        this.time = time;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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
}
