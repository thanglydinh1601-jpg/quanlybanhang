package ui;

import dao.NhaCungCapDAO;
import dao.PhieuNhapDAO;
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
import model.ChiTietPhieuNhap;
import model.NhaCungCap;
import model.PhieuNhap;
import model.SanPham;
import util.PhienLamViec;
import util.TienIch;

public class PnlPhieuNhap extends JPanel {
    private final PhieuNhapDAO dao = new PhieuNhapDAO();
    private final NhaCungCapDAO nhaCungCapDao = new NhaCungCapDAO();
    private final SanPhamDAO sanPhamDao = new SanPhamDAO();

    private final JComboBox<NhaCungCap> cboNhaCungCap = new JComboBox<>();
    private final JComboBox<SanPham> cboSanPham = new JComboBox<>();
    private final JSpinner spnSoLuong = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
    private final JTextField txtGiaNhap = new JTextField(10);
    private final JLabel lblTong = new JLabel("0");
    private final List<ChiTietPhieuNhap> gioNhap = new ArrayList<>();
    private final DefaultTableModel mhGio = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"}, 0) {
        @Override
        public boolean isCellEditable(int hang, int cot) {
            return false;
        }
    };
    private final JTable bangGio = new JTable(mhGio);

    private final JTextField txtTim = new JTextField(20);
    private final DefaultTableModel mhDs = new DefaultTableModel(new String[]{"ID", "Mã phiếu nhập", "Ngày nhập", "Nhân viên", "Nhà cung cấp", "Tổng tiền"}, 0) {
        @Override
        public boolean isCellEditable(int hang, int cot) {
            return false;
        }
    };
    private final JTable bangDs = new JTable(mhDs);
    private final DefaultTableModel mhCt = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Số lượng", "Giá nhập", "Thành tiền"}, 0) {
        @Override
        public boolean isCellEditable(int hang, int cot) {
            return false;
        }
    };
    private final JTable bangCt = new JTable(mhCt);

    public PnlPhieuNhap() {
        setLayout(new BorderLayout());
        JTabbedPane tab = new JTabbedPane();
        tab.addTab("Lập phiếu nhập", taoTabLap());
        tab.addTab("Danh sách phiếu nhập", taoTabDanhSach());
        add(tab, BorderLayout.CENTER);
        taiCombo();
        taiDanhSach();
    }

    private JPanel taoTabLap() {
        JPanel goc = new JPanel(new BorderLayout(8, 8));
        goc.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pnlChon = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        pnlChon.add(new JLabel("Nhà cung cấp:"));
        pnlChon.add(cboNhaCungCap);
        pnlChon.add(new JLabel("Sản phẩm:"));
        pnlChon.add(cboSanPham);
        pnlChon.add(new JLabel("Số lượng:"));
        pnlChon.add(spnSoLuong);
        pnlChon.add(new JLabel("Giá nhập:"));
        pnlChon.add(txtGiaNhap);
        JButton btnThem = new JButton("Thêm vào phiếu");
        JButton btnBo = new JButton("Bỏ dòng đã chọn");
        pnlChon.add(btnThem);
        pnlChon.add(btnBo);
        goc.add(pnlChon, BorderLayout.NORTH);

        bangGio.setRowHeight(24);
        bangGio.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        goc.add(new JScrollPane(bangGio), BorderLayout.CENTER);

        JPanel pnlTong = new JPanel(new GridLayout(0, 2, 8, 8));
        pnlTong.add(new JLabel("Tổng tiền:"));
        pnlTong.add(lblTong);
        JButton btnLap = new JButton("Lập phiếu nhập");
        JButton btnHuy = new JButton("Hủy phiếu");
        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        pnlNut.add(btnLap);
        pnlNut.add(btnHuy);
        JPanel pnlDuoi = new JPanel(new BorderLayout(5, 5));
        pnlDuoi.add(pnlTong, BorderLayout.CENTER);
        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        goc.add(pnlDuoi, BorderLayout.SOUTH);

        cboSanPham.addActionListener(e -> {
            SanPham sp = (SanPham) cboSanPham.getSelectedItem();
            if (sp != null) {
                txtGiaNhap.setText(sp.giaNhap.stripTrailingZeros().toPlainString());
            }
        });
        btnThem.addActionListener(e -> themVaoPhieu());
        btnBo.addActionListener(e -> {
            int hang = bangGio.getSelectedRow();
            if (hang < 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần bỏ");
                return;
            }
            gioNhap.remove(hang);
            capNhatGio();
        });
        btnHuy.addActionListener(e -> {
            gioNhap.clear();
            capNhatGio();
        });
        btnLap.addActionListener(e -> lapPhieu());
        return goc;
    }

    private JPanel taoTabDanhSach() {
        JPanel goc = new JPanel(new BorderLayout(8, 8));
        goc.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));

        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnTim = new JButton("Tìm kiếm");
        JButton btnMoi = new JButton("Làm mới");
        JButton btnXoa = new JButton("Xóa phiếu nhập");
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
        pnlCt.add(new JLabel("Chi tiết phiếu nhập"), BorderLayout.NORTH);
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
        btnXoa.addActionListener(e -> xoaPhieu());
        bangDs.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && bangDs.getSelectedRow() >= 0) {
                taiChiTiet((Integer) mhDs.getValueAt(bangDs.getSelectedRow(), 0));
            }
        });
        return goc;
    }

    private void taiCombo() {
        try {
            cboNhaCungCap.removeAllItems();
            for (NhaCungCap n : nhaCungCapDao.layTatCa("")) {
                cboNhaCungCap.addItem(n);
            }
            cboSanPham.removeAllItems();
            for (SanPham s : sanPhamDao.layDangBan()) {
                cboSanPham.addItem(s);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void themVaoPhieu() {
        SanPham sp = (SanPham) cboSanPham.getSelectedItem();
        if (sp == null) {
            JOptionPane.showMessageDialog(this, "Chưa có sản phẩm để chọn");
            return;
        }
        try {
            BigDecimal gia = TienIch.soThapPhan(txtGiaNhap.getText(), "Giá nhập");
            if (gia.signum() < 0) {
                throw new Exception("Giá nhập không hợp lệ");
            }
            int soLuong = (Integer) spnSoLuong.getValue();
            ChiTietPhieuNhap co = null;
            for (ChiTietPhieuNhap ct : gioNhap) {
                if (ct.sanPhamId == sp.id) {
                    co = ct;
                    break;
                }
            }
            if (co == null) {
                co = new ChiTietPhieuNhap();
                co.sanPhamId = sp.id;
                co.maSanPham = sp.maSanPham;
                co.tenSanPham = sp.tenSanPham;
                co.soLuong = 0;
                gioNhap.add(co);
            }
            co.soLuong += soLuong;
            co.giaNhap = gia;
            co.thanhTien = gia.multiply(new BigDecimal(co.soLuong));
            spnSoLuong.setValue(1);
            capNhatGio();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private BigDecimal tinhTong() {
        BigDecimal tong = BigDecimal.ZERO;
        for (ChiTietPhieuNhap ct : gioNhap) {
            tong = tong.add(ct.thanhTien);
        }
        return tong;
    }

    private void capNhatGio() {
        mhGio.setRowCount(0);
        for (ChiTietPhieuNhap ct : gioNhap) {
            mhGio.addRow(new Object[]{ct.maSanPham, ct.tenSanPham, ct.soLuong, TienIch.tien(ct.giaNhap), TienIch.tien(ct.thanhTien)});
        }
        lblTong.setText(TienIch.tien(tinhTong()));
    }

    private void lapPhieu() {
        if (gioNhap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Phiếu nhập đang trống", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        NhaCungCap ncc = (NhaCungCap) cboNhaCungCap.getSelectedItem();
        if (ncc == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn nhà cung cấp", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        try {
            PhieuNhap pn = new PhieuNhap();
            pn.maPhieuNhap = "PN" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            pn.nhanVienId = PhienLamViec.nhanVienHienTai == null ? null : PhienLamViec.nhanVienHienTai.id;
            pn.nhaCungCapId = ncc.id;
            pn.tongTien = tinhTong();
            dao.taoMoi(pn, gioNhap);
            JOptionPane.showMessageDialog(this, "Lập phiếu nhập " + pn.maPhieuNhap + " thành công");
            gioNhap.clear();
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
            for (PhieuNhap p : dao.layTatCa(txtTim.getText().trim())) {
                mhDs.addRow(new Object[]{p.id, p.maPhieuNhap, p.ngayNhap, p.tenNhanVien, p.tenNhaCungCap, TienIch.tien(p.tongTien)});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void taiChiTiet(int phieuId) {
        try {
            mhCt.setRowCount(0);
            for (ChiTietPhieuNhap ct : dao.layChiTiet(phieuId)) {
                mhCt.addRow(new Object[]{ct.maSanPham, ct.tenSanPham, ct.soLuong, TienIch.tien(ct.giaNhap), TienIch.tien(ct.thanhTien)});
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void xoaPhieu() {
        int hang = bangDs.getSelectedRow();
        if (hang < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu nhập cần xóa");
            return;
        }
        int chon = JOptionPane.showConfirmDialog(this, "Xóa phiếu nhập sẽ trừ lại tồn kho. Bạn có chắc chắn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (chon != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            dao.xoa((Integer) mhDs.getValueAt(hang, 0));
            taiDanhSach();
            taiCombo();
            JOptionPane.showMessageDialog(this, "Xóa phiếu nhập thành công");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
