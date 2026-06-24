package com.activitymonitor.util;

//Dibuat oleh: muhamad rifki, hamid bromo
// Digunakan untuk mengimpor model Activity
import com.activitymonitor.model.Activity;
// Digunakan untuk mengimpor model Organization
import com.activitymonitor.model.Organization;
// Digunakan untuk elemen-elemen PDF iText
import com.itextpdf.text.*;
// Digunakan untuk fitur PDF iText
import com.itextpdf.text.pdf.*;
// Digunakan untuk membuat garis pemisah di PDF
import com.itextpdf.text.pdf.draw.LineSeparator;

// Digunakan untuk menulis file output
import java.io.FileOutputStream;
// Digunakan untuk memformat tanggal
import java.text.SimpleDateFormat;
// Digunakan untuk mengambil tanggal saat ini
import java.util.Date;
// Digunakan untuk menyimpan daftar kegiatan
import java.util.List;

// Kelas untuk mengekspor data ke file PDF
public class PDFExporter {

    // Warna utama untuk aksen dan garis
    private static final BaseColor COLOR_PRIMARY  = new BaseColor(0, 128, 128);
    // Warna header tabel
    private static final BaseColor COLOR_HEADER   = new BaseColor(0, 95, 95);
    // Warna latar baris ganjil pada tabel
    private static final BaseColor COLOR_ROW_ALT  = new BaseColor(240, 248, 248);
    // Warna teks utama
    private static final BaseColor COLOR_TEXT     = new BaseColor(40, 40, 40);

    // Font untuk judul laporan
    private static final Font FONT_TITLE    = FontFactory.getFont(
        FontFactory.HELVETICA_BOLD, 18, COLOR_HEADER);
    // Font untuk subjudul laporan
    private static final Font FONT_SUBTITLE = FontFactory.getFont(
        FontFactory.HELVETICA, 11, BaseColor.GRAY);
    // Font untuk informasi meta
    private static final Font FONT_META     = FontFactory.getFont(
        FontFactory.HELVETICA, 10, COLOR_TEXT);
    // Font untuk header tabel
    private static final Font FONT_TH       = FontFactory.getFont(
        FontFactory.HELVETICA_BOLD, 10, BaseColor.WHITE);
    // Font untuk isi tabel
    private static final Font FONT_TD       = FontFactory.getFont(
        FontFactory.HELVETICA, 10, COLOR_TEXT);
    // Font untuk footer dokumen
    private static final Font FONT_FOOTER   = FontFactory.getFont(
        FontFactory.HELVETICA, 9, BaseColor.GRAY);

    //muhamad rifki, hamid bromo - enkapsulasi - mengekspor data ke file PDF
    // Mengekspor daftar kegiatan ke file PDF
    public static boolean export(List<Activity> activities,
                                 Organization org, String outputPath) {
        try {
            //buat folder output
            // Membuat folder output jika belum ada
            new java.io.File("output").mkdirs();

            // Membuat dokumen PDF dengan orientasi landscape
            Document doc = new Document(PageSize.A4.rotate(),
                36, 36, 36, 36);
            PdfWriter.getInstance(doc, new FileOutputStream(outputPath));
            doc.open();

            // Menambahkan komponen-komponen laporan
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


    //muhamad rifki, hamid bromo - enkapsulasi - menambahkan header ke dokumen PDF
    // Menambahkan header laporan (judul organisasi dan subjudul)
    private static void addHeader(Document doc, Organization org)
            throws DocumentException {
        // Mengambil nama organisasi, default "Organisasi" jika null
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

        //garis pemisah
        // Menambahkan garis pemisah antara header dan konten
        LineSeparator line = new LineSeparator(2, 100, COLOR_PRIMARY,
            Element.ALIGN_CENTER, -2);
        doc.add(new Chunk(line));
        doc.add(Chunk.NEWLINE);
    }


    //muhamad rifki, hamid bromo - enkapsulasi - menambahkan meta info ke PDF
    // Menambahkan informasi meta (periode, ketua, total kegiatan)
    private static void addMeta(Document doc, Organization org, int total)
            throws DocumentException {
        // Mengambil data organisasi, default "-" jika null
        String period  = org != null ? org.getPeriod() : "-";
        String leader  = org != null ? org.getLeader() : "-";
        // Format tanggal cetak
        String printed = new SimpleDateFormat("dd MMMM yyyy, HH:mm")
            .format(new Date());

        // Membuat tabel meta dengan 3 kolom
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

    //muhamad rifki, hamid bromo - enkapsulasi - membuat cell meta info PDF
    // Membuat cell untuk tabel meta info (label + nilai)
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


    //muhamad rifki, hamid bromo - enkapsulasi - menambahkan tabel data ke PDF
    // Menambahkan tabel berisi daftar kegiatan ke PDF
    private static void addTable(Document doc, List<Activity> activities)
            throws DocumentException {
        // Membuat tabel dengan 7 kolom (No, Nama, Deskripsi, Tanggal, Lokasi, Peserta, Status)
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{0.5f, 2.5f, 2.5f, 1.2f, 1.5f, 0.8f, 1.2f});

        //header row
        // Menambahkan baris header tabel
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
        // Mengiterasi setiap kegiatan untuk ditampilkan sebagai baris
        for (int i = 0; i < activities.size(); i++) {
            Activity a = activities.get(i);
            // Warna latar bergantian untuk setiap baris
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

    //muhamad rifki, hamid bromo - enkapsulasi - menambahkan cell ke tabel PDF
    // Menambahkan satu cell ke tabel dengan styling yang sesuai
    private static void addCell(PdfPTable table, String text,
                                 boolean alt, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FONT_TD));
        cell.setPadding(7);
        cell.setHorizontalAlignment(align);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        // Mengatur warna latar bergantian untuk setiap baris
        cell.setBackgroundColor(alt ? COLOR_ROW_ALT : BaseColor.WHITE);
        cell.setBorderColor(new BaseColor(220, 220, 220));
        table.addCell(cell);
    }

    //muhamad rifki, hamid bromo - enkapsulasi - menerjemahkan status ke Bahasa Indonesia
    // Menerjemahkan status kegiatan ke Bahasa Indonesia
    private static String translateStatus(String status) {
        switch (status) {
            case "planned"   : return "Direncanakan";
            case "ongoing"   : return "Berlangsung";
            case "completed" : return "Selesai";
            default          : return status;
        }
    }


    //muhamad rifki, hamid bromo - enkapsulasi - menambahkan footer ke dokumen PDF
    // Menambahkan footer ke dokumen PDF
    private static void addFooter(Document doc) throws DocumentException {
        doc.add(Chunk.NEWLINE);
        // Garis pemisah footer
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
