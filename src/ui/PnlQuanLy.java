package ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public abstract class PnlQuanLy extends JPanel {
    protected final JTable bang = new JTable();
    protected final DefaultTableModel mh;
    protected final JPanel pnlForm = new JPanel(new GridLayout(0, 4, 8, 8));
    protected final JTextField txtTim = new JTextField(20);
    private final JButton btnThem = new JButton("Thêm");
    private final JButton btnSua = new JButton("Sửa");
    private final JButton btnXoa = new JButton("Xóa");
    private final JButton btnMoi = new JButton("Làm mới");
    private final JButton btnTim = new JButton("Tìm kiếm");

    protected PnlQuanLy(String[] tieuDe) {
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        mh = new DefaultTableModel(tieuDe, 0) {
            @Override
            public boolean isCellEditable(int hang, int cot) {
                return false;
            }
        };
        bang.setModel(mh);
        bang.setRowHeight(24);
        bang.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPanel pnlTim = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pnlTim.add(new JLabel("Từ khóa:"));
        pnlTim.add(txtTim);
        pnlTim.add(btnTim);
        add(pnlTim, BorderLayout.NORTH);
        add(new JScrollPane(bang), BorderLayout.CENTER);

        JPanel pnlNut = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        pnlNut.add(btnThem);
        pnlNut.add(btnSua);
        pnlNut.add(btnXoa);
        pnlNut.add(btnMoi);

        JPanel pnlDuoi = new JPanel(new BorderLayout(5, 5));
        pnlDuoi.add(pnlForm, BorderLayout.CENTER);
        pnlDuoi.add(pnlNut, BorderLayout.SOUTH);
        add(pnlDuoi, BorderLayout.SOUTH);
    }

    protected void khoiTao() {
        btnTim.addActionListener(e -> tai());
        txtTim.addActionListener(e -> tai());
        btnMoi.addActionListener(e -> {
            txtTim.setText("");
            xoaTrang();
            tai();
        });
        bang.getSelectionModel().addListSelectionListener(e -> {
            int hang = bang.getSelectedRow();
            if (!e.getValueIsAdjusting() && hang >= 0) {
                hienThiDong(hang);
            }
        });
        btnThem.addActionListener(e -> {
            try {
                themMoi();
                tai();
                xoaTrang();
                thongBao("Thêm thành công");
            } catch (Exception ex) {
                baoLoi(ex);
            }
        });
        btnSua.addActionListener(e -> {
            Integer id = idDangChon();
            if (id == null) {
                thongBao("Vui lòng chọn dòng cần sửa");
                return;
            }
            try {
                capNhat(id);
                tai();
                thongBao("Cập nhật thành công");
            } catch (Exception ex) {
                baoLoi(ex);
            }
        });
        btnXoa.addActionListener(e -> {
            Integer id = idDangChon();
            if (id == null) {
                thongBao("Vui lòng chọn dòng cần xóa");
                return;
            }
            int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa dòng đã chọn?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (chon != JOptionPane.YES_OPTION) {
                return;
            }
            try {
                xoaBanGhi(id);
                tai();
                xoaTrang();
                thongBao("Xóa thành công");
            } catch (Exception ex) {
                baoLoi(ex);
            }
        });
        tai();
    }

    protected void tai() {
        try {
            taiDuLieu(txtTim.getText().trim());
        } catch (Exception ex) {
            baoLoi(ex);
        }
    }

    protected void themTruong(String nhan, JComponent thanhPhan) {
        pnlForm.add(new JLabel(nhan));
        pnlForm.add(thanhPhan);
    }

    protected Integer idDangChon() {
        int hang = bang.getSelectedRow();
        if (hang < 0) {
            return null;
        }
        return (Integer) mh.getValueAt(hang, 0);
    }

    protected String giaTri(int hang, int cot) {
        Object o = mh.getValueAt(hang, cot);
        return o == null ? "" : o.toString();
    }

    protected void thongBao(String noiDung) {
        JOptionPane.showMessageDialog(this, noiDung, "Thông báo", JOptionPane.INFORMATION_MESSAGE);
    }

    protected void baoLoi(Exception ex) {
        String noiDung = ex.getMessage();
        if (noiDung != null && noiDung.contains("REFERENCE constraint")) {
            noiDung = "Không thể xóa vì dữ liệu đang được sử dụng ở nơi khác";
        } else if (noiDung != null && noiDung.contains("UNIQUE KEY constraint")) {
            noiDung = "Dữ liệu bị trùng mã hoặc giá trị duy nhất đã tồn tại";
        }
        JOptionPane.showMessageDialog(this, noiDung, "Lỗi", JOptionPane.ERROR_MESSAGE);
    }

    protected abstract void taiDuLieu(String tuKhoa) throws Exception;

    protected abstract void hienThiDong(int hang);

    protected abstract void themMoi() throws Exception;

    protected abstract void capNhat(int id) throws Exception;

    protected abstract void xoaBanGhi(int id) throws Exception;

    protected abstract void xoaTrang();
}
