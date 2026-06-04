package com.activitymonitor.util;

import com.activitymonitor.dao.ActivityDAO;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * Utility ekspor "PDF".
 *
 * Penting: project ini belum memiliki dependency PDFBox (package
 * org.apache.pdfbox.* tidak ditemukan
 * dari jar yang ada di lib/). Supaya project tetap bisa compile, implementasi
 * saat ini
 * menggunakan fallback: menulis konten teks dengan ekstensi .pdf.
 *
 * Jika Anda menambahkan PDFBox yang benar ke lib/, implementasi ini bisa
 * diganti menjadi exporter PDF beneran.
 */
public class PDFExporter {

    /**
     * Fallback export: menulis konten ke file (nama bebas, tetapi ekstensi
     * diarahkan ke .pdf).
     */
    public static void exportActivitiesToPDF(List<ActivityDAO> activities, String outPath) throws IOException {
        if (activities == null) {
            throw new IllegalArgumentException("activities tidak boleh null");
        }
        if (outPath == null || outPath.trim().isEmpty()) {
            throw new IllegalArgumentException("outPath tidak boleh kosong");
        }

        Path output = Path.of(outPath);
        Path parent = output.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }

        try (BufferedWriter w = Files.newBufferedWriter(output)) {
            w.write("SIMPOKA - Daftar Kegiatan");
            w.newLine();
            w.write("============================================================");
            w.newLine();

            for (ActivityDAO a : activities) {
                w.write("ID=" + a.getId());
                w.write(" | Nama=" + safe(a.getNamaKegiatan()));
                w.write(" | Tanggal=" + (a.getTanggal() == null ? "" : a.getTanggal().toString()));
                w.write(" | Lokasi=" + safe(a.getLokasi()));
                w.write(" | Peserta=" + a.getJumlahPeserta());
                w.write(" | Status=" + safe(a.getStatus()));
                w.newLine();
            }
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }
}
