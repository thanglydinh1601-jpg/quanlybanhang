package ui;

import dao.KhachHangDAO;
import javax.swing.JTextField;
import model.KhachHang;
import util.TienIch;

public class PnlKhachHang extends PnlQuanLy {
    private final KhachHangDAO dao = new KhachHangDAO();
    private final JTextField txtMa = new JTextField();
    private final JTextField txtHoTen = new JTextField();
    private final JTextField txtSdt = new JTextField();
    private final JTextField txtEmail = new JTextField();
    private final JTextField txtDiaChi = new JTextField();
    private final JTextField txtDiem = new JTextField("0");

    public PnlKhachHang() {
        super(new String[]{"ID", "Mã khách hàng", "Họ tên", "Số điện thoại", "Email", "Địa chỉ", "Điểm tích lũy", "Ngày tạo"});
        themTruong("Mã khách hàng:", txtMa);
        themTruong("Họ tên:", txtHoTen);
        themTruong("Số điện thoại:", txtSdt);
        themTruong("Email:", txtEmail);
        themTruong("Địa chỉ:", txtDiaChi);
        themTruong("Điểm tích lũy:", txtDiem);
        khoiTao();
    }

    @Override
    protected void taiDuLieu(String tuKhoa) throws Exception {
        mh.setRowCount(0);
        for (KhachHang k : dao.layTatCa(tuKhoa)) {
            mh.addRow(new Object[]{k.id, k.maKhachHang, k.hoTen, k.soDienThoai, k.email, k.diaChi, k.diemTichLuy, k.ngayTao});
        }
    }

    @Override
    protected void hienThiDong(int hang) {
        txtMa.setText(giaTri(hang, 1));
        txtHoTen.setText(giaTri(hang, 2));
        txtSdt.setText(giaTri(hang, 3));
        txtEmail.setText(giaTri(hang, 4));
        txtDiaChi.setText(giaTri(hang, 5));
        txtDiem.setText(giaTri(hang, 6));
    }

    private KhachHang docForm() throws Exception {
        TienIch.batBuoc(txtMa.getText(), "Mã khách hàng");
        TienIch.batBuoc(txtHoTen.getText(), "Họ tên");
        TienIch.batBuoc(txtSdt.getText(), "Số điện thoại");
        KhachHang k = new KhachHang();
        k.maKhachHang = txtMa.getText().trim();
        k.hoTen = txtHoTen.getText().trim();
        k.soDienThoai = txtSdt.getText().trim();
        k.email = txtEmail.getText().trim();
        k.diaChi = txtDiaChi.getText().trim();
        k.diemTichLuy = TienIch.rong(txtDiem.getText()) ? 0 : TienIch.soNguyen(txtDiem.getText(), "Điểm tích lũy");
        return k;
    }

    @Override
    protected void themMoi() throws Exception {
        dao.them(docForm());
    }

    @Override
    protected void capNhat(int id) throws Exception {
        KhachHang k = docForm();
        k.id = id;
        dao.sua(k);
    }

    @Override
    protected void xoaBanGhi(int id) throws Exception {
        dao.xoa(id);
    }

    @Override
    protected void xoaTrang() {
        txtMa.setText("");
        txtHoTen.setText("");
        txtSdt.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtDiem.setText("0");
        bang.clearSelection();
    }
}
