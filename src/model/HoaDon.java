package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class HoaDon {
    public int id;
    public String maHoaDon;
    public Integer nhanVienId;
    public Integer khachHangId;
    public Timestamp ngayLap;
    public BigDecimal tongTien;
    public BigDecimal giamGia;
    public BigDecimal thanhToan;
    public String ghiChu;
    public String tenNhanVien;
    public String tenKhachHang;

    public HoaDon() {
    }
}
