package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.ChiTietPhieuNhap;
import model.PhieuNhap;
import util.KetNoi;

public class PhieuNhapDAO {

    private PhieuNhap anh(ResultSet rs) throws SQLException {
        PhieuNhap p = new PhieuNhap();
        p.id = rs.getInt("id");
        p.maPhieuNhap = rs.getString("ma_phieu_nhap");
        int nv = rs.getInt("nhan_vien_id");
        p.nhanVienId = rs.wasNull() ? null : nv;
        int ncc = rs.getInt("nha_cung_cap_id");
        p.nhaCungCapId = rs.wasNull() ? null : ncc;
        p.ngayNhap = rs.getTimestamp("ngay_nhap");
        p.tongTien = rs.getBigDecimal("tong_tien");
        p.tenNhanVien = rs.getString("ten_nhan_vien");
        p.tenNhaCungCap = rs.getString("ten_ncc");
        return p;
    }

    public List<PhieuNhap> layTatCa(String tuKhoa) throws SQLException {
        List<PhieuNhap> ds = new ArrayList<>();
        String sql = "SELECT pn.*, nv.ho_ten AS ten_nhan_vien, ncc.ten_ncc FROM phieu_nhap pn "
                + "LEFT JOIN nhan_vien nv ON pn.nhan_vien_id = nv.id "
                + "LEFT JOIN nha_cung_cap ncc ON pn.nha_cung_cap_id = ncc.id "
                + "WHERE pn.ma_phieu_nhap LIKE ? OR nv.ho_ten LIKE ? OR ncc.ten_ncc LIKE ? ORDER BY pn.id DESC";
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

    public List<ChiTietPhieuNhap> layChiTiet(int phieuNhapId) throws SQLException {
        List<ChiTietPhieuNhap> ds = new ArrayList<>();
        String sql = "SELECT ct.*, sp.ma_san_pham, sp.ten_san_pham FROM chi_tiet_phieu_nhap ct "
                + "JOIN san_pham sp ON ct.san_pham_id = sp.id WHERE ct.phieu_nhap_id = ? ORDER BY ct.id";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, phieuNhapId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ChiTietPhieuNhap ct = new ChiTietPhieuNhap();
                    ct.id = rs.getInt("id");
                    ct.phieuNhapId = rs.getInt("phieu_nhap_id");
                    ct.sanPhamId = rs.getInt("san_pham_id");
                    ct.soLuong = rs.getInt("so_luong");
                    ct.giaNhap = rs.getBigDecimal("gia_nhap");
                    ct.thanhTien = rs.getBigDecimal("thanh_tien");
                    ct.maSanPham = rs.getString("ma_san_pham");
                    ct.tenSanPham = rs.getString("ten_san_pham");
                    ds.add(ct);
                }
            }
        }
        return ds;
    }

    public void taoMoi(PhieuNhap pn, List<ChiTietPhieuNhap> ds) throws SQLException {
        try (Connection c = KetNoi.lay()) {
            c.setAutoCommit(false);
            try {
                int phieuId;
                String sqlPn = "INSERT INTO phieu_nhap(ma_phieu_nhap, nhan_vien_id, nha_cung_cap_id, tong_tien) VALUES(?,?,?,?)";
                try (PreparedStatement ps = c.prepareStatement(sqlPn, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, pn.maPhieuNhap);
                    ps.setObject(2, pn.nhanVienId, Types.INTEGER);
                    ps.setObject(3, pn.nhaCungCapId, Types.INTEGER);
                    ps.setBigDecimal(4, pn.tongTien);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        rs.next();
                        phieuId = rs.getInt(1);
                    }
                }
                String sqlCt = "INSERT INTO chi_tiet_phieu_nhap(phieu_nhap_id, san_pham_id, so_luong, gia_nhap, thanh_tien) VALUES(?,?,?,?,?)";
                String sqlTon = "UPDATE san_pham SET so_luong_ton = so_luong_ton + ?, gia_nhap = ? WHERE id = ?";
                for (ChiTietPhieuNhap ct : ds) {
                    try (PreparedStatement ps = c.prepareStatement(sqlCt)) {
                        ps.setInt(1, phieuId);
                        ps.setInt(2, ct.sanPhamId);
                        ps.setInt(3, ct.soLuong);
                        ps.setBigDecimal(4, ct.giaNhap);
                        ps.setBigDecimal(5, ct.thanhTien);
                        ps.executeUpdate();
                    }
                    try (PreparedStatement ps = c.prepareStatement(sqlTon)) {
                        ps.setInt(1, ct.soLuong);
                        ps.setBigDecimal(2, ct.giaNhap);
                        ps.setInt(3, ct.sanPhamId);
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

    public void xoa(int phieuNhapId) throws SQLException {
        try (Connection c = KetNoi.lay()) {
            c.setAutoCommit(false);
            try {
                List<ChiTietPhieuNhap> ds = layChiTiet(phieuNhapId);
                String sqlTon = "UPDATE san_pham SET so_luong_ton = so_luong_ton - ? WHERE id = ? AND so_luong_ton >= ?";
                for (ChiTietPhieuNhap ct : ds) {
                    try (PreparedStatement ps = c.prepareStatement(sqlTon)) {
                        ps.setInt(1, ct.soLuong);
                        ps.setInt(2, ct.sanPhamId);
                        ps.setInt(3, ct.soLuong);
                        if (ps.executeUpdate() == 0) {
                            throw new SQLException("Không thể xóa phiếu nhập vì tồn kho của " + ct.tenSanPham + " không đủ để hoàn lại");
                        }
                    }
                }
                try (PreparedStatement ps = c.prepareStatement("DELETE FROM phieu_nhap WHERE id = ?")) {
                    ps.setInt(1, phieuNhapId);
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
