
CREATE LOGIN qlbanhang WITH PASSWORD = '123456';
GO
USE qlbanhang;
GO
CREATE USER qlbanhang FOR LOGIN qlbanhang;
GO
ALTER ROLE db_owner ADD MEMBER qlbanhang;
GO

CREATE TABLE vai_tro (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_vai_tro VARCHAR(50) NOT NULL UNIQUE,
    ten_vai_tro NVARCHAR(100) NOT NULL
);


CREATE TABLE nhan_vien (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_nhan_vien VARCHAR(50) NOT NULL UNIQUE,
    ho_ten NVARCHAR(100) NOT NULL,
    gioi_tinh NVARCHAR(10),
    ngay_sinh DATE,
    so_dien_thoai VARCHAR(15),
    email VARCHAR(100),
    dia_chi NVARCHAR(MAX),
    tai_khoan VARCHAR(50) NOT NULL UNIQUE,
    mat_khau VARCHAR(255) NOT NULL, 
    vai_tro_id INT FOREIGN KEY REFERENCES vai_tro(id),
    trang_thai TINYINT DEFAULT 1 
);

CREATE TABLE khach_hang (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_khach_hang VARCHAR(50) NOT NULL UNIQUE,
    ho_ten NVARCHAR(100) NOT NULL,
    so_dien_thoai VARCHAR(15) NOT NULL,
    email VARCHAR(100),
    dia_chi NVARCHAR(MAX),
    diem_tich_luy INT DEFAULT 0,
    ngay_tao DATETIME DEFAULT GETDATE()
);
CREATE TABLE danh_muc (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_danh_muc VARCHAR(50) NOT NULL UNIQUE,
    ten_danh_muc NVARCHAR(100) NOT NULL,
    mo_ta NVARCHAR(MAX)
);

CREATE TABLE nha_cung_cap (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_ncc VARCHAR(50) NOT NULL UNIQUE,
    ten_ncc NVARCHAR(150) NOT NULL,
    so_dien_thoai VARCHAR(15),
    email VARCHAR(100),
    dia_chi NVARCHAR(MAX)
);

CREATE TABLE san_pham (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_san_pham VARCHAR(50) NOT NULL UNIQUE,
    ten_san_pham NVARCHAR(150) NOT NULL,
    danh_muc_id INT FOREIGN KEY REFERENCES danh_muc(id),
    gia_nhap DECIMAL(18, 2) NOT NULL,
    gia_ban DECIMAL(18, 2) NOT NULL,
    so_luong_ton INT DEFAULT 0,
    don_vi_tinh NVARCHAR(20),
    hinh_anh VARCHAR(255),
    trang_thai TINYINT DEFAULT 1 
);

CREATE TABLE hoa_don (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_hoa_don VARCHAR(50) NOT NULL UNIQUE,
    nhan_vien_id INT FOREIGN KEY REFERENCES nhan_vien(id),
    khach_hang_id INT FOREIGN KEY REFERENCES khach_hang(id),
    ngay_lap DATETIME DEFAULT GETDATE(),
    tong_tien DECIMAL(18, 2) NOT NULL,
    giam_gia DECIMAL(18, 2) DEFAULT 0,
    thanh_toan DECIMAL(18, 2) NOT NULL,
    ghi_chu NVARCHAR(MAX)
);
CREATE TABLE chi_tiet_hoa_don (
    id INT IDENTITY(1,1) PRIMARY KEY,
    hoa_don_id INT FOREIGN KEY REFERENCES hoa_don(id) ON DELETE CASCADE,
    san_pham_id INT FOREIGN KEY REFERENCES san_pham(id),
    so_luong INT NOT NULL,
    don_gia DECIMAL(18, 2) NOT NULL,
    thanh_tien DECIMAL(18, 2) NOT NULL
);
CREATE TABLE phieu_nhap (
    id INT IDENTITY(1,1) PRIMARY KEY,
    ma_phieu_nhap VARCHAR(50) NOT NULL UNIQUE,
    nhan_vien_id INT FOREIGN KEY REFERENCES nhan_vien(id),
    nha_cung_cap_id INT FOREIGN KEY REFERENCES nha_cung_cap(id),
    ngay_nhap DATETIME DEFAULT GETDATE(),
    tong_tien DECIMAL(18, 2) NOT NULL
);

CREATE TABLE chi_tiet_phieu_nhap (
    id INT IDENTITY(1,1) PRIMARY KEY,
    phieu_nhap_id INT FOREIGN KEY REFERENCES phieu_nhap(id) ON DELETE CASCADE,
    san_pham_id INT FOREIGN KEY REFERENCES san_pham(id),
    so_luong INT NOT NULL,
    gia_nhap DECIMAL(18, 2) NOT NULL,
    thanh_tien DECIMAL(18, 2) NOT NULL
);
GO
INSERT INTO vai_tro(ma_vai_tro, ten_vai_tro) VALUES ('ADMIN', N'Quản trị viên');
INSERT INTO vai_tro(ma_vai_tro, ten_vai_tro) VALUES ('NHANVIEN', N'Nhân viên bán hàng');

INSERT INTO nhan_vien(ma_nhan_vien, ho_ten, gioi_tinh, tai_khoan, mat_khau, vai_tro_id, trang_thai)
VALUES ('NV001', N'Quản trị hệ thống', N'Nam', 'admin', '123456', (SELECT id FROM vai_tro WHERE ma_vai_tro = 'ADMIN'), 1);

INSERT INTO danh_muc(ma_danh_muc, ten_danh_muc, mo_ta) VALUES ('DM001', N'Đồ uống', N'Các loại nước giải khát');
INSERT INTO danh_muc(ma_danh_muc, ten_danh_muc, mo_ta) VALUES ('DM002', N'Bánh kẹo', N'Bánh và kẹo các loại');

INSERT INTO nha_cung_cap(ma_ncc, ten_ncc, so_dien_thoai, email, dia_chi)
VALUES ('NCC001', N'Công ty Phân phối Hà Nội', '0901234567', 'ncc@example.com', N'Hà Nội');

INSERT INTO san_pham(ma_san_pham, ten_san_pham, danh_muc_id, gia_nhap, gia_ban, so_luong_ton, don_vi_tinh, trang_thai)
VALUES ('SP001', N'Nước suối', (SELECT id FROM danh_muc WHERE ma_danh_muc = 'DM001'), 4000, 6000, 100, N'Chai', 1);
INSERT INTO san_pham(ma_san_pham, ten_san_pham, danh_muc_id, gia_nhap, gia_ban, so_luong_ton, don_vi_tinh, trang_thai)
VALUES ('SP002', N'Bánh quy', (SELECT id FROM danh_muc WHERE ma_danh_muc = 'DM002'), 12000, 18000, 50, N'Gói', 1);

INSERT INTO khach_hang(ma_khach_hang, ho_ten, so_dien_thoai, dia_chi)
VALUES ('KH001', N'Nguyễn Văn A', '0912345678', N'Hà Nội');