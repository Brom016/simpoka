package com.activitymonitor.view;

//Dibuat oleh: katrina, hamid bromo
import java.awt.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.AbstractBorder;

public class UIConstants {

    public static final Color PRIMARY       = new Color(0, 150, 136);
    public static final Color PRIMARY_DARK  = new Color(0, 121, 107);
    public static final Color PRIMARY_LIGHT = new Color(224, 242, 241);
    public static final Color BG            = new Color(245, 247, 250);
    public static final Color CARD_BG       = Color.WHITE;
    public static final Color BORDER        = new Color(229, 231, 235);
    public static final Color TEXT          = new Color(17,  24,  39);
    public static final Color TEXT_MUTED    = new Color(107, 114, 128);
    public static final Color TEXT_LIGHT    = new Color(156, 163, 175);

    public static final Color BADGE_PLANNED_BG = new Color(219, 234, 254);
    public static final Color BADGE_PLANNED_FG = new Color(29,  78,  216);
    public static final Color BADGE_ONGOING_BG = new Color(254, 243, 199);
    public static final Color BADGE_ONGOING_FG = new Color(180,  83,   9);
    public static final Color BADGE_DONE_BG    = new Color(209, 250, 229);
    public static final Color BADGE_DONE_FG    = new Color(6,   95,  70);

    public static final Font F_BRAND    = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font F_TITLE    = new Font("Segoe UI", Font.BOLD, 20);
    public static final Font F_SUBTITLE = new Font("Segoe UI", Font.PLAIN, 12);
    public static final Font F_LABEL    = new Font("Segoe UI", Font.BOLD, 12);
    public static final Font F_BODY     = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font F_SMALL    = new Font("Segoe UI", Font.PLAIN, 11);
    public static final Font F_BTN      = new Font("Segoe UI", Font.BOLD, 13);


    //katrina, hamid bromo - enkapsulasi - membuat label ikon kustom

