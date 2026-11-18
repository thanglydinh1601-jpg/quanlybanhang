package model;

public class DanhMuc {
    public int id;
    public String maDanhMuc;
    public String tenDanhMuc;
    public String moTa;

    public DanhMuc() {
    }

    @Override
    public String toString() {
        return tenDanhMuc;
    }
}
