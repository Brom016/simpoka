package com.activitymonitor;

//Dibuat oleh: hamid bromo
import com.activitymonitor.controller.AuthController;
import com.activitymonitor.view.LoginFrame;
import javax.swing.*;

public class App {
    //hamid bromo - enkapsulasi - entry point utama aplikasi
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //flatlaf
            try {
                UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf");
            } catch (Exception ignored) {
                //fallback system look
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
