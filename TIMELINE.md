# Timeline Pengembangan — Sistem Monitoring Kegiatan UKM

## Informasi Proyek

| | |
|---|---|
| **Nama Sistem** | Sistem Monitoring & Pelaporan Kegiatan UKM |
| **Platform** | Aplikasi Desktop (Java Swing) |
| **Durasi** | 7 Minggu |
| **Tim** | Kelompok 7 |

---

## Minggu 1 — Setup & Database

**Fokus:** Persiapan lingkungan pengembangan dan pembuatan database

**Tugas:**
- Install tools: JDK, VS Code + Java Extension Pack, XAMPP
- Buat database MySQL dan 3 tabel: `organisasi`, `users`, `kegiatan`
- Konfigurasi koneksi Java ke MySQL via JDBC
- Uji koneksi berhasil dari aplikasi

**Output:** Aplikasi berhasil terhubung ke database


---

## Minggu 2 — Create (Tambah Data)

**Fokus:** Form input dan penyimpanan data kegiatan

**Tugas:**
- Buat form input: nama kegiatan, deskripsi, tanggal, tempat, jumlah peserta, status
- Implementasi query `INSERT INTO kegiatan`
- Validasi form — semua field wajib diisi sebelum disimpan

**Output:** Data kegiatan berhasil disimpan ke database


---

## Minggu 3 — Read (Tampil Data)

**Fokus:** Menampilkan data kegiatan di antarmuka

**Tugas:**
- Implementasi `JTable` + `JScrollPane`
- Query `SELECT * FROM kegiatan`
- Data dari database tampil otomatis saat aplikasi dibuka

**Output:** Daftar kegiatan tampil di tabel aplikasi


---

## Minggu 4 — Update & Delete

**Fokus:** Edit dan hapus data kegiatan

**Tugas:**
- Fungsi pilih baris di tabel → data masuk ke form
- Query `UPDATE kegiatan` untuk menyimpan perubahan
- Query `DELETE FROM kegiatan` dengan dialog konfirmasi (`JOptionPane`)

**Output:** Operasi CRUD lengkap berjalan


---

## Minggu 5 — Pencarian & Ekspor PDF

**Fokus:** Fitur pencarian data dan pembuatan laporan PDF

**Tugas:**
- Input field pencarian + query `SELECT WHERE nama_kegiatan LIKE`
- Implementasi iText/PDFBox untuk generate laporan
- Format PDF: header organisasi, tabel kegiatan, footer tanggal cetak + kolom tanda tangan

**Output:** Fitur pencarian berjalan + file PDF berhasil dibuat


---

## Minggu 6 — Testing & Debug

**Fokus:** Uji seluruh fitur dan perbaikan bug

**Tugas:**
- Uji semua skenario: login gagal, form kosong, hapus, ekspor PDF
- Perbaiki bug yang ditemukan
- Pastikan tidak ada crash atau error tak tertangani

**Output:** Aplikasi stabil dan berjalan tanpa error


---

## Minggu 7 — Finishing & Presentasi

**Fokus:** Perapian tampilan dan persiapan demo

**Tugas:**
- Rapikan layout UI
- Isi data dummy yang representatif untuk demo
- Latihan alur demo dari login hingga ekspor PDF
- Siapkan jawaban untuk pertanyaan teknis

**Output:** Sistem siap dipresentasikan


---

## Ringkasan Timeline

| Minggu | Fokus | Output |
|--------|-------|--------|
| 1 | Setup & Database | Koneksi DB berhasil |
| 2 | Create | Data tersimpan ke DB |
| 3 | Read | Data tampil di tabel |
| 4 | Update & Delete | CRUD lengkap |
| 5 | Pencarian + PDF | Search + laporan PDF |
| 6 | Testing & Debug | Aplikasi stabil |
| 7 | Finishing | Siap presentasi |

---

*Kelompok 7 — Sistem Monitoring & Pelaporan Kegiatan UKM*
