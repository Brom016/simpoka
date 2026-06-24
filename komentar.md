# Pedoman Penulisan Komentar Kode

## Tujuan

Tambahkan komentar pada setiap class, atribut, method, dan logika penting agar kode mudah dipahami saat dibaca maupun dipresentasikan.

## Aturan Umum

1. Berikan komentar **1 baris** yang menjelaskan fungsi dari setiap bagian kode.
2. Jelaskan secara singkat:

   * Fungsinya untuk apa.
   * Digunakan atau ditujukan ke mana.
   * Mengambil data dari mana (jika ada).
   * Tujuan dari suatu kondisi (`if`, `switch`, `try-catch`, dan sebagainya) jika memang diperlukan.
3. Jangan membuat penjelasan panjang. Cukup ringkas, jelas, dan mudah dibaca.
4. Jika dalam satu class terdapat beberapa method atau atribut yang memiliki fungsi atau penjelasan yang sama, **jangan mengulang komentar yang sama**. Berikan komentar hanya pada kemunculan pertama atau jika memang ada perbedaan fungsi.
5. Jangan mengubah logika, nama variabel, format kode, maupun struktur program. Hanya tambahkan komentar.
6. Pertahankan komentar identitas pembuat yang sudah ada.

---

## Yang Harus Diberi Komentar

### 1. Package

Jelaskan fungsi package secara singkat.

```java
package com.activitymonitor.view;
// Package yang berisi tampilan (UI) aplikasi.
```

---

### 2. Import

Jelaskan kegunaan setiap import atau kelompok import.

```java
import java.awt.*;
// Digunakan untuk komponen tampilan seperti layout, warna, dan font.

import java.awt.event.*;
// Digunakan untuk menangani event dari komponen UI.

import javax.swing.*;
// Digunakan untuk membangun antarmuka menggunakan Swing.
```

---

### 3. Class

Jelaskan tujuan class.

```java
public class LoginFrame extends JFrame {
// Menampilkan halaman login sebagai tampilan awal aplikasi.
```

---

### 4. Atribut / Variabel

Jelaskan fungsi setiap atribut.

```java
private JTextField usernameField;
// Menyimpan input username dari pengguna.

private JPasswordField passwordField;
// Menyimpan input password secara aman.

private JButton loginButton;
// Tombol untuk memproses login.

private JLabel errorLabel;
// Menampilkan pesan kesalahan kepada pengguna.
```

---

### 5. Constructor

Jelaskan tujuan constructor.

```java
public LoginFrame() {
// Menginisialisasi seluruh komponen dan konfigurasi tampilan login.
```

---

### 6. Method

Jelaskan fungsi method secara singkat.

```java
private void setupFrame() {
// Mengatur konfigurasi utama jendela login.
```

Jika terdapat pemanggilan method lain, jelaskan bila diperlukan.

```java
getContentPane().setBackground(UIConstants.BG);
// Mengatur warna latar menggunakan konstanta BG dari kelas UIConstants.
```

---

### 7. Percabangan

Berikan komentar hanya jika logikanya perlu dijelaskan.

```java
if (user == null) {
// Jika pengguna tidak ditemukan, tampilkan pesan kesalahan.
}
```

```java
if (passwordValid) {
// Jika password sesuai, lanjut ke proses login.
}
```

---

### 8. Perulangan

Jelaskan tujuan perulangan.

```java
for (Activity a : activities) {
// Menampilkan seluruh data kegiatan ke tabel.
}
```

---

### 9. Pemanggilan Data

Jika mengambil data dari class, database, controller, atau model lain, jelaskan sumbernya.

```java
controller.login(username, password);
// Mengirim data login ke controller untuk proses autentikasi.
```

```java
organizationModel.getAllOrganizations();
// Mengambil seluruh data organisasi dari model.
```

---

### 10. Konstanta atau Class Lain

Jelaskan asal atau tujuan penggunaannya.

```java
UIConstants.BG
// Menggunakan warna latar yang telah didefinisikan pada kelas UIConstants.
```

---

## Hindari

Jangan membuat komentar seperti:

```java
// Method
// Variabel
// Tombol
// Login
```

Karena tidak memberikan informasi yang bermanfaat.

---

## Format Komentar

Gunakan komentar satu baris (`//`) dan letakkan tepat di atas atau di samping kode yang dijelaskan.

Contoh:

```java
// Menampilkan halaman login sebagai tampilan awal aplikasi.
public class LoginFrame extends JFrame {

    // Menyimpan input username dari pengguna.
    private JTextField usernameField;

    // Menyimpan input password secara aman.
    private JPasswordField passwordField;

    // Tombol untuk memproses login.
    private JButton loginButton;

    // Menampilkan pesan kesalahan kepada pengguna.
    private JLabel errorLabel;

    // Menginisialisasi komponen dan konfigurasi halaman login.
    public LoginFrame() {
        initComponents();
        setupFrame();
    }

    // Mengatur konfigurasi utama jendela login.
    private void setupFrame() {
        setTitle("SIMPOKA - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(460, 540);
        setLocationRelativeTo(null);
        setResizable(false);

        // Menggunakan warna latar dari konstanta UIConstants.
        getContentPane().setBackground(UIConstants.BG);
    }
}
```

---

## Prioritas

Fokus utama komentar adalah menjelaskan:

1. Fungsi kode.
2. Tujuan penggunaan.
3. Sumber data atau tujuan pemanggilan.
4. Alasan adanya percabangan atau kondisi (jika diperlukan).

Gunakan bahasa yang singkat dan bahasa biasa aja bahasa sehari hari gaul atau apalah, konsisten, dan mudah dipahami saat membaca kode maupun saat presentasi.
