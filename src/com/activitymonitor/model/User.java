package com.activitymonitor.model;

public class User {
    private int    id;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private int    organizationId;

    public User() {}

    public User(int id, String fullName, String username,
                String password, String role, int organizationId) {
        this.id             = id;
        this.fullName       = fullName;
        this.username       = username;
        this.password       = password;
        this.role           = role;
        this.organizationId = organizationId;
    }

    public int    getId()             { return id; }
    public String getFullName()       { return fullName; }
    public String getUsername()       { return username; }
    public String getPassword()       { return password; }
    public String getRole()           { return role; }
    public int    getOrganizationId() { return organizationId; }

    public void setId(int id)                     { this.id             = id; }
    public void setFullName(String fullName)       { this.fullName       = fullName; }
    public void setUsername(String username)       { this.username       = username; }
    public void setPassword(String password)       { this.password       = password; }
    public void setRole(String role)               { this.role           = role; }
    public void setOrganizationId(int orgId)       { this.organizationId = orgId; }
}