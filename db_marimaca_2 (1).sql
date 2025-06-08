-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Jun 2025 pada 16.44
-- Versi server: 10.4.32-MariaDB
-- Versi PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `db_marimaca_2`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`) VALUES
(1, 'admin1', 'password123'),
(2, 'admin2', 'PIhywJRoL0w/zf/auAqNNR2+7psxop5uoJK1HPRz5zI=');

-- --------------------------------------------------------

--
-- Struktur dari tabel `anggota`
--

CREATE TABLE `anggota` (
  `id_anggota` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `alamat` text DEFAULT NULL,
  `no_telepon` varchar(20) DEFAULT NULL,
  `tanggal_daftar` date NOT NULL DEFAULT curdate(),
  `email` varchar(100) DEFAULT NULL,
  `status_aktif` tinyint(1) DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `anggota`
--

INSERT INTO `anggota` (`id_anggota`, `nama`, `alamat`, `no_telepon`, `tanggal_daftar`, `email`, `status_aktif`) VALUES
(1, 'Andi Wijaya', 'Jl. Melati No.10, Jakarta', '081234567890', '2023-01-05', 'andi.wijaya@example.com', 1),
(2, 'Siti Aminah', 'Jl. Mawar No.7, Depok', '082345678901', '2022-12-20', 'siti.aminah@example.com', 1),
(3, 'Budi Santoso', 'Jl. Kenanga No.20, Bogor', '083456789012', '2021-11-15', 'budi.santoso@example.com', 0),
(4, 'Dewi Lestari', 'Jl. Anggrek No.5, Tangerang', '084567890123', '2024-03-12', 'dewi.lestari@example.com', 1),
(5, 'Rian Pratama', 'Jl. Dahlia No.12, Bekasi', '085678901234', '2023-05-22', 'rian.pratama@example.com', 1),
(6, 'Nina Kartika', 'Jl. Melati No.3, Jakarta', '086789012345', '2020-09-09', 'nina.kartika@example.com', 0),
(7, 'Agus Salim', 'Jl. Flamboyan No.11, Depok', '087890123456', '2022-07-18', 'agus.salim@example.com', 1),
(8, 'Lina Marlina', 'Jl. Cempaka No.8, Bogor', '088901234567', '2023-04-30', 'lina.marlina@example.com', 1),
(9, 'Tono Susilo', 'Jl. Kamboja No.9, Tangerang', '089012345678', '2021-12-01', 'tono.susilo@example.com', 0),
(10, 'Fitri Handayani', 'Jl. Bougenville No.4, Bekasi', '080123456789', '2024-02-10', 'fitri.handayani@example.com', 1),
(11, 'asep imam wahyudi', 'JL Candranala, Lebakwangi Kuningan', '085889336609', '2025-05-26', 'asep@gmail.com', 1),
(12, 'Muhammad Iqbal Pratama', 'Bekasi , Jawa Barat', '0812345678', '2025-05-27', 'iqbal@gmail.com', 1),
(13, 'Dery Husnairi', 'Cinagara, Kab Kuningan', '0812345678', '2025-06-07', 'dery@gmail.com', 1),
(14, 'Dila Septiola', 'Loji, Karawang', '08654368886', '2025-06-07', 'dila@gmail.com', 1);

-- --------------------------------------------------------

--
-- Struktur dari tabel `buku`
--

CREATE TABLE `buku` (
  `id_buku` int(11) NOT NULL,
  `judul_buku` varchar(200) NOT NULL,
  `author` varchar(100) DEFAULT NULL,
  `publisher` varchar(100) DEFAULT NULL,
  `type` varchar(50) NOT NULL,
  `location` enum('A1','A2','A3','A4','A5','A6','A7') DEFAULT NULL,
  `status` enum('Tersedia','Dipinjam','Rusak','Hilang') DEFAULT 'Tersedia'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `buku`
--

INSERT INTO `buku` (`id_buku`, `judul_buku`, `author`, `publisher`, `type`, `location`, `status`) VALUES
(1, 'Laskar Pelangi', 'Andrea Hirata', 'Bentang Pustaka', 'Novel', 'A1', 'Dipinjam'),
(2, 'Bumi Manusia', 'Pramoedya Ananta Toer', 'Lentera Dipantara', 'Novel', 'A1', 'Dipinjam'),
(3, 'Negeri 5 Menara', 'A. Fuadi', 'Republika', 'Novel', 'A1', 'Dipinjam'),
(4, 'Gadis Kretek', 'Ratih Kumala', 'Gramedia', 'Novel', 'A1', 'Tersedia'),
(5, 'Sang Pemimpi', 'Andrea Hirata', 'Bentang Pustaka', 'Novel', 'A1', 'Tersedia'),
(6, 'Harry Potter and the Sorcerer\'s Stone', 'J.K. Rowling', 'Bloomsbury', 'Novel', 'A1', 'Tersedia'),
(7, 'The Hobbit', 'J.R.R. Tolkien', 'Allen & Unwin', 'Novel', 'A1', 'Tersedia'),
(8, 'To Kill a Mockingbird', 'Harper Lee', 'J.B. Lippincott & Co.', 'Novel', 'A1', 'Tersedia'),
(9, 'The Catcher in the Rye', 'J.D. Salinger', 'Little, Brown and Company', 'Novel', 'A1', 'Tersedia'),
(10, 'The Great Gatsby', 'F. Scott Fitzgerald', 'Charles Scribner\'s Sons', 'Novel', 'A1', 'Rusak'),
(11, 'Ensiklopedia Dunia', 'Various', 'Gramedia', 'Ensiklopedia', 'A2', 'Tersedia'),
(12, 'Ensiklopedia Anak', 'Various', 'Erlangga', 'Ensiklopedia', 'A2', 'Tersedia'),
(13, 'Ensiklopedia Teknologi', 'Various', 'Erlangga', 'Ensiklopedia', 'A2', 'Tersedia'),
(14, 'Ensiklopedia Hewan', 'Various', 'Gramedia', 'Ensiklopedia', 'A2', 'Tersedia'),
(15, 'Ensiklopedia Flora', 'Various', 'Erlangga', 'Ensiklopedia', 'A2', 'Tersedia'),
(16, 'Ensiklopedia Sejarah', 'Various', 'Gramedia', 'Ensiklopedia', 'A2', 'Tersedia'),
(17, 'Ensiklopedia Sains', 'Various', 'Erlangga', 'Ensiklopedia', 'A2', 'Tersedia'),
(18, 'Ensiklopedia Pendidikan', 'Various', 'Gramedia', 'Ensiklopedia', 'A2', 'Tersedia'),
(19, 'Ensiklopedia Budaya', 'Various', 'Erlangga', 'Ensiklopedia', 'A2', 'Tersedia'),
(20, 'Ensiklopedia Agama', 'Various', 'Gramedia', 'Ensiklopedia', 'A2', 'Tersedia'),
(21, 'Naruto Vol.1', 'Masashi Kishimoto', 'Shueisha', 'Komik', 'A3', 'Tersedia'),
(22, 'Naruto Vol.2', 'Masashi Kishimoto', 'Shueisha', 'Komik', 'A3', 'Tersedia'),
(23, 'One Piece Vol.1', 'Eiichiro Oda', 'Shueisha', 'Komik', 'A3', 'Tersedia'),
(24, 'Dragon Ball Vol.1', 'Akira Toriyama', 'Shueisha', 'Komik', 'A3', 'Tersedia'),
(25, 'Detective Conan Vol.1', 'Gosho Aoyama', 'Shogakukan', 'Komik', 'A3', 'Tersedia'),
(26, 'Komik Batman Vol.1', 'Bob Kane', 'DC Comics', 'Komik', 'A3', 'Tersedia'),
(27, 'Komik Superman Vol.1', 'Jerry Siegel', 'DC Comics', 'Komik', 'A3', 'Rusak'),
(28, 'Komik Avengers Vol.1', 'Stan Lee', 'Marvel Comics', 'Komik', 'A3', 'Tersedia'),
(29, 'Komik Doraemon Vol.1', 'Fujiko F. Fujio', 'Shogakukan', 'Komik', 'A3', 'Tersedia'),
(30, 'Komik Bleach Vol.1', 'Tite Kubo', 'Shueisha', 'Komik', 'A3', 'Tersedia'),
(31, 'Ilmu Komputer Dasar', 'Bambang Hariyanto', 'Informatika', 'Buku Pelajaran', 'A4', 'Tersedia'),
(32, 'Fisika Dasar', 'S. Tipler', 'W.H. Freeman', 'Buku Pelajaran', 'A4', 'Tersedia'),
(33, 'Matematika Diskrit', 'Kenneth Rosen', 'McGraw-Hill', 'Buku Pelajaran', 'A4', 'Tersedia'),
(34, 'Pengantar Ekonomi', 'Paul Samuelson', 'McGraw-Hill', 'Buku Pelajaran', 'A4', 'Tersedia'),
(35, 'Ilmu Politik', 'Robert Dahl', 'Yale University Press', 'Buku Pelajaran', 'A4', 'Tersedia'),
(36, 'Kimia Organik', 'Clayden', 'Oxford', 'Buku Pelajaran', 'A4', 'Tersedia'),
(37, 'Biologi Dasar', 'Campbell', 'Pearson', 'Buku Pelajaran', 'A4', 'Tersedia'),
(38, 'Teknologi Informasi', 'Andrew Tanenbaum', 'Prentice Hall', 'Buku Pelajaran', 'A4', 'Tersedia'),
(39, 'Sejarah Perang Dunia', 'Winston Churchill', 'Penguin', 'Buku Pelajaran', 'A4', 'Tersedia'),
(40, 'Matematika Lanjut', 'Gilbert Strang', 'Wellesley-Cambridge Press', 'Buku Pelajaran', 'A4', 'Tersedia'),
(41, 'Sejarah Indonesia', 'Taufik Abdullah', 'Gramedia', 'Sejarah', 'A5', 'Tersedia'),
(42, 'Sejarah Dunia Modern', 'Eric Hobsbawm', 'Penguin', 'Sejarah', 'A5', 'Tersedia'),
(43, 'Sejarah Perang Dunia', 'Winston Churchill', 'Penguin', 'Sejarah', 'A5', 'Tersedia'),
(44, 'Sejarah Asia', 'John Keay', 'HarperCollins', 'Sejarah', 'A5', 'Tersedia'),
(45, 'Sejarah Amerika', 'Howard Zinn', 'Harper Perennial', 'Sejarah', 'A5', 'Tersedia'),
(46, 'Sejarah Eropa', 'Norman Davies', 'Oxford University Press', 'Sejarah', 'A5', 'Tersedia'),
(47, 'Sejarah Timur Tengah', 'Bernard Lewis', 'Oxford University Press', 'Sejarah', 'A5', 'Tersedia'),
(48, 'Sejarah Afrika', 'John Iliffe', 'Cambridge University Press', 'Sejarah', 'A5', 'Tersedia'),
(49, 'Sejarah Australia', 'Henry Reynolds', 'Allen & Unwin', 'Sejarah', 'A5', 'Tersedia'),
(50, 'Sejarah Jepang', 'Marius B. Jansen', 'Harvard University Press', 'Sejarah', 'A5', 'Tersedia'),
(51, 'Biografi Soekarno', 'John Legge', 'Pustaka', 'Biografi', 'A6', 'Tersedia'),
(52, 'Steve Jobs', 'Walter Isaacson', 'Simon & Schuster', 'Biografi', 'A6', 'Tersedia'),
(53, 'Biografi Nelson Mandela', 'Anthony Sampson', 'Little Brown', 'Biografi', 'A6', 'Tersedia'),
(54, 'Biografi Albert Einstein', 'Walter Isaacson', 'Simon & Schuster', 'Biografi', 'A6', 'Tersedia'),
(55, 'Biografi Bill Gates', 'Walter Isaacson', 'Simon & Schuster', 'Biografi', 'A6', 'Tersedia'),
(56, 'Biografi Elon Musk', 'Ashlee Vance', 'Ecco', 'Biografi', 'A6', 'Tersedia'),
(57, 'Biografi Marie Curie', 'Eve Curie', 'Simon & Schuster', 'Biografi', 'A6', 'Tersedia'),
(58, 'Biografi Leonardo da Vinci', 'Walter Isaacson', 'Simon & Schuster', 'Biografi', 'A6', 'Tersedia'),
(59, 'Biografi Gandhi', 'Louis Fischer', 'Oxford University Press', 'Biografi', 'A6', 'Tersedia'),
(60, 'Biografi Churchill', 'Roy Jenkins', 'Penguin', 'Biografi', 'A6', 'Tersedia'),
(61, 'Kamus Besar Bahasa Indonesia', 'Pusat Bahasa', 'Balai Pustaka', 'Referensi', 'A7', 'Tersedia'),
(62, 'Kamus Inggris-Indonesia', 'John Smith', 'Oxford', 'Referensi', 'A7', 'Tersedia'),
(63, 'Glosarium Istilah IT', 'Tech Writer', 'Informatika', 'Referensi', 'A7', 'Tersedia'),
(64, 'Daftar Singkatan', 'Various', 'Gramedia', 'Referensi', 'A7', 'Tersedia'),
(65, 'Panduan Penulisan', 'Stephen King', 'Scribner', 'Referensi', 'A7', 'Tersedia'),
(66, 'Manual Bahasa Indonesia', 'Pusat Bahasa', 'Balai Pustaka', 'Referensi', 'A7', 'Tersedia'),
(67, 'Tesaurus Bahasa Indonesia', 'Pusat Bahasa', 'Balai Pustaka', 'Referensi', 'A7', 'Tersedia'),
(68, 'Ensiklopedia Indonesia', 'Various', 'Gramedia', 'Referensi', 'A7', 'Tersedia'),
(69, 'Atlas Dunia', 'National Geographic', 'National Geographic Society', 'Referensi', 'A7', 'Tersedia'),
(70, 'Panduan Tata Bahasa', 'Linguist A', 'Universitas Indonesia', 'Referensi', 'A7', 'Tersedia');

-- --------------------------------------------------------

--
-- Struktur dari tabel `koneksibasisdata`
--

CREATE TABLE `koneksibasisdata` (
  `id_koneksi` int(11) NOT NULL,
  `userName` varchar(50) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `database_name` varchar(100) DEFAULT NULL,
  `host` varchar(100) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `koneksibasisdata`
--

INSERT INTO `koneksibasisdata` (`id_koneksi`, `userName`, `password`, `database_name`, `host`) VALUES
(1, 'admin1', 'password123', 'db_marimaca', 'localhost'),
(2, 'admin2', 'adminpass2', 'db_marimaca', 'localhost'),
(3, 'admin3', 'adminpass3', 'db_marimaca', 'localhost'),
(4, 'admin4', 'adminpass4', 'db_marimaca', 'localhost'),
(5, 'admin5', 'adminpass5', 'db_marimaca', 'localhost'),
(6, 'admin6', 'adminpass6', 'db_marimaca', 'localhost'),
(7, 'admin7', 'adminpass7', 'db_marimaca', 'localhost'),
(8, 'admin8', 'adminpass8', 'db_marimaca', 'localhost'),
(9, 'admin9', 'adminpass9', 'db_marimaca', 'localhost'),
(10, 'admin10', 'adminpass10', 'db_marimaca', 'localhost');

-- --------------------------------------------------------

--
-- Struktur dari tabel `login`
--

CREATE TABLE `login` (
  `id_login` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `id_anggota` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `login`
--

INSERT INTO `login` (`id_login`, `username`, `email`, `password`, `id_anggota`) VALUES
(1, 'andiwijaya', 'andi.wijaya@example.com', 'password123', 1),
(2, 'sitiaminah', 'siti.aminah@example.com', 'password123', 2),
(3, 'budisantoso', 'budi.santoso@example.com', 'password123', 3),
(4, 'dewilestari', 'dewi.lestari@example.com', 'password123', 4),
(5, 'rianpratama', 'rian.pratama@example.com', 'password123', 5),
(6, 'ninakartika', 'nina.kartika@example.com', 'password123', 6),
(7, 'agussalim', 'agus.salim@example.com', 'password123', 7),
(8, 'linamarlina', 'lina.marlina@example.com', 'password123', 8),
(9, 'tonosusilo', 'tono.susilo@example.com', 'password123', 9),
(10, 'fitrihandayani', 'fitri.handayani@example.com', 'password123', 10);

-- --------------------------------------------------------

--
-- Struktur dari tabel `peminjaman`
--

CREATE TABLE `peminjaman` (
  `id_peminjaman` int(11) NOT NULL,
  `id_buku` int(11) NOT NULL,
  `id_anggota` int(11) NOT NULL,
  `tanggal_pinjam` date NOT NULL,
  `tanggal_jatuh_tempo` date NOT NULL,
  `tanggal_pengembalian` date DEFAULT NULL,
  `status_peminjaman` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `peminjaman`
--

INSERT INTO `peminjaman` (`id_peminjaman`, `id_buku`, `id_anggota`, `tanggal_pinjam`, `tanggal_jatuh_tempo`, `tanggal_pengembalian`, `status_peminjaman`) VALUES
(1, 1, 1, '2025-05-27', '2025-05-27', '2025-05-27', 'Dikembalikan'),
(2, 2, 11, '2025-05-27', '2025-05-29', '2025-05-27', 'Dikembalikan'),
(4, 70, 4, '2025-05-27', '2025-06-26', '2025-05-27', 'Dikembalikan'),
(5, 1, 11, '2025-05-27', '2025-06-01', '2025-05-27', 'Dikembalikan'),
(7, 1, 1, '2025-05-29', '2025-06-01', NULL, 'Terlambat'),
(8, 2, 11, '2025-05-29', '2025-06-29', NULL, 'Dipinjam'),
(9, 3, 11, '2025-05-29', '2025-06-29', NULL, 'Dipinjam'),
(10, 4, 11, '2025-05-29', '2025-05-30', '2025-06-02', 'Dikembalikan');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengumuman`
--

