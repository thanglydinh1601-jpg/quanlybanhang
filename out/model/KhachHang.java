package model;

import java.sql.Timestamp;

public class KhachHang {
    public int id;
    public String maKhachHang;
    public String hoTen;
    public String soDienThoai;
    public String email;
    public String diaChi;
    public int diemTichLuy;
    public Timestamp ngayTao;

    public KhachHang() {
    }

    @Override
    public String toString() {
        if (id == 0) {
            return hoTen;
        }
        return maKhachHang + " - " + hoTen;
    }
}
