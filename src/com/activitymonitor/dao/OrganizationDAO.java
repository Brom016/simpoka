package com.activitymonitor.dao;

//Dibuat oleh: muhamad rifki, hamid bromo
// Digunakan untuk mengimpor model Organization
import com.activitymonitor.model.Organization;
import com.activitymonitor.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// Kelas DAO untuk mengelola data organisasi di database
public class OrganizationDAO {

    //muhamad rifki, hamid bromo - enkapsulasi - mencari data berdasarkan ID
    // Mencari organisasi berdasarkan ID-nya
    public Organization findById(int id) {
        String sql = "SELECT * FROM organizations WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            // Jika ditemukan, mapping ke objek Organization
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("OrganizationDAO.findById: " + e.getMessage());
        }
        return null;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil semua data dari database
    // Mengambil semua data organisasi, diurutkan berdasarkan nama
    public List<Organization> findAll() {
        List<Organization> list = new ArrayList<>();
        String sql = "SELECT * FROM organizations ORDER BY name ASC";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            // Memetakan setiap baris hasil query ke objek Organization
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("OrganizationDAO.findAll: " + e.getMessage());
        }
        return list;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memetakan baris database ke objek
    // Memetakan satu baris ResultSet ke objek Organization
    private Organization mapRow(ResultSet rs) throws SQLException {
        return new Organization(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("leader"),
            rs.getString("period")
        );
    }
}
