package com.activitymonitor;

import com.activitymonitor.dao.UserDAO;
import com.activitymonitor.dao.OrganizationDAO;
import com.activitymonitor.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();

                // Uji baca tabel organizations
                ResultSet rs = stmt.executeQuery("SELECT * FROM organizations");
                System.out.println("\n--- Daftar Organisasi ---");
                while (rs.next()) {
                    System.out.println(
                            rs.getInt("id") + " | " +
                                    rs.getString("name") + " | " +
                                    rs.getString("leader"));
                }

                // Uji baca tabel users
                rs = stmt.executeQuery("SELECT * FROM users");
                System.out.println("\n--- Daftar User ---");
                while (rs.next()) {
                    System.out.println(
                            rs.getString("username") + " | " +
                                    rs.getString("role"));
                }
                rs.close();
                stmt.close();

            } catch (Exception e) {
                System.err.println("Query gagal: " + e.getMessage());
            }
        }

        // ===== TEST OrganizationDAO =====
        try {
            OrganizationDAO dao = new OrganizationDAO();

            System.out.println("\n--- OrganizationDAO: getAllOrganizations() ---");
            java.util.List<OrganizationDAO.OrganizationRow> all = dao.getAllOrganizations();
            System.out.println("Jumlah: " + all.size());
            for (int i = 0; i < Math.min(all.size(), 5); i++) {
                OrganizationDAO.OrganizationRow org = all.get(i);
                System.out.println(
                        org.getId() + " | " + org.getName() + " | " + org.getLeader() + " | " + org.getPeriod());
            }

            System.out.println("\n--- OrganizationDAO: findById(1) ---");
            OrganizationDAO.OrganizationRow one = dao.findById(1);
            if (one != null) {
                System.out.println(
                        one.getId() + " | " + one.getName() + " | " + one.getLeader() + " | " + one.getPeriod());
            } else {
                System.out.println("Tidak ditemukan");
            }

            System.out.println("\n--- UserDAO: findById(1) ---");
            UserDAO userDao = new UserDAO();
            com.activitymonitor.model.User u1 = userDao.findById(1);
            if (u1 != null) {
                System.out.println(u1.getId() + " | " + u1.getFullName() + " | " + u1.getUsername() + " | "
                        + u1.getRole() + " | org=" + u1.getOrganizationId());
            } else {
                System.out.println("User tidak ditemukan");
            }

            System.out.println("\n--- UserDAO: findByUsername('bromo.admin') ---");
            com.activitymonitor.model.User ub = userDao.findByUsername("bromo.admin");
            if (ub != null) {
                System.out.println(ub.getId() + " | " + ub.getFullName() + " | " + ub.getUsername() + " | "
                        + ub.getRole() + " | org=" + ub.getOrganizationId());
            } else {
                System.out.println("User tidak ditemukan");
            }

            System.out.println("\n--- UserDAO: authenticate('bromo.admin','admin123') ---");
            boolean ok = userDao.authenticate("bromo.admin", "admin123");
            System.out.println("Authenticate success? " + ok);

        } catch (Exception e) {
            e.printStackTrace();
        }

        DBConnection.closeConnection();
    }
}
