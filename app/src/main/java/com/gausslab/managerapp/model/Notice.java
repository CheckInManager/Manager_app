package com.gausslab.managerapp.model;

public class Notice {
    private String noticeName;
    private String memo;
    private String worksiteKeyValue;
    private String time;
    private String keyValue;
    private String worksiteName;

    public Notice() {
    }

    public Notice(String noticeName, String memo, String worksiteKeyValue, String time,String keyValue,String worksiteName) {
        this.noticeName = noticeName;
        this.memo = memo;
        this.worksiteKeyValue = worksiteKeyValue;
        this.time = time;
        this.keyValue = keyValue;
        this.worksiteName = worksiteName;
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

    public String getWorksiteKeyValue() {
        return worksiteKeyValue;
    }

    public void setWorksiteKeyValue(String worksiteKeyValue) {
        this.worksiteKeyValue = worksiteKeyValue;
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

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }
}
