package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class PhieuNhap {
    public int id;
    public String maPhieuNhap;
    public Integer nhanVienId;
    public Integer nhaCungCapId;
    public Timestamp ngayNhap;
    public BigDecimal tongTien;
    public String tenNhanVien;
    public String tenNhaCungCap;

    public PhieuNhap() {
    }
}
