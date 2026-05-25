package com.activitymonitor.dao;

import com.activitymonitor.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ActivityDAO {

    // DAO + model ringan agar bisa diuji langsung di package dao.
    // (Mengikuti format setter/getter yang dipakai oleh method CRUD di file ini.)
    private int id;
    private String namaKegiatan;
    private String deskripsi;
    private java.sql.Date tanggal;
    private String lokasi;
    private int jumlahPeserta;
    private String status;
    private int createdBy;
    private int organizationId;

    public ActivityDAO() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNamaKegiatan() {
        return namaKegiatan;
    }

    public void setNamaKegiatan(String namaKegiatan) {
        this.namaKegiatan = namaKegiatan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public java.sql.Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(java.sql.Date tanggal) {
        this.tanggal = tanggal;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public int getJumlahPeserta() {
        return jumlahPeserta;
    }

    public void setJumlahPeserta(int jumlahPeserta) {
        this.jumlahPeserta = jumlahPeserta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public int getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    // Helper untuk mengubah status dari Database (Inggris) ke UI (Indonesia)
    private String mapStatusToUI(String dbStatus) {
        switch (dbStatus) {
            case "planned":
                return "Direncanakan";
            case "ongoing":
                return "Berlangsung";
            case "completed":
                return "Selesai";
            default:
                return "Direncanakan";
        }
    }

    // Helper untuk mengubah status dari UI (Indonesia) ke Database (Inggris)
    private String mapStatusToDB(String uiStatus) {
        switch (uiStatus) {
            case "Direncanakan":
                return "planned";
            case "Berlangsung":
                return "ongoing";
            case "Selesai":
                return "completed";
            default:
                return "planned";
        }
    }

    // 1. Ambil Semua Data Kegiatan (Menampilkan data di tabel utama)
    public List<ActivityDAO> getAllActivities() {
        List<ActivityDAO> list = new ArrayList<>();
        String query = "SELECT * FROM activities ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ActivityDAO activity = new ActivityDAO();
                activity.setId(rs.getInt("id"));
                activity.setNamaKegiatan(rs.getString("name")); // Sesuai kolom 'name'
                activity.setDeskripsi(rs.getString("description"));
                activity.setTanggal(rs.getDate("date"));
                activity.setLokasi(rs.getString("location"));
                activity.setJumlahPeserta(rs.getInt("participant_count")); // Sesuai kolom 'participant_count'

                // Konversi status agar sesuai tampilan UI temanmu
                activity.setStatus(mapStatusToUI(rs.getString("status")));

                // Menyimpan ID relasi jika sewaktu-waktu dibutuhkan controller
                activity.setCreatedBy(rs.getInt("created_by"));
                activity.setOrganizationId(rs.getInt("organization_id"));

                list.add(activity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Tambah Kegiatan Baru
    // Catatan: Pastikan object 'activity' sudah diset 'createdBy' dan
    // 'organizationId' dari session user yang login
    public boolean insertActivity(ActivityDAO activity) {
        String query = "INSERT INTO activities (name, description, date, location, participant_count, status, created_by, organization_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, activity.getNamaKegiatan());
            ps.setString(2, activity.getDeskripsi());
            ps.setDate(3, new java.sql.Date(activity.getTanggal().getTime()));
            ps.setString(4, activity.getLokasi());
            ps.setInt(5, activity.getJumlahPeserta());
            ps.setString(6, mapStatusToDB(activity.getStatus())); // Konversi ke Enum DB ('planned', dll)
            ps.setInt(7, activity.getCreatedBy()); // ID user yang sedang login
            ps.setInt(8, activity.getOrganizationId()); // ID organisasi user

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Update Data Kegiatan
    public boolean updateActivity(ActivityDAO activity) {
        String query = "UPDATE activities SET name = ?, description = ?, date = ?, location = ?, participant_count = ?, status = ? WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, activity.getNamaKegiatan());
            ps.setString(2, activity.getDeskripsi());
            ps.setDate(3, new java.sql.Date(activity.getTanggal().getTime()));
            ps.setString(4, activity.getLokasi());
            ps.setInt(5, activity.getJumlahPeserta());
            ps.setString(6, mapStatusToDB(activity.getStatus()));
            ps.setInt(7, activity.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 4. Hapus Kegiatan
    public boolean deleteActivity(int id) {
        String query = "DELETE FROM activities WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 5. Cari Kegiatan berdasarkan Nama atau Lokasi
    public List<ActivityDAO> searchActivities(String keyword) {
        List<ActivityDAO> list = new ArrayList<>();
        String query = "SELECT * FROM activities WHERE name LIKE ? OR location LIKE ? ORDER BY date DESC";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            String searchPattern = "%" + keyword + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ActivityDAO activity = new ActivityDAO();
                    activity.setId(rs.getInt("id"));
                    activity.setNamaKegiatan(rs.getString("name"));
                    activity.setDeskripsi(rs.getString("description"));
                    activity.setTanggal(rs.getDate("date"));
                    activity.setLokasi(rs.getString("location"));
                    activity.setJumlahPeserta(rs.getInt("participant_count"));
                    activity.setStatus(mapStatusToUI(rs.getString("status")));
                    activity.setCreatedBy(rs.getInt("created_by"));
                    activity.setOrganizationId(rs.getInt("organization_id"));

                    list.add(activity);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}