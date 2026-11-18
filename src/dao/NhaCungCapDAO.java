package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.NhaCungCap;
import util.KetNoi;

public class NhaCungCapDAO {

    private NhaCungCap anh(ResultSet rs) throws SQLException {
        NhaCungCap n = new NhaCungCap();
        n.id = rs.getInt("id");
        n.maNcc = rs.getString("ma_ncc");
        n.tenNcc = rs.getString("ten_ncc");
        n.soDienThoai = rs.getString("so_dien_thoai");
        n.email = rs.getString("email");
        n.diaChi = rs.getString("dia_chi");
        return n;
    }

    public List<NhaCungCap> layTatCa(String tuKhoa) throws SQLException {
        List<NhaCungCap> ds = new ArrayList<>();
        String sql = "SELECT * FROM nha_cung_cap WHERE ma_ncc LIKE ? OR ten_ncc LIKE ? OR so_dien_thoai LIKE ? ORDER BY id";
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

    public void them(NhaCungCap n) throws SQLException {
        String sql = "INSERT INTO nha_cung_cap(ma_ncc, ten_ncc, so_dien_thoai, email, dia_chi) VALUES(?, ?, ?, ?, ?)";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n.maNcc);
            ps.setString(2, n.tenNcc);
            ps.setString(3, n.soDienThoai);
            ps.setString(4, n.email);
            ps.setString(5, n.diaChi);
            ps.executeUpdate();
        }
    }

    public void sua(NhaCungCap n) throws SQLException {
        String sql = "UPDATE nha_cung_cap SET ma_ncc=?, ten_ncc=?, so_dien_thoai=?, email=?, dia_chi=? WHERE id=?";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, n.maNcc);
            ps.setString(2, n.tenNcc);
            ps.setString(3, n.soDienThoai);
            ps.setString(4, n.email);
            ps.setString(5, n.diaChi);
            ps.setInt(6, n.id);
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("DELETE FROM nha_cung_cap WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
