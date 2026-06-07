package com.activitymonitor.util;

import com.activitymonitor.model.User;

public class SessionManager {

    private static SessionManager instance;
    private User currentUser;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdmin() {
        if (currentUser == null || currentUser.getRole() == null) return false;
        String role = currentUser.getRole().toLowerCase();
        return role.equals("admin")
            || role.equals("admin_utama")
            || role.equals("super_admin")
            || role.equals("main_admin");
    }

    public void clear() {
        currentUser = null;
    }
}
