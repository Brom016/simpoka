package com.activitymonitor;

//Dibuat oleh: hamid bromo
import com.activitymonitor.controller.AuthController; // Mengelola autentikasi login.
import com.activitymonitor.view.LoginFrame; // Tampilan halaman login.
import javax.swing.*; // Komponen UI Swing (UIManager, SwingUtilities).

// Entry point utama aplikasi.
public class App {
    //hamid bromo - enkapsulasi - entry point utama aplikasi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Menerapkan tema FlatLightLaf dari FlatLaf.
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception ignored) {
                // Jika FlatLaf gagal, pakai tema default sistem.
                try { UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e2) {}
            }
            // Membuka halaman login dan menghubungkan dengan controller.
            LoginFrame loginFrame = new LoginFrame();
            new AuthController(loginFrame);
            loginFrame.setVisible(true);
        });
    }
}
