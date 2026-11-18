package model;

import java.math.BigDecimal;

public class SanPham {
    public int id;
    public String maSanPham;
    public String tenSanPham;
    public Integer danhMucId;
    public String tenDanhMuc;
    public BigDecimal giaNhap;
    public BigDecimal giaBan;
    public int soLuongTon;
    public String donViTinh;
    public String hinhAnh;
    public int trangThai;

    public SanPham() {
    }

    @Override
    public String toString() {
        return maSanPham + " - " + tenSanPham + " (tồn: " + soLuongTon + ")";
    }
}
