package com.activitymonitor.util;

//Dibuat oleh: muhamad rifki, hamid bromo
import com.activitymonitor.model.User;

public class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    //muhamad rifki, hamid bromo - enkapsulasi - method constructorSessionManager
    private SessionManager() {}

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil instance singleton
    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - menyimpan user yang login ke session
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil user yang sedang login
    public User getCurrentUser() {
        return currentUser;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memeriksa status login
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memeriksa apakah user adalah admin
    public boolean isAdmin() {
        if (currentUser == null || currentUser.getRole() == null) return false;
        String role = currentUser.getRole().toLowerCase();
        return role.equals("admin")
            || role.equals("admin_utama")
            || role.equals("super_admin")
            || role.equals("main_admin");
    }

    //muhamad rifki, hamid bromo - enkapsulasi - membersihkan session login
    public void clear() {
        currentUser = null;
    }
}

