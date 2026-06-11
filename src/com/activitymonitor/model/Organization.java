package com.activitymonitor.model;

//Dibuat oleh: ahmad irfaul, hamid bromo
public class Organization {
    private int    id;
    private String name;
    private String leader;
    private String period;

    //ahmad irfaul, hamid bromo - enkapsulasi - method constructorOrganization
    public Organization() {}

    //ahmad irfaul, hamid bromo - enkapsulasi - method constructorOrganization
    public Organization(int id, String name, String leader, String period) {
        this.id     = id;
        this.name   = name;
        this.leader = leader;
        this.period = period;
    }

    //ahmad irfaul, hamid bromo - enkapsulasi - method getId
    public int    getId()     { return id; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getName
    public String getName()   { return name; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getLeader
    public String getLeader() { return leader; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getPeriod
    public String getPeriod() { return period; }

    //ahmad irfaul, hamid bromo - enkapsulasi - method setId
    public void setId(int id)          { this.id     = id; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setName
    public void setName(String name)   { this.name   = name; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setLeader
    public void setLeader(String l)    { this.leader = l; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setPeriod
    public void setPeriod(String p)    { this.period = p; }
}
