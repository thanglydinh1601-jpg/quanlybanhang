package ui;

import dao.HoaDonDAO;
import dao.KhachHangDAO;
import dao.SanPhamDAO;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import model.ChiTietHoaDon;
import model.HoaDon;
import model.KhachHang;
import model.SanPham;
import util.PhienLamViec;
import util.TienIch;

public class PnlHoaDon extends JPanel {
    private final HoaDonDAO dao = new HoaDonDAO();
    private final KhachHangDAO khachHangDao = new KhachHangDAO();
    private final SanPhamDAO sanPhamDao = new SanPhamDAO();

    private final JComboBox<KhachHang> cboKhachHang = new JComboBox<>();
    private final JComboBox<SanPham> cboSanPham = new JComboBox<>();
    private final JSpinner spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
    private final JTextField txtGiamGia = new JTextField("0", 10);
    private final JTextField txtGhiChu = new JTextField(25);
    private final JLabel lblTong = new JLabel("0");
    private final JLabel lblThanhToan = new JLabel("0");
    private final List<ChiTietHoaDon> gioHang = new ArrayList<>();
    private final DefaultTableModel mhGio = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
        @Override
        public boolean isCellEditable(int hang, int cot) {
            return false;
        }
    };
    private final JTable bangGio = new JTable(mhGio);

    private final JTextField txtTim = new JTextField(20);
    private final DefaultTableModel mhDs = new DefaultTableModel(new String[]{"ID", "Mã hóa đơn", "Ngày lập", "Nhân viên", "Khách hàng", "Tổng tiền", "Giảm giá", "Thanh toán", "Ghi chú"}, 0) {
        @Override
        public boolean isCellEditable(int hang, int cot) {
            return false;
        }
    };
    private final JTable bangDs = new JTable(mhDs);
    private final DefaultTableModel mhCt = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Đơn giá", "Thành tiền"}, 0) {
        @Override
        public boolean isCellEditable(int hang, int cot) {
            return false;
        }
    };
    private final JTable bangCt = new JTable(mhCt);

    public PnlHoaDon() {
        setLayout(new BorderLayout());
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Lập hóa đơn", taoTabLap());
        tab.addTab("Danh sách hóa đơn", taoTabDanhSach());
        add(tab, BorderLayout.CENTER);
        taiCombo();
        taiDanhSach();
    }

    private JPanel taoTabLap() {
        JPanel goc = new JPanel(new BorderLayout(8, 8));
        goc.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pnlChon = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        pnlChon.add(new JLabel("Khách hàng:"));
        pnlChon.add(cboKhachHang);
        pnlChon.add(new JLabel("Sản phẩm:"));
        pnlChon.add(cboSanPham);
        pnlChon.add(new JLabel("Số lượng:"));
        pnlChon.add(spnSoLuong);
        JButton btnThemGio = new JButton("Thêm vào giỏ");
        JButton btnBoGio = new JButton("Bỏ dòng đã chọn");
        pnlChon.add(btnThemGio);
        pnlChon.add(btnBoGio);
        goc.add(pnlChon, BorderLayout.NORTH);

        bangGio.setRowHeight(24);
        bangGio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        goc.add(new JScrollPane(bangGio), BorderLayout.CENTER);

        JPanel pnlTong = new JPanel(new GridLayout(0, 2, 8, 8));
        pnlTong.add(new JLabel("Tổng tiền:"));
        pnlTong.add(lblTong);
        pnlTong.add(new JLabel("Giảm giá:"));
        pnlTong.add(txtGiamGia);
        pnlTong.add(new JLabel("Thanh toán:"));
        pnlTong.add(lblThanhToan);
        pnlTong.add(new JLabel("Ghi chú:"));
        pnlTong.add(txtGhiChu);
        JButton btnLap = new JButton("Lập hóa đơn");
        JButton btnHuy = new JButton("Hủy giỏ hàng");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlNut.add(btnLap);
        pnlNut.add(btnHuy);
        JPanel pnlDuoi = new JPanel(new BorderLayout(5, 5));
        pnlDuoi.add(pnlTong, BorderLayout.CENTER);
        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        goc.add(pnlDuoi, BorderLayout.SOUTH);

        btnThemGio.addActionListener(e -> themVaoGio());
        btnBoGio.addActionListener(e -> {
            int hang = bangGio.getSelectedRow();
            if (hang < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần bỏ");
                return;
            }
            gioHang.remove(hang);
            capNhatGio();
        });
        btnHuy.addActionListener(e -> {
            gioHang.clear();
            txtGiamGia.setText("0");
            txtGhiChu.setText("");
            capNhatGio();
        });
        btnLap.addActionListener(e -> lapHoaDon());
        txtGiamGia.addActionListener(e -> capNhatTong());
        txtGiamGia.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusLost(java.awt.event.FocusEvent e) {
                capNhatTong();
            }
        });
        return goc;
    }

    private JPanel taoTabDanhSach() {
        JPanel goc = new JPanel(new BorderLayout(8, 8));
        goc.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTim = new JButton("Tìm kiếm");
        JButton btnMoi = new JButton("Làm mới");
        JButton btnXoa = new JButton("Xóa hóa đơn");
        pnlTim.add(new JLabel("Từ khóa:"));
        pnlTim.add(txtTim);
        pnlTim.add(btnTim);
        pnlTim.add(btnMoi);
        pnlTim.add(btnXoa);
        goc.add(pnlTim, BorderLayout.NORTH);

        bangDs.setRowHeight(24);
        bangDs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bangCt.setRowHeight(24);
        JPanel pnlCt = new JPanel(new BorderLayout());
        pnlCt.add(new JLabel("Chi tiết hóa đơn"), BorderLayout.NORTH);
        pnlCt.add(new JScrollPane(bangCt), BorderLayout.CENTER);
        JSplitPane chia = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(bangDs), pnlCt);
        chia.setResizeWeight(0.6);
        goc.add(chia, BorderLayout.CENTER);

        btnTim.addActionListener(e -> taiDanhSach());
        txtTim.addActionListener(e -> taiDanhSach());
        btnMoi.addActionListener(e -> {
            txtTim.setText("");
            taiDanhSach();
        });
        btnXoa.addActionListener(e -> xoaHoaDon());
        bangDs.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bangDs.getSelectedRow() >= 0) {
                taiChiTiet((Integer) mhDs.getValueAt(bangDs.getSelectedRow(), 0));
            }
        });
        return goc;
    }

    private void taiCombo() {
        try {
            cboKhachHang.removeAllItems();
            KhachHang le = new KhachHang();
            le.id = 0;
            le.hoTen = "Khách lẻ";
            cboKhachHang.addItem(le);
            for (KhachHang k : khachHangDao.layTatCa("")) {
                cboKhachHang.addItem(k);
            }
            cboSanPham.removeAllItems();
            for (SanPham s : sanPhamDao.layDangBan()) {
                cboSanPham.addItem(s);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themVaoGio() {
        SanPham sp = (SanPham) cboSanPham.getSelectedItem();
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm để chọn");
            return;
        }
        int soLuong = (Integer) spnSoLuong.getValue();
        ChiTietHoaDon co = null;
        for (ChiTietHoaDon ct : gioHang) {
            if (ct.sanPhamId == sp.id) {
                co = ct;
                break;
            }
        }
        int tongSoLuong = soLuong + (co == null ? 0 : co.soLuong);
        if (tongSoLuong > sp.soLuongTon) {
            JOptionPane.showMessageDialog(this, "Số lượng vượt quá tồn kho (" + sp.soLuongTon + ")", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (co == null) {
            co = new ChiTietHoaDon();
            co.sanPhamId = sp.id;
            co.maSanPham = sp.maSanPham;
            co.tenSanPham = sp.tenSanPham;
            co.donGia = sp.giaBan;
            gioHang.add(co);
        }
        co.soLuong = tongSoLuong;
        co.thanhTien = co.donGia.multiply(new BigDecimal(co.soLuong));
        spnSoLuong.setValue(1);
        capNhatGio();
    }

    private void capNhatGio() {
        mhGio.setRowCount(0);
        for (ChiTietHoaDon ct : gioHang) {
            mhGio.addRow(new Object[]{ct.maSanPham, ct.tenSanPham, ct.soLuong, TienIch.tien(ct.donGia), TienIch.tien(ct.thanhTien)});
        }
        capNhatTong();
    }

    private BigDecimal tinhTong() {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietHoaDon ct : gioHang) {
            tong = tong.add(ct.thanhTien);
        }
        return tong;
    }

    private void capNhatTong() {
        BigDecimal tong = tinhTong();
        BigDecimal giam = BigDecimal.ZERO;
        try {
            giam = TienIch.soThapPhan(txtGiamGia.getText(), "Giảm giá");
        } catch (Exception ex) {
            giam = BigDecimal.ZERO;
        }
        lblTong.setText(TienIch.tien(tong));
        lblThanhToan.setText(TienIch.tien(tong.subtract(giam)));
    }

    private void lapHoaDon() {
        if (gioHang.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giỏ hàng đang trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            BigDecimal tong = tinhTong();
            BigDecimal giam = TienIch.rong(txtGiamGia.getText()) ? BigDecimal.ZERO : TienIch.soThapPhan(txtGiamGia.getText(), "Giảm giá");
            if (giam.signum() < 0 || giam.compareTo(tong) > 0) {
                throw new Exception("Giảm giá không hợp lệ");
            }
            HoaDon hd = new HoaDon();
            hd.maHoaDon = "HD" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            hd.nhanVienId = PhienLamViec.nhanVienHienTai == null ? null : PhienLamViec.nhanVienHienTai.id;
            KhachHang kh = (KhachHang) cboKhachHang.getSelectedItem();
            hd.khachHangId = kh == null || kh.id == 0 ? null : kh.id;
            hd.tongTien = tong;
            hd.giamGia = giam;
            hd.thanhToan = tong.subtract(giam);
            hd.ghiChu = txtGhiChu.getText().trim();
            dao.taoMoi(hd, gioHang);
            JOptionPane.showMessageDialog(this, "Lập hóa đơn " + hd.maHoaDon + " thành công\nKhách cần thanh toán: " + TienIch.tien(hd.thanhToan));
            gioHang.clear();
            txtGiamGia.setText("0");
            txtGhiChu.setText("");
            capNhatGio();
            taiCombo();
            taiDanhSach();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void taiDanhSach() {
        try {
            mhDs.setRowCount(0);
            mhCt.setRowCount(0);
            for (HoaDon h : dao.layTatCa(txtTim.getText().trim())) {
                mhDs.addRow(new Object[]{h.id, h.maHoaDon, h.ngayLap, h.tenNhanVien, h.tenKhachHang == null ? "Khách lẻ" : h.tenKhachHang, TienIch.tien(h.tongTien), TienIch.tien(h.giamGia), TienIch.tien(h.thanhToan), h.ghiChu});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void taiChiTiet(int hoaDonId) {
        try {
            mhCt.setRowCount(0);
            for (ChiTietHoaDon ct : dao.layChiTiet(hoaDonId)) {
                mhCt.addRow(new Object[]{ct.maSanPham, ct.tenSanPham, ct.soLuong, TienIch.tien(ct.donGia), TienIch.tien(ct.thanhTien)});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaHoaDon() {
        int hang = bangDs.getSelectedRow();
        if (hang < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn cần xóa");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this, "Xóa hóa đơn sẽ hoàn lại tồn kho. Bạn có chắc chắn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.xoa((Integer) mhDs.getValueAt(hang, 0));
            taiDanhSach();
            taiCombo();
            JOptionPane.showMessageDialog(this, "Xóa hóa đơn thành công");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
