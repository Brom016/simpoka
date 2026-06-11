package com.activitymonitor.dao;

//Dibuat oleh: muhamad rifki, hamid bromo
import com.activitymonitor.model.User;
import com.activitymonitor.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    // Authenticate login - returns User if valid, null if not
    //muhamad rifki, hamid bromo - enkapsulasi - memverifikasi kredensial login user
    public User authenticate(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        } catch (SQLException e) {
            System.err.println("UserDAO.authenticate: " + e.getMessage());
        }
        return null;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - mengambil semua data dari database
    public List<User> findAll() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users ORDER BY full_name ASC";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("UserDAO.findAll: " + e.getMessage());
        }
        return list;
    }

    //muhamad rifki, hamid bromo - enkapsulasi - menyimpan data baru ke database
    public boolean insert(User user) {
        String sql = "INSERT INTO users (full_name, username, password, role, organization_id) "
                   + "VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, user.getPassword());
            stmt.setString(4, user.getRole());
            stmt.setInt(5, user.getOrganizationId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("UserDAO.insert: " + e.getMessage());
            return false;
        }
    }

    //muhamad rifki, hamid bromo - enkapsulasi - memetakan baris database ke objek
    private User mapRow(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("full_name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("role"),
            rs.getInt("organization_id")
        );
    }
}
