package com.activitymonitor.model;

import java.sql.Date;

public class Activity {
    private int    id;
    private String name;
    private String description;
    private Date   date;
    private String location;
    private int    participantCount;
    private String status;
    private int    createdBy;
    private int    organizationId;

    public Activity() {}

    public Activity(int id, String name, String description, Date date,
                    String location, int participantCount, String status,
                    int createdBy, int organizationId) {
        this.id               = id;
        this.name             = name;
        this.description      = description;
        this.date             = date;
        this.location         = location;
        this.participantCount = participantCount;
        this.status           = status;
        this.createdBy        = createdBy;
        this.organizationId   = organizationId;
    }

    public int    getId()               { return id; }
    public String getName()             { return name; }
    public String getDescription()      { return description; }
    public Date   getDate()             { return date; }
    public String getLocation()         { return location; }
    public int    getParticipantCount() { return participantCount; }
    public String getStatus()           { return status; }
    public int    getCreatedBy()        { return createdBy; }
    public int    getOrganizationId()   { return organizationId; }

    public void setId(int id)                         { this.id               = id; }
    public void setName(String name)                   { this.name             = name; }
    public void setDescription(String description)     { this.description      = description; }
    public void setDate(Date date)                     { this.date             = date; }
    public void setLocation(String location)           { this.location         = location; }
    public void setParticipantCount(int count)         { this.participantCount = count; }
    public void setStatus(String status)               { this.status           = status; }
    public void setCreatedBy(int createdBy)            { this.createdBy        = createdBy; }
    public void setOrganizationId(int organizationId)  { this.organizationId   = organizationId; }
}