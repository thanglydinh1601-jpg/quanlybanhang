package ui;

import dao.DanhMucDAO;
import javax.swing.JTextField;
import model.DanhMuc;
import util.TienIch;

public class PnlDanhMuc extends PnlQuanLy {
    private final DanhMucDAO dao = new DanhMucDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JTextField txtMoTa = new JTextField();

    public PnlDanhMuc() {
        super(new String[]{"ID", "Mã danh mục", "Tên danh mục", "Mô tả"});
        themTruong("Mã danh mục:", txtMa);
        themTruong("Tên danh mục:", txtTen);
        themTruong("Mô tả:", txtMoTa);
        khoiTao();
    }

    @Override
    protected void taiDuLieu(String tuKhoa) throws Exception {
        mh.setRowCount(0);
        for (DanhMuc d : dao.layTatCa(tuKhoa)) {
            mh.addRow(new Object[]{d.id, d.maDanhMuc, d.tenDanhMuc, d.moTa});
        }
    }

    @Override
    protected void hienThiDong(int hang) {
        txtMa.setText(giaTri(hang, 1));
        txtTen.setText(giaTri(hang, 2));
        txtMoTa.setText(giaTri(hang, 3));
    }

    private DanhMuc docForm() throws Exception {
        TienIch.batBuoc(txtMa.getText(), "Mã danh mục");
        TienIch.batBuoc(txtTen.getText(), "Tên danh mục");
        DanhMuc d = new DanhMuc();
        d.maDanhMuc = txtMa.getText().trim();
        d.tenDanhMuc = txtTen.getText().trim();
        d.moTa = txtMoTa.getText().trim();
        return d;
    }

    @Override
    protected void themMoi() throws Exception {
        dao.them(docForm());
    }

    @Override
    protected void capNhat(int id) throws Exception {
        DanhMuc d = docForm();
        d.id = id;
        dao.sua(d);
    }

    @Override
    protected void xoaBanGhi(int id) throws Exception {
        dao.xoa(id);
    }

    @Override
    protected void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtMoTa.setText("");
        bang.clearSelection();
    }
}
