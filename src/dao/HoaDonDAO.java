package dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.ChiTietHoaDon;
import model.HoaDon;
import util.KetNoi;

public class HoaDonDAO {
    private static final BigDecimal TIEN_MOI_DIEM = new BigDecimal(10000);

    private HoaDon anh(ResultSet rs) throws SQLException {
        HoaDon h = new HoaDon();
        h.id = rs.getInt("id");
        h.maHoaDon = rs.getString("ma_hoa_don");
        int nv = rs.getInt("nhan_vien_id");
        h.nhanVienId = rs.wasNull() ? null : nv;
        int kh = rs.getInt("khach_hang_id");
        h.khachHangId = rs.wasNull() ? null : kh;
        h.ngayLap = rs.getTimestamp("ngay_lap");
        h.tongTien = rs.getBigDecimal("tong_tien");
        h.giamGia = rs.getBigDecimal("giam_gia");
        h.thanhToan = rs.getBigDecimal("thanh_toan");
        h.ghiChu = rs.getString("ghi_chu");
        h.tenNhanVien = rs.getString("ten_nhan_vien");
        h.tenKhachHang = rs.getString("ten_khach_hang");
        return h;
    }

    public List<HoaDon> layTatCa(String tuKhoa) throws SQLException {
        List<HoaDon> ds = new ArrayList<>();
        String sql = "SELECT hd.*, nv.ho_ten AS ten_nhan_vien, kh.ho_ten AS ten_khach_hang FROM hoa_don hd "
                + "LEFT JOIN nhan_vien nv ON hd.nhan_vien_id = nv.id "
                + "LEFT JOIN khach_hang kh ON hd.khach_hang_id = kh.id "
                + "WHERE hd.ma_hoa_don LIKE ? OR nv.ho_ten LIKE ? OR kh.ho_ten LIKE ? ORDER BY hd.id DESC";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + tuKhoa + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            ps.setString(3, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(anh(rs));
                }
            }
        }
        return ds;
    }

    public List<ChiTietHoaDon> layChiTiet(int hoaDonId) throws SQLException {
        List<ChiTietHoaDon> ds = new ArrayList<>();
        String sql = "SELECT ct.*, sp.ma_san_pham, sp.ten_san_pham FROM chi_tiet_hoa_don ct "
                + "JOIN san_pham sp ON ct.san_pham_id = sp.id WHERE ct.hoa_don_id = ? ORDER BY ct.id";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, hoaDonId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.id = rs.getInt("id");
                    ct.hoaDonId = rs.getInt("hoa_don_id");
                    ct.sanPhamId = rs.getInt("san_pham_id");
                    ct.soLuong = rs.getInt("so_luong");
                    ct.donGia = rs.getBigDecimal("don_gia");
                    ct.thanhTien = rs.getBigDecimal("thanh_tien");
                    ct.maSanPham = rs.getString("ma_san_pham");
                    ct.tenSanPham = rs.getString("ten_san_pham");
                    ds.add(ct);
                }
            }
        }
        return ds;
    }

    public void taoMoi(HoaDon hd, List<ChiTietHoaDon> ds) throws SQLException {
        try (Connection c = KetNoi.lay()) {
            c.setAutoCommit(false);
            try {
                int hoaDonId;
                String sqlHd = "INSERT INTO hoa_don(ma_hoa_don, nhan_vien_id, khach_hang_id, tong_tien, giam_gia, thanh_toan, ghi_chu) VALUES(?,?,?,?,?,?,?)";
                try (PreparedStatement ps = c.prepareStatement(sqlHd, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, hd.maHoaDon);
                    ps.setObject(2, hd.nhanVienId, Types.INTEGER);
                    ps.setObject(3, hd.khachHangId, Types.INTEGER);
                    ps.setBigDecimal(4, hd.tongTien);
                    ps.setBigDecimal(5, hd.giamGia);
                    ps.setBigDecimal(6, hd.thanhToan);
                    ps.setString(7, hd.ghiChu);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        hoaDonId = rs.getInt(1);
                    }
                }
                String sqlCt = "INSERT INTO chi_tiet_hoa_don(hoa_don_id, san_pham_id, so_luong, don_gia, thanh_tien) VALUES(?,?,?,?,?)";
                String sqlTon = "UPDATE san_pham SET so_luong_ton = so_luong_ton - ? WHERE id = ? AND so_luong_ton >= ?";
                for (ChiTietHoaDon ct : ds) {
                    try (PreparedStatement ps = c.prepareStatement(sqlCt)) {
                        ps.setInt(1, hoaDonId);
                        ps.setInt(2, ct.sanPhamId);
                        ps.setInt(3, ct.soLuong);
                        ps.setBigDecimal(4, ct.donGia);
                        ps.setBigDecimal(5, ct.thanhTien);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = c.prepareStatement(sqlTon)) {
                        ps.setInt(1, ct.soLuong);
                        ps.setInt(2, ct.sanPhamId);
                        ps.setInt(3, ct.soLuong);
                        if (ps.executeUpdate() == 0) {
                            throw new SQLException("Sản phẩm " + ct.tenSanPham + " không đủ số lượng tồn kho");
                        }
                    }
                }
                if (hd.khachHangId != null) {
                    int diem = hd.thanhToan.divide(TIEN_MOI_DIEM, 0, RoundingMode.DOWN).intValue();
                    try (PreparedStatement ps = c.prepareStatement("UPDATE khach_hang SET diem_tich_luy = diem_tich_luy + ? WHERE id = ?")) {
                        ps.setInt(1, diem);
                        ps.setInt(2, hd.khachHangId);
                        ps.executeUpdate();
                    }
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }

    public void xoa(int hoaDonId) throws SQLException {
        try (Connection c = KetNoi.lay()) {
            c.setAutoCommit(false);
            try {
                Integer khachHangId = null;
                BigDecimal thanhToan = BigDecimal.ZERO;
                try (PreparedStatement ps = c.prepareStatement("SELECT khach_hang_id, thanh_toan FROM hoa_don WHERE id = ?")) {
                    ps.setInt(1, hoaDonId);
                    try (ResultSet rs = ps.executeQuery()) {
                        if (rs.next()) {
                            int kh = rs.getInt(1);
                            khachHangId = rs.wasNull() ? null : kh;
                            thanhToan = rs.getBigDecimal(2);
                        }
                    }
                }
                String sqlTon = "UPDATE san_pham SET so_luong_ton = so_luong_ton + ct.so_luong FROM san_pham sp JOIN chi_tiet_hoa_don ct ON ct.san_pham_id = sp.id WHERE ct.hoa_don_id = ?";
                try (PreparedStatement ps = c.prepareStatement(sqlTon)) {
                    ps.setInt(1, hoaDonId);
                    ps.executeUpdate();
                }
                if (khachHangId != null) {
                    int diem = thanhToan.divide(TIEN_MOI_DIEM, 0, RoundingMode.DOWN).intValue();
                    String sqlDiem = "UPDATE khach_hang SET diem_tich_luy = CASE WHEN diem_tich_luy >= ? THEN diem_tich_luy - ? ELSE 0 END WHERE id = ?";
                    try (PreparedStatement ps = c.prepareStatement(sqlDiem)) {
                        ps.setInt(1, diem);
                        ps.setInt(2, diem);
                        ps.setInt(3, khachHangId);
                        ps.executeUpdate();
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM hoa_don WHERE id = ?")) {
                    ps.setInt(1, hoaDonId);
                    ps.executeUpdate();
                }
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            }
        }
    }
}
