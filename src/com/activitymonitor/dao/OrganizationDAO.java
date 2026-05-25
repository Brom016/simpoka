package com.activitymonitor.dao;

import com.activitymonitor.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrganizationDAO {

    public static class OrganizationRow {
        private int id;
        private String name;
        private String leader;
        private String period;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLeader() {
            return leader;
        }

        public void setLeader(String leader) {
            this.leader = leader;
        }

        public String getPeriod() {
            return period;
        }

        public void setPeriod(String period) {
            this.period = period;
        }
    }

    public List<OrganizationRow> getAllOrganizations() {
        List<OrganizationRow> list = new ArrayList<>();
        String query = "SELECT id, name, leader, period FROM organizations ORDER BY id";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                OrganizationRow org = new OrganizationRow();
                org.setId(rs.getInt("id"));
                org.setName(rs.getString("name"));
                org.setLeader(rs.getString("leader"));
                org.setPeriod(rs.getString("period"));
                list.add(org);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    public OrganizationRow findById(int id) {
        String query = "SELECT id, name, leader, period FROM organizations WHERE id = ? LIMIT 1";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    OrganizationRow org = new OrganizationRow();
                    org.setId(rs.getInt("id"));
                    org.setName(rs.getString("name"));
                    org.setLeader(rs.getString("leader"));
                    org.setPeriod(rs.getString("period"));
                    return org;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
