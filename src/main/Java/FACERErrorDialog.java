import javax.swing.*;

public class FACERErrorDialog {

    public static void showConfigurationCompleteError(String title, String message) {
        final JComponent[] inputs = new JComponent[]{
                new JLabel(message),
        };
        JOptionPane.showMessageDialog(null, inputs, title, JOptionPane.ERROR_MESSAGE);
    }
}

