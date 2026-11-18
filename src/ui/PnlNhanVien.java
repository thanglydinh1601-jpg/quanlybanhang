package ui;

import dao.NhanVienDAO;
import dao.VaiTroDAO;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import model.NhanVien;
import model.VaiTro;
import util.TienIch;

public class PnlNhanVien extends PnlQuanLy {
    private final NhanVienDAO dao = new NhanVienDAO();
    private final VaiTroDAO vaiTroDao = new VaiTroDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JComboBox<String> cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
    private final JTextField txtNgaySinh = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JTextField txtTaiKhoan = new JTextField();
    private final JPasswordField txtMatKhau = new JPasswordField();
    private final JComboBox<VaiTro> cboVaiTro = new JComboBox<>();
    private final JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Khóa"});

    public PnlNhanVien() {
        super(new String[]{"ID", "Mã nhân viên", "Họ tên", "Giới tính", "Ngày sinh", "Số điện thoại", "Email", "Địa chỉ", "Tài khoản", "Vai trò", "Trạng thái"});
        themTruong("Mã nhân viên:", txtMa);
        themTruong("Họ tên:", txtHoTen);
        themTruong("Giới tính:", cboGioiTinh);
        themTruong("Ngày sinh (yyyy-MM-dd):", txtNgaySinh);
        themTruong("Số điện thoại:", txtSdt);
        themTruong("Email:", txtEmail);
        themTruong("Địa chỉ:", txtDiaChi);
        themTruong("Tài khoản:", txtTaiKhoan);
        themTruong("Mật khẩu:", txtMatKhau);
        themTruong("Vai trò:", cboVaiTro);
        themTruong("Trạng thái:", cboTrangThai);
        taiVaiTro();
        khoiTao();
    }

    private void taiVaiTro() {
        try {
            cboVaiTro.removeAllItems();
            List<VaiTro> ds = vaiTroDao.layTatCa("");
            for (VaiTro v : ds) {
                cboVaiTro.addItem(v);
            }
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    @Override
    protected void taiDuLieu(String tuKhoa) throws Exception {
        taiVaiTro();
        mh.setRowCount(0);
        for (NhanVien n : dao.layTatCa(tuKhoa)) {
            mh.addRow(new Object[]{n.id, n.maNhanVien, n.hoTen, n.gioiTinh, n.ngaySinh, n.soDienThoai, n.email, n.diaChi, n.taiKhoan, n.tenVaiTro, n.trangThai == 1 ? "Hoạt động" : "Khóa"});
        }
    }

    @Override
    protected void hienThiDong(int hang) {
        txtMa.setText(giaTri(hang, 1));
        txtHoTen.setText(giaTri(hang, 2));
        cboGioiTinh.setSelectedItem(giaTri(hang, 3));
        txtNgaySinh.setText(giaTri(hang, 4));
        txtSdt.setText(giaTri(hang, 5));
        txtEmail.setText(giaTri(hang, 6));
        txtDiaChi.setText(giaTri(hang, 7));
        txtTaiKhoan.setText(giaTri(hang, 8));
        txtMatKhau.setText("");
        String tenVaiTro = giaTri(hang, 9);
        for (int i = 0; i < cboVaiTro.getItemCount(); i++) {
            if (cboVaiTro.getItemAt(i).tenVaiTro.equals(tenVaiTro)) {
                cboVaiTro.setSelectedIndex(i);
                break;
            }
        }
        cboTrangThai.setSelectedItem(giaTri(hang, 10));
    }

    private NhanVien docForm() throws Exception {
        TienIch.batBuoc(txtMa.getText(), "Mã nhân viên");
        TienIch.batBuoc(txtHoTen.getText(), "Họ tên");
        TienIch.batBuoc(txtTaiKhoan.getText(), "Tài khoản");
        NhanVien n = new NhanVien();
        n.maNhanVien = txtMa.getText().trim();
        n.hoTen = txtHoTen.getText().trim();
        n.gioiTinh = (String) cboGioiTinh.getSelectedItem();
        n.ngaySinh = TienIch.ngay(txtNgaySinh.getText(), "Ngày sinh");
        n.soDienThoai = txtSdt.getText().trim();
        n.email = txtEmail.getText().trim();
        n.diaChi = txtDiaChi.getText().trim();
        n.taiKhoan = txtTaiKhoan.getText().trim();
        n.matKhau = new String(txtMatKhau.getPassword());
        VaiTro v = (VaiTro) cboVaiTro.getSelectedItem();
        n.vaiTroId = v == null ? null : v.id;
        n.trangThai = cboTrangThai.getSelectedIndex() == 0 ? 1 : 0;
        return n;
    }

    @Override
    protected void themMoi() throws Exception {
        NhanVien n = docForm();
        TienIch.batBuoc(n.matKhau, "Mật khẩu");
        dao.them(n);
    }

    @Override
    protected void capNhat(int id) throws Exception {
        NhanVien n = docForm();
        n.id = id;
        if (TienIch.rong(n.matKhau)) {
            n.matKhau = dao.layMatKhau(id);
        }
        dao.sua(n);
    }

    @Override
    protected void xoaBanGhi(int id) throws Exception {
        dao.xoa(id);
    }

    @Override
    protected void xoaTrang() {
        txtMa.setText("");
        txtHoTen.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtNgaySinh.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtTaiKhoan.setText("");
        txtMatKhau.setText("");
        cboTrangThai.setSelectedIndex(0);
        bang.clearSelection();
    }
}
