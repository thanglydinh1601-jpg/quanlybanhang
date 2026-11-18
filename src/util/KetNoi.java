package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class KetNoi {
    private static final String URL=
            "jdbc:sqlserver://127.0.0.1:1433;databaseName=qlbanhang;encrypt=true;trustServerCertificate=true;";
    private static final String TAI_KHOAN = "qlbanhang";
    private static final String MAT_KHAU = "123456";

    public static Connection lay() throws SQLException {
        return DriverManager.getConnection(URL, TAI_KHOAN, MAT_KHAU);
    }
}
