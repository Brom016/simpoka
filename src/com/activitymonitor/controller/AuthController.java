package com.activitymonitor.controller;

import com.activitymonitor.dao.UserDAO;
import com.activitymonitor.model.User;
import com.activitymonitor.util.SessionManager;

public class AuthController {

    private final UserDAO userDAO;

    public AuthController() {
        this.userDAO = new UserDAO();
    }

    // Untuk UI/login: panggil metode ini dengan username & password.
    // Jika sukses, current user disimpan ke SessionManager.
    public boolean login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }
        if (password == null) {
            return false;
        }

        // authenticate() hanya return boolean.
        boolean ok = userDAO.authenticate(username.trim(), password);
        if (!ok) {
            SessionManager.clear();
            return false;
        }

        // Ambil user lengkap agar bisa dipakai di Dashboard.
        User u = userDAO.findByUsername(username.trim());
        SessionManager.setCurrentUser(u);
        return u != null;
    }

    public void logout() {
        SessionManager.clear();
    }

    public User getCurrentUser() {
        return SessionManager.getCurrentUser();
    }
}
