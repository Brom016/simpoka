package com.activitymonitor.controller;

import com.activitymonitor.dao.UserDAO;
import com.activitymonitor.model.User;
import com.activitymonitor.util.SessionManager;
import com.activitymonitor.view.DashboardFrame;
import com.activitymonitor.view.LoginFrame;

public class AuthController {

    private final LoginFrame loginFrame;
    private final UserDAO    userDAO;

    public AuthController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.userDAO    = new UserDAO();
        bindEvents();
    }

    private void bindEvents() {
        loginFrame.addLoginListener(e -> handleLogin());
    }

    private void handleLogin() {
        String username = loginFrame.getUsername();
        String password = loginFrame.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginFrame.showError("Username dan password wajib diisi.");
            return;
        }

        loginFrame.setLoginEnabled(false);
        loginFrame.clearError();

        User user = userDAO.authenticate(username, password);

        if (user == null) {
            loginFrame.showError("Username atau password salah.");
            loginFrame.setLoginEnabled(true);
            return;
        }

        // Set session
        SessionManager.getInstance().setCurrentUser(user);

        // Open dashboard
        DashboardFrame dashboard = new DashboardFrame();
        dashboard.setUserInfo(user.getFullName(), user.getRole());

        new ActivityController(dashboard, user);

        dashboard.addLogoutListener(ev -> {
            SessionManager.getInstance().clear();
            dashboard.dispose();
            loginFrame.setLoginEnabled(true);
            loginFrame.setVisible(true);
        });

        loginFrame.setVisible(false);
        dashboard.setVisible(true);
    }
}