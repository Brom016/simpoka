package com.activitymonitor.util;

//Dibuat oleh: ahmad irfaul, hamid bromo
// Digunakan untuk membaca file secara offline
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

// Kelas untuk mengelola koneksi database
public class DBConnection {

    // Menyimpan satu koneksi database (singleton)
    private static Connection connection = null;

    // URL koneksi database
    private static String DB_URL;
    // Username database
    private static String DB_USER;
    // Password database
    private static String DB_PASSWORD;

    // Blok statis dieksekusi saat class pertama kali dimuat
    static {
        loadConfig();
    }

    //ahmad irfaul, hamid bromo - enkapsulasi - memuat konfigurasi dari file properties
    // Memuat konfigurasi database dari file properties
    private static void loadConfig() {
        Properties props = new Properties();

        //coba baca dari file
        // Mencoba membaca file database.properties dari direktori project
        try (InputStream fileStream =
                new FileInputStream("database.properties")) {
            props.load(fileStream);

        } catch (IOException e1) {
            //fallback classpath
            // Jika tidak ditemukan, mencoba membaca dari classpath
            try (InputStream cpStream =
                    DBConnection.class.getClassLoader()
                        .getResourceAsStream("database.properties")) {
                if (cpStream != null) {
                    props.load(cpStream);
                } else {
                    System.err.println("[DBConnection] database.properties tidak ditemukan.");
                    System.err.println("Buat file database.properties di root folder project.");
                }
            } catch (IOException e2) {
                System.err.println("[DBConnection] Gagal membaca config: " + e2.getMessage());
            }
        }

        // Mengambil konfigurasi dengan nilai default
        DB_URL      = props.getProperty("DB_URL",      "jdbc:mysql://localhost:3306/activity_monitor");
        DB_USER     = props.getProperty("DB_USER",     "root");
        DB_PASSWORD = props.getProperty("DB_PASSWORD", "");
    }

    //ahmad irfaul, hamid bromo - enkapsulasi - mengambil koneksi database
    // Mendapatkan koneksi database (membuat jika belum ada)
    public static Connection getConnection() {
        // Jika koneksi belum dibuat, buat koneksi baru
        if (connection == null) {
            try {
                // Memuat driver MySQL JDBC
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                System.out.println("[DBConnection] Koneksi berhasil.");
            } catch (ClassNotFoundException e) {
                System.err.println("[DBConnection] Driver tidak ditemukan: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("[DBConnection] Koneksi gagal: " + e.getMessage());
            }
        }
        return connection;
    }

    //ahmad irfaul, hamid bromo - enkapsulasi - menutup koneksi database
    // Menutup koneksi database jika sedang terbuka
    public static void closeConnection() {
        // Jika koneksi masih terbuka, tutup koneksi
        if (connection != null) {
            try {
                connection.close();
                connection = null;
                System.out.println("[DBConnection] Koneksi ditutup.");
            } catch (SQLException e) {
                System.err.println("[DBConnection] Gagal menutup koneksi: " + e.getMessage());
            }
        }
    }
}
