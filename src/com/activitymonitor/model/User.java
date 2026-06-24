package com.activitymonitor.model;

//Dibuat oleh: muhamad rifki, hamid bromo
// Model untuk entitas pengguna
public class User {
    // ID unik pengguna
    private int    id;
    // Nama lengkap pengguna
    private String fullName;
    // Username untuk login
    private String username;
    // Password untuk login
    private String password;
    // Role pengguna (admin/user)
    private String role;
    // ID organisasi tempat pengguna terdaftar
    private int    organizationId;

    //muhamad rifki, hamid bromo - enkapsulasi - method constructorUser
    public User() {}

    //muhamad rifki, hamid bromo - enkapsulasi - method constructorUser
    public User(int id, String fullName, String username,
                String password, String role, int organizationId) {
        this.id             = id;
        this.fullName       = fullName;
        this.username       = username;
        this.password       = password;
        this.role           = role;
        this.organizationId = organizationId;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - method getId
    public int    getId()             { return id; }
    //muhamad rifki, hamid bromo - enkapsulasi - method getFullName
    public String getFullName()       { return fullName; }
    //muhamad rifki, hamid bromo - enkapsulasi - mengambil input username
    public String getUsername()       { return username; }
    //muhamad rifki, hamid bromo - enkapsulasi - mengambil input password
    public String getPassword()       { return password; }
    //muhamad rifki, hamid bromo - enkapsulasi - method getRole
    public String getRole()           { return role; }
    //muhamad rifki, hamid bromo - enkapsulasi - method getOrganizationId
    public int    getOrganizationId() { return organizationId; }

    //muhamad rifki, hamid bromo - enkapsulasi - method setId
    public void setId(int id)                     { this.id             = id; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setFullName
    public void setFullName(String fullName)       { this.fullName       = fullName; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setUsername
    public void setUsername(String username)       { this.username       = username; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setPassword
    public void setPassword(String password)       { this.password       = password; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setRole
    public void setRole(String role)               { this.role           = role; }
    //muhamad rifki, hamid bromo - enkapsulasi - method setOrganizationId
    public void setOrganizationId(int orgId)       { this.organizationId = orgId; }
}
