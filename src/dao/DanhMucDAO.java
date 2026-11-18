package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.DanhMuc;
import util.KetNoi;

public class DanhMucDAO {

    private DanhMuc anh(ResultSet rs) throws SQLException {
        DanhMuc d = new DanhMuc();
        d.id = rs.getInt("id");
        d.maDanhMuc = rs.getString("ma_danh_muc");
        d.tenDanhMuc = rs.getString("ten_danh_muc");
        d.moTa = rs.getString("mo_ta");
        return d;
    }

    public List<DanhMuc> layTatCa(String tuKhoa) throws SQLException {
        List<DanhMuc> ds = new ArrayList<>();
        String sql = "SELECT * FROM danh_muc WHERE ma_danh_muc LIKE ? OR ten_danh_muc LIKE ? ORDER BY id";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            String k = "%" + tuKhoa + "%";
            ps.setString(1, k);
            ps.setString(2, k);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    ds.add(anh(rs));
                }
            }
        }
        return ds;
    }

    public void them(DanhMuc d) throws SQLException {
        String sql = "INSERT INTO danh_muc(ma_danh_muc, ten_danh_muc, mo_ta) VALUES(?, ?, ?)";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.maDanhMuc);
            ps.setString(2, d.tenDanhMuc);
            ps.setString(3, d.moTa);
            ps.executeUpdate();
        }
    }

    public void sua(DanhMuc d) throws SQLException {
        String sql = "UPDATE danh_muc SET ma_danh_muc=?, ten_danh_muc=?, mo_ta=? WHERE id=?";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, d.maDanhMuc);
            ps.setString(2, d.tenDanhMuc);
            ps.setString(3, d.moTa);
            ps.setInt(4, d.id);
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("DELETE FROM danh_muc WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
