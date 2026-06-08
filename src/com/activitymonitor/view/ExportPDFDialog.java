package com.activitymonitor.view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ExportPDFDialog extends JDialog {

    private JTextField fileNameField;
    private JButton    exportBtn;
    private JButton    cancelBtn;

    public ExportPDFDialog(Frame parent) {
        super(parent, true);
        initComponents();
        setupDialog();
    }

    private void setupDialog() {
        setTitle("Ekspor PDF");
        setSize(380, 320);
        setLocationRelativeTo(getParent());
        setResizable(false);
    }

    private void initComponents() {
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(Color.WHITE);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Color.WHITE);
        header.setBorder(new CompoundBorder(
            new MatteBorder(0, 0, 1, 0, UIConstants.BORDER),
            new EmptyBorder(14, 20, 14, 20)
        ));
        JLabel title = new JLabel("Ekspor Laporan PDF");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));
        title.setForeground(UIConstants.TEXT);

        JButton close = new JButton("\u00D7");
        close.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        close.setBorderPainted(false);
        close.setContentAreaFilled(false);
        close.setFocusPainted(false);
        close.setForeground(UIConstants.TEXT_MUTED);
        close.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        close.addActionListener(e -> dispose());

        header.add(title, BorderLayout.WEST);
        header.add(close, BorderLayout.EAST);

        // Body
        JPanel body = new JPanel();
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
        body.setBackground(Color.WHITE);
        body.setBorder(new EmptyBorder(24, 24, 12, 24));

        // PDF icon (drawn)
        JPanel iconWrap = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconWrap.setOpaque(false);
        JLabel icon = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                // Circle bg
                g2.setColor(new Color(254, 226, 226));
                g2.fillOval(0, 0, 52, 52);
                // Doc icon
                g2.setColor(new Color(220, 60, 60));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 22));
                FontMetrics fm = g2.getFontMetrics();
                String s = "\uD83D\uDCC4";
                g2.drawString(s, (52 - fm.stringWidth(s)) / 2, 34);
                g2.dispose();
            }
        };
        icon.setPreferredSize(new Dimension(52, 52));
        iconWrap.add(icon);

        JLabel desc = new JLabel("Laporan akan disimpan ke folder output/");
        desc.setFont(UIConstants.F_SMALL);
        desc.setForeground(UIConstants.TEXT_MUTED);
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel fileLabel = new JLabel("Nama File");
        fileLabel.setFont(UIConstants.F_LABEL);
        fileLabel.setForeground(UIConstants.TEXT);
        fileLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        fileNameField = new JTextField("laporan-kegiatan.pdf");
        fileNameField.setFont(UIConstants.F_BODY);
        fileNameField.setBorder(new CompoundBorder(
            new UIConstants.RoundedBorder(6, UIConstants.BORDER, 1),
            new EmptyBorder(6, 10, 6, 10)
        ));
        fileNameField.setBackground(Color.WHITE);
        fileNameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
        fileNameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        body.add(iconWrap);
        body.add(Box.createVerticalStrut(8));
        body.add(desc);
        body.add(Box.createVerticalStrut(20));
        body.add(fileLabel);
        body.add(Box.createVerticalStrut(6));
        body.add(fileNameField);

        // Footer
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 14));
        footer.setBackground(Color.WHITE);
        footer.setBorder(new MatteBorder(1, 0, 0, 0, UIConstants.BORDER));

        cancelBtn = UIConstants.outlineButton("Cancel");
        cancelBtn.setPreferredSize(new Dimension(90, 36));
        cancelBtn.addActionListener(e -> dispose());

        exportBtn = UIConstants.primaryButton("Export");
        exportBtn.setPreferredSize(new Dimension(90, 36));

        footer.add(cancelBtn);
        footer.add(exportBtn);

        root.add(header, BorderLayout.NORTH);
        root.add(body,   BorderLayout.CENTER);
        root.add(footer, BorderLayout.SOUTH);

        setContentPane(root);
    }

    public String getFileName()  { return fileNameField.getText().trim(); }
    public void addExportListener(ActionListener l) { exportBtn.addActionListener(l); }
}