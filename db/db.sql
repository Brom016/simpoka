CREATE DATABASE IF NOT EXISTS activity_monitor
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE activity_monitor;

CREATE TABLE organizations (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  leader VARCHAR(100) NOT NULL,
  period VARCHAR(20) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id)
);

CREATE TABLE users (
  id INT NOT NULL AUTO_INCREMENT,
  full_name VARCHAR(100) NOT NULL,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(255) NOT NULL,
  role ENUM('admin', 'member') NOT NULL DEFAULT 'member',
  organization_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

  PRIMARY KEY (id),
  UNIQUE KEY uq_username (username),

  FOREIGN KEY (organization_id)
    REFERENCES organizations(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT
);

CREATE TABLE activities (
  id INT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  date DATE NOT NULL,
  location VARCHAR(255) NOT NULL,
  participant_count INT NOT NULL DEFAULT 0,
  status ENUM('planned', 'ongoing', 'completed') NOT NULL DEFAULT 'planned',
  created_by INT NOT NULL,
  organization_id INT NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
    ON UPDATE CURRENT_TIMESTAMP,

  PRIMARY KEY (id),

  FOREIGN KEY (created_by)
    REFERENCES users(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

  FOREIGN KEY (organization_id)
    REFERENCES organizations(id)
    ON UPDATE CASCADE
    ON DELETE RESTRICT,

  INDEX idx_name (name),
  INDEX idx_date (date),
  INDEX idx_status (status)
);

INSERT INTO organizations (name, leader, period) VALUES
('Unit Kegiatan Mahasiswa Bahasa', 'Yanto Suroso', '2025/2026'),
('Himpunan Mahasiswa Jurusan Teknik Informatika', 'Nabila Septina Rahmajanti', '2025/2026'),
('Badan Eksekutif Mahasiswa Fakultas', 'Sulastri', '2025/2026');

INSERT INTO users (full_name, username, password, role, organization_id) VALUES
('Bromo', 'bromo.admin', 'admin123', 'admin', 1),
('Siti Rahayu', 'siti.member', 'member123', 'member', 1),

('Nabila Septina Rahmajanti', 'nabila.admin', 'admin123', 'admin', 2),
('Ahmad Fauzi', 'ahmad.member', 'member123', 'member', 2),

('Sulastri', 'sulastri.admin', 'admin123', 'admin', 3),
('Dimas Prasetyo', 'dimas.member', 'member123', 'member', 3);

INSERT INTO activities (
  name,
  description,
  date,
  location,
  participant_count,
  status,
  created_by,
  organization_id
) VALUES
(
  'Pelatihan Public Speaking Bahasa Inggris',
  'Kegiatan pelatihan kemampuan berbicara bahasa Inggris untuk anggota baru UKM Bahasa.',
  '2025-02-10',
  'Aula Gedung A Lt. 2',
  30,
  'completed',
  1,
  1
),
(
  'Lomba Debat Antar Fakultas',
  'Kompetisi debat bahasa Indonesia dan bahasa Inggris antar fakultas.',
  '2025-06-20',
  'Auditorium Utama',
  120,
  'planned',
  1,
  1
),
(
  'Seminar Teknologi Artificial Intelligence',
  'Diskusi perkembangan AI dan implementasinya di dunia industri teknologi.',
  '2025-03-05',
  'Ruang Seminar Informatika',
  80,
  'completed',
  3,
  2
),
(
  'Hackathon Internal HMJTI',
  'Kompetisi pengembangan aplikasi berbasis web dan mobile selama 24 jam.',
  '2025-05-15',
  'Lab Komputer Gedung C',
  50,
  'ongoing',
  3,
  2
),
(
  'Rapat Kerja Tahunan BEM Fakultas',
  'Pembahasan program kerja dan evaluasi kegiatan organisasi mahasiswa fakultas.',
  '2025-04-01',
  'Aula Rektorat',
  100,
  'completed',
  5,
  3
),
(
  'Bakti Sosial Mahasiswa',
  'Kegiatan donor darah dan pembagian bantuan sosial kepada masyarakat sekitar.',
  '2025-07-10',
  'Lapangan Kampus Utama',
  150,
  'planned',
  5,
  3
);