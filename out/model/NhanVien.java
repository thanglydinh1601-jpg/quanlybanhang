package model;

import java.sql.Date;

public class NhanVien {
    public int id;
    public String maNhanVien;
    public String hoTen;
    public String gioiTinh;
    public Date ngaySinh;
    public String soDienThoai;
    public String email;
    public String diaChi;
    public String taiKhoan;
    public String matKhau;
    public Integer vaiTroId;
    public String maVaiTro;
    public String tenVaiTro;
    public int trangThai;

    public NhanVien() {
    }

    @Override
    public String toString() {
        return maNhanVien + " - " + hoTen;
    }
}
