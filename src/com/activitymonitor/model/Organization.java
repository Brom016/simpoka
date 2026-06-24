package com.activitymonitor.model;
//Dibuat oleh: muhamad rifki, hamid bromo
// Model untuk entitas organisasi
public class Organization {
    // ID unik organisasi
    private int    id;
    // Nama organisasi
    private String name;
    // Ketua organisasi
    private String leader;
    // Periode jabatan organisasi
    private String period;

    //muhamad rifki, hamid bromo - enkapsulasi - method constructorOrganization
    public Organization() {}

    //muhamad rifki, hamid bromo - enkapsulasi - method constructorOrganization
    public Organization(int id, String name, String leader, String period) {
        this.id     = id;
        this.name   = name;
        this.leader = leader;
        this.period = period;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - method getId
    public int    getId()     { return id; }
    //muhamad rifki, hamid bromo - enkapsulasi - method getName
    public String getName()   { return name; }
    //muhamad rifki, hamid bromo - enkapsulasi - method getLeader
    public String getLeader() { return leader; }
    //muhamad rifki, hamid bromo - enkapsulasi - method getPeriod
    public String getPeriod() { return period; }

    //muhamad rifki, hamid bromo - enkapsulasi - method setId
    public void setId(int id)          { this.id     = id; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setName
    public void setName(String name)   { this.name   = name; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setLeader
    public void setLeader(String l)    { this.leader = l; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setPeriod
    public void setPeriod(String p)    { this.period = p; }
}
