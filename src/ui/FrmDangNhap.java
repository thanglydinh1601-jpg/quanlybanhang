package ui;

import dao.NhanVienDAO;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.NhanVien;
import util.PhienLamViec;

public class FrmDangNhap extends JFrame {
    private final JTextField txtTaiKhoan = new JTextField(18);
    private final JPasswordField txtMatKhau = new JPasswordField(18);
    private final NhanVienDAO dao = new NhanVienDAO();

    public FrmDangNhap() {
        setTitle("Đăng nhập hệ thống quản lý bán hàng");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JPanel pnl = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(8, 8, 8, 8);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel tieuDe = new JLabel("ĐĂNG NHẬP", JLabel.CENTER);
        tieuDe.setFont(tieuDe.getFont().deriveFont(20f));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        pnl.add(tieuDe, g);

        g.gridwidth = 1;
        g.gridy = 1;
        pnl.add(new JLabel("Tài khoản:"), g);
        g.gridx = 1;
        pnl.add(txtTaiKhoan, g);

        g.gridx = 0;
        g.gridy = 2;
        pnl.add(new JLabel("Mật khẩu:"), g);
        g.gridx = 1;
        pnl.add(txtMatKhau, g);

        JButton btnDangNhap = new JButton("Đăng nhập");
        JButton btnThoat = new JButton("Thoát");
        g.gridx = 0;
        g.gridy = 3;
        pnl.add(btnDangNhap, g);
        g.gridx = 1;
        pnl.add(btnThoat, g);

        btnDangNhap.addActionListener(e -> dangNhap());
        txtMatKhau.addActionListener(e -> dangNhap());
        btnThoat.addActionListener(e -> System.exit(0));

        setContentPane(pnl);
        getRootPane().setDefaultButton(btnDangNhap);
        pack();
        setLocationRelativeTo(null);
    }

    private void dangNhap() {
        String taiKhoan = txtTaiKhoan.getText().trim();
        String matKhau = new String(txtMatKhau.getPassword());
        if (taiKhoan.isEmpty() || matKhau.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tài khoản và mật khẩu", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            NhanVien nv = dao.dangNhap(taiKhoan, matKhau);
            if (nv == null) {
                JOptionPane.showMessageDialog(this, "Sai tài khoản, mật khẩu hoặc tài khoản đã bị khóa", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            PhienLamViec.nhanVienHienTai = nv;
            new FrmChinh().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Không thể kết nối cơ sở dữ liệu: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
