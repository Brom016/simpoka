package com.activitymonitor;

import com.activitymonitor.controller.AuthController;
import com.activitymonitor.view.LoginFrame;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        // Gunakan tampilan sistem operasi
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Fallback ke default Swing
        }

        // Jalankan di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginFrame loginFrame = new LoginFrame();
            new AuthController(loginFrame);
            loginFrame.setVisible(true);
        });
    }
}