package com.activitymonitor.model;

public class Organization {
    private int    id;
    private String name;
    private String leader;
    private String period;

    public Organization() {}

    public Organization(int id, String name, String leader, String period) {
        this.id     = id;
        this.name   = name;
        this.leader = leader;
        this.period = period;
    }

    public int    getId()     { return id; }
    public String getName()   { return name; }
    public String getLeader() { return leader; }
    public String getPeriod() { return period; }

    public void setId(int id)          { this.id     = id; }
    public void setName(String name)   { this.name   = name; }
    public void setLeader(String l)    { this.leader = l; }
    public void setPeriod(String p)    { this.period = p; }
}