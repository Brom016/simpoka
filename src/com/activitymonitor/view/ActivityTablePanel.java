package com.activitymonitor.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class ActivityTablePanel extends JPanel {

    private JTextField    searchField;
    private JButton       searchButton;
    private JButton       addButton;
    private JButton       editButton;
    private JButton       deleteButton;
    private JButton       exportButton;
    private JTable        table;
    private DefaultTableModel tableModel;

    private static final Color COLOR_PRIMARY  = new Color(0, 128, 128);
    private static final Color COLOR_DANGER   = new Color(200, 60, 60);
    private static final Color COLOR_SUCCESS  = new Color(40, 160, 80);
    private static final Color COLOR_CARD     = Color.WHITE;
    private static final Font  FONT_TABLE     = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_HEADER    = new Font("Segoe UI", Font.BOLD, 13);
    private static final Font  FONT_BUTTON    = new Font("Segoe UI", Font.BOLD, 12);

    private static final String[] COLUMNS = {
        "No", "Nama Kegiatan", "Tanggal", "Lokasi", "Peserta", "Status"
    };

    public ActivityTablePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 12));
        setOpaque(false);

        add(buildToolbar(),    BorderLayout.NORTH);
        add(buildTableCard(),  BorderLayout.CENTER);
        add(buildActionBar(),  BorderLayout.SOUTH);
    }

    // ── Toolbar (search + add) ────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new BorderLayout(10, 0));
        toolbar.setOpaque(false);

        // Search
        JPanel searchPanel = new JPanel(new BorderLayout(6, 0));
        searchPanel.setOpaque(false);
        searchPanel.setPreferredSize(new Dimension(320, 36));

        searchField = new JTextField();
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        searchField.setPreferredSize(new Dimension(0, 36));
        searchField.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        searchField.putClientProperty("JTextField.placeholderText",
            "Cari nama kegiatan...");

        searchButton = buildButton("Cari", COLOR_PRIMARY, Color.WHITE);
        searchButton.setPreferredSize(new Dimension(70, 36));

        searchPanel.add(searchField,  BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Add button
        addButton = buildButton("+ Tambah Kegiatan", COLOR_SUCCESS, Color.WHITE);
        addButton.setPreferredSize(new Dimension(180, 36));

        toolbar.add(searchPanel, BorderLayout.WEST);
        toolbar.add(addButton,   BorderLayout.EAST);

        return toolbar;
    }

    // ── Table card ────────────────────────────────────────────────
    private JPanel buildTableCard() {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(COLOR_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1, true),
            new EmptyBorder(0, 0, 0, 0)
        ));

        // Table model
        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setFont(FONT_TABLE);
        table.setRowHeight(40);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(240, 240, 240));
        table.setSelectionBackground(new Color(0, 128, 128, 30));
        table.setSelectionForeground(new Color(30, 30, 30));
        table.setFocusable(false);

        // Header style
        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_HEADER);
        header.setBackground(new Color(248, 250, 250));
        header.setForeground(new Color(80, 80, 80));
        header.setBorder(new MatteBorder(0, 0, 2, 0, new Color(0, 128, 128)));
        header.setReorderingAllowed(false);

        // Column widths
        TableColumnModel cm = table.getColumnModel();
        cm.getColumn(0).setPreferredWidth(40);   // No
        cm.getColumn(0).setMaxWidth(50);
        cm.getColumn(1).setPreferredWidth(280);  // Nama
        cm.getColumn(2).setPreferredWidth(110);  // Tanggal
        cm.getColumn(3).setPreferredWidth(180);  // Lokasi
        cm.getColumn(4).setPreferredWidth(70);   // Peserta
        cm.getColumn(5).setPreferredWidth(110);  // Status

        // Status cell renderer (badge color)
        table.getColumnModel().getColumn(5).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    t, value, isSelected, hasFocus, row, col);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.BOLD, 11));

                String status = value != null ? value.toString() : "";
                switch (status) {
                    case "planned"   :
                        label.setForeground(new Color(30, 100, 200));
                        label.setText("Direncanakan");
                        break;
                    case "ongoing"   :
                        label.setForeground(new Color(180, 120, 0));
                        label.setText("Berlangsung");
                        break;
                    case "completed" :
                        label.setForeground(new Color(30, 140, 60));
                        label.setText("Selesai");
                        break;
                    default:
                        label.setForeground(new Color(100, 100, 100));
                }

                if (!isSelected) label.setBackground(Color.WHITE);
                return label;
            }
        });

        // No column — auto number
        table.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                boolean isSelected, boolean hasFocus, int row, int col) {
                JLabel label = (JLabel) super.getTableCellRendererComponent(
                    t, value, isSelected, hasFocus, row, col);
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                label.setForeground(new Color(150, 150, 150));
                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        card.add(scrollPane, BorderLayout.CENTER);
        return card;
    }

    // ── Action bar (edit + delete + export) ──────────────────────
    private JPanel buildActionBar() {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        bar.setOpaque(false);

        editButton   = buildButton("Edit",         COLOR_PRIMARY,              Color.WHITE);
        deleteButton = buildButton("Hapus",        COLOR_DANGER,               Color.WHITE);
        exportButton = buildButton("Ekspor PDF",   new Color(100, 60, 180),    Color.WHITE);

        editButton.setPreferredSize(new Dimension(100, 36));
        deleteButton.setPreferredSize(new Dimension(100, 36));
        exportButton.setPreferredSize(new Dimension(130, 36));

        bar.add(editButton);
        bar.add(deleteButton);
        bar.add(Box.createHorizontalStrut(10));
        bar.add(exportButton);

        return bar;
    }

    // ── Helper ───────────────────────────────────────────────────
    private JButton buildButton(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BUTTON);
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(6, 16, 6, 16));

        Color hover = bg.darker();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(hover); }
            public void mouseExited(MouseEvent e)  { btn.setBackground(bg); }
        });

        return btn;
    }

    // ── Public API untuk ActivityController ──────────────────────

    public void setTableData(Object[][] data) {
        tableModel.setRowCount(0);
        int no = 1;
        for (Object[] row : data) {
            Object[] rowWithNo = new Object[COLUMNS.length];
            rowWithNo[0] = no++;
            System.arraycopy(row, 0, rowWithNo, 1, row.length);
            tableModel.addRow(rowWithNo);
        }
    }

    public void clearTable() {
        tableModel.setRowCount(0);
    }

    public int getSelectedRow() {
        return table.getSelectedRow();
    }

    public Object getValueAt(int row, int col) {
        return tableModel.getValueAt(row, col);
    }

    public String getSearchKeyword() {
        return searchField.getText().trim();
    }

    public void addSearchListener(ActionListener l)  { searchButton.addActionListener(l); }
    public void addAddListener(ActionListener l)     { addButton.addActionListener(l); }
    public void addEditListener(ActionListener l)    { editButton.addActionListener(l); }
    public void addDeleteListener(ActionListener l)  { deleteButton.addActionListener(l); }
    public void addExportListener(ActionListener l)  { exportButton.addActionListener(l); }

    public void addTableSelectionListener(javax.swing.event.ListSelectionListener l) {
        table.getSelectionModel().addListSelectionListener(l);
    }
}