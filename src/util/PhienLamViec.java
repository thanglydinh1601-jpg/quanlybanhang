package util;

import model.NhanVien;

public class PhienLamViec {
    public static NhanVien nhanVienHienTai;

    public static boolean laQuanTri() {
        return nhanVienHienTai != null && nhanVienHienTai.maVaiTro != null
                && nhanVienHienTai.maVaiTro.equalsIgnoreCase("ADMIN");
    }
}
