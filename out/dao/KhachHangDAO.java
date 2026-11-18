package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.KhachHang;
import util.KetNoi;

public class KhachHangDAO {

    private KhachHang anh(ResultSet rs) throws SQLException {
        KhachHang k = new KhachHang();
        k.id = rs.getInt("id");
        k.maKhachHang = rs.getString("ma_khach_hang");
        k.hoTen = rs.getString("ho_ten");
        k.soDienThoai = rs.getString("so_dien_thoai");
        k.email = rs.getString("email");
        k.diaChi = rs.getString("dia_chi");
        k.diemTichLuy = rs.getInt("diem_tich_luy");
        k.ngayTao = rs.getTimestamp("ngay_tao");
        return k;
    }

    public List<KhachHang> layTatCa(String tuKhoa) throws SQLException {
        List<KhachHang> ds = new ArrayList<>();
        String sql = "SELECT * FROM khach_hang WHERE ma_khach_hang LIKE ? OR ho_ten LIKE ? OR so_dien_thoai LIKE ? ORDER BY id";
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

    public void them(KhachHang k) throws SQLException {
        String sql = "INSERT INTO khach_hang(ma_khach_hang, ho_ten, so_dien_thoai, email, dia_chi, diem_tich_luy) VALUES(?, ?, ?, ?, ?, ?)";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.maKhachHang);
            ps.setString(2, k.hoTen);
            ps.setString(3, k.soDienThoai);
            ps.setString(4, k.email);
            ps.setString(5, k.diaChi);
            ps.setInt(6, k.diemTichLuy);
            ps.executeUpdate();
        }
    }

    public void sua(KhachHang k) throws SQLException {
        String sql = "UPDATE khach_hang SET ma_khach_hang=?, ho_ten=?, so_dien_thoai=?, email=?, dia_chi=?, diem_tich_luy=? WHERE id=?";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, k.maKhachHang);
            ps.setString(2, k.hoTen);
            ps.setString(3, k.soDienThoai);
            ps.setString(4, k.email);
            ps.setString(5, k.diaChi);
            ps.setInt(6, k.diemTichLuy);
            ps.setInt(7, k.id);
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("DELETE FROM khach_hang WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
