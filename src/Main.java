import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import ui.FrmDangNhap;

public class Main {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new FrmDangNhap().setVisible(true));
    }
}
