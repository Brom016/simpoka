package com.activitymonitor.util;

//Dibuat oleh: muhamad rifki, hamid bromo
// Digunakan untuk mengimpor model User
import com.activitymonitor.model.User;

// Kelas untuk mengelola session login (singleton)
public class SessionManager {

    // Instance tunggal SessionManager (singleton)
    private static SessionManager instance;
    // User yang sedang login
    private User currentUser;

    //muhamad rifki, hamid bromo - enkapsulasi - method constructorSessionManager
    // Constructor private untuk pattern singleton
    private SessionManager() {}

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil instance singleton
    // Mendapatkan instance tunggal SessionManager
    public static SessionManager getInstance() {
        // Membuat instance baru jika belum ada
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - menyimpan user yang login ke session
    // Menyimpan user yang sedang login ke session
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil user yang sedang login
    // Mengambil user yang sedang login
    public User getCurrentUser() {
        return currentUser;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memeriksa status login
    // Memeriksa apakah ada user yang sedang login
    public boolean isLoggedIn() {
        return currentUser != null;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memeriksa apakah user adalah admin
    // Memeriksa apakah user yang login memiliki role admin
    public boolean isAdmin() {
        // Jika user null atau role null, bukan admin
        if (currentUser == null || currentUser.getRole() == null) return false;
        String role = currentUser.getRole().toLowerCase();
        return role.equals("admin")
            || role.equals("admin_utama")
            || role.equals("super_admin")
            || role.equals("main_admin");
    }

    //muhamad rifki, hamid bromo - enkapsulasi - membersihkan session login
    // Membersihkan session login (logout)
    public void clear() {
        currentUser = null;
    }
}

