package com.activitymonitor.view;


public class DashboardFrame extends JFrame {

    private JLabel UserInfoLabel;
    private JButton logoutButton;
    private ActivityTablePanel tablePanel;

    private static final Color COLOR_PRIMARY    = new Color(0, 128, 128);
    private static final Color COLOR_NAVBAR     = new Color(0, 95, 95);
    private static final Color COLOR_BG         = new Color(240, 244, 245);
    private static final Font  FONT_BRAND       = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font  FONT_USER        = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font  FONT_LOGOUT      = new Font("Segoe UI", Font.PLAIN, 12);

    public DashboardFrame() {
        initComponents();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("SIMPOKA - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(800, 500));
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());

        // Navbar
        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(COLOR_NAVBAR);
        navbar.setPreferredSize(new Dimension(0, 52));
        navbar.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel brand = new JLabel("SIMPOKA");
        brand.setFont(FONT_BRAND);
        brand.setForeground(Color.WHITE);

        JPanel navRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        navRight.setOpaque(false);

        UserInfoLabel = new JLabel("...");
        UserInfoLabel.setFont(FONT_USER);
        UserInfoLabel.setForeground(new Color(200, 230, 230));

        logoutButton = new JButton("Logout");
        logoutButton.setFont(FONT_LOGOUT);
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setBackground(new Color(180, 60, 60));
        logoutButton.setBorderPainted(false);
        logoutButton.setFocusPainted(false);
        logoutButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        logoutButton.setBorder(new EmptyBorder(6, 14, 6, 14));

        //hoover
        logoutButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                logoutButton.setBackground(new Color(160, 40, 40));
            }

            public void mouseExited(MouseEvent e) {
                logoutButton.setBackground(new Color(180, 60, 60));
            }
        });

        navRight.add(UserInfoLabel);
        navRight.add(logoutButton);

        navbar.add(brand, BorderLayout.WEST);
        navbar.add(navRight, BorderLayout.EAST);

        // Main content
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(COLOR_BG);
        content.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Page header
        JPanel pageHeader = new JPanel(new BorderLayout());
        pageHeader.setOpaque(false);
        pageHeader.setBorder(new EmptyBorder(0, 0, 16, 0));
 
        JLabel pageTitle = new JLabel("Daftar Kegiatan");
        pageTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        pageTitle.setForeground(new Color(40, 40, 40));
 
        JLabel pageSubtitle = new JLabel("Kelola seluruh kegiatan organisasi");
        pageSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pageSubtitle.setForeground(new Color(130, 130, 130));
 
        JPanel titleStack = new JPanel();
        titleStack.setLayout(new BoxLayout(titleStack, BoxLayout.Y_AXIS));
        titleStack.setOpaque(false);
        titleStack.add(pageTitle);
        titleStack.add(Box.createVerticalStrut(2));
        titleStack.add(pageSubtitle);
 
        pageHeader.add(titleStack, BorderLayout.WEST);
 
        // Table panel
        tablePanel = new ActivityTablePanel();
 
        content.add(pageHeader, BorderLayout.NORTH);
        content.add(tablePanel, BorderLayout.CENTER);
 
        add(navbar, BorderLayout.NORTH);
        add(content, BorderLayout.CENTER);
    }
    
    //public API
    public void setUserInfo(String fullName, String role) {
        String roleLabel = role.equals("admin") ? "Admin" : "Anggota";
        UserInfoLabel.setText(fullName + " | " + roleLabel);
    }

    public void addLogoutListener(ActionListener listener) {
        logoutButton.addActionListener(listener);
    }

    public ActivityTablePanel getTablePanel() {
        return tablePanel;
    }
    

    

}