    public static JLabel icon(String type, int size, Color color) {
        return new JLabel() {
            @Override
            //katrina, hamid bromo - overriding (polimorfisme) - menggambar komponen kustom
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(color);
                int p = 2;
                int s = size - p * 2;
                g2.translate(p, p);
                switch (type) {
                    case "grid":   drawGrid(g2, s);   break;
                    case "list":   drawList(g2, s);   break;
                    case "doc":    drawDoc(g2, s);    break;
                    case "logout": drawLogout(g2, s); break;
                    case "search": drawSearch(g2, s); break;
                    case "user":   drawUser(g2, s);   break;
                    case "plus":   drawPlus(g2, s);   break;
                    case "dots":   drawDots(g2, s);   break;
                    case "pdf":    drawPdf(g2, s);    break;
                }
                g2.dispose();
            }

            @Override public Dimension getPreferredSize()  { return new Dimension(size, size); }
            @Override public Dimension getMinimumSize()    { return getPreferredSize(); }
            @Override public Dimension getMaximumSize()    { return getPreferredSize(); }
            @Override public boolean   isOpaque()          { return false; }
        };
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon grid
    private static void drawGrid(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int h = s / 2 - 1;
        g.drawRoundRect(0, 0, h, h, 3, 3);
        g.drawRoundRect(h + 2, 0, h, h, 3, 3);
        g.drawRoundRect(0, h + 2, h, h, 3, 3);
        g.drawRoundRect(h + 2, h + 2, h, h, 3, 3);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon list
    private static void drawList(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.fillOval(0, s / 4 - 1, 3, 3);
        g.drawLine(6, s / 4, s, s / 4);
        g.fillOval(0, s / 2 - 1, 3, 3);
        g.drawLine(6, s / 2, s, s / 2);
        g.fillOval(0, 3 * s / 4 - 1, 3, 3);
        g.drawLine(6, 3 * s / 4, s, 3 * s / 4);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon dokumen
    private static void drawDoc(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int fold = s / 4;
        int[] xs = {0, s - fold, s, 0};
        int[] ys = {0, 0, fold, 0};
        g.drawRoundRect(0, 0, s - 1, s - 1, 3, 3);
        g.drawLine(s - fold, 0, s - fold, fold);
        g.drawLine(s - fold, fold, s, fold);
        g.drawLine(3, s * 2 / 5, s - 3, s * 2 / 5);
        g.drawLine(3, s * 3 / 5, s - 3, s * 3 / 5);
        g.drawLine(3, s * 4 / 5, s * 3 / 5, s * 4 / 5);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon logout
    private static void drawLogout(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        // Door frame
        int d = s / 4;
        g.drawLine(d, 0, 0, 0);
        g.drawLine(0, 0, 0, s);
        g.drawLine(0, s, d, s);
        // Arrow
        g.drawLine(d + 1, s / 2, s, s / 2);
        g.drawLine(s - s / 3, s / 2 - s / 4, s, s / 2);
        g.drawLine(s - s / 3, s / 2 + s / 4, s, s / 2);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon search
    private static void drawSearch(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int r = s * 3 / 7;
        g.drawOval(0, 0, r * 2, r * 2);
        int lx = (int) (r + r * Math.cos(Math.PI / 4));
        int ly = (int) (r + r * Math.sin(Math.PI / 4));
        g.drawLine(lx, ly, s, s);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon user
    private static void drawUser(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        int cx = s / 2, cy = s / 3, r = s / 4;
        g.drawOval(cx - r, cy - r, r * 2, r * 2);
        g.drawArc(s / 8, s / 2, s * 3 / 4, s * 2 / 3, 0, 180);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon plus
    private static void drawPlus(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawLine(s / 2, 2, s / 2, s - 2);
        g.drawLine(2, s / 2, s - 2, s / 2);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon titik tiga
    private static void drawDots(Graphics2D g, int s) {
        int r = 2, cx = s / 2;
        g.fillOval(cx - r, s / 5 - r, r * 2, r * 2);
        g.fillOval(cx - r, s / 2 - r, r * 2, r * 2);
        g.fillOval(cx - r, s * 4 / 5 - r, r * 2, r * 2);
    }

    //katrina, hamid bromo - enkapsulasi - menggambar ikon PDF
    private static void drawPdf(Graphics2D g, int s) {
        g.setStroke(new BasicStroke(1.6f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g.drawRoundRect(1, 1, s - 2, s - 2, 4, 4);
        g.setFont(new Font("Segoe UI", Font.BOLD, s / 3));
        FontMetrics fm = g.getFontMetrics();
        String t = "PDF";
        g.drawString(t, (s - fm.stringWidth(t)) / 2,
            (s + fm.getAscent() - fm.getDescent()) / 2);
    }


    public static class RoundedBorder extends AbstractBorder {
        private final int radius, thickness;
        private final Color color;

        //katrina, hamid bromo - enkapsulasi - method constructorRoundedBorder
        public RoundedBorder(int radius, Color color, int thickness) {
            this.radius = radius; this.color = color; this.thickness = thickness;
        }

        @Override
        //katrina, hamid bromo - overriding (polimorfisme) - menggambar border kustom
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(new RoundRectangle2D.Double(
                x + 0.5, y + 0.5, w - 1, h - 1, radius, radius));
            g2.dispose();
        }

        @Override
        //katrina, hamid bromo - overriding (polimorfisme) - mengambil ukuran inset border
        public Insets getBorderInsets(Component c) {
            int i = thickness + 4; return new Insets(i, i, i, i);
        }
    }


    public static class RoundedPanel extends JPanel {
        private final int radius;
        //katrina, hamid bromo - enkapsulasi - method constructorRoundedPanel
        public RoundedPanel(int radius) {
            this.radius = radius; setOpaque(false);
        }

        @Override
        //katrina, hamid bromo - overriding (polimorfisme) - menggambar komponen kustom
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(new Color(0, 0, 0, 10));
            g2.fillRoundRect(2, 3, getWidth() - 3, getHeight() - 3, radius, radius);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 3, radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }
    }


    public static class StatusBadge extends JLabel {
        private Color bg = BORDER;

        //katrina, hamid bromo - enkapsulasi - method constructorStatusBadge
        public StatusBadge(String status) {
            setStatus(status);
            setHorizontalAlignment(CENTER);
            setFont(new Font("Segoe UI", Font.BOLD, 11));
            setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
            setOpaque(false);
        }

        //katrina, hamid bromo - enkapsulasi - mengubah tampilan status badge
        public void setStatus(String status) {
            switch (status != null ? status : "") {
                case "planned":
                    setText("Direncanakan");
                    setForeground(BADGE_PLANNED_FG);
                    bg = BADGE_PLANNED_BG;
                    break;
                case "ongoing":
                    setText("Berlangsung");
                    setForeground(BADGE_ONGOING_FG);
                    bg = BADGE_ONGOING_BG;
                    break;
                case "completed":
                    setText("Selesai");
                    setForeground(BADGE_DONE_FG);
                    bg = BADGE_DONE_BG;
                    break;
                default:
                    setText(status);
                    setForeground(TEXT_MUTED);
                    bg = BORDER;
            }
        }

        @Override
        //katrina, hamid bromo - overriding (polimorfisme) - menggambar komponen kustom
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
            g2.dispose();
            super.paintComponent(g);
        }
    }


    //katrina, hamid bromo - enkapsulasi - membuat tombol primary dengan style

    public static JButton primaryButton(String text) {
        return styledButton(text, PRIMARY, PRIMARY_DARK, Color.WHITE);
    }

    //katrina, hamid bromo - enkapsulasi - membuat tombol outline dengan style
    public static JButton outlineButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(F_BTN);
        btn.setForeground(TEXT);
        btn.setBackground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new RoundedBorder(6, BORDER, 1));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            //katrina, hamid bromo - interface (abstraksi) - menangani mouse masuk area
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(BG); }
            //katrina, hamid bromo - interface (abstraksi) - menangani mouse keluar area
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(Color.WHITE); }
        });
        return btn;
    }

    //katrina, hamid bromo - enkapsulasi - membuat tombol dengan style kustom
    private static JButton styledButton(String text, Color bg, Color hover, Color fg) {
        JButton btn = new JButton(text);
        btn.setFont(F_BTN);
        btn.setForeground(fg);
        btn.setBackground(bg);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(BorderFactory.createEmptyBorder(8, 18, 8, 18));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            //katrina, hamid bromo - interface (abstraksi) - menangani mouse masuk area
            public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(hover); }
            //katrina, hamid bromo - interface (abstraksi) - menangani mouse keluar area
            public void mouseExited(java.awt.event.MouseEvent e)  { btn.setBackground(bg); }
        });
        return btn;
    }
}
