package com.gausslab.managerapp.model;

import androidx.annotation.Nullable;

import java.math.BigDecimal;

public class Worksite {
    private String name;
    private String worksiteName;
    private String startDate;
    private String endDate;
    private String location;
    private String keyValue;

    public Worksite() {

    }

    public Worksite(String worksiteName, String startDate, String endDate, String location, String keyValue) {
        this.worksiteName = worksiteName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.keyValue = keyValue;
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

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(!(obj instanceof Worksite)){
            return false;
        }
        Worksite toCompare = (Worksite) obj;
        return keyValue.equals(toCompare.keyValue) &&
               worksiteName.equals(toCompare.worksiteName) &&
               location.equals(toCompare.location) &&
               startDate.equals(toCompare.startDate) &&
               endDate.equals(toCompare.endDate);
    }
}
