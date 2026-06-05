package com.activitymonitor.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ActivityFormPanel extends JDialog {

    private JTextField  nameField;
    private JTextArea   descriptionArea;
    private JTextField  dateField;
    private JTextField  locationField;
    private JTextField  participantField;
    private JComboBox<String> statusCombo;
    private JButton     saveButton;
    private JButton     cancelButton;
    private JLabel      titleLabel;

    private boolean     isEditMode = false;

    private static final Color COLOR_PRIMARY = new Color(0, 128, 128);
    private static final Color COLOR_BG      = new Color(248, 250, 250);
    private static final Font  FONT_TITLE    = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font  FONT_LABEL    = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font  FONT_FIELD    = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font  FONT_BUTTON   = new Font("Segoe UI", Font.BOLD, 13);

    public ActivityFormPanel(Frame parent, boolean editMode) {
        super(parent, true);
        this.isEditMode = editMode;
        initComponents();
        setupDialog();
    }

    private void setupDialog() {
        setTitle(isEditMode ? "Edit Kegiatan" : "Tambah Kegiatan");
        setSize(480, 540);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(COLOR_BG);

        root.add(buildHeader(),  BorderLayout.NORTH);
        root.add(buildForm(),    BorderLayout.CENTER);
        root.add(buildFooter(),  BorderLayout.SOUTH);

        setContentPane(root);
    }

    // ── Header ───────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(COLOR_PRIMARY);
        header.setBorder(new EmptyBorder(16, 24, 16, 24));

        titleLabel = new JLabel(isEditMode ? "Edit Kegiatan" : "Tambah Kegiatan");
        titleLabel.setFont(FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);

        JLabel sub = new JLabel(isEditMode
            ? "Perbarui informasi kegiatan"
            : "Isi data kegiatan baru");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        sub.setForeground(new Color(200, 235, 235));

        JPanel stack = new JPanel();
        stack.setLayout(new BoxLayout(stack, BoxLayout.Y_AXIS));
        stack.setOpaque(false);
        stack.add(titleLabel);
        stack.add(Box.createVerticalStrut(3));
        stack.add(sub);

        header.add(stack, BorderLayout.WEST);
        return header;
    }

    // ── Form body ────────────────────────────────────────────────
    private JScrollPane buildForm() {
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        form.setBorder(new EmptyBorder(20, 24, 20, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.insets  = new Insets(0, 0, 14, 0);

        nameField        = buildTextField();
        descriptionArea  = buildTextArea();
        dateField        = buildTextField();
        locationField    = buildTextField();
        participantField = buildTextField();
        statusCombo      = new JComboBox<>(
            new String[]{"planned", "ongoing", "completed"});
        statusCombo.setFont(FONT_FIELD);
        statusCombo.setPreferredSize(new Dimension(0, 36));
        statusCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                switch (value.toString()) {
                    case "planned"   : setText("Direncanakan"); break;
                    case "ongoing"   : setText("Berlangsung");  break;
                    case "completed" : setText("Selesai");      break;
                }
                return this;
            }
        });

        dateField.setToolTipText("Format: YYYY-MM-DD  contoh: 2025-08-17");

        addRow(form, gc, 0, "Nama Kegiatan *",  nameField);
        addRow(form, gc, 1, "Deskripsi",        new JScrollPane(descriptionArea) {{
            setPreferredSize(new Dimension(0, 80));
            setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
        }});
        addRow(form, gc, 2, "Tanggal * (YYYY-MM-DD)", dateField);
        addRow(form, gc, 3, "Lokasi *",         locationField);
        addRow(form, gc, 4, "Jumlah Peserta",   participantField);
        addRow(form, gc, 5, "Status",            statusCombo);

        JScrollPane scroll = new JScrollPane(form);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        return scroll;
    }

    private void addRow(JPanel form, GridBagConstraints gc,
                        int row, String labelText, JComponent field) {
        gc.gridy = row * 2;
        JLabel label = new JLabel(labelText);
        label.setFont(FONT_LABEL);
        label.setForeground(new Color(70, 70, 70));
        gc.insets = new Insets(0, 0, 4, 0);
        form.add(label, gc);

        gc.gridy = row * 2 + 1;
        gc.insets = new Insets(0, 0, 14, 0);
        form.add(field, gc);
    }

    // ── Footer (buttons) ─────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        footer.setBackground(new Color(248, 250, 250));
        footer.setBorder(new MatteBorder(1, 0, 0, 0, new Color(220, 220, 220)));

        cancelButton = new JButton("Batal");
        cancelButton.setFont(FONT_BUTTON);
        cancelButton.setForeground(new Color(80, 80, 80));
        cancelButton.setBackground(Color.WHITE);
        cancelButton.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(8, 20, 8, 20)
        ));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        cancelButton.addActionListener(e -> dispose());

        saveButton = new JButton(isEditMode ? "Simpan Perubahan" : "Simpan");
        saveButton.setFont(FONT_BUTTON);
        saveButton.setForeground(Color.WHITE);
        saveButton.setBackground(COLOR_PRIMARY);
        saveButton.setBorderPainted(false);
        saveButton.setFocusPainted(false);
        saveButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        saveButton.setBorder(new EmptyBorder(8, 20, 8, 20));

        saveButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                saveButton.setBackground(new Color(0, 105, 105));
            }
            public void mouseExited(MouseEvent e) {
                saveButton.setBackground(COLOR_PRIMARY);
            }
        });

        footer.add(cancelButton);
        footer.add(saveButton);
        return footer;
    }

    // ── Field builders ───────────────────────────────────────────
    private JTextField buildTextField() {
        JTextField field = new JTextField();
        field.setFont(FONT_FIELD);
        field.setPreferredSize(new Dimension(0, 36));
        field.setBorder(new CompoundBorder(
            new LineBorder(new Color(200, 200, 200), 1, true),
            new EmptyBorder(4, 10, 4, 10)
        ));
        return field;
    }

    private JTextArea buildTextArea() {
        JTextArea area = new JTextArea(3, 0);
        area.setFont(FONT_FIELD);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setBorder(new EmptyBorder(6, 10, 6, 10));
        return area;
    }

    // ── Public API untuk ActivityController ──────────────────────

    public String getActivityName()    { return nameField.getText().trim(); }
    public String getDescription()     { return descriptionArea.getText().trim(); }
    public String getDate()            { return dateField.getText().trim(); }
    public String getActivityLocation()        { return locationField.getText().trim(); }
    public String getParticipantCount(){ return participantField.getText().trim(); }
    public String getStatus()          {
        return statusCombo.getSelectedItem().toString();
    }

    public void setActivityName(String v)     { nameField.setText(v); }
    public void setDescription(String v)      { descriptionArea.setText(v); }
    public void setDate(String v)             { dateField.setText(v); }
    public void setLocation(String v)         { locationField.setText(v); }
    public void setParticipantCount(String v) { participantField.setText(v); }
    public void setStatus(String v)           { statusCombo.setSelectedItem(v); }

    public void addSaveListener(ActionListener l) {
        saveButton.addActionListener(l);
    }

    public boolean validate(JLabel errorTarget) {
        if (getActivityName().isEmpty()) {
            if (errorTarget != null) errorTarget.setText("Nama kegiatan wajib diisi.");
            nameField.requestFocus();
            return false;
        }
        if (getDate().isEmpty()) {
            if (errorTarget != null) errorTarget.setText("Tanggal wajib diisi.");
            dateField.requestFocus();
            return false;
        }
        if (!getDate().matches("\\d{4}-\\d{2}-\\d{2}")) {
            if (errorTarget != null) errorTarget.setText("Format tanggal harus YYYY-MM-DD.");
            dateField.requestFocus();
            return false;
        }
        if (getActivityLocation().isEmpty()) {
            if (errorTarget != null) errorTarget.setText("Lokasi wajib diisi.");
            locationField.requestFocus();
            return false;
        }
        return true;
    }
}