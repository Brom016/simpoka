package com.activitymonitor.controller;

//Dibuat oleh: hamid bromo
import com.activitymonitor.dao.UserDAO; // Mengakses data user dari database.
import com.activitymonitor.model.User; // Model data user.
import com.activitymonitor.util.SessionManager; // Mengelola session login.
import com.activitymonitor.view.DashboardFrame; // Tampilan dashboard utama.
import com.activitymonitor.view.LoginFrame; // Tampilan halaman login.
import javax.swing.SwingWorker; // Menjalankan proses async di background.

// Controller yang menangani proses login dan autentikasi user.
public class AuthController {

    private final LoginFrame loginFrame;
    private final UserDAO    userDAO;

    //hamid bromo - enkapsulasi - method constructorAuthController
    // Menginisialisasi controller dengan tampilan login dan DAO user.
    public AuthController(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        this.userDAO    = new UserDAO();
        bindEvents();
    }

    //hamid bromo - enkapsulasi - mengikat event listener ke komponen UI
    // Mendaftarkan listener ke tombol login di LoginFrame.
    private void bindEvents() {
        loginFrame.addLoginListener(e -> handleLogin());
    }

    //hamid bromo - enkapsulasi - menangani proses login user
    // Memvalidasi input, menjalankan autentikasi di background, lalu navigasi ke dashboard.
    private void handleLogin() {
        String username = loginFrame.getUsername();
        String password = loginFrame.getPassword();
        // Jika ada field kosong, tampilkan error.
        if (username.isEmpty() || password.isEmpty()) {
            loginFrame.showError("Username dan password wajib diisi.");
            return;
        }

        loginFrame.setLoginEnabled(false);
        loginFrame.clearError();
        // Autentikasi di background agar UI tidak freeze.
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
                    // Jika user tidak ditemukan, tampilkan error.
                    if (user == null) {
                        loginFrame.showError("Username atau password salah.");
                        loginFrame.setLoginEnabled(true);
                        return;
                    }
                    // Simpan user ke session dan buka dashboard.
                    SessionManager.getInstance().setCurrentUser(user);

                    DashboardFrame dashboard = new DashboardFrame();
                    dashboard.setUserInfo(user.getFullName(),
                        user.getUsername().equalsIgnoreCase("bromo.admin")
                            ? "admin_utama" : user.getRole());
                    // Hubungkan dashboard dengan controller kegiatan.
                    new ActivityController(dashboard, user);
                    // Listener untuk tombol logout di dashboard.
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

