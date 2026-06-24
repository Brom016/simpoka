package com.activitymonitor.view;

//Dibuat oleh: katrina, hamid bromo
// Digunakan untuk kelas dasar AWT
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// Frame login aplikasi
public class LoginFrame extends JFrame {

    // Input username
    private JTextField     usernameField;
    // Input password
    private JPasswordField passwordField;
    // Tombol login
    private JButton        loginButton;
    // Label pesan error
    private JLabel         errorLabel;

    // Menginisialisasi frame login
    //katrina, hamid bromo - enkapsulasi - method constructorLoginFrame
    public LoginFrame() {
        initComponents();
        setupFrame();
    }

    // Mengatur judul, ukuran, dan posisi frame login
    //katrina, hamid bromo - enkapsulasi - mengatur konfigurasi frame
    private void setupFrame() {
        setTitle("SIMPOKA - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 540);
        setLocationRelativeTo(null);
        setResizable(false);
        getContentPane().setBackground(UIConstants.BG);
    }

    // Membangun seluruh komponen UI login
    //katrina, hamid bromo - enkapsulasi - inisialisasi komponen UI
    private void initComponents() {
        setLayout(new GridBagLayout());
        getContentPane().setBackground(UIConstants.BG);

        UIConstants.RoundedPanel card = new UIConstants.RoundedPanel(12);
        card.setBackground(Color.WHITE);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(42, 44, 36, 44));
        card.setPreferredSize(new Dimension(380, 450));

        JLabel title = new JLabel("SIMPOKA");
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(UIConstants.PRIMARY);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Sistem Informasi Monitoring Program Kerja Organisasi");
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
        userLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        userLabel.setMaximumSize(new Dimension(292, 20));

        JPanel userWrap = buildFieldWrap();
        userWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        userWrap.setMaximumSize(new Dimension(292, 40));
        userWrap.setPreferredSize(new Dimension(292, 40));
        JLabel userIcon = new JLabel("  \u25A1 ");
        userIcon.setForeground(UIConstants.TEXT_LIGHT);
        usernameField = new JTextField();
        usernameField.setFont(UIConstants.F_BODY);
        usernameField.setHorizontalAlignment(JTextField.LEFT);
        usernameField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 10));
        usernameField.setBackground(Color.WHITE);
        userWrap.add(userIcon, BorderLayout.WEST);
        userWrap.add(usernameField, BorderLayout.CENTER);

        // Password
        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(UIConstants.F_LABEL);
        passLabel.setForeground(UIConstants.TEXT);
        passLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        passLabel.setMaximumSize(new Dimension(292, 20));

        JPanel passWrap = buildFieldWrap();
        passWrap.setAlignmentX(Component.CENTER_ALIGNMENT);
        passWrap.setMaximumSize(new Dimension(292, 40));
        passWrap.setPreferredSize(new Dimension(292, 40));
        JLabel passIcon = new JLabel("  \u25CB ");
        passIcon.setForeground(UIConstants.TEXT_LIGHT);
        passwordField = new JPasswordField();
        passwordField.setFont(UIConstants.F_BODY);
        passwordField.setHorizontalAlignment(JTextField.LEFT);
        passwordField.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 4));
        passwordField.setBackground(Color.WHITE);
        JButton toggle = new JButton("\u25CE");
        toggle.setBorderPainted(false);
        toggle.setContentAreaFilled(false);
        toggle.setFocusPainted(false);
        toggle.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        toggle.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 8));
        // Toggle visibilitas password (tampil/sembunyi)
        toggle.addActionListener(e ->
            passwordField.setEchoChar(
                passwordField.getEchoChar() == 0 ? '\u2022' : (char) 0));
        passWrap.add(passIcon,      BorderLayout.WEST);
        passWrap.add(passwordField, BorderLayout.CENTER);
        passWrap.add(toggle,        BorderLayout.EAST);

        // Error
        errorLabel = new JLabel(" ");
        errorLabel.setFont(UIConstants.F_SMALL);
        errorLabel.setForeground(new Color(220, 53, 69));
        errorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        errorLabel.setMaximumSize(new Dimension(292, 18));

        // Button
        loginButton = UIConstants.primaryButton("Masuk");
        loginButton.setMaximumSize(new Dimension(292, 42));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        // Footer
        JLabel footer = new JLabel("\u00A9 SIMPOKA 2026 Kelompok 7");
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

    // Membangun wrapper panel untuk field input
    //katrina, hamid bromo - enkapsulasi - membangun wrapper field input
    private JPanel buildFieldWrap() {
        JPanel wrap = new JPanel(new BorderLayout(4, 0));
        wrap.setBackground(Color.WHITE);
        wrap.setBorder(new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1));
        return wrap;
    }

    // Mengambil input dari field login
    //katrina, hamid bromo - enkapsulasi - mengambil input username
    public String getUsername()    { return usernameField.getText().trim(); }
    //katrina, hamid bromo - enkapsulasi - mengambil input password
    public String getPassword()    { return new String(passwordField.getPassword()); }
    // Menampilkan pesan error pada form login
    //katrina, hamid bromo - enkapsulasi - menampilkan pesan error
    public void showError(String m){ errorLabel.setText(m); }
    // Membersihkan pesan error
    //katrina, hamid bromo - enkapsulasi - membersihkan pesan error
    public void clearError()       { errorLabel.setText(" "); }
    // Mengaktifkan atau menonaktifkan tombol login
    //katrina, hamid bromo - enkapsulasi - mengaktifkan/menonaktifkan tombol login
    public void setLoginEnabled(boolean b) {
        loginButton.setEnabled(b);
        loginButton.setText(b ? "Masuk" : "Memproses...");
    }
    // Menambahkan listener untuk tombol login
    //katrina, hamid bromo - enkapsulasi - menambahkan listener tombol login
    public void addLoginListener(ActionListener l) {
        loginButton.addActionListener(l);
    }

    // Entry point utama aplikasi
    //katrina, hamid bromo - enkapsulasi - entry point utama aplikasi
    public static void main(String[] args) {
        // Jalankan aplikasi di Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Set look and feel FlatLaf
            try { UIManager.setLookAndFeel("com.formdev.flatlaf.FlatLightLaf"); }
            catch (Exception ignored) {}
            new LoginFrame().setVisible(true);
        });
    }
}

