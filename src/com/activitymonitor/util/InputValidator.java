package com.activitymonitor.util;

//Dibuat oleh: muhamad rifki, hamid bromo
// Kelas untuk memvalidasi input dari pengguna
public class InputValidator {

    //muhamad rifki, hamid bromo - enkapsulasi - memeriksa apakah nilai kosong
    // Memeriksa apakah string kosong atau null
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memvalidasi format tanggal
    // Memvalidasi format tanggal YYYY-MM-DD
    public static boolean isValidDate(String value) {
        return value != null && value.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memvalidasi angka positif
    // Memvalidasi apakah string berisi angka positif
    public static boolean isPositiveInteger(String value) {
        // Jika kosong, dianggap valid (boleh kosong)
        if (isEmpty(value)) return true;
        try {
            return Integer.parseInt(value) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memvalidasi input kegiatan
    // Memvalidasi seluruh input kegiatan sekaligus, mengembalikan pesan error jika ada
    public static String validateActivity(String name, String date,
                                          String location, String participant) {
        // Validasi nama kegiatan harus diisi
        if (isEmpty(name))
            return "Nama kegiatan wajib diisi.";
        // Validasi tanggal harus diisi
        if (isEmpty(date))
            return "Tanggal wajib diisi.";
        // Validasi format tanggal harus YYYY-MM-DD
        if (!isValidDate(date))
            return "Format tanggal harus YYYY-MM-DD. Contoh: 2025-08-17";
        // Validasi lokasi harus diisi
        if (isEmpty(location))
            return "Lokasi wajib diisi.";
        // Validasi jumlah peserta harus angka positif
        if (!isPositiveInteger(participant))
            return "Jumlah peserta harus berupa angka positif.";
        return null; // null = valid
    }
}
