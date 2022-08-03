package com.gausslab.managerapp.model;

public class Worksite {
    private String worksiteName;
    private String startDate;
    private String lastDate;
    private String location;

    public Worksite(String worksiteName, String startDate, String lastDate, String location) {
        this.worksiteName = worksiteName;
        this.startDate = startDate;
        this.lastDate = lastDate;
        this.location = location;
    }

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
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
