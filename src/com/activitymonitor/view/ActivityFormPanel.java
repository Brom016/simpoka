package com.activitymonitor.view;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ActivityFormPanel extends JDialog {

    private JTextField        nameField;
    private JTextArea         descArea;
    private JTextField        dateField;
    private JTextField        locationField;
    private JTextField        participantField;
    private JComboBox<String> statusCombo;
    private JButton           saveBtn;
    private JButton           cancelBtn;
    private final boolean     editMode;

    private int     pickerYear;
    private int     pickerMonth;
    private JLabel  monthLabel;
    private JPanel  calGrid;
    private JDialog dateDialog;

    private static final String[] STATUS_KEYS   = {"planned","ongoing","completed"};
    private static final String[] STATUS_LABELS = {"Direncanakan","Berlangsung","Selesai"};

    public ActivityFormPanel(Frame parent, boolean editMode) {
        super(parent, true);
        this.editMode = editMode;
        Calendar now = Calendar.getInstance();
        pickerYear  = now.get(Calendar.YEAR);
        pickerMonth = now.get(Calendar.MONTH);
        initComponents();
        setupDialog();
    }

    private void setupDialog() {
        setTitle(editMode ? "Edit Kegiatan" : "Tambah Kegiatan");
        setSize(520, 530);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);
        root.add(buildHeader(),   BorderLayout.NORTH);
        root.add(buildFormBody(), BorderLayout.CENTER);
        root.add(buildFooter(),   BorderLayout.SOUTH);
        setContentPane(root);
    }

    // ── Header ────────────────────────────────────────────────────
    private JPanel buildHeader() {
        JPanel h = new JPanel(new BorderLayout());
        h.setBackground(Color.WHITE);
        h.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UIConstants.BORDER),
            new EmptyBorder(16, 24, 16, 24)
        ));
        JLabel title = new JLabel(editMode ? "Edit Kegiatan" : "Tambah Kegiatan");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIConstants.TEXT);
        h.add(title, BorderLayout.WEST);
        h.add(closeButton(), BorderLayout.EAST);
        return h;
    }

    // ── Form ──────────────────────────────────────────────────────
    private JScrollPane buildFormBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(20, 24, 4, 24));

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.weightx = 1.0;
        gc.weighty = 0;

        nameField        = styledField("Contoh: Seminar Teknologi");
        descArea         = styledTextArea("Masukkan deskripsi singkat kegiatan");
        dateField        = styledField("YYYY-MM-DD");
        locationField    = styledField("Gedung Serbaguna");
        participantField = styledField("0");
        statusCombo      = buildStatusCombo();

        fullRow(body, gc, 0, "Nama Kegiatan *", nameField);

        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1));
        descScroll.setPreferredSize(new Dimension(0, 80));
        fullRow(body, gc, 2, "Deskripsi", descScroll);

        twoColRow(body, gc, 4,
            "Tanggal *",      buildDateRow(),
            "Lokasi *",       locationField);

        twoColRow(body, gc, 6,
            "Jumlah Peserta", participantField,
            "Status",         statusCombo);

        gc.gridx = 0; gc.gridy = 8;
        gc.gridwidth = 2; gc.weighty = 1.0;
        gc.fill = GridBagConstraints.BOTH;
        body.add(Box.createVerticalGlue(), gc);

        JScrollPane scroll = new JScrollPane(body);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(Color.WHITE);
        SwingUtilities.invokeLater(() -> scroll.getVerticalScrollBar().setValue(0));
        return scroll;
    }

    private JPanel buildDateRow() {
        JPanel row = new JPanel(new BorderLayout(6, 0));
        row.setOpaque(false);
        dateField.setPreferredSize(new Dimension(0, 36));

        JButton calBtn = new JButton();
        calBtn.setPreferredSize(new Dimension(36, 36));
        calBtn.setBorder(new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1));
        calBtn.setBackground(Color.WHITE);
        calBtn.setFocusPainted(false);
        calBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        calBtn.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 6));
        calBtn.add(UIConstants.icon("doc", 14, UIConstants.PRIMARY));
        calBtn.addActionListener(e -> showDatePicker());

        row.add(dateField, BorderLayout.CENTER);
        row.add(calBtn,    BorderLayout.EAST);
        return row;
    }

    // ── Footer ────────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        cancelBtn = UIConstants.outlineButton("Cancel");
        saveBtn   = UIConstants.primaryButton("Save");
        cancelBtn.setPreferredSize(new Dimension(90, 36));
        saveBtn.setPreferredSize(new Dimension(90, 36));
        cancelBtn.addActionListener(e -> dispose());

        footer.add(cancelBtn);
        footer.add(saveBtn);
        return footer;
    }

    // ── Date picker ───────────────────────────────────────────────
    private void showDatePicker() {
        if (!dateField.getText().isEmpty()) {
            try {
                Calendar parsed = Calendar.getInstance();
                parsed.setTime(new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText()));
                pickerYear  = parsed.get(Calendar.YEAR);
                pickerMonth = parsed.get(Calendar.MONTH);
            } catch (Exception ignored) {}
        }

        dateDialog = new JDialog(this, "Pilih Tanggal", true);
        // Diperbesar: 360x340 agar tiap cell cukup lebar
        dateDialog.setSize(360, 340);
        dateDialog.setLocationRelativeTo(this);
        dateDialog.setResizable(false);

        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));

        // Nav row
        JPanel nav = new JPanel(new BorderLayout());
        nav.setBackground(Color.WHITE);

        JButton prev = navButton("<");
        JButton next = navButton(">");
        prev.addActionListener(e -> {
            pickerMonth--;
            if (pickerMonth < 0) { pickerMonth = 11; pickerYear--; }
            refreshCalGrid();
        });
        next.addActionListener(e -> {
            pickerMonth++;
            if (pickerMonth > 11) { pickerMonth = 0; pickerYear++; }
            refreshCalGrid();
        });

        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        monthLabel.setForeground(UIConstants.TEXT);

        nav.add(prev,       BorderLayout.WEST);
        nav.add(monthLabel, BorderLayout.CENTER);
        nav.add(next,       BorderLayout.EAST);

        // Grid: 0 gap — biarkan cell sebesar mungkin
        calGrid = new JPanel(new GridLayout(7, 7, 0, 0));
        calGrid.setBackground(Color.WHITE);

        String[] days = {"Min","Sen","Sel","Rab","Kam","Jum","Sab"};
        for (String d : days) {
            JLabel dl = new JLabel(d, SwingConstants.CENTER);
            dl.setFont(new Font("Segoe UI", Font.BOLD, 11));
            dl.setForeground(UIConstants.TEXT_MUTED);
            calGrid.add(dl);
        }
        for (int i = 0; i < 42; i++) calGrid.add(new JLabel());

        refreshCalGrid();

        panel.add(nav,     BorderLayout.NORTH);
        panel.add(calGrid, BorderLayout.CENTER);
        dateDialog.setContentPane(panel);
        dateDialog.setVisible(true);
    }

    private void refreshCalGrid() {
        // Update label bulan
        Calendar labelCal = Calendar.getInstance();
        labelCal.set(pickerYear, pickerMonth, 1);
        monthLabel.setText(new SimpleDateFormat("MMMM yyyy", new Locale("id", "ID"))
            .format(labelCal.getTime()));

        // Hapus 42 slot hari (pertahankan 7 header)
        while (calGrid.getComponentCount() > 7) {
            calGrid.remove(calGrid.getComponentCount() - 1);
        }

        // Kalender bulan ini — set ke tanggal 1 agar getActualMaximum akurat
        Calendar cal = Calendar.getInstance();
        cal.set(pickerYear, pickerMonth, 1);

        int startOffset = cal.get(Calendar.DAY_OF_WEEK) - 1; // 0=Sun
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Info hari ini
        Calendar today = Calendar.getInstance();
        int todayYear  = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay   = today.get(Calendar.DAY_OF_MONTH);

        // Sel kosong sebelum hari pertama
        for (int i = 0; i < startOffset; i++) {
            JLabel empty = new JLabel();
            calGrid.add(empty);
        }

        // Tombol hari
        for (int d = 1; d <= daysInMonth; d++) {
            final int day = d;

            // Pakai JLabel custom agar tidak ada minimum-size constraint dari JButton
            JLabel btn = new JLabel(String.valueOf(d), SwingConstants.CENTER) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                        RenderingHints.VALUE_ANTIALIAS_ON);
                    if (getBackground() != null && isOpaque()) {
                        g2.setColor(getBackground());
                        g2.fillRoundRect(2, 2, getWidth()-4, getHeight()-4, 8, 8);
                    }
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            btn.setOpaque(false);
            btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            boolean isToday = (d == todayDay
                && pickerMonth == todayMonth
                && pickerYear  == todayYear);

            if (isToday) {
                btn.setBackground(UIConstants.PRIMARY);
                btn.setForeground(Color.WHITE);
                btn.setOpaque(false); // paintComponent handles bg
            } else {
                btn.setBackground(null);
                btn.setForeground(UIConstants.TEXT);
            }

            btn.addMouseListener(new MouseAdapter() {
                final Color hoverBg = UIConstants.PRIMARY_LIGHT;
                @Override public void mouseEntered(MouseEvent e) {
                    if (!isToday) btn.setBackground(hoverBg);
                    btn.repaint();
                }
                @Override public void mouseExited(MouseEvent e) {
                    if (!isToday) btn.setBackground(null);
                    btn.repaint();
                }
                @Override public void mouseClicked(MouseEvent e) {
                    Calendar selected = Calendar.getInstance();
                    selected.set(pickerYear, pickerMonth, day);
                    dateField.setText(
                        new SimpleDateFormat("yyyy-MM-dd").format(selected.getTime()));
                    if (dateDialog != null) dateDialog.dispose();
                }
            });

            calGrid.add(btn);
        }

        // Isi sisa slot agar grid tetap 42 cell
        int filled    = startOffset + daysInMonth;
        int remaining = 42 - filled;
        for (int i = 0; i < remaining; i++) {
            calGrid.add(new JLabel());
        }

        calGrid.revalidate();
        calGrid.repaint();
    }

    // ── Layout helpers ────────────────────────────────────────────
    private void fullRow(JPanel p, GridBagConstraints gc,
                          int row, String label, JComponent field) {
        gc.gridx = 0; gc.gridy = row;
        gc.gridwidth = 2; gc.insets = new Insets(0, 0, 5, 0);
        gc.weighty = 0; gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(mkLabel(label), gc);
        gc.gridy = row + 1;
        gc.insets = new Insets(0, 0, 14, 0);
        p.add(field, gc);
        gc.gridwidth = 1;
    }

    private void twoColRow(JPanel p, GridBagConstraints gc, int row,
                            String lLabel, JComponent lField,
                            String rLabel, JComponent rField) {
        gc.gridwidth = 1;
        gc.gridx = 0; gc.gridy = row; gc.insets = new Insets(0, 0, 5, 8);
        p.add(mkLabel(lLabel), gc);
        gc.gridx = 1; gc.insets = new Insets(0, 8, 5, 0);
        p.add(mkLabel(rLabel), gc);
        gc.gridx = 0; gc.gridy = row + 1; gc.insets = new Insets(0, 0, 14, 8);
        p.add(lField, gc);
        gc.gridx = 1; gc.insets = new Insets(0, 8, 14, 0);
        p.add(rField, gc);
    }

    private JLabel mkLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(UIConstants.F_LABEL);
        l.setForeground(UIConstants.TEXT);
        return l;
    }

    private JTextField styledField(String placeholder) {
        JTextField f = new JTextField() {
            @Override
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

    private JTextArea styledTextArea(String placeholder) {
        JTextArea a = new JTextArea(3, 0) {
            @Override
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

    private JButton closeButton() {
        JButton btn = new JButton("\u00D7");
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        btn.setBorderPainted(false);
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setForeground(UIConstants.TEXT_MUTED);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> dispose());
        return btn;
    }

    private JButton navButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(Color.WHITE);
        btn.setBorder(new UIConstants.RoundedBorder(4, UIConstants.BORDER, 1));
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(32, 28));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
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
            if (STATUS_KEYS[i].equals(v)) { statusCombo.setSelectedIndex(i); return; }
        }
    }

    public void addSaveListener(ActionListener l) { saveBtn.addActionListener(l); }

    public boolean validateForm() {
        if (getActivityName().isEmpty())    { warn("Nama kegiatan wajib diisi."); return false; }
        if (getDate().isEmpty())             { warn("Tanggal wajib diisi.");        return false; }
        if (getActivityLocation().isEmpty()) { warn("Lokasi wajib diisi.");         return false; }
        return true;
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validasi", JOptionPane.WARNING_MESSAGE);
    }
}