CREATE TABLE `pengumuman` (
  `id_pengumuman` int(11) NOT NULL,
  `judul` varchar(255) NOT NULL,
  `isi` text NOT NULL,
  `tanggal_dibuat` date DEFAULT curdate()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengumuman`
--

INSERT INTO `pengumuman` (`id_pengumuman`, `judul`, `isi`, `tanggal_dibuat`) VALUES
(1, 'Perpustakaan Tutup Sementara', 'Perpustakaan Marimaca akan tutup sementara pada tanggal 1 Juni 2025 karena maintenance sistem.', '2025-05-26'),
(2, 'Lomba Membaca Cepat', 'Daftarkan dirimu untuk mengikuti lomba membaca cepat tingkat fakultas. Pendaftaran dibuka hingga 10 Juni 2025.', '2025-05-26'),
(3, 'Update Jam Operasional', 'Mulai 3 Juni 2025, jam operasional perpustakaan berubah menjadi 08.00 - 18.00 WIB.', '2025-05-26'),
(4, 'Donasi Buku Terbuka', 'Perpustakaan menerima donasi buku dari mahasiswa dan dosen. Hubungi petugas untuk informasi lebih lanjut.', '2025-05-26'),
(5, 'Peminjaman Diperpanjang', 'Peminjaman buku selama libur semester diperpanjang hingga 30 hari.', '2025-05-26'),
(8, 'Apresiasi Pembaca', 'Mulai periode ini, setiap 6 bulan sekali, kami akan memberikan reward menarik untuk 3 orang pengunjung paling aktif yang sering datang dan memanfaatkan fasilitas perpustakaan.\n\nSalam literasi,\nTim Pustakawan', '2025-06-08');

-- --------------------------------------------------------

--
-- Struktur dari tabel `pengunjung`
--

CREATE TABLE `pengunjung` (
  `id_pengunjung` int(11) NOT NULL,
  `nama` varchar(100) NOT NULL,
  `no_telepon` varchar(20) NOT NULL,
  `tanggal_kunjungan` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `pengunjung`
--

INSERT INTO `pengunjung` (`id_pengunjung`, `nama`, `no_telepon`, `tanggal_kunjungan`) VALUES
(1, 'Andi Wijaya', '081234567890', '2025-05-01 03:15:00'),
(2, 'Siti Aminah', '082345678901', '2025-05-02 04:20:00'),
(3, 'Budi Santoso', '083456789012', '2025-05-03 02:05:00'),
(4, 'Dewi Lestari', '084567890123', '2025-05-04 07:30:00'),
(5, 'Rian Pratama', '085678901234', '2025-05-05 09:45:00'),
(6, 'Nina Kartika', '086789012345', '2025-05-06 06:10:00'),
(7, 'Agus Salim', '087890123456', '2025-05-07 01:55:00'),
(8, 'Lina Marlina', '088901234567', '2025-05-08 05:25:00'),
(9, 'Tono Susilo', '089012345678', '2025-05-09 08:00:00'),
(10, 'Fitri Handayani', '080123456789', '2025-05-10 10:40:00'),
(11, 'Nugrah Adinda', '0812345678', '2025-05-27 10:55:52'),
(12, 'Nugrah kedua', '081254475', '2025-05-27 10:57:15'),
(13, 'YUDI WAHYUDI', '08977031709', '2025-05-27 16:28:36');

-- --------------------------------------------------------

--
-- Struktur dari tabel `user`
--

CREATE TABLE `user` (
  `id_anggota` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `user`
--

INSERT INTO `user` (`id_anggota`, `username`, `password`) VALUES
(2, 'Siti Aminah', 'XWwvNTD3msyjO27kp8th/Q3o9TnYCISoo/zUvvUC89o='),
(5, 'Rian Pratama', '73l8gRjwLftklgfdXT+MdiMEjJwGPVMsyVxe16iYpk8='),
(11, 'Asep Imam Wahyudi', '7ojPWqKB5pCDN8/ro9Mcja9swyo9AmygPVWsTTbeFxg=');

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- Indeks untuk tabel `anggota`
--
ALTER TABLE `anggota`
  ADD PRIMARY KEY (`id_anggota`);

--
-- Indeks untuk tabel `buku`
--
ALTER TABLE `buku`
  ADD PRIMARY KEY (`id_buku`);

--
-- Indeks untuk tabel `koneksibasisdata`
--
ALTER TABLE `koneksibasisdata`
  ADD PRIMARY KEY (`id_koneksi`);

--
-- Indeks untuk tabel `login`
--
ALTER TABLE `login`
  ADD PRIMARY KEY (`id_login`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `id_anggota` (`id_anggota`);

--
-- Indeks untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD PRIMARY KEY (`id_peminjaman`),
  ADD KEY `fk_peminjaman_buku` (`id_buku`),
  ADD KEY `fk_peminjaman_anggota` (`id_anggota`);

--
-- Indeks untuk tabel `pengumuman`
--
ALTER TABLE `pengumuman`
  ADD PRIMARY KEY (`id_pengumuman`);

--
-- Indeks untuk tabel `pengunjung`
--
ALTER TABLE `pengunjung`
  ADD PRIMARY KEY (`id_pengunjung`);

--
-- Indeks untuk tabel `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id_anggota`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `admin`
--
ALTER TABLE `admin`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT untuk tabel `anggota`
--
ALTER TABLE `anggota`
  MODIFY `id_anggota` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;

--
-- AUTO_INCREMENT untuk tabel `buku`
--
ALTER TABLE `buku`
  MODIFY `id_buku` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=71;

--
-- AUTO_INCREMENT untuk tabel `koneksibasisdata`
--
ALTER TABLE `koneksibasisdata`
  MODIFY `id_koneksi` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `login`
--
ALTER TABLE `login`
  MODIFY `id_login` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  MODIFY `id_peminjaman` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT untuk tabel `pengumuman`
--
ALTER TABLE `pengumuman`
  MODIFY `id_pengumuman` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT untuk tabel `pengunjung`
--
ALTER TABLE `pengunjung`
  MODIFY `id_pengunjung` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `login`
--
ALTER TABLE `login`
  ADD CONSTRAINT `login_ibfk_1` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`);

--
-- Ketidakleluasaan untuk tabel `peminjaman`
--
ALTER TABLE `peminjaman`
  ADD CONSTRAINT `fk_peminjaman_anggota` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_peminjaman_buku` FOREIGN KEY (`id_buku`) REFERENCES `buku` (`id_buku`) ON UPDATE CASCADE;

--
-- Ketidakleluasaan untuk tabel `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `fk_user_references_anggota` FOREIGN KEY (`id_anggota`) REFERENCES `anggota` (`id_anggota`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
