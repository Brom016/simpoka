package com.activitymonitor.view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;

public class ActivityTablePanel extends JPanel {

    private JTextField        searchField;
    private JButton           addButton;
    private JButton           editButton;
    private JButton           deleteButton;
    private JButton           exportButton;
    private JTable            table;
    private DefaultTableModel tableModel;
    private JLabel            countLabel;
    private JButton           prevBtn;
    private JButton           nextBtn;
    private boolean           crudEnabled = true;

    private ActionListener onEdit, onDelete, onStatusChange;
    private int[] activityIds = new int[0];

    private static final String[] COLUMNS = {
        "No", "Nama Kegiatan", "Tanggal", "Lokasi", "Peserta", "Status", ""
    };
    private static final String[] STATUS_KEYS = {"planned", "ongoing", "completed"};
    private static final String[] STATUS_LABELS = {"Direncanakan", "Berlangsung", "Selesai"};

    public ActivityTablePanel() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(0, 12));
        setOpaque(false);

        add(buildToolbar(),   BorderLayout.NORTH);
        add(buildTableCard(), BorderLayout.CENTER);
        add(buildFooter(),    BorderLayout.SOUTH);
    }

    // ── Toolbar ───────────────────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);

        // Search field with drawn icon
        JPanel searchWrap = new JPanel(new BorderLayout(4, 0));
        searchWrap.setBackground(Color.WHITE);
        searchWrap.setBorder(new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1));
        searchWrap.setPreferredSize(new Dimension(280, 38));

        JLabel searchIcon = UIConstants.icon("search", 16, UIConstants.TEXT_LIGHT);
        searchIcon.setBorder(new EmptyBorder(0, 10, 0, 4));

        searchField = new JTextField();
        searchField.setFont(UIConstants.F_BODY);
        searchField.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 10));
        searchField.setBackground(Color.WHITE);

        searchWrap.add(searchIcon,  BorderLayout.WEST);
        searchWrap.add(searchField, BorderLayout.CENTER);

        // Buttons
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btns.setOpaque(false);

        exportButton = UIConstants.outlineButton("Ekspor PDF");
        editButton   = UIConstants.outlineButton("Edit");
        deleteButton = UIConstants.outlineButton("Hapus");
        addButton    = UIConstants.primaryButton("+ Tambah Kegiatan");

        btns.add(exportButton);
        btns.add(editButton);
        btns.add(deleteButton);
        btns.add(addButton);

        bar.add(searchWrap, BorderLayout.WEST);
        bar.add(btns,       BorderLayout.EAST);
        return bar;
    }

    // ── Table card ────────────────────────────────────────────────
    private UIConstants.RoundedPanel buildTableCard() {
        UIConstants.RoundedPanel card = new UIConstants.RoundedPanel(10);
        card.setBackground(Color.WHITE);
        card.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(COLUMNS, 0) {
            @Override public boolean isCellEditable(int r, int c) { return c == 6; }
        };

        table = new JTable(tableModel);
        table.setFont(UIConstants.F_BODY);
        table.setRowHeight(46);
        table.setShowVerticalLines(false);
        table.setShowHorizontalLines(true);
        table.setGridColor(new Color(243, 244, 246));
        table.setSelectionBackground(UIConstants.PRIMARY_LIGHT);
        table.setSelectionForeground(UIConstants.TEXT);
        table.setFocusable(false);
        table.setBackground(Color.WHITE);
        table.setIntercellSpacing(new Dimension(0, 0));

        // Header
        JTableHeader header = table.getTableHeader();
        header.setFont(UIConstants.F_LABEL);
        header.setBackground(new Color(249, 250, 251));
        header.setForeground(UIConstants.TEXT_MUTED);
        header.setBorder(new MatteBorder(0, 0, 2, 0, UIConstants.BORDER));
        header.setPreferredSize(new Dimension(0, 42));
        header.setReorderingAllowed(false);

        // Column widths
        int[] widths = {44, 260, 110, 160, 70, 130, 46};
        for (int i = 0; i < widths.length; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(widths[i]);
        }
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(6).setMaxWidth(50);

        // No renderer - center gray
        table.getColumnModel().getColumn(0).setCellRenderer(
            new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object v,
                        boolean sel, boolean foc, int r, int c) {
                    JLabel l = (JLabel) super.getTableCellRendererComponent(
                        t, v, sel, foc, r, c);
                    l.setHorizontalAlignment(SwingConstants.CENTER);
                    l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                    l.setForeground(UIConstants.TEXT_LIGHT);
                    if (!sel) l.setBackground(Color.WHITE);
                    l.setBorder(new EmptyBorder(0, 0, 0, 0));
                    return l;
                }
            });

        // Peserta renderer - center
        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(4).setCellRenderer(centerR);

        // Status badge renderer
        table.getColumnModel().getColumn(5).setCellRenderer(
            new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    UIConstants.StatusBadge badge =
                        new UIConstants.StatusBadge(val != null ? val.toString() : "");
                    JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 8));
                    wrap.setBackground(sel ? UIConstants.PRIMARY_LIGHT : Color.WHITE);
                    wrap.add(badge);
                    return wrap;
                }
            });

        // Action (3-dot) renderer
        table.getColumnModel().getColumn(6).setCellRenderer(
            new TableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable t, Object val,
                        boolean sel, boolean foc, int row, int col) {
                    JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
                    wrap.setBackground(sel ? UIConstants.PRIMARY_LIGHT : Color.WHITE);
                    wrap.add(UIConstants.icon("dots", 20, UIConstants.TEXT_MUTED));
                    return wrap;
                }
            });

        // Action editor (clickable 3-dot)
        table.getColumnModel().getColumn(6).setCellEditor(
            new DefaultCellEditor(new JCheckBox()) {
                private int editRow;
                private final JPanel wrap = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
                private final JLabel dotsLbl = UIConstants.icon("dots", 20, UIConstants.TEXT_MUTED);

                {
                    setClickCountToStart(1);
                    wrap.setBackground(Color.WHITE);
                    wrap.add(dotsLbl);
                    wrap.addMouseListener(new MouseAdapter() {
                        public void mouseClicked(MouseEvent e) {
                            fireEditingStopped();
                            showMenu(wrap, editRow);
                        }
                    });
                }

                @Override
                public Component getTableCellEditorComponent(JTable t, Object v,
                        boolean sel, int row, int col) {
                    editRow = row;
                    return wrap;
                }

                @Override public Object getCellEditorValue() { return ""; }
            });

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        card.add(scroll, BorderLayout.CENTER);
        return card;
    }

    private void showMenu(Component invoker, int row) {
        if (!crudEnabled) return;

        JPopupMenu menu = new JPopupMenu();

        JMenuItem editItem = new JMenuItem("  Edit Kegiatan");
        editItem.setFont(UIConstants.F_BODY);
        editItem.addActionListener(e -> {
            table.setRowSelectionInterval(row, row);
            if (onEdit != null) onEdit.actionPerformed(e);
        });

        JMenuItem statusItem = new JMenuItem("  Ubah Status");
        statusItem.setFont(UIConstants.F_BODY);
        statusItem.addActionListener(e -> {
            table.setRowSelectionInterval(row, row);
            showStatusDialog(row);
        });

        JMenuItem delItem = new JMenuItem("  Hapus");
        delItem.setFont(UIConstants.F_BODY);
        delItem.setForeground(new Color(220, 38, 38));
        delItem.addActionListener(e -> {
            table.setRowSelectionInterval(row, row);
            if (onDelete != null) onDelete.actionPerformed(e);
        });

        menu.add(editItem);
        menu.add(statusItem);
        menu.addSeparator();
        menu.add(delItem);
        menu.show(invoker, -80, invoker.getHeight());
    }

    // ── Footer ────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        countLabel = new JLabel("Menampilkan 0 dari 0 data");
        countLabel.setFont(UIConstants.F_SMALL);
        countLabel.setForeground(UIConstants.TEXT_MUTED);

        JPanel paging = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        paging.setOpaque(false);

        prevBtn = UIConstants.outlineButton("Previous");
        nextBtn = UIConstants.outlineButton("Next");
        prevBtn.setFont(UIConstants.F_SMALL);
        nextBtn.setFont(UIConstants.F_SMALL);
        prevBtn.setEnabled(false);
        nextBtn.setEnabled(false);

        paging.add(prevBtn);
        paging.add(nextBtn);

        footer.add(countLabel, BorderLayout.WEST);
        footer.add(paging,     BorderLayout.EAST);
        return footer;
    }

    // ── Public API ────────────────────────────────────────────────
    public void setTableData(Object[][] data, int[] ids) {
        tableModel.setRowCount(0);
        activityIds = ids != null ? ids : new int[0];
        int no = 1;
        for (Object[] row : data) {
            Object[] r = new Object[COLUMNS.length];
            r[0] = no++;
            System.arraycopy(row, 0, r, 1, Math.min(row.length, COLUMNS.length - 2));
            r[COLUMNS.length - 1] = "";
            tableModel.addRow(r);
        }
        countLabel.setText("Menampilkan " + data.length + " dari " + data.length + " data");
    }

    public void setTableData(Object[][] data) {
        setTableData(data, new int[data.length]);
    }

    public void clearTable()               { tableModel.setRowCount(0); }
    public int    getSelectedRow()         { return table.getSelectedRow(); }
    public Object getValueAt(int r, int c) { return tableModel.getValueAt(r, c); }
    public String getSearchKeyword()       { return searchField.getText().trim(); }
    public int    getActivityId(int row)   { 
        return row >= 0 && row < activityIds.length ? activityIds[row] : -1; 
    }

    private void showStatusDialog(int row) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
            "Ubah Status", true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(SwingUtilities.getWindowAncestor(this));

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("Pilih Status Baru:");
        label.setFont(UIConstants.F_BODY);

        JComboBox<String> statusCombo = new JComboBox<>(STATUS_LABELS);
        statusCombo.setFont(UIConstants.F_BODY);
        statusCombo.setBackground(Color.WHITE);
        statusCombo.setPreferredSize(new Dimension(0, 36));

        String currentStatus = (String) tableModel.getValueAt(row, 5);
        for (int i = 0; i < STATUS_LABELS.length; i++) {
            if (STATUS_LABELS[i].equals(currentStatus)) {
                statusCombo.setSelectedIndex(i);
                break;
            }
        }

        JButton saveBtn = UIConstants.primaryButton("Simpan");
        JButton cancelBtn = UIConstants.outlineButton("Batal");

        saveBtn.addActionListener(e -> {
            int idx = statusCombo.getSelectedIndex();
            String newStatus = idx >= 0 ? STATUS_KEYS[idx] : "planned";
            if (onStatusChange != null) {
                onStatusChange.actionPerformed(new ActionEvent(statusCombo,
                    ActionEvent.ACTION_PERFORMED, newStatus));
            }
            dialog.dispose();
        });
        cancelBtn.addActionListener(e -> dialog.dispose());

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        btnPanel.setOpaque(false);
        btnPanel.add(cancelBtn);
        btnPanel.add(saveBtn);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 10));
        centerPanel.setOpaque(false);
        centerPanel.add(label, BorderLayout.NORTH);
        centerPanel.add(statusCombo, BorderLayout.CENTER);

        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);

        dialog.setContentPane(panel);
        dialog.setVisible(true);
    }

    public void addSearchListener(ActionListener l) {
        searchField.addActionListener(l);
        searchField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) { l.actionPerformed(null); }
        });
    }
    public void addAddListener(ActionListener l)    { addButton.addActionListener(l); }
    public void addEditListener(ActionListener l) {
        this.onEdit = l;
        editButton.addActionListener(l);
    }
    public void addDeleteListener(ActionListener l) {
        this.onDelete = l;
        deleteButton.addActionListener(l);
    }
    public void addStatusChangeListener(ActionListener l) {
        this.onStatusChange = l;
    }
    public void addExportListener(ActionListener l) { exportButton.addActionListener(l); }
    public void addTableSelectionListener(
            javax.swing.event.ListSelectionListener l) {
        table.getSelectionModel().addListSelectionListener(l);
    }

    public void setCrudEnabled(boolean enabled) {
        crudEnabled = enabled;
        addButton.setVisible(enabled);
        editButton.setVisible(enabled);
        deleteButton.setVisible(enabled);
        table.getColumnModel().getColumn(6).setMinWidth(enabled ? 46 : 0);
        table.getColumnModel().getColumn(6).setMaxWidth(enabled ? 50 : 0);
        table.getColumnModel().getColumn(6).setPreferredWidth(enabled ? 46 : 0);
        revalidate();
        repaint();
    }
}
