package com.activitymonitor.view;

import com.activitymonitor.model.Activity;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class DashboardFrame extends JFrame {

    private JLabel             userNameLabel;
    private JLabel             userRoleLabel;
    private ActivityTablePanel tablePanel;
    private JTable             reportTable;
    private DefaultTableModel  reportTableModel;
    private JButton            reportExportButton;
    private ActionListener     reportExportListener;
    private JPanel             contentArea;
    private JPanel             sidebar;

    // Sidebar item refs for active state toggle
    private JPanel activeSidebarItem;

    public DashboardFrame() {
        initComponents();
        setupFrame();
    }

    private void setupFrame() {
        setTitle("Activity Monitor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 680);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(900, 580));
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(UIConstants.BG);

        tablePanel = new ActivityTablePanel();
        sidebar = buildSidebar();
        add(sidebar,       BorderLayout.WEST);
        add(buildMain(),   BorderLayout.CENTER);
    }

    // ── Sidebar ───────────────────────────────────────────────────
    private JPanel buildSidebar() {
        JPanel side = new JPanel();
        side.setLayout(new BoxLayout(side, BoxLayout.Y_AXIS));
        side.setBackground(Color.WHITE);
        side.setPreferredSize(new Dimension(165, 0));
        side.setBorder(new MatteBorder(0, 0, 0, 1, UIConstants.BORDER));

        // Logo area
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 14));
        logoPanel.setOpaque(false);
        JLabel logoIcon = UIConstants.icon("grid", 22, UIConstants.PRIMARY);
        JLabel logoText = new JLabel("Activity");
        logoText.setFont(new Font("Segoe UI", Font.BOLD, 13));
        logoText.setForeground(UIConstants.PRIMARY);
        logoPanel.add(logoIcon);
        logoPanel.add(logoText);
        logoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 52));
        side.add(logoPanel);

        // Divider
        JSeparator sep = new JSeparator();
        sep.setForeground(UIConstants.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        side.add(sep);
        side.add(Box.createVerticalStrut(8));

        // Menu items
        JPanel dashItem = buildSideItem("grid",   "Dashboard", true,  e -> showDashboard());
        JPanel kegItem  = buildSideItem("list",   "Kegiatan",  false, e -> showDashboard());
        JPanel lapItem  = buildSideItem("doc",    "Laporan",   false, e -> showLaporan());

        activeSidebarItem = dashItem;

        side.add(dashItem);
        side.add(kegItem);
        side.add(lapItem);
        side.add(Box.createVerticalGlue());

        // Logout
        JPanel logoutItem = buildSideItem("logout", "Keluar", false, null);
        logoutItem.setName("logout");
        side.add(logoutItem);
        side.add(Box.createVerticalStrut(12));

        return side;
    }

    private JPanel buildSideItem(String iconType, String label,
                                  boolean active, ActionListener onClick) {
        JPanel item = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 9));
        item.setOpaque(true);
        item.setBackground(active ? UIConstants.PRIMARY_LIGHT : Color.WHITE);
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color iconColor = active ? UIConstants.PRIMARY : UIConstants.TEXT_MUTED;
        JLabel ico  = UIConstants.icon(iconType, 16, iconColor);
        JLabel text = new JLabel(label);
        text.setFont(active
            ? new Font("Segoe UI", Font.BOLD, 13)
            : UIConstants.F_BODY);
        text.setForeground(active ? UIConstants.PRIMARY : UIConstants.TEXT_MUTED);

        item.add(ico);
        item.add(text);

        if (active) {
            item.setBorder(new MatteBorder(0, 3, 0, 0, UIConstants.PRIMARY));
        } else {
            item.setBorder(new EmptyBorder(0, 3, 0, 0));
        }

        item.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (item != activeSidebarItem)
                    item.setBackground(new Color(245, 250, 249));
            }
            public void mouseExited(MouseEvent e) {
                if (item != activeSidebarItem)
                    item.setBackground(Color.WHITE);
            }
            public void mouseClicked(MouseEvent e) {
                if (onClick != null) {
                    setActiveItem(item, ico, text);
                    onClick.actionPerformed(
                        new ActionEvent(item, ActionEvent.ACTION_PERFORMED, label));
                }
            }
        });

        return item;
    }

    private void setActiveItem(JPanel item, JLabel ico, JLabel text) {
        if (activeSidebarItem != null) {
            activeSidebarItem.setBackground(Color.WHITE);
            activeSidebarItem.setBorder(new EmptyBorder(0, 3, 0, 0));
            // Reset old item colors
            for (Component c : activeSidebarItem.getComponents()) {
                if (c instanceof JLabel) {
                    c.setForeground(UIConstants.TEXT_MUTED);
                    ((JLabel) c).setFont(UIConstants.F_BODY);
                }
            }
        }
        activeSidebarItem = item;
        item.setBackground(UIConstants.PRIMARY_LIGHT);
        item.setBorder(new MatteBorder(0, 3, 0, 0, UIConstants.PRIMARY));
        ico.setForeground(UIConstants.PRIMARY);
        text.setForeground(UIConstants.PRIMARY);
        text.setFont(new Font("Segoe UI", Font.BOLD, 13));
    }

    // ── Main content ──────────────────────────────────────────────
    private JPanel buildMain() {
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(UIConstants.BG);
        main.add(buildTopBar(),  BorderLayout.NORTH);

        contentArea = new JPanel(new BorderLayout());
        contentArea.setBackground(UIConstants.BG);
        contentArea.setBorder(new EmptyBorder(24, 24, 24, 24));

        // Default: show dashboard
        showDashboard();

        main.add(contentArea, BorderLayout.CENTER);
        return main;
    }

    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(Color.WHITE);
        bar.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UIConstants.BORDER),
            new EmptyBorder(10, 20, 10, 20)
        ));

        // Left: brand + breadcrumb
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        left.setOpaque(false);

        JLabel brand = new JLabel("Activity Monitor");
        brand.setFont(new Font("Segoe UI", Font.BOLD, 15));
        brand.setForeground(UIConstants.PRIMARY);

        JLabel pipe = new JLabel("  |  ");
        pipe.setForeground(UIConstants.BORDER);

        JLabel breadcrumb = new JLabel("Beranda  \u203A  Dashboard");
        breadcrumb.setFont(UIConstants.F_SMALL);
        breadcrumb.setForeground(UIConstants.TEXT_MUTED);

        left.add(brand);
        left.add(pipe);
        left.add(breadcrumb);

        // Right: user info + avatar
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        right.setOpaque(false);

        JPanel userInfo = new JPanel();
        userInfo.setLayout(new BoxLayout(userInfo, BoxLayout.Y_AXIS));
        userInfo.setOpaque(false);

        userNameLabel = new JLabel("...");
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        userNameLabel.setForeground(UIConstants.TEXT);
        userNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        userRoleLabel = new JLabel("...");
        userRoleLabel.setFont(new Font("Segoe UI", Font.BOLD, 10));
        userRoleLabel.setForeground(UIConstants.PRIMARY);
        userRoleLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        userInfo.add(userNameLabel);
        userInfo.add(userRoleLabel);

        JLabel avatarIcon = UIConstants.icon("user", 22, UIConstants.TEXT_MUTED);

        right.add(userInfo);
        right.add(avatarIcon);

        bar.add(left,  BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ── Content views ─────────────────────────────────────────────
    private void showDashboard() {
        contentArea.removeAll();

        JLabel pageTitle = new JLabel("Daftar Kegiatan");
        pageTitle.setFont(UIConstants.F_TITLE);
        pageTitle.setForeground(UIConstants.TEXT);
        pageTitle.setBorder(new EmptyBorder(0, 0, 16, 0));

        contentArea.add(pageTitle,  BorderLayout.NORTH);
        contentArea.add(tablePanel, BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private void showLaporan() {
        contentArea.removeAll();

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        JPanel titleWrap = new JPanel();
        titleWrap.setOpaque(false);
        titleWrap.setLayout(new BoxLayout(titleWrap, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Laporan Kegiatan");
        title.setFont(UIConstants.F_TITLE);
        title.setForeground(UIConstants.TEXT);

        JLabel sub = new JLabel("Pilih kegiatan yang ingin dimasukkan ke PDF.");
        sub.setFont(UIConstants.F_BODY);
        sub.setForeground(UIConstants.TEXT_MUTED);

        titleWrap.add(title);
        titleWrap.add(Box.createVerticalStrut(4));
        titleWrap.add(sub);

        reportExportButton = UIConstants.primaryButton("Ekspor PDF");
        reportExportButton.setPreferredSize(new Dimension(130, 38));
        if (reportExportListener != null) {
            reportExportButton.addActionListener(reportExportListener);
        }

        header.add(titleWrap, BorderLayout.WEST);
        header.add(reportExportButton, BorderLayout.EAST);

        contentArea.add(header, BorderLayout.NORTH);
        contentArea.add(buildReportTableCard(), BorderLayout.CENTER);
        contentArea.revalidate();
        contentArea.repaint();
    }

    private UIConstants.RoundedPanel buildReportTableCard() {
        UIConstants.RoundedPanel card = new UIConstants.RoundedPanel(10);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());

        if (reportTableModel == null) {
            reportTableModel = new DefaultTableModel(
                new Object[]{"Pilih", "Nama Kegiatan", "Tanggal", "Lokasi", "Status"}, 0) {
                @Override public boolean isCellEditable(int row, int col) {
                    return col == 0;
                }
                @Override public Class<?> getColumnClass(int col) {
                    return col == 0 ? Boolean.class : String.class;
                }
            };
        }

        reportTable = new JTable(reportTableModel);
        reportTable.setFont(UIConstants.F_BODY);
        reportTable.setRowHeight(44);
        reportTable.setShowVerticalLines(false);
        reportTable.setShowHorizontalLines(true);
        reportTable.setGridColor(new Color(243, 244, 246));
        reportTable.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        reportTable.setSelectionForeground(UIConstants.TEXT);
        reportTable.setFocusable(false);
        reportTable.setBackground(Color.WHITE);
        reportTable.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader tableHeader = reportTable.getTableHeader();
        tableHeader.setFont(UIConstants.F_LABEL);
        tableHeader.setBackground(new Color(249, 250, 251));
        tableHeader.setForeground(UIConstants.TEXT_MUTED);
        tableHeader.setBorder(new MatteBorder(0, 0, 2, 0, UIConstants.BORDER));
        tableHeader.setPreferredSize(new Dimension(0, 42));
        tableHeader.setReorderingAllowed(false);

        int[] widths = {60, 280, 120, 180, 140};
        for (int i = 0; i < widths.length; i++) {
            reportTable.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        reportTable.getColumnModel().getColumn(0).setMaxWidth(70);

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        reportTable.getColumnModel().getColumn(2).setCellRenderer(centerR);

        reportTable.getColumnModel().getColumn(4).setCellRenderer(
            (table, value, selected, focused, row, col) -> {
                UIConstants.StatusBadge badge =
                    new UIConstants.StatusBadge(value != null ? value.toString() : "");
                JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
                wrap.setBackground(selected ? UIConstants.PRIMARY_LIGHT : Color.WHITE);
                wrap.add(badge);
                return wrap;
            });

        JScrollPane scroll = new JScrollPane(reportTable);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    // ── Public API ────────────────────────────────────────────────
    public void setUserInfo(String name, String role) {
        userNameLabel.setText(name);
        String normalizedRole = role != null ? role.toLowerCase() : "";
        if (normalizedRole.equals("admin_utama")
                || normalizedRole.equals("super_admin")
                || normalizedRole.equals("main_admin")) {
            userRoleLabel.setText("ADMIN UTAMA");
        } else if (normalizedRole.equals("admin")) {
            userRoleLabel.setText("ADMIN ORGANISASI");
        } else {
            userRoleLabel.setText("ANGGOTA");
        }
    }

    public void addLogoutListener(ActionListener l) {
        for (Component c : sidebar.getComponents()) {
            if (c instanceof JPanel && "logout".equals(((JPanel) c).getName())) {
                ((JPanel) c).addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        l.actionPerformed(new ActionEvent(
                            e.getSource(), ActionEvent.ACTION_PERFORMED, "logout"));
                    }
                });
            }
        }
    }

    public ActivityTablePanel getTablePanel() { return tablePanel; }

    public void setReportActivities(List<Activity> activities) {
        if (reportTableModel == null) {
            reportTableModel = new DefaultTableModel(
                new Object[]{"Pilih", "Nama Kegiatan", "Tanggal", "Lokasi", "Status"}, 0) {
                @Override public boolean isCellEditable(int row, int col) {
                    return col == 0;
                }
                @Override public Class<?> getColumnClass(int col) {
                    return col == 0 ? Boolean.class : String.class;
                }
            };
        }

        reportTableModel.setRowCount(0);
        for (Activity activity : activities) {
            reportTableModel.addRow(new Object[]{
                Boolean.TRUE,
                activity.getName(),
                activity.getDate() != null ? activity.getDate().toString() : "",
                activity.getLocation(),
                activity.getStatus()
            });
        }
    }

    public List<Integer> getSelectedReportRows() {
        List<Integer> rows = new ArrayList<>();
        if (reportTableModel == null) return rows;

        for (int i = 0; i < reportTableModel.getRowCount(); i++) {
            Object selected = reportTableModel.getValueAt(i, 0);
            if (Boolean.TRUE.equals(selected)) rows.add(i);
        }
        return rows;
    }

    public void addReportExportListener(ActionListener l) {
        reportExportListener = l;
        if (reportExportButton != null) {
            reportExportButton.addActionListener(l);
        }
    }
}
