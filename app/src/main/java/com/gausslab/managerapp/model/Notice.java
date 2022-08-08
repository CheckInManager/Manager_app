package com.gausslab.managerapp.model;

public class Notice {
    private String noticeName;
    private String memo;
    private String worksiteName;

    public Notice(String noticeName, String memo, String worksiteName) {
        this.noticeName = noticeName;
        this.memo = memo;
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

    public String getWorksiteName() {
        return worksiteName;
    }

    public void setWorksiteName(String worksiteName) {
        this.worksiteName = worksiteName;
    }

}
