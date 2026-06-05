package com.activitymonitor.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {

    private JTextField     usernameField;
    private JPasswordField passwordField;
    private JButton        loginButton;
    private JLabel         errorLabel;

    public LoginFrame() {
        initComponents();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Activity Monitor — Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(440, 520);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIConstants.BG);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(UIConstants.BG);

        UIConstants.RoundedPanel card = new UIConstants.RoundedPanel(12);
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(40, 40, 36, 40));
        card.setPreferredSize(new Dimension(360, 440));

        JLabel title = new JLabel("Activity Monitor");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sistem Monitoring Kegiatan Organisasi");
        subtitle.setFont(UIConstants.F_SUBTITLE);
        subtitle.setForeground(UIConstants.TEXT_MUTED);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Username
        JLabel userLabel = new JLabel("Username");
        userLabel.setFont(UIConstants.F_LABEL);
        userLabel.setForeground(UIConstants.TEXT);
        userLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel userWrap = buildFieldWrap();
        userWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        userWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel userIcon = new JLabel("  \u25A1 ");
        userIcon.setForeground(UIConstants.TEXT_LIGHT);
        usernameField = new JTextField();
        usernameField.setFont(UIConstants.F_BODY);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 10));
        usernameField.setBackground(Color.WHITE);
        userWrap.add(userIcon, BorderLayout.WEST);
        userWrap.add(usernameField, BorderLayout.CENTER);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UIConstants.F_LABEL);
        passLabel.setForeground(UIConstants.TEXT);
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel passWrap = buildFieldWrap();
        passWrap.setAlignmentX(Component.LEFT_ALIGNMENT);
        passWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        JLabel passIcon = new JLabel("  \u25CB ");
        passIcon.setForeground(UIConstants.TEXT_LIGHT);
        passwordField = new JPasswordField();
        passwordField.setFont(UIConstants.F_BODY);
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        passwordField.setBackground(Color.WHITE);
        JButton toggle = new JButton("\u25CE");
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setFocusPainted(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        toggle.addActionListener(e ->
            passwordField.setEchoChar(
                passwordField.getEchoChar() == 0 ? 'u2022' : (char) 0));
        passWrap.add(passIcon,      BorderLayout.WEST);
        passWrap.add(passwordField, BorderLayout.CENTER);
        passWrap.add(toggle,        BorderLayout.EAST);

        // Error
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIConstants.F_SMALL);
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Button
        loginButton = UIConstants.primaryButton("Masuk");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Footer
        JLabel footer = new JLabel("\u00A9 2026 Student Organization Activity Monitor");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer.setForeground(UIConstants.TEXT_LIGHT);
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        passwordField.addActionListener(e -> loginButton.doClick());

        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(sep);
        card.add(Box.createVerticalStrut(24));
        card.add(userLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(userWrap);
        card.add(Box.createVerticalStrut(14));
        card.add(passLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passWrap);
        card.add(Box.createVerticalStrut(6));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(16));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(20));
        card.add(footer);

        add(card);
    }

    private JPanel buildFieldWrap() {
        JPanel wrap = new JPanel(new BorderLayout(4, 0));
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1));
        return wrap;
    }

    public String getUsername()    { return usernameField.getText().trim(); }
    public String getPassword()    { return new String(passwordField.getPassword()); }
    public void showError(String m){ errorLabel.setText(m); }
    public void clearError()       { errorLabel.setText(" "); }
    public void setLoginEnabled(boolean b) {
        loginButton.setEnabled(b);
        loginButton.setText(b ? "Masuk" : "Memproses...");
    }
    public void addLoginListener(ActionListener l) {
        loginButton.addActionListener(l);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try { UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf"); }
            catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}