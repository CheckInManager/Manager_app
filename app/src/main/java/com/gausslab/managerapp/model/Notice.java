package com.gausslab.managerapp.model;

import java.util.Date;

public class Notice {
    private String noticeName;
    private String memo;
    private String worksiteName;
    private String time;
    private String keyValue;

    public Notice() {
    }

    public Notice(String noticeName, String memo, String worksiteName, String time,String keyValue) {
        this.noticeName = noticeName;
        this.memo = memo;
        this.worksiteName = worksiteName;
        this.time = time;
        this.keyValue = keyValue;
    }

    public String getNoticeName() {
        return noticeName;
    }

    public void setNoticeName(String noticeName) {
        this.noticeName = noticeName;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(String keyValue) {
        this.keyValue = keyValue;
    }
}
