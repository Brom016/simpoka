package com.activitymonitor.dao;

import com.activitymonitor.model.Organization;
import com.activitymonitor.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {

    public Organization findById(int id) {
        String sql = "SELECT * FROM organizations WHERE id = ?";
        try (PreparedStatement stmt = DBConnection.getConnection()
                .prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("OrganizationDAO.findById: " + e.getMessage());
        }
        return null;
    }

    public List<Organization> findAll() {
        List<Organization> list = new ArrayList<>();
        String sql = "SELECT * FROM organizations ORDER BY name ASC";
        try (Statement stmt = DBConnection.getConnection().createStatement();
             ResultSet rs   = stmt.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("OrganizationDAO.findAll: " + e.getMessage());
        }
        return list;
    }

    private Organization mapRow(ResultSet rs) throws SQLException {
        return new Organization(
            rs.getInt("id"),
            rs.getString("name"),
            rs.getString("leader"),
            rs.getString("period")
        );
    }
}