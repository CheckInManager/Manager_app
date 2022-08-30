package com.gausslab.managerapp.model;

public class Notice
{
    private long id;
    private String title;
    private String content;
    private Worksite worksite;
    private long timestamp;
    private long worksiteId;

    public Notice()
    {
    }

    public Notice(long id, String title, String content, Worksite worksite, long timestamp,long worksiteId)
    {
        this.id = id;
        this.title = title;
        this.content = content;
        this.worksite = worksite;
        this.timestamp = timestamp;
        this.worksiteId = worksiteId;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public Worksite getWorksite()
    {
        return worksite;
    }

    public void setWorksite(Worksite worksite)
    {
        this.worksite = worksite;
    }

    public long getTimestamp()
    {
        return timestamp;
    }

    public void setTimestamp(long timestamp)
    {
        this.timestamp = timestamp;
    }

    public long getWorksiteId() {
        return worksiteId;
    }

    public void setWorksiteId(long worksiteId) {
        this.worksiteId = worksiteId;
    }
}
