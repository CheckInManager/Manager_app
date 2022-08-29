package com.gausslab.managerapp.model;

import androidx.annotation.Nullable;

public class Worksite {
    private String worksiteName;
    private long startDateStamp;
    private long endDateStamp;
    private String location;
    private long id;

    public Worksite() {

    }

    public Worksite(String worksiteName, long startDateStamp, long endDateStamp, String location, long id) {
        this.worksiteName = worksiteName;
        this.startDateStamp = startDateStamp;
        this.endDateStamp = endDateStamp;
        this.location = location;
        this.id = id;
    }

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }

    public long getStartDateStamp() {
        return startDateStamp;
    }

    public void setStartDateStamp(long startDateStamp) {
        this.startDateStamp = startDateStamp;
    }

    public long getEndDateStamp() {
        return endDateStamp;
    }

    public void setEndDateStamp(long endDateStamp) {
        this.endDateStamp = endDateStamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public boolean equals(@Nullable Object obj)
    {
        if(!(obj instanceof Worksite)){
            return false;
        }
        Worksite toCompare = (Worksite) obj;
//        return id.equals(toCompare.id) &&
        return worksiteName.equals(toCompare.worksiteName) &&
               location.equals(toCompare.location) ;
//               startDateStamp.equals(toCompare.startDateStamp) &&
//               endDateStamp.equals(toCompare.endDateStamp);
    }
}
