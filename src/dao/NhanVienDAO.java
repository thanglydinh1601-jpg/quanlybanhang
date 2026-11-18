package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import model.NhanVien;
import util.KetNoi;

public class NhanVienDAO {
    private static final String CAU_TRUY_VAN = "SELECT nv.*, vt.ma_vai_tro, vt.ten_vai_tro FROM nhan_vien nv LEFT JOIN vai_tro vt ON nv.vai_tro_id = vt.id ";

    private NhanVien anh(ResultSet rs) throws SQLException {
        NhanVien n = new NhanVien();
        n.id = rs.getInt("id");
        n.maNhanVien = rs.getString("ma_nhan_vien");
        n.hoTen = rs.getString("ho_ten");
        n.gioiTinh = rs.getString("gioi_tinh");
        n.ngaySinh = rs.getDate("ngay_sinh");
        n.soDienThoai = rs.getString("so_dien_thoai");
        n.email = rs.getString("email");
        n.diaChi = rs.getString("dia_chi");
        n.taiKhoan = rs.getString("tai_khoan");
        n.matKhau = rs.getString("mat_khau");
        int vt = rs.getInt("vai_tro_id");
        n.vaiTroId = rs.wasNull() ? null : vt;
        n.maVaiTro = rs.getString("ma_vai_tro");
        n.tenVaiTro = rs.getString("ten_vai_tro");
        n.trangThai = rs.getInt("trang_thai");
        return n;
    }

    public NhanVien dangNhap(String taiKhoan, String matKhau) throws SQLException {
        String sql = CAU_TRUY_VAN + "WHERE nv.tai_khoan = ? AND nv.mat_khau = ? AND nv.trang_thai = 1";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, taiKhoan);
            ps.setString(2, matKhau);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return anh(rs);
                }
            }
        }
        return null;
    }

    public List<NhanVien> layTatCa(String tuKhoa) throws SQLException {
        List<NhanVien> ds = new ArrayList<>();
        String sql = CAU_TRUY_VAN + "WHERE nv.ma_nhan_vien LIKE ? OR nv.ho_ten LIKE ? OR nv.tai_khoan LIKE ? OR nv.so_dien_thoai LIKE ? ORDER BY nv.id";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + tuKhoa + "%";
            for (int i = 1; i <= 4; i++) {
                ps.setString(i, k);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(anh(rs));
                }
            }
        }
        return ds;
    }

    public void them(NhanVien n) throws SQLException {
        String sql = "INSERT INTO nhan_vien(ma_nhan_vien, ho_ten, gioi_tinh, ngay_sinh, so_dien_thoai, email, dia_chi, tai_khoan, mat_khau, vai_tro_id, trang_thai) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n.maNhanVien);
            ps.setString(2, n.hoTen);
            ps.setString(3, n.gioiTinh);
            ps.setDate(4, n.ngaySinh);
            ps.setString(5, n.soDienThoai);
            ps.setString(6, n.email);
            ps.setString(7, n.diaChi);
            ps.setString(8, n.taiKhoan);
            ps.setString(9, n.matKhau);
            ps.setObject(10, n.vaiTroId, Types.INTEGER);
            ps.setInt(11, n.trangThai);
            ps.executeUpdate();
        }
    }

    public void sua(NhanVien n) throws SQLException {
        String sql = "UPDATE nhan_vien SET ma_nhan_vien=?, ho_ten=?, gioi_tinh=?, ngay_sinh=?, so_dien_thoai=?, email=?, dia_chi=?, tai_khoan=?, mat_khau=?, vai_tro_id=?, trang_thai=? WHERE id=?";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n.maNhanVien);
            ps.setString(2, n.hoTen);
            ps.setString(3, n.gioiTinh);
            ps.setDate(4, n.ngaySinh);
            ps.setString(5, n.soDienThoai);
            ps.setString(6, n.email);
            ps.setString(7, n.diaChi);
            ps.setString(8, n.taiKhoan);
            ps.setString(9, n.matKhau);
            ps.setObject(10, n.vaiTroId, Types.INTEGER);
            ps.setInt(11, n.trangThai);
            ps.setInt(12, n.id);
            ps.executeUpdate();
        }
    }

    public String layMatKhau(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("SELECT mat_khau FROM nhan_vien WHERE id=?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString(1);
                }
            }
        }
        return null;
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("DELETE FROM nhan_vien WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
