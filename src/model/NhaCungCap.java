package model;

public class NhaCungCap {
    public int id;
    public String maNcc;
    public String tenNcc;
    public String soDienThoai;
    public String email;
    public String diaChi;

    public NhaCungCap() {
    }

    @Override
    public String toString() {
        return maNcc + " - " + tenNcc;
    }
}
