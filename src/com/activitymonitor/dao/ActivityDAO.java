package com.activitymonitor.dao;

//Dibuat oleh: muhamad rifki, hamid bromo
// Digunakan untuk mengimpor model Activity
import com.activitymonitor.model.Activity;
import com.activitymonitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Kelas DAO untuk mengelola data kegiatan di database
public class ActivityDAO {

    //muhamad rifki, hamid bromo - enkapsulasi - menyimpan data baru ke database
    // Menyimpan data kegiatan baru ke database
    public boolean insert(Activity a) {
        String sql = "INSERT INTO activities "
                   + "(name, description, date, location, participant_count, status, created_by, organization_id) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        // Menggunakan try-with-resources untuk koneksi otomatis
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            // Mengisi parameter query dengan data kegiatan
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getDescription());
            stmt.setDate(3, a.getDate());
            stmt.setString(4, a.getLocation());
            stmt.setInt(5, a.getParticipantCount());
            stmt.setString(6, a.getStatus());
            stmt.setInt(7, a.getCreatedBy());
            stmt.setInt(8, a.getOrganizationId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { // Jika terjadi error database
            System.err.println("ActivityDAO.insert: " + e.getMessage());
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil semua data dari database
    // Mengambil semua kegiatan berdasarkan organisasi, diurutkan tanggal terbaru
    public List<Activity> findAll(int organizationId) {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activities WHERE organization_id = ? ORDER BY date DESC";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setInt(1, organizationId);
            ResultSet rs = stmt.executeQuery();
            // Memetakan setiap baris hasil query ke objek Activity
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ActivityDAO.findAll: " + e.getMessage());
        }
        return list;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil semua data dari database
    // Mengambil semua kegiatan tanpa filter organisasi
    public List<Activity> findAll() {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activities ORDER BY date DESC";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            // Memetakan setiap baris hasil query ke objek Activity
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ActivityDAO.findAllGlobal: " + e.getMessage());
        }
        return list;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mencari data berdasarkan ID
    // Mencari satu kegiatan berdasarkan ID-nya
    public Activity findById(int id) {
        String sql = "SELECT * FROM activities WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            // Jika ditemukan, mapping ke objek Activity
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("ActivityDAO.findById: " + e.getMessage());
        }
        return null;
    }

    //muhamad rifki, hamid bromo - interface (abstraksi) - mencari data berdasarkan kata kunci
    // Mencari kegiatan berdasarkan kata kunci pada nama, dalam satu organisasi
    public List<Activity> findByKeyword(String keyword, int organizationId) {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activities "
                   + "WHERE organization_id = ? AND name LIKE ? "
                   + "ORDER BY date DESC";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setInt(1, organizationId);
            stmt.setString(2, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            // Memetakan setiap baris hasil query ke objek Activity
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ActivityDAO.findByKeyword: " + e.getMessage());
        }
        return list;
    }

    //muhamad rifki, hamid bromo - interface (abstraksi) - mencari data berdasarkan kata kunci
    // Mencari kegiatan berdasarkan kata kunci pada nama, tanpa filter organisasi
    public List<Activity> findByKeyword(String keyword) {
        List<Activity> list = new ArrayList<>();
        String sql = "SELECT * FROM activities "
                   + "WHERE name LIKE ? "
                   + "ORDER BY date DESC";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();
            // Memetakan setiap baris hasil query ke objek Activity
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("ActivityDAO.findByKeywordGlobal: " + e.getMessage());
        }
        return list;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memperbarui data di database
    // Memperbarui data kegiatan berdasarkan ID
    public boolean update(Activity a) {
        String sql = "UPDATE activities SET name=?, description=?, date=?, location=?, "
                   + "participant_count=?, status=? WHERE id=?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            // Mengisi parameter dengan data kegiatan terbaru
            stmt.setString(1, a.getName());
            stmt.setString(2, a.getDescription());
            stmt.setDate(3, a.getDate());
            stmt.setString(4, a.getLocation());
            stmt.setInt(5, a.getParticipantCount());
            stmt.setString(6, a.getStatus());
            stmt.setInt(7, a.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActivityDAO.update: " + e.getMessage());
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memperbarui status kegiatan
    // Memperbarui status kegiatan (planned/ongoing/completed)
    public boolean updateStatus(int id, String status) {
        String sql = "UPDATE activities SET status = ? WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActivityDAO.updateStatus: " + e.getMessage());
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - menghapus data dari database
    // Menghapus kegiatan berdasarkan ID
    public boolean delete(int id) {
        String sql = "DELETE FROM activities WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("ActivityDAO.delete: " + e.getMessage());
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memetakan baris database ke objek
    // Memetakan satu baris ResultSet ke objek Activity
    private Activity mapRow(ResultSet rs) throws SQLException {
        return new Activity(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("description"),
            rs.getDate("date"),
            rs.getString("location"),
            rs.getInt("participant_count"),
            rs.getString("status"),
            rs.getInt("created_by"),
            rs.getInt("organization_id")
        );
    }
}

