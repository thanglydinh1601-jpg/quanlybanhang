package ui;

import dao.NhaCungCapDAO;
import javax.swing.JTextField;
import model.NhaCungCap;
import util.TienIch;

public class PnlNhaCungCap extends PnlQuanLy {
    private final NhaCungCapDAO dao = new NhaCungCapDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtDiaChi = new JTextField();

    public PnlNhaCungCap() {
        super(new String[]{"ID", "Mã NCC", "Tên nhà cung cấp", "Số điện thoại", "Email", "Địa chỉ"});
        themTruong("Mã NCC:", txtMa);
        themTruong("Tên nhà cung cấp:", txtTen);
        themTruong("Số điện thoại:", txtSdt);
        themTruong("Email:", txtEmail);
        themTruong("Địa chỉ:", txtDiaChi);
        khoiTao();
    }

    @Override
    protected void taiDuLieu(String tuKhoa) throws Exception {
        mh.setRowCount(0);
        for (NhaCungCap n : dao.layTatCa(tuKhoa)) {
            mh.addRow(new Object[]{n.id, n.maNcc, n.tenNcc, n.soDienThoai, n.email, n.diaChi});
        }
    }

    @Override
    protected void hienThiDong(int hang) {
        txtMa.setText(giaTri(hang, 1));
        txtTen.setText(giaTri(hang, 2));
        txtSdt.setText(giaTri(hang, 3));
        txtEmail.setText(giaTri(hang, 4));
        txtDiaChi.setText(giaTri(hang, 5));
    }

    private NhaCungCap docForm() throws Exception {
        TienIch.batBuoc(txtMa.getText(), "Mã nhà cung cấp");
        TienIch.batBuoc(txtTen.getText(), "Tên nhà cung cấp");
        NhaCungCap n = new NhaCungCap();
        n.maNcc = txtMa.getText().trim();
        n.tenNcc = txtTen.getText().trim();
        n.soDienThoai = txtSdt.getText().trim();
        n.email = txtEmail.getText().trim();
        n.diaChi = txtDiaChi.getText().trim();
        return n;
    }

    @Override
    protected void themMoi() throws Exception {
        dao.them(docForm());
    }

    @Override
    protected void capNhat(int id) throws Exception {
        NhaCungCap n = docForm();
        n.id = id;
        dao.sua(n);
    }

    @Override
    protected void xoaBanGhi(int id) throws Exception {
        dao.xoa(id);
    }

    @Override
    protected void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        bang.clearSelection();
    }
}
