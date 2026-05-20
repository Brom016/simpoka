package com.activitymonitor;

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
                        rs.getString("leader")
                    );
                }

                // Uji baca tabel users
                rs = stmt.executeQuery("SELECT * FROM users");
                System.out.println("\n--- Daftar User ---");
                while (rs.next()) {
                    System.out.println(
                        rs.getString("username") + " | " +
                        rs.getString("role")
                    );
                }

                rs.close();
                stmt.close();

            } catch (Exception e) {
                System.err.println("Query gagal: " + e.getMessage());
            }
        }

        DBConnection.closeConnection();
    }
}