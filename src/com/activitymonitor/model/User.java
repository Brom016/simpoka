package com.activitymonitor.model;

//Dibuat oleh: ahmad irfaul, hamid bromo
public class User {
    private int    id;
    private String fullName;
    private String username;
    private String password;
    private String role;
    private int    organizationId;

    //ahmad irfaul, hamid bromo - enkapsulasi - method constructorUser
    public User() {}

    //ahmad irfaul, hamid bromo - enkapsulasi - method constructorUser
    public User(int id, String fullName, String username,
                String password, String role, int organizationId) {
        this.id             = id;
        this.fullName       = fullName;
        this.username       = username;
        this.password       = password;
        this.role           = role;
        this.organizationId = organizationId;
    }

    //ahmad irfaul, hamid bromo - enkapsulasi - method getId
    public int    getId()             { return id; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getFullName
    public String getFullName()       { return fullName; }
    //ahmad irfaul, hamid bromo - enkapsulasi - mengambil input username
    public String getUsername()       { return username; }
    //ahmad irfaul, hamid bromo - enkapsulasi - mengambil input password
    public String getPassword()       { return password; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getRole
    public String getRole()           { return role; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method getOrganizationId
    public int    getOrganizationId() { return organizationId; }

    //ahmad irfaul, hamid bromo - enkapsulasi - method setId
    public void setId(int id)                     { this.id             = id; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setFullName
    public void setFullName(String fullName)       { this.fullName       = fullName; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setUsername
    public void setUsername(String username)       { this.username       = username; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setPassword
    public void setPassword(String password)       { this.password       = password; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setRole
    public void setRole(String role)               { this.role           = role; }
    //ahmad irfaul, hamid bromo - enkapsulasi - method setOrganizationId
    public void setOrganizationId(int orgId)       { this.organizationId = orgId; }
}
