package com.activitymonitor.controller;

//Dibuat oleh: hamid bromo
// Digunakan untuk akses data kegiatan
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
import java.util.concurrent.ExecutionException;
import javax.swing.*;

// Kontroler untuk mengelola kegiatan (CRUD, pencarian, ekspor PDF)
public class ActivityController {

    // Frame dashboard utama
    private final DashboardFrame   dashboard;
    // Panel tabel kegiatan
    private final ActivityTablePanel tablePanel;
    // DAO untuk akses data kegiatan
    private final ActivityDAO      activityDAO;
    // DAO untuk akses data organisasi
    private final OrganizationDAO  orgDAO;
    // User yang sedang login
    private final User             currentUser;

    // Cache daftar kegiatan saat ini, index sesuai baris tabel
    private List<Activity> currentList;

    // Menginisialisasi controller dengan dashboard dan user yang login
    //hamid bromo - enkapsulasi - method constructorActivityController
    public ActivityController(DashboardFrame dashboard, User currentUser) {
        this.dashboard   = dashboard;
        this.tablePanel  = dashboard.getTablePanel();
        this.activityDAO = new ActivityDAO();
        this.orgDAO      = new OrganizationDAO();
        this.currentUser = currentUser;

        // Ikat event listener dan muat data awal
        bindEvents();
        tablePanel.setCrudEnabled(canManageActivities());
        loadAllAsync();
    }

    // Memuat data kegiatan secara asynchronous menggunakan SwingWorker
    //hamid bromo - enkapsulasi - memuat data kegiatan secara async
    private void loadAllAsync() {
        new SwingWorker<List<Activity>, Void>() {
            @Override
            //hamid bromo - overriding (polimorfisme) - menjalankan proses di background thread
            protected List<Activity> doInBackground() {
                // Muat semua kegiatan atau sesuai organisasi user
                return canViewAllOrganizations()
                    ? activityDAO.findAll()
                    : activityDAO.findAll(currentUser.getOrganizationId());
            }

            @Override
            //hamid bromo - overriding (polimorfisme) - menangani hasil setelah background selesai
            protected void done() {
                try {
                    // Ambil hasil dan render ke tabel
                    currentList = get();
                    renderTable(currentList);
                } catch (InterruptedException | ExecutionException e) {
                    System.err.println("Gagal memuat kegiatan: " + e.getMessage());
                }
            }
        }.execute();
    }


    // Memuat semua data kegiatan dari database (synchronous)
    //hamid bromo - enkapsulasi - memuat semua data kegiatan dari database

    private void loadAll() {
        // Muat semua kegiatan atau filter berdasarkan organisasi user
        currentList = canViewAllOrganizations()
            ? activityDAO.findAll()
            : activityDAO.findAll(currentUser.getOrganizationId());
        renderTable(currentList);
    }

