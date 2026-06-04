package com.activitymonitor.controller;

import com.activitymonitor.dao.ActivityDAO;
import com.activitymonitor.dao.OrganizationDAO;
import com.activitymonitor.model.Activity;
import com.activitymonitor.model.Organization;
import com.activitymonitor.model.User;
import com.activitymonitor.util.InputValidator;
import com.activitymonitor.util.PDFExporter;
import com.activitymonitor.view.ActivityFormPanel;
import com.activitymonitor.view.ActivityTablePanel;
import com.activitymonitor.view.DashboardFrame;

import javax.swing.*;
import java.sql.Date;
import java.util.List;

public class ActivityController {

    private final DashboardFrame   dashboard;
    private final ActivityTablePanel tablePanel;
    private final ActivityDAO      activityDAO;
    private final OrganizationDAO  orgDAO;
    private final User             currentUser;

    // Cache ID per baris tabel — index i = row i = activities.get(i).getId()
    private List<Activity> currentList;

    public ActivityController(DashboardFrame dashboard, User currentUser) {
        this.dashboard   = dashboard;
        this.tablePanel  = dashboard.getTablePanel();
        this.activityDAO = new ActivityDAO();
        this.orgDAO      = new OrganizationDAO();
        this.currentUser = currentUser;

        bindEvents();
        loadAll();
    }

    // ── Load data ─────────────────────────────────────────────────
    private void loadAll() {
        currentList = activityDAO.findAll(currentUser.getOrganizationId());
        renderTable(currentList);
    }

    private void renderTable(List<Activity> list) {
        Object[][] data = new Object[list.size()][];
        for (int i = 0; i < list.size(); i++) {
            Activity a = list.get(i);
            data[i] = new Object[]{
                a.getName(),
                a.getDate(),
                a.getLocation(),
                a.getParticipantCount(),
                a.getStatus()
            };
        }
        tablePanel.setTableData(data);
    }

    // ── Bind events ───────────────────────────────────────────────
    private void bindEvents() {
        tablePanel.addSearchListener(e -> handleSearch());
        tablePanel.addAddListener(e    -> handleAdd());
        tablePanel.addEditListener(e   -> handleEdit());
        tablePanel.addDeleteListener(e -> handleDelete());
        tablePanel.addExportListener(e -> handleExport());
    }

    // ── Search ────────────────────────────────────────────────────
    private void handleSearch() {
        String keyword = tablePanel.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAll();
        } else {
            currentList = activityDAO.findByKeyword(keyword,
                currentUser.getOrganizationId());
            renderTable(currentList);
        }
    }

    // ── Add ───────────────────────────────────────────────────────
    private void handleAdd() {
        ActivityFormPanel form = new ActivityFormPanel(dashboard, false);
        form.addSaveListener(e -> {
            String error = InputValidator.validateActivity(
                form.getActivityName(), form.getDate(),
                form.getLocation(), form.getParticipantCount()
            );
            if (error != null) {
                JOptionPane.showMessageDialog(form, error,
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Activity a = buildActivity(form, 0);
            if (activityDAO.insert(a)) {
                form.dispose();
                loadAll();
                JOptionPane.showMessageDialog(dashboard,
                    "Kegiatan berhasil ditambahkan.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(form,
                    "Gagal menyimpan data. Coba lagi.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    // ── Edit ──────────────────────────────────────────────────────
    private void handleEdit() {
        int row = tablePanel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(dashboard,
                "Pilih kegiatan yang ingin diedit.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Activity selected = currentList.get(row);
        ActivityFormPanel form = new ActivityFormPanel(dashboard, true);

        form.setActivityName(selected.getName());
        form.setDescription(selected.getDescription() != null
            ? selected.getDescription() : "");
        form.setDate(selected.getDate().toString());
        form.setLocation(selected.getLocation());
        form.setParticipantCount(String.valueOf(selected.getParticipantCount()));
        form.setStatus(selected.getStatus());

        form.addSaveListener(e -> {
            String error = InputValidator.validateActivity(
                form.getActivityName(), form.getDate(),
                form.getLocation(), form.getParticipantCount()
            );
            if (error != null) {
                JOptionPane.showMessageDialog(form, error,
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Activity updated = buildActivity(form, selected.getId());
            if (activityDAO.update(updated)) {
                form.dispose();
                loadAll();
                JOptionPane.showMessageDialog(dashboard,
                    "Kegiatan berhasil diperbarui.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(form,
                    "Gagal memperbarui data. Coba lagi.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        form.setVisible(true);
    }

    // ── Delete ────────────────────────────────────────────────────
    private void handleDelete() {
        int row = tablePanel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(dashboard,
                "Pilih kegiatan yang ingin dihapus.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Activity selected = currentList.get(row);
        int confirm = JOptionPane.showConfirmDialog(dashboard,
            "Hapus kegiatan \"" + selected.getName() + "\"?\n"
            + "Tindakan ini tidak dapat dibatalkan.",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (activityDAO.delete(selected.getId())) {
                loadAll();
                JOptionPane.showMessageDialog(dashboard,
                    "Kegiatan berhasil dihapus.",
                    "Berhasil", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(dashboard,
                    "Gagal menghapus data. Coba lagi.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ── Export PDF ────────────────────────────────────────────────
    private void handleExport() {
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Tidak ada data untuk diekspor.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Organization org = orgDAO.findById(currentUser.getOrganizationId());
        String outputPath = "output/laporan-kegiatan-"
            + System.currentTimeMillis() + ".pdf";

        boolean success = PDFExporter.export(currentList, org, outputPath);
        if (success) {
            JOptionPane.showMessageDialog(dashboard,
                "Laporan berhasil disimpan ke:\n" + outputPath,
                "Ekspor Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dashboard,
                "Gagal membuat laporan PDF.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helper ────────────────────────────────────────────────────
    private Activity buildActivity(ActivityFormPanel form, int id) {
        String countStr = form.getParticipantCount();
        int count = countStr.isEmpty() ? 0 : Integer.parseInt(countStr);

        return new Activity(
            id,
            form.getActivityName(),
            form.getDescription(),
            Date.valueOf(form.getDate()),
            form.getLocation(),
            count,
            form.getStatus(),
            currentUser.getId(),
            currentUser.getOrganizationId()
        );
    }
}