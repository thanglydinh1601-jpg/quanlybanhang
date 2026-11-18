package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;
import model.NhanVien;
import util.PhienLamViec;

public class FrmChinh extends JFrame {

    public FrmChinh() {
        NhanVien nv = PhienLamViec.nhanVienHienTai;
        setTitle("Hệ thống quản lý bán hàng");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1100, 700));

        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Hóa đơn bán hàng", new PnlHoaDon());
        tab.addTab("Sản phẩm", new PnlSanPham());
        tab.addTab("Khách hàng", new PnlKhachHang());
        if (PhienLamViec.laQuanTri()) {
            tab.addTab("Phiếu nhập", new PnlPhieuNhap());
            tab.addTab("Danh mục", new PnlDanhMuc());
            tab.addTab("Nhà cung cấp", new PnlNhaCungCap());
            tab.addTab("Nhân viên", new PnlNhanVien());
            tab.addTab("Vai trò", new PnlVaiTro());
        }

        JPanel pnlTren = new JPanel(new BorderLayout());
        String vaiTro = nv.tenVaiTro == null ? "" : " (" + nv.tenVaiTro + ")";
        pnlTren.add(new JLabel("  Xin chào: " + nv.hoTen + vaiTro), BorderLayout.WEST);
        JButton btnDangXuat = new JButton("Đăng xuất");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pnlNut.add(btnDangXuat);
        pnlTren.add(pnlNut, BorderLayout.EAST);

        btnDangXuat.addActionListener(e -> {
            int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (chon == JOptionPane.YES_OPTION) {
                PhienLamViec.nhanVienHienTai = null;
                new FrmDangNhap().setVisible(true);
                dispose();
            }
        });

        setLayout(new BorderLayout());
        add(pnlTren, BorderLayout.NORTH);
        add(tab, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(null);
    }
}
