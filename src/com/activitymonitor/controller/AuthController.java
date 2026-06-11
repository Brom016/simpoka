package com.activitymonitor.controller;

//Dibuat oleh: hamid bromo
import com.activitymonitor.dao.UserDAO;
import com.activitymonitor.model.User;
import com.activitymonitor.util.SessionManager;
import com.activitymonitor.view.DashboardFrame;
import com.activitymonitor.view.LoginFrame;
import javax.swing.SwingWorker;

public class AuthController {

    private final LoginFrame loginFrame;
    private final UserDAO    userDAO;

    //hamid bromo - enkapsulasi - method constructorAuthController
    public AuthController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.userDAO    = new UserDAO();
        bindEvents();
    }

    //hamid bromo - enkapsulasi - mengikat event listener ke komponen UI
    private void bindEvents() {
        loginFrame.addLoginListener(e -> handleLogin());
    }

    //hamid bromo - enkapsulasi - menangani proses login user
    private void handleLogin() {
        String username = loginFrame.getUsername();
        String password = loginFrame.getPassword();

        if (username.isEmpty() || password.isEmpty()) {
            loginFrame.showError("Username dan password wajib diisi.");
            return;
        }

        loginFrame.setLoginEnabled(false);
        loginFrame.clearError();

        new SwingWorker<User, Void>() {
            @Override
            //hamid bromo - overriding (polimorfisme) - menjalankan proses di background thread
            protected User doInBackground() {
                return userDAO.authenticate(username, password);
            }

            @Override
            //hamid bromo - overriding (polimorfisme) - menangani hasil setelah background selesai
            protected void done() {
                try {
                    User user = get();
                    if (user == null) {
                        loginFrame.showError("Username atau password salah.");
                        loginFrame.setLoginEnabled(true);
                        return;
                    }

                    SessionManager.getInstance().setCurrentUser(user);

                    DashboardFrame dashboard = new DashboardFrame();
                    dashboard.setUserInfo(user.getFullName(),
                        user.getUsername().equalsIgnoreCase("bromo.admin")
                            ? "admin_utama" : user.getRole());

                    new ActivityController(dashboard, user);

                    dashboard.addLogoutListener(ev -> {
                        SessionManager.getInstance().clear();
                        dashboard.dispose();
                        loginFrame.setLoginEnabled(true);
                        loginFrame.setVisible(true);
                    });

                    loginFrame.setVisible(false);
                    dashboard.setVisible(true);
                } catch (Exception e) {
                    loginFrame.showError("Gagal terhubung ke database. Coba lagi.");
                    loginFrame.setLoginEnabled(true);
                }
            }
        }.execute();
    }
}

