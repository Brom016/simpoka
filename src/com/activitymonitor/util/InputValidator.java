package com.activitymonitor.util;

//Dibuat oleh: muhamad rifki, hamid bromo
public class InputValidator {

    //muhamad rifki, hamid bromo - enkapsulasi - memeriksa apakah nilai kosong
    public static boolean isEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memvalidasi format tanggal
    public static boolean isValidDate(String value) {
        return value != null && value.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memvalidasi angka positif
    public static boolean isPositiveInteger(String value) {
        if (isEmpty(value)) return true; // boleh kosong
        try {
            return Integer.parseInt(value) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memvalidasi input kegiatan
    public static String validateActivity(String name, String date,
                                          String location, String participant) {
        if (isEmpty(name))
            return "Nama kegiatan wajib diisi.";
        if (isEmpty(date))
            return "Tanggal wajib diisi.";
        if (!isValidDate(date))
            return "Format tanggal harus YYYY-MM-DD. Contoh: 2025-08-17";
        if (isEmpty(location))
            return "Lokasi wajib diisi.";
        if (!isPositiveInteger(participant))
            return "Jumlah peserta harus berupa angka positif.";
        return null; // null = valid
    }
}
