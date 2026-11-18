package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.SanPham;
import util.KetNoi;

public class SanPhamDAO {
    private static final String CAU_TRUY_VAN = "SELECT sp.*, dm.ten_danh_muc FROM san_pham sp LEFT JOIN danh_muc dm ON sp.danh_muc_id = dm.id ";

    private SanPham anh(ResultSet rs) throws SQLException {
        SanPham s = new SanPham();
        s.id = rs.getInt("id");
        s.maSanPham = rs.getString("ma_san_pham");
        s.tenSanPham = rs.getString("ten_san_pham");
        int dm = rs.getInt("danh_muc_id");
        s.danhMucId = rs.wasNull() ? null : dm;
        s.tenDanhMuc = rs.getString("ten_danh_muc");
        s.giaNhap = rs.getBigDecimal("gia_nhap");
        s.giaBan = rs.getBigDecimal("gia_ban");
        s.soLuongTon = rs.getInt("so_luong_ton");
        s.donViTinh = rs.getString("don_vi_tinh");
        s.hinhAnh = rs.getString("hinh_anh");
        s.trangThai = rs.getInt("trang_thai");
        return s;
    }

    public List<SanPham> layTatCa(String tuKhoa) throws SQLException {
        List<SanPham> ds = new ArrayList<>();
        String sql = CAU_TRUY_VAN + "WHERE sp.ma_san_pham LIKE ? OR sp.ten_san_pham LIKE ? OR dm.ten_danh_muc LIKE ? ORDER BY sp.id";
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

    public List<SanPham> layDangBan() throws SQLException {
        List<SanPham> ds = new ArrayList<>();
        String sql = CAU_TRUY_VAN + "WHERE sp.trang_thai = 1 ORDER BY sp.ten_san_pham";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ds.add(anh(rs));
            }
        }
        return ds;
    }

    public void them(SanPham s) throws SQLException {
        String sql = "INSERT INTO san_pham(ma_san_pham, ten_san_pham, danh_muc_id, gia_nhap, gia_ban, so_luong_ton, don_vi_tinh, hinh_anh, trang_thai) VALUES(?,?,?,?,?,?,?,?,?)";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.maSanPham);
            ps.setString(2, s.tenSanPham);
            ps.setObject(3, s.danhMucId, Types.INTEGER);
            ps.setBigDecimal(4, s.giaNhap);
            ps.setBigDecimal(5, s.giaBan);
            ps.setInt(6, s.soLuongTon);
            ps.setString(7, s.donViTinh);
            ps.setString(8, s.hinhAnh);
            ps.setInt(9, s.trangThai);
            ps.executeUpdate();
        }
    }

    public void sua(SanPham s) throws SQLException {
        String sql = "UPDATE san_pham SET ma_san_pham=?, ten_san_pham=?, danh_muc_id=?, gia_nhap=?, gia_ban=?, so_luong_ton=?, don_vi_tinh=?, hinh_anh=?, trang_thai=? WHERE id=?";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, s.maSanPham);
            ps.setString(2, s.tenSanPham);
            ps.setObject(3, s.danhMucId, Types.INTEGER);
            ps.setBigDecimal(4, s.giaNhap);
            ps.setBigDecimal(5, s.giaBan);
            ps.setInt(6, s.soLuongTon);
            ps.setString(7, s.donViTinh);
            ps.setString(8, s.hinhAnh);
            ps.setInt(9, s.trangThai);
            ps.setInt(10, s.id);
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("DELETE FROM san_pham WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
