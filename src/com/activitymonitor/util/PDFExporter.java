package com.activitymonitor.util;

import com.activitymonitor.model.Activity;
import com.activitymonitor.model.Organization;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PDFExporter {

    private static final BaseColor COLOR_PRIMARY  = new BaseColor(0, 128, 128);
    private static final BaseColor COLOR_HEADER   = new BaseColor(0, 95, 95);
    private static final BaseColor COLOR_ROW_ALT  = new BaseColor(240, 248, 248);
    private static final BaseColor COLOR_TEXT     = new BaseColor(40, 40, 40);

    private static final Font FONT_TITLE    = FontFactory.getFont(
        FontFactory.HELVETICA_BOLD, 18, COLOR_HEADER);
    private static final Font FONT_SUBTITLE = FontFactory.getFont(
        FontFactory.HELVETICA, 11, BaseColor.GRAY);
    private static final Font FONT_META     = FontFactory.getFont(
        FontFactory.HELVETICA, 10, COLOR_TEXT);
    private static final Font FONT_TH       = FontFactory.getFont(
        FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    private static final Font FONT_TD       = FontFactory.getFont(
        FontFactory.HELVETICA, 10, COLOR_TEXT);
    private static final Font FONT_FOOTER   = FontFactory.getFont(
        FontFactory.HELVETICA, 9, BaseColor.GRAY);

    public static boolean export(List<Activity> activities,
                                 Organization org, String outputPath) {
        try {
            // Buat folder output jika belum ada
            new java.io.File("output").mkdirs();

            Document doc = new Document(PageSize.A4.rotate(),
                36, 36, 36, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
            doc.open();

            addHeader(doc, org);
            addMeta(doc, org, activities.size());
            addTable(doc, activities);
            addFooter(doc);

            doc.close();
            return true;

        } catch (Exception e) {
            System.err.println("PDFExporter.export: " + e.getMessage());
            return false;
        }
    }

    // ── Header ───────────────────────────────────────────────────
    private static void addHeader(Document doc, Organization org)
            throws DocumentException {
        String orgName = org != null ? org.getName() : "Organisasi";

        Paragraph title = new Paragraph(orgName, FONT_TITLE);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setSpacingAfter(4);
        doc.add(title);

        Paragraph subtitle = new Paragraph(
            "Laporan Kegiatan Organisasi", FONT_SUBTITLE);
        subtitle.setAlignment(Element.ALIGN_CENTER);
        subtitle.setSpacingAfter(12);
        doc.add(subtitle);

        // Garis pemisah
        LineSeparator line = new LineSeparator(2, 100, COLOR_PRIMARY,
            Element.ALIGN_CENTER, -2);
        doc.add(new Chunk(line));
        doc.add(Chunk.NEWLINE);
    }

    // ── Meta info ─────────────────────────────────────────────────
    private static void addMeta(Document doc, Organization org, int total)
            throws DocumentException {
        String period  = org != null ? org.getPeriod() : "-";
        String leader  = org != null ? org.getLeader() : "-";
        String printed = new SimpleDateFormat("dd MMMM yyyy, HH:mm")
            .format(new Date());

        PdfPTable meta = new PdfPTable(3);
        meta.setWidthPercentage(100);
        meta.setSpacingBefore(10);
        meta.setSpacingAfter(14);
        meta.setWidths(new float[]{2f, 2f, 2f});

        meta.addCell(metaCell("Periode", period));
        meta.addCell(metaCell("Ketua", leader));
        meta.addCell(metaCell("Total Kegiatan", String.valueOf(total)));

        doc.add(meta);

        Paragraph printInfo = new Paragraph(
            "Dicetak pada: " + printed, FONT_FOOTER);
        printInfo.setAlignment(Element.ALIGN_RIGHT);
        printInfo.setSpacingAfter(8);
        doc.add(printInfo);
    }

    private static PdfPCell metaCell(String label, String value) {
        Paragraph p = new Paragraph();
        p.add(new Chunk(label + "\n",
            FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9, BaseColor.GRAY)));
        p.add(new Chunk(value,
            FontFactory.getFont(FontFactory.HELVETICA, 11, COLOR_TEXT)));
        PdfPCell cell = new PdfPCell(p);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(4);
        return cell;
    }

    // ── Data table ────────────────────────────────────────────────
    private static void addTable(Document doc, List<Activity> activities)
            throws DocumentException {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.5f, 2.5f, 2.5f, 1.2f, 1.5f, 0.8f, 1.2f});

        // Header row
        String[] headers = {"No", "Nama Kegiatan", "Deskripsi",
                            "Tanggal", "Lokasi", "Peserta", "Status"};
        for (String h : headers) {
            PdfPCell cell = new PdfPCell(new Phrase(h, FONT_TH));
            cell.setBackgroundColor(COLOR_HEADER);
            cell.setPadding(8);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setBorderColor(BaseColor.WHITE);
            table.addCell(cell);
        }

        // Data rows
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            boolean alt = i % 2 == 1;

            addCell(table, String.valueOf(i + 1), alt, Element.ALIGN_CENTER);
            addCell(table, a.getName(), alt, Element.ALIGN_LEFT);
            addCell(table, a.getDescription() != null
                ? a.getDescription() : "-", alt, Element.ALIGN_LEFT);
            addCell(table, a.getDate().toString(), alt, Element.ALIGN_CENTER);
            addCell(table, a.getLocation(), alt, Element.ALIGN_LEFT);
            addCell(table, String.valueOf(a.getParticipantCount()),
                alt, Element.ALIGN_CENTER);
            addCell(table, translateStatus(a.getStatus()),
                alt, Element.ALIGN_CENTER);
        }

        doc.add(table);
    }

    private static void addCell(PdfPTable table, String text,
                                 boolean alt, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TD));
        cell.setPadding(7);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setBackgroundColor(alt ? COLOR_ROW_ALT : BaseColor.WHITE);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }

    private static String translateStatus(String status) {
        switch (status) {
            case "planned"   : return "Direncanakan";
            case "ongoing"   : return "Berlangsung";
            case "completed" : return "Selesai";
            default          : return status;
        }
    }

    // ── Footer ───────────────────────────────────────────────────
    private static void addFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        LineSeparator line = new LineSeparator(1, 100, BaseColor.LIGHT_GRAY,
            Element.ALIGN_CENTER, -2);
        doc.add(new Chunk(line));

        Paragraph footer = new Paragraph(
            "\nDokumen ini digenerate secara otomatis oleh Activity Monitor.",
            FONT_FOOTER);
        footer.setAlignment(Element.ALIGN_CENTER);
        doc.add(footer);
    }
}