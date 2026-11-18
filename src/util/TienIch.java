package util;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.DecimalFormat;

public class TienIch {
    private static final DecimalFormat DINH_DANG_TIEN = new DecimalFormat("#,##0");

    public static boolean rong(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static void batBuoc(String giaTri, String tenTruong) throws Exception {
        if (rong(giaTri)) {
            throw new Exception(tenTruong + " không được để trống");
        }
    }

    public static BigDecimal soThapPhan(String s, String tenTruong) throws Exception {
        try {
            return new BigDecimal(s.trim().replace(",", ""));
        } catch (Exception e) {
            throw new Exception(tenTruong + " phải là số hợp lệ");
        }
    }

    public static int soNguyen(String s, String tenTruong) throws Exception {
        try {
            return Integer.parseInt(s.trim().replace(",", ""));
        } catch (Exception e) {
            throw new Exception(tenTruong + " phải là số nguyên hợp lệ");
        }
    }

    public static Date ngay(String s, String tenTruong) throws Exception {
        if (rong(s)) {
            return null;
        }
        try {
            return Date.valueOf(s.trim());
        } catch (Exception e) {
            throw new Exception(tenTruong + " phải có dạng yyyy-MM-dd");
        }
    }

    public static String tien(BigDecimal so) {
        if (so == null) {
            return "";
        }
        return DINH_DANG_TIEN.format(so);
    }

    public static String chuoi(Object o) {
        return o == null ? "" : o.toString();
    }
}
