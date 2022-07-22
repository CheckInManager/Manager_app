package com.gausslab.managerapp.model;

public class Worksite {
    private String workName;
    private String startDate;
    private String lastDate;
    private String location;

    public Worksite(String workName, String startDate, String lastDate, String location) {
        this.workName = workName;
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.location = location;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getLastDate() {
        return lastDate;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
