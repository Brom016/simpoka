package com.activitymonitor.view;

// Mengimpor library Swing untuk membuat GUI
// Mengimpor library AWT untuk warna, font, layout, dll
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

// Class utama
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;

    private static final Color COLOR_PRIMARY    = new Color(0, 128, 128);
    private static final Color COLOR_BG         = new Color(240, 244, 245);
    private static final Color COLOR_CARD       = Color.WHITE;
    private static final Color COLOR_ERROR      = new Color(200, 50, 50);
    private static final Font  FONT_TITLE       = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font  FONT_SUBTITLE    = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font  FONT_LABEL       = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_FIELD       = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_BUTTON      = new Font("Segoe UI", Font.BOLD, 13);
    
    public LoginFrame() {
        initComponents();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("SIMPOKA - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(420, 480);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void initComponents() {
        //background panel
        JPanel background = new JPanel(new GridBagLayout());
        background.setBackground(COLOR_BG);

        //card panel
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(40, 40, 40, 40)
        ));

        card.setPreferredSize(new Dimension(360,440));

        //title
        JLabel title = new JLabel("SIMPOKA");
        title.setFont(FONT_TITLE);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setForeground(COLOR_PRIMARY);

        //subtitle
        JLabel subtitle = new JLabel("Sistem Pelaporan Kegiatan Mahasiswa");
        subtitle.setFont(FONT_SUBTITLE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitle.setForeground(new Color(120, 120, 120));

        //separator
        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(220, 220, 220));
        separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));

        // Username field
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(FONT_LABEL);
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        usernameField = new JTextField();
        usernameField.setFont(FONT_FIELD);
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);
        usernameField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
 
        // Password field
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(FONT_LABEL);
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        passwordField = new JPasswordField();
        passwordField.setFont(FONT_FIELD);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);
        passwordField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
 
        // Error label
        errorLabel = new JLabel(" ");
        errorLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        errorLabel.setForeground(COLOR_ERROR);
        errorLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
 
        // Login button
        loginButton = new JButton("Masuk");
        loginButton.setFont(FONT_BUTTON);
        loginButton.setBackground(COLOR_PRIMARY);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
 
        // Hover effect
        loginButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                loginButton.setBackground(new Color(0, 105, 105));
            }
            public void mouseExited(MouseEvent e) {
                loginButton.setBackground(COLOR_PRIMARY);
            }
        });
 
        // Enter key triggers login
        passwordField.addActionListener(e -> loginButton.doClick());
 
        // Footer
        JLabel footer = new JLabel("© 2026 SIMPOKA");
        footer.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        footer.setForeground(new Color(180, 180, 180));
        footer.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Assemble card
        card.add(title);
        card.add(Box.createVerticalStrut(6));
        card.add(subtitle);
        card.add(Box.createVerticalStrut(20));
        card.add(separator);
        card.add(Box.createVerticalStrut(24));
        card.add(usernameLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(usernameField);
        card.add(Box.createVerticalStrut(16));
        card.add(passwordLabel);
        card.add(Box.createVerticalStrut(6));
        card.add(passwordField);
        card.add(Box.createVerticalStrut(6));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(loginButton);
        card.add(Box.createVerticalStrut(20));
        card.add(footer);

        background.add(card);
        setContentPane(background);
 
    }
    
    //public API untuk aaAuthController

    public String getUsername() {
        return usernameField.getText().trim();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void showError(String message) {
        errorLabel.setText(message);
    }

    public void clearError() {
        errorLabel.setText(" ");
    }

    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void setLoginEnabled(boolean enabled) {
        loginButton.setEnabled(enabled);
        loginButton.setText(enabled ? "Masuk" : "Memproses...");
    }
    

    //testing
    public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        LoginFrame frame = new LoginFrame();
        frame.setVisible(true);
    });
}

}