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
import java.awt.Desktop;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

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
        tablePanel.setCrudEnabled(canManageActivities());
        loadAll();
    }

    // ── Load data ─────────────────────────────────────────────────
    private void loadAll() {
        currentList = canViewAllOrganizations()
            ? activityDAO.findAll()
            : activityDAO.findAll(currentUser.getOrganizationId());
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
        dashboard.setReportActivities(list);
    }

    // ── Bind events ───────────────────────────────────────────────
    private void bindEvents() {
        tablePanel.addSearchListener(e -> handleSearch());
        tablePanel.addAddListener(e    -> handleAdd());
        tablePanel.addEditListener(e   -> handleEdit());
        tablePanel.addDeleteListener(e -> handleDelete());
        tablePanel.addExportListener(e -> handleExport());
        dashboard.addReportExportListener(e -> handleSelectedExport());
    }

    // ── Search ────────────────────────────────────────────────────
    private void handleSearch() {
        String keyword = tablePanel.getSearchKeyword();
        if (keyword.isEmpty()) {
            loadAll();
        } else {
            currentList = canViewAllOrganizations()
                ? activityDAO.findByKeyword(keyword)
                : activityDAO.findByKeyword(keyword, currentUser.getOrganizationId());
            renderTable(currentList);
        }
    }

    // ── Add ───────────────────────────────────────────────────────
    private void handleAdd() {
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

        ActivityFormPanel form = new ActivityFormPanel(dashboard, false);
        form.addSaveListener(e -> {
            String error = InputValidator.validateActivity(
                form.getActivityName(), form.getDate(),
                form.getActivityLocation(), form.getParticipantCount()
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
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

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
        form.setActivityLocation(selected.getLocation());
        form.setParticipantCount(String.valueOf(selected.getParticipantCount()));
        form.setStatus(selected.getStatus());

        form.addSaveListener(e -> {
            String error = InputValidator.validateActivity(
                form.getActivityName(), form.getDate(),
                form.getActivityLocation(), form.getParticipantCount()
            );
            if (error != null) {
                JOptionPane.showMessageDialog(form, error,
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Activity updated = buildActivity(form, selected.getId(),
                selected.getCreatedBy(), selected.getOrganizationId());
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
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

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

    // ── Export PDF dengan JFileChooser ────────────────────────────
    private void handleExport() {
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Tidak ada data untuk diekspor.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // File chooser — seperti Save dialog di browser
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan PDF");
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "PDF Files (*.pdf)", "pdf"));
        chooser.setSelectedFile(
            new java.io.File("laporan-kegiatan.pdf"));
        chooser.setAcceptAllFileFilterUsed(false);

        // Buka di folder output/ jika ada, kalau tidak di Documents
        java.io.File outputDir = new java.io.File("output");
        if (outputDir.exists()) {
            chooser.setCurrentDirectory(outputDir);
        } else {
            chooser.setCurrentDirectory(
                javax.swing.filechooser.FileSystemView
                    .getFileSystemView().getDefaultDirectory());
        }

        int result = chooser.showSaveDialog(dashboard);
        if (result != JFileChooser.APPROVE_OPTION) return;

        // Pastikan ekstensi .pdf
        String path = chooser.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".pdf")) path += ".pdf";

        Organization org = orgDAO.findById(currentUser.getOrganizationId());
        boolean success = PDFExporter.export(currentList, org, path);

        if (success) {
            // Buka folder tempat file disimpan (Windows)
            String finalPath = path;
            int open = JOptionPane.showConfirmDialog(dashboard,
                "Laporan berhasil disimpan ke:\n" + finalPath
                + "\n\nBuka lokasi file?",
                "Ekspor Berhasil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            if (open == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(
                        new java.io.File(finalPath).getParentFile());
                } catch (Exception ignored) {}
            }
        } else {
            JOptionPane.showMessageDialog(dashboard,
                "Gagal membuat laporan PDF.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Helper ────────────────────────────────────────────────────
    private void handleSelectedExport() {
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Tidak ada data untuk diekspor.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Integer> selectedRows = dashboard.getSelectedReportRows();
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Pilih minimal satu kegiatan untuk dibuat laporan.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Activity> selectedActivities = new ArrayList<>();
        for (Integer row : selectedRows) {
            if (row >= 0 && row < currentList.size()) {
                selectedActivities.add(currentList.get(row));
            }
        }

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan PDF");
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "PDF Files (*.pdf)", "pdf"));
        chooser.setSelectedFile(new java.io.File("laporan-kegiatan.pdf"));
        chooser.setAcceptAllFileFilterUsed(false);

        java.io.File outputDir = new java.io.File("output");
        if (outputDir.exists()) {
            chooser.setCurrentDirectory(outputDir);
        } else {
            chooser.setCurrentDirectory(
                javax.swing.filechooser.FileSystemView
                    .getFileSystemView().getDefaultDirectory());
        }

        int result = chooser.showSaveDialog(dashboard);
        if (result != JFileChooser.APPROVE_OPTION) return;

        String path = chooser.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".pdf")) path += ".pdf";

        Organization org = orgDAO.findById(currentUser.getOrganizationId());
        boolean success = PDFExporter.export(selectedActivities, org, path);

        if (success) {
            String finalPath = path;
            int open = JOptionPane.showConfirmDialog(dashboard,
                "Laporan berhasil disimpan ke:\n" + finalPath
                + "\n\nBuka lokasi file?",
                "Ekspor Berhasil",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.INFORMATION_MESSAGE);
            if (open == JOptionPane.YES_OPTION) {
                try {
                    Desktop.getDesktop().open(
                        new java.io.File(finalPath).getParentFile());
                } catch (Exception ignored) {}
            }
        } else {
            JOptionPane.showMessageDialog(dashboard,
                "Gagal membuat laporan PDF.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Activity buildActivity(ActivityFormPanel form, int id) {
        return buildActivity(form, id, currentUser.getId(), currentUser.getOrganizationId());
    }

    private Activity buildActivity(ActivityFormPanel form, int id,
                                   int createdBy, int organizationId) {
        String countStr = form.getParticipantCount();
        int count = countStr.isEmpty() ? 0 : Integer.parseInt(countStr);

        return new Activity(
            id,
            form.getActivityName(),
            form.getDescription(),
            Date.valueOf(form.getDate()),
            form.getActivityLocation(),
            count,
            form.getStatus(),
            createdBy,
            organizationId
        );
    }

    private boolean canManageActivities() {
        String role = currentUser.getRole();
        return role != null && role.equalsIgnoreCase("admin")
            || canViewAllOrganizations();
    }

    private boolean canViewAllOrganizations() {
        String role = currentUser.getRole() != null
            ? currentUser.getRole().toLowerCase()
            : "";
        String username = currentUser.getUsername() != null
            ? currentUser.getUsername().toLowerCase()
            : "";

        return role.equals("super_admin")
            || role.equals("admin_utama")
            || role.equals("main_admin")
            || username.equals("bromo.admin")
            || username.equals("admin");
    }

    private void showAccessDenied() {
        JOptionPane.showMessageDialog(dashboard,
            "Hanya admin yang dapat menambah, mengubah, atau menghapus kegiatan.",
            "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
    }
}