    // Menampilkan data kegiatan ke tabel panel
    //hamid bromo - overriding (polimorfisme) - menampilkan data ke tabel
    private void renderTable(List<Activity> list) {
        // Konversi daftar kegiatan ke array data tabel
        Object[][] data = new Object[list.size()][];
        int[] ids = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Activity a = list.get(i);
            ids[i] = a.getId();
            data[i] = new Object[]{
                a.getName(),
                a.getDate(),
                a.getLocation(),
                a.getParticipantCount(),
                a.getStatus()
            };
        }
        tablePanel.setTableData(data, ids);
        dashboard.setReportActivities(list);
    }


    // Mengikat event listener dari panel ke method handler
    //hamid bromo - enkapsulasi - mengikat event listener ke komponen UI

    private void bindEvents() {
        tablePanel.addSearchListener(e -> handleSearch());
        tablePanel.addAddListener(e    -> handleAdd());
        tablePanel.addEditListener(e   -> handleEdit());
        tablePanel.addDeleteListener(e -> handleDelete());
        tablePanel.addStatusChangeListener(e -> handleStatusChange(e));
        tablePanel.addExportListener(e -> handleExport());
        dashboard.addReportExportListener(e -> handleSelectedExport());
    }


    // Menangani event pencarian kegiatan
    //hamid bromo - enkapsulasi - menangani pencarian kegiatan

    private void handleSearch() {
        String keyword = tablePanel.getSearchKeyword();
        // Jika keyword kosong, muat semua data, jika tidak lakukan pencarian
        if (keyword.isEmpty()) {
            loadAll();
        } else {
            currentList = canViewAllOrganizations()
                ? activityDAO.findByKeyword(keyword)
                : activityDAO.findByKeyword(keyword, currentUser.getOrganizationId());
            renderTable(currentList);
        }
    }


    // Menangani penambahan kegiatan baru melalui form
    //hamid bromo - enkapsulasi - menangani penambahan kegiatan baru

    private void handleAdd() {
        // Periksa hak akses sebelum menambah
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

        ActivityFormPanel form = new ActivityFormPanel(dashboard, false);
        form.addSaveListener(e -> {
            // Validasi input form
            String error = InputValidator.validateActivity(
                form.getActivityName(), form.getDate(),
                form.getActivityLocation(), form.getParticipantCount()
            );
            // Jika validasi gagal, tampilkan pesan
            if (error != null) {
                JOptionPane.showMessageDialog(form, error,
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Simpan kegiatan ke database
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


    // Menangani pengeditan kegiatan yang dipilih
    //hamid bromo - enkapsulasi - menangani pengeditan kegiatan

    private void handleEdit() {
        // Periksa hak akses
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

        // Periksa apakah ada baris yang dipilih
        int row = tablePanel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(dashboard,
                "Pilih kegiatan yang ingin diedit.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Isi form dengan data kegiatan yang dipilih
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
            // Validasi input form
            String error = InputValidator.validateActivity(
                form.getActivityName(), form.getDate(),
                form.getActivityLocation(), form.getParticipantCount()
            );
            if (error != null) {
                JOptionPane.showMessageDialog(form, error,
                    "Validasi Gagal", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update kegiatan di database
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


    // Menangani penghapusan kegiatan yang dipilih
    //hamid bromo - enkapsulasi - menangani penghapusan kegiatan

    private void handleDelete() {
        // Periksa hak akses
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

        // Periksa baris yang dipilih
        int row = tablePanel.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(dashboard,
                "Pilih kegiatan yang ingin dihapus.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Konfirmasi penghapusan
        Activity selected = currentList.get(row);
        int confirm = JOptionPane.showConfirmDialog(dashboard,
            "Hapus kegiatan \"" + selected.getName() + "\"?\n"
            + "Tindakan ini tidak dapat dibatalkan.",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        // Jika dikonfirmasi, hapus dari database
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


    // Menangani perubahan status kegiatan
    //hamid bromo - enkapsulasi - menangani perubahan status kegiatan

    private void handleStatusChange(java.awt.event.ActionEvent e) {
        // Periksa hak akses
        if (!canManageActivities()) {
            showAccessDenied();
            return;
        }

        // Periksa baris yang dipilih
        int row = tablePanel.getSelectedRow();
        if (row < 0) return;

        String newStatus = e.getActionCommand();
        int activityId = tablePanel.getActivityId(row);
        
        // Validasi ID kegiatan
        if (activityId <= 0) {
            JOptionPane.showMessageDialog(dashboard,
                "Tidak dapat mengubah status. Coba refresh halaman.",
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (activityDAO.updateStatus(activityId, newStatus)) {
            loadAll();
            JOptionPane.showMessageDialog(dashboard,
                "Status kegiatan berhasil diperbarui.",
                "Berhasil", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(dashboard,
                "Gagal mengubah status. Coba lagi.",
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // Menangani ekspor semua kegiatan ke PDF
    //hamid bromo - enkapsulasi - mengekspor laporan ke PDF

    private void handleExport() {
        // Periksa apakah ada data untuk diekspor
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Tidak ada data untuk diekspor.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Tampilkan dialog pemilih lokasi file
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Simpan Laporan PDF");
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter(
                "PDF Files (*.pdf)", "pdf"));
        chooser.setSelectedFile(
            new java.io.File("laporan-kegiatan.pdf"));
        chooser.setAcceptAllFileFilterUsed(false);

        //buka folder output
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

        // Pastikan ekstensi file .pdf
        String path = chooser.getSelectedFile().getAbsolutePath();
        if (!path.toLowerCase().endsWith(".pdf")) path += ".pdf";

        // Ekspor ke PDF
        Organization org = orgDAO.findById(currentUser.getOrganizationId());
        boolean success = PDFExporter.export(currentList, org, path);

        // Tampilkan hasil ekspor
        if (success) {
            // Tanya user apakah ingin membuka folder file
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


    // Menangani ekspor kegiatan terpilih ke PDF
    //hamid bromo - enkapsulasi - mengekspor kegiatan terpilih ke PDF

    private void handleSelectedExport() {
        // Periksa apakah ada data
        if (currentList == null || currentList.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Tidak ada data untuk diekspor.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Ambil baris yang dipilih pada tabel laporan
        List<Integer> selectedRows = dashboard.getSelectedReportRows();
        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(dashboard,
                "Pilih minimal satu kegiatan untuk dibuat laporan.",
                "Perhatian", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Kumpulkan kegiatan yang dipilih
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

    // Membangun objek Activity dari data form (tanpa createdBy)
    //hamid bromo - enkapsulasi - membangun objek Activity dari form
    private Activity buildActivity(ActivityFormPanel form, int id) {
        return buildActivity(form, id, currentUser.getId(), currentUser.getOrganizationId());
    }

    // Membangun objek Activity dari data form dengan createdBy dan organizationId
    //hamid bromo - enkapsulasi - membangun objek Activity dari form
    private Activity buildActivity(ActivityFormPanel form, int id,
                                   int createdBy, int organizationId) {
        // Parse jumlah peserta, default 0 jika kosong
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

    // Memeriksa apakah user memiliki hak akses CRUD kegiatan
    //hamid bromo - enkapsulasi - memeriksa hak akses kelola kegiatan
    private boolean canManageActivities() {
        String role = currentUser.getRole();
        return role != null && role.equalsIgnoreCase("admin")
            || canViewAllOrganizations();
    }

    // Memeriksa apakah user dapat melihat kegiatan semua organisasi
    //hamid bromo - enkapsulasi - memeriksa hak akses lihat semua organisasi
    private boolean canViewAllOrganizations() {
        String role = currentUser.getRole() != null
            ? currentUser.getRole().toLowerCase()
            : "";
        String username = currentUser.getUsername() != null
            ? currentUser.getUsername().toLowerCase()
            : "";

        // Super admin dan admin utama dapat melihat semua organisasi
        return role.equals("super_admin")
            || role.equals("admin_utama")
            || role.equals("main_admin")
            || username.equals("bromo.admin")
            || username.equals("admin");
    }

    // Menampilkan dialog akses ditolak
    //hamid bromo - enkapsulasi - menampilkan pesan akses ditolak
    private void showAccessDenied() {
        JOptionPane.showMessageDialog(dashboard,
            "Hanya admin yang dapat menambah, mengubah, atau menghapus kegiatan.",
            "Akses Ditolak", JOptionPane.WARNING_MESSAGE);
    }
}

