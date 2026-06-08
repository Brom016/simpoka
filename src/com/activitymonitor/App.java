package com.activitymonitor;

import com.activitymonitor.controller.AuthController;
import com.activitymonitor.view.LoginFrame;
import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // FlatLaf untuk tampilan modern
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception ignored) {
                // Fallback ke system look
                try { UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e2) {}
            }

            LoginFrame loginFrame = new LoginFrame();
            new AuthController(loginFrame);
            loginFrame.setVisible(true);
        });
    }
}