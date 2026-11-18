package ui;

import dao.VaiTroDAO;
import javax.swing.JTextField;
import model.VaiTro;
import util.TienIch;

public class PnlVaiTro extends PnlQuanLy {
    private final VaiTroDAO dao = new VaiTroDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();

    public PnlVaiTro() {
        super(new String[]{"ID", "Mã vai trò", "Tên vai trò"});
        themTruong("Mã vai trò:", txtMa);
        themTruong("Tên vai trò:", txtTen);
        khoiTao();
    }

    @Override
    protected void taiDuLieu(String tuKhoa) throws Exception {
        mh.setRowCount(0);
        for (VaiTro v : dao.layTatCa(tuKhoa)) {
            mh.addRow(new Object[]{v.id, v.maVaiTro, v.tenVaiTro});
        }
    }

    @Override
    protected void hienThiDong(int hang) {
        txtMa.setText(giaTri(hang, 1));
        txtTen.setText(giaTri(hang, 2));
    }

    private VaiTro docForm() throws Exception {
        TienIch.batBuoc(txtMa.getText(), "Mã vai trò");
        TienIch.batBuoc(txtTen.getText(), "Tên vai trò");
        VaiTro v = new VaiTro();
        v.maVaiTro = txtMa.getText().trim();
        v.tenVaiTro = txtTen.getText().trim();
        return v;
    }

    @Override
    protected void themMoi() throws Exception {
        dao.them(docForm());
    }

    @Override
    protected void capNhat(int id) throws Exception {
        VaiTro v = docForm();
        v.id = id;
        dao.sua(v);
    }

    @Override
    protected void xoaBanGhi(int id) throws Exception {
        dao.xoa(id);
    }

    @Override
    protected void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        bang.clearSelection();
    }
}
