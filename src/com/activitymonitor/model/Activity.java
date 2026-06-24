package com.activitymonitor.model;

//Dibuat oleh: hamid bromo
// Digunakan untuk merepresentasikan tanggal dalam database
import java.sql.Date;

// Model untuk entitas kegiatan
public class Activity {
    // ID unik kegiatan
    private int    id;
    // Nama kegiatan
    private String name;
    // Deskripsi detail kegiatan
    private String description;
    // Tanggal pelaksanaan kegiatan
    private Date   date;
    // Lokasi kegiatan
    private String location;
    // Jumlah peserta kegiatan
    private int    participantCount;
    // Status kegiatan (planned/ongoing/completed)
    private String status;
    // ID user yang membuat kegiatan
    private int    createdBy;
    // ID organisasi tempat kegiatan dibuat
    private int    organizationId;

    //hamid bromo - enkapsulasi - method constructorActivity
    public Activity() {}

    //hamid bromo - enkapsulasi - method constructorActivity
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

    //hamid bromo - enkapsulasi - method getId
    public int    getId()               { return id; }
    //hamid bromo - enkapsulasi - method getName
    public String getName()             { return name; }
    //hamid bromo - enkapsulasi - method getDescription
    public String getDescription()      { return description; }
    //hamid bromo - enkapsulasi - method getDate
    public Date   getDate()             { return date; }
    //hamid bromo - enkapsulasi - method getLocation
    public String getLocation()         { return location; }
    //hamid bromo - enkapsulasi - method getParticipantCount
    public int    getParticipantCount() { return participantCount; }
    //hamid bromo - enkapsulasi - method getStatus
    public String getStatus()           { return status; }
    //hamid bromo - enkapsulasi - method getCreatedBy
    public int    getCreatedBy()        { return createdBy; }
    //hamid bromo - enkapsulasi - method getOrganizationId
    public int    getOrganizationId()   { return organizationId; }

    //hamid bromo - enkapsulasi - method setId
    public void setId(int id)                         { this.id               = id; }
    //hamid bromo - enkapsulasi - method setName
    public void setName(String name)                   { this.name             = name; }
    //hamid bromo - enkapsulasi - method setDescription
    public void setDescription(String description)     { this.description      = description; }
    //hamid bromo - enkapsulasi - method setDate
    public void setDate(Date date)                     { this.date             = date; }
    //hamid bromo - enkapsulasi - method setLocation
    public void setLocation(String location)           { this.location         = location; }
    //hamid bromo - enkapsulasi - method setParticipantCount
    public void setParticipantCount(int count)         { this.participantCount = count; }
    //hamid bromo - enkapsulasi - mengubah tampilan status badge
    public void setStatus(String status)               { this.status           = status; }
    //hamid bromo - enkapsulasi - method setCreatedBy
    public void setCreatedBy(int createdBy)            { this.createdBy        = createdBy; }
    //hamid bromo - enkapsulasi - method setOrganizationId
    public void setOrganizationId(int organizationId)  { this.organizationId   = organizationId; }
}
