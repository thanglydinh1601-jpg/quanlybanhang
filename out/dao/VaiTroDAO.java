package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.VaiTro;
import util.KetNoi;

public class VaiTroDAO {

    private VaiTro anh(ResultSet rs) throws SQLException {
        VaiTro v = new VaiTro();
        v.id = rs.getInt("id");
        v.maVaiTro = rs.getString("ma_vai_tro");
        v.tenVaiTro = rs.getString("ten_vai_tro");
        return v;
    }

    public List<VaiTro> layTatCa(String tuKhoa) throws SQLException {
        List<VaiTro> ds = new ArrayList<>();
        String sql = "SELECT * FROM vai_tro WHERE ma_vai_tro LIKE ? OR ten_vai_tro LIKE ? ORDER BY id";
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

    public void them(VaiTro v) throws SQLException {
        String sql = "INSERT INTO vai_tro(ma_vai_tro, ten_vai_tro) VALUES(?, ?)";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.maVaiTro);
            ps.setString(2, v.tenVaiTro);
            ps.executeUpdate();
        }
    }

    public void sua(VaiTro v) throws SQLException {
        String sql = "UPDATE vai_tro SET ma_vai_tro=?, ten_vai_tro=? WHERE id=?";
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, v.maVaiTro);
            ps.setString(2, v.tenVaiTro);
            ps.setInt(3, v.id);
            ps.executeUpdate();
        }
    }

    public void xoa(int id) throws SQLException {
        try (Connection c = KetNoi.lay(); PreparedStatement ps = c.prepareStatement("DELETE FROM vai_tro WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
