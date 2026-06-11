package com.activitymonitor.model;

//Dibuat oleh: ahmad irfaul, hamid bromo
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

    //ahmad irfaul, hamid bromo - enkapsulasi - method constructorActivity
    public Activity() {}

    //ahmad irfaul, hamid bromo - enkapsulasi - method constructorActivity
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

    //ahmad irfaul, hamid bromo - enkapsulasi - method getId
    public int    getId()               { return id; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getName
    public String getName()             { return name; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getDescription
    public String getDescription()      { return description; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getDate
    public Date   getDate()             { return date; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getLocation
    public String getLocation()         { return location; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getParticipantCount
    public int    getParticipantCount() { return participantCount; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getStatus
    public String getStatus()           { return status; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getCreatedBy
    public int    getCreatedBy()        { return createdBy; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getOrganizationId
    public int    getOrganizationId()   { return organizationId; }

    //ahmad irfaul, hamid bromo - enkapsulasi - method setId
    public void setId(int id)                         { this.id               = id; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setName
    public void setName(String name)                   { this.name             = name; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setDescription
    public void setDescription(String description)     { this.description      = description; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setDate
    public void setDate(Date date)                     { this.date             = date; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setLocation
    public void setLocation(String location)           { this.location         = location; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setParticipantCount
    public void setParticipantCount(int count)         { this.participantCount = count; }
    //ahmad irfaul, hamid bromo - enkapsulasi - mengubah tampilan status badge
    public void setStatus(String status)               { this.status           = status; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setCreatedBy
    public void setCreatedBy(int createdBy)            { this.createdBy        = createdBy; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setOrganizationId
    public void setOrganizationId(int organizationId)  { this.organizationId   = organizationId; }
}
