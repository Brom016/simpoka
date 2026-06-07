package com.activitymonitor.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ActivityFormPanel extends JDialog {

    private JTextField  nameField;
    private JTextArea   descArea;
    private JTextField  dateField;
    private JTextField  locationField;
    private JTextField  participantField;
    private JComboBox<String> statusCombo;
    private JButton     saveBtn;
    private JButton     cancelBtn;
    private boolean     editMode;

    private static final String[] STATUS_KEYS    = {"planned","ongoing","completed"};
    private static final String[] STATUS_LABELS  = {"Direncanakan","Berlangsung","Selesai"};

    public ActivityFormPanel(Frame parent, boolean editMode) {
        super(parent, true);
        this.editMode = editMode;
        initComponents();
        setupDialog();
    }

    private void setupDialog() {
        setTitle(editMode ? "Edit Kegiatan" : "Tambah Kegiatan");
        setSize(560, 560);
        setLocationRelativeTo(getParent());
        setResizable(false);
        getRootPane().setBorder(new UIConstants.RoundedBorder(10, UIConstants.BORDER, 1));
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UIConstants.BORDER),
            new EmptyBorder(18, 24, 18, 24)
        ));

        JLabel title = new JLabel(editMode ? "Edit Kegiatan" : "Tambah Kegiatan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIConstants.TEXT);

        JButton close = new JButton("\u00D7");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);
        close.setForeground(UIConstants.TEXT_MUTED);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());

        header.add(title, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);

        // Form body
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0.0;

        nameField        = buildTextField("Contoh: Seminar Teknologi");
        descArea         = buildTextArea("Masukkan deskripsi singkat kegiatan");
        dateField        = buildTextField("YYYY-MM-DD");
        locationField    = buildTextField("Gedung Serbaguna");
        participantField = buildTextField("0");
        statusCombo      = buildStatusCombo();

        addFieldRow(body, gc, 0, 0, 2, "Nama Kegiatan", nameField);

        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1));
        descScroll.setPreferredSize(new Dimension(0, 82));
        addFieldRow(body, gc, 2, 0, 2, "Deskripsi", descScroll);

        addTwoColumnRow(body, gc, 4,
            "Tanggal", dateField,
            "Lokasi", locationField);
        addTwoColumnRow(body, gc, 6,
            "Jumlah Peserta", participantField,
            "Status", statusCombo);

        JScrollPane bodyScroll = new JScrollPane(body);
        bodyScroll.setBorder(BorderFactory.createEmptyBorder());

        // Footer buttons
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        cancelBtn = UIConstants.outlineButton("Cancel");
        saveBtn   = UIConstants.primaryButton("Save");
        saveBtn.setPreferredSize(new Dimension(90, 36));
        cancelBtn.setPreferredSize(new Dimension(90, 36));
        cancelBtn.addActionListener(e -> dispose());

        footer.add(cancelBtn);
        footer.add(saveBtn);

        root.add(header,      BorderLayout.NORTH);
        root.add(bodyScroll,  BorderLayout.CENTER);
        root.add(footer,      BorderLayout.SOUTH);

        setContentPane(root);
    }

    private void addFieldRow(JPanel p, GridBagConstraints gc,
                              int startRow, int colX, int colSpan,
                              String label, JComponent field) {
        gc.gridx    = colX;
        gc.gridy    = startRow;
        gc.gridwidth = colSpan;
        gc.insets   = new Insets(0, 0, 4, colSpan == 1 ? 8 : 0);
        p.add(buildLabel(label), gc);

        gc.gridy    = startRow + 1;
        gc.insets   = new Insets(0, 0, 14, colSpan == 1 ? 8 : 0);
        p.add(field, gc);
        gc.gridwidth = 1;
    }

    private void addTwoColumnRow(JPanel p, GridBagConstraints gc, int startRow,
                                  String leftLabel, JComponent leftField,
                                  String rightLabel, JComponent rightField) {
        gc.gridwidth = 1;

        gc.gridx = 0;
        gc.gridy = startRow;
        gc.insets = new Insets(0, 0, 4, 8);
        p.add(buildLabel(leftLabel), gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 8, 4, 0);
        p.add(buildLabel(rightLabel), gc);

        gc.gridx = 0;
        gc.gridy = startRow + 1;
        gc.insets = new Insets(0, 0, 14, 8);
        p.add(leftField, gc);

        gc.gridx = 1;
        gc.insets = new Insets(0, 8, 14, 0);
        p.add(rightField, gc);

        gc.gridx = 0;
        gc.insets = new Insets(0, 0, 14, 0);
    }

    private JLabel buildLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.F_LABEL);
        l.setForeground(UIConstants.TEXT);
        return l;
    }

    private JTextField buildTextField(String placeholder) {
        JTextField f = new JTextField() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g.setColor(UIConstants.TEXT_LIGHT);
                    g.setFont(UIConstants.F_BODY);
                    FontMetrics fm = g.getFontMetrics();
                    g.drawString(placeholder, 10,
                        (getHeight() - fm.getHeight()) / 2 + fm.getAscent());
                }
            }
        };
        f.setFont(UIConstants.F_BODY);
        f.setBackground(Color.WHITE);
        f.setBorder(new CompoundBorder(
            new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        f.setPreferredSize(new Dimension(0, 36));
        return f;
    }

    private JTextArea buildTextArea(String placeholder) {
        JTextArea a = new JTextArea(3, 0) {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    g.setColor(UIConstants.TEXT_LIGHT);
                    g.setFont(UIConstants.F_BODY);
                    g.drawString(placeholder, 10, 20);
                }
            }
        };
        a.setFont(UIConstants.F_BODY);
        a.setBackground(Color.WHITE);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBorder(new EmptyBorder(8, 10, 8, 10));
        return a;
    }

    private JComboBox<String> buildStatusCombo() {
        JComboBox<String> cb = new JComboBox<>(STATUS_LABELS);
        cb.setFont(UIConstants.F_BODY);
        cb.setBackground(Color.WHITE);
        cb.setPreferredSize(new Dimension(0, 36));
        return cb;
    }

    // ── Public API ────────────────────────────────────────────────
    public String getActivityName()     { return nameField.getText().trim(); }
    public String getDescription()      { return descArea.getText().trim(); }
    public String getDate()             { return dateField.getText().trim(); }
    public String getActivityLocation() { return locationField.getText().trim(); }
    public String getParticipantCount() { return participantField.getText().trim(); }
    public String getStatus()           { return STATUS_KEYS[statusCombo.getSelectedIndex()]; }

    public void setActivityName(String v)     { nameField.setText(v); }
    public void setDescription(String v)      { descArea.setText(v); }
    public void setDate(String v)             { dateField.setText(v); }
    public void setActivityLocation(String v) { locationField.setText(v); }
    public void setParticipantCount(String v) { participantField.setText(v); }
    public void setStatus(String v) {
        for (int i = 0; i < STATUS_KEYS.length; i++) {
            if (STATUS_KEYS[i].equals(v)) { statusCombo.setSelectedIndex(i); break; }
        }
    }

    public void addSaveListener(ActionListener l) { saveBtn.addActionListener(l); }

    public boolean validateForm() {
        if (getActivityName().isEmpty()) {
            showWarn("Nama kegiatan wajib diisi."); return false;
        }
        if (getDate().isEmpty()) {
            showWarn("Tanggal wajib diisi."); return false;
        }
        if (getActivityLocation().isEmpty()) {
            showWarn("Lokasi wajib diisi."); return false;
        }
        return true;
    }

    private void showWarn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validasi", JOptionPane.WARNING_MESSAGE);
    }
}
