package ui;

import dao.DanhMucDAO;
import dao.SanPhamDAO;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import model.DanhMuc;
import model.SanPham;
import util.TienIch;

public class PnlSanPham extends PnlQuanLy {
    private final SanPhamDAO dao = new SanPhamDAO();
    private final DanhMucDAO danhMucDao = new DanhMucDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtTen = new JTextField();
    private final JComboBox<DanhMuc> cboDanhMuc = new JComboBox<>();
    private final JTextField txtGiaNhap = new JTextField();
    private final JTextField txtGiaBan = new JTextField();
    private final JTextField txtTon = new JTextField("0");
    private final JTextField txtDonVi = new JTextField();
    private final JTextField txtHinhAnh = new JTextField();
    private final JComboBox<String> cboTrangThai = new JComboBox<>(new String[]{"Đang bán", "Ngừng bán"});

    public PnlSanPham() {
        super(new String[]{"ID", "Mã sản phẩm", "Tên sản phẩm", "Danh mục", "Giá nhập", "Giá bán", "Tồn kho", "Đơn vị tính", "Hình ảnh", "Trạng thái"});
        themTruong("Mã sản phẩm:", txtMa);
        themTruong("Tên sản phẩm:", txtTen);
        themTruong("Danh mục:", cboDanhMuc);
        themTruong("Giá nhập:", txtGiaNhap);
        themTruong("Giá bán:", txtGiaBan);
        themTruong("Số lượng tồn:", txtTon);
        themTruong("Đơn vị tính:", txtDonVi);
        themTruong("Hình ảnh:", txtHinhAnh);
        themTruong("Trạng thái:", cboTrangThai);
        khoiTao();
    }

    private void taiDanhMuc() throws Exception {
        cboDanhMuc.removeAllItems();
        for (DanhMuc d : danhMucDao.layTatCa("")) {
            cboDanhMuc.addItem(d);
        }
    }

    @Override
    protected void taiDuLieu(String tuKhoa) throws Exception {
        taiDanhMuc();
        mh.setRowCount(0);
        for (SanPham s : dao.layTatCa(tuKhoa)) {
            mh.addRow(new Object[]{s.id, s.maSanPham, s.tenSanPham, s.tenDanhMuc, TienIch.tien(s.giaNhap), TienIch.tien(s.giaBan), s.soLuongTon, s.donViTinh, s.hinhAnh, s.trangThai == 1 ? "Đang bán" : "Ngừng bán"});
        }
    }

    @Override
    protected void hienThiDong(int hang) {
        txtMa.setText(giaTri(hang, 1));
        txtTen.setText(giaTri(hang, 2));
        String tenDanhMuc = giaTri(hang, 3);
        for (int i = 0; i < cboDanhMuc.getItemCount(); i++) {
            if (cboDanhMuc.getItemAt(i).tenDanhMuc.equals(tenDanhMuc)) {
                cboDanhMuc.setSelectedIndex(i);
                break;
            }
        }
        txtGiaNhap.setText(giaTri(hang, 4).replace(",", ""));
        txtGiaBan.setText(giaTri(hang, 5).replace(",", ""));
        txtTon.setText(giaTri(hang, 6));
        txtDonVi.setText(giaTri(hang, 7));
        txtHinhAnh.setText(giaTri(hang, 8));
        cboTrangThai.setSelectedItem(giaTri(hang, 9));
    }

    private SanPham docForm() throws Exception {
        TienIch.batBuoc(txtMa.getText(), "Mã sản phẩm");
        TienIch.batBuoc(txtTen.getText(), "Tên sản phẩm");
        SanPham s = new SanPham();
        s.maSanPham = txtMa.getText().trim();
        s.tenSanPham = txtTen.getText().trim();
        DanhMuc d = (DanhMuc) cboDanhMuc.getSelectedItem();
        s.danhMucId = d == null ? null : d.id;
        s.giaNhap = TienIch.soThapPhan(txtGiaNhap.getText(), "Giá nhập");
        s.giaBan = TienIch.soThapPhan(txtGiaBan.getText(), "Giá bán");
        s.soLuongTon = TienIch.rong(txtTon.getText()) ? 0 : TienIch.soNguyen(txtTon.getText(), "Số lượng tồn");
        s.donViTinh = txtDonVi.getText().trim();
        s.hinhAnh = txtHinhAnh.getText().trim();
        s.trangThai = cboTrangThai.getSelectedIndex() == 0 ? 1 : 0;
        return s;
    }

    @Override
    protected void themMoi() throws Exception {
        dao.them(docForm());
    }

    @Override
    protected void capNhat(int id) throws Exception {
        SanPham s = docForm();
        s.id = id;
        dao.sua(s);
    }

    @Override
    protected void xoaBanGhi(int id) throws Exception {
        dao.xoa(id);
    }

    @Override
    protected void xoaTrang() {
        txtMa.setText("");
        txtTen.setText("");
        txtGiaNhap.setText("");
        txtGiaBan.setText("");
        txtTon.setText("0");
        txtDonVi.setText("");
        txtHinhAnh.setText("");
        cboTrangThai.setSelectedIndex(0);
        bang.clearSelection();
    }
}
