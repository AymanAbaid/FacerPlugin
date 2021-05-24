import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class FACERConfigurationDialogWrapper extends DialogWrapper {

    JTextField databaseURL = new JTextField();
    JTextField stopwordsPath = new JTextField();
    JTextField luceneIndexPath = new JTextField();
    JTextField logFilePath = new JTextField();
    FACERConfigurationStateComponent configurationComponent = FACERConfigurationStateComponent.getInstance();

    public FACERConfigurationDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("Test DialogWrapper");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        // database url information

        JLabel databaseURLLabel = new JLabel("Database URL Information");
        databaseURLLabel.setFont(databaseURLLabel.getFont().deriveFont(Font.BOLD, 12f));
        JLabel databaseURLMessageLine1 = new JLabel("This URL will be used to access FACER database for recommendations.");
        JLabel databaseURLMessageLine2 = new JLabel("URL can be of the format:");
        JLabel databaseURLMessageLine3 = new JLabel("\"jdbc:mysql://localhost<:port>/<database_name>?useSSL=false&user=<username>&password=<password>\"");

        databaseURLLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        databaseURLMessageLine1.setAlignmentX(Component.LEFT_ALIGNMENT);
        databaseURLMessageLine2.setAlignmentX(Component.LEFT_ALIGNMENT);
        databaseURLMessageLine3.setAlignmentX(Component.LEFT_ALIGNMENT);
        databaseURL.setAlignmentX(Component.LEFT_ALIGNMENT);
        databaseURL.setText(configurationComponent.getDatabaseURL());

        dialogPanel.add(databaseURLLabel);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(databaseURLMessageLine1);
        dialogPanel.add(databaseURLMessageLine2);
        dialogPanel.add(databaseURLMessageLine3);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(databaseURL);

        dialogPanel.add(Box.createVerticalStrut(10));

        // stopwords file information

        AddFileFolderPathField(dialogPanel, "Stopwords File Path", "This path will be used to access stopwords file for FACER method recommendations.", "Choose file", JFileChooser.FILES_ONLY, stopwordsPath, configurationComponent.getStopwordsPath());

        dialogPanel.add(Box.createVerticalStrut(10));

        // lucene index folder information

        AddFileFolderPathField(dialogPanel, "Lucene Index Path", "This path will be used to access Lucene Index folder for FACER method recommendations.", "Choose folder", JFileChooser.DIRECTORIES_ONLY, luceneIndexPath, configurationComponent.getLucenePath());

        AddFileFolderPathField(dialogPanel, "Log File Path", "This path will be used to log interaction events for FACER.", "Choose folder", JFileChooser.DIRECTORIES_ONLY, logFilePath, configurationComponent.getLogFilePath());

        return dialogPanel;
    }

    private void AddFileFolderPathField(JPanel dialogPanel, String s, String s2, String s3, int filesOnly, JTextField stopwordsPath, String stopwordsPath2) {
        JLabel stopwordsPathLabel = new JLabel(s);
        stopwordsPathLabel.setFont(stopwordsPathLabel.getFont().deriveFont(Font.BOLD, 12f));
        JLabel stopwordsPathMessage = new JLabel(s2);

        JButton stopwordsPathButton = new JButton(AllIcons.Actions.Menu_open);
        stopwordsPathButton.setBorder(BorderFactory.createEmptyBorder());
        stopwordsPathButton.setPreferredSize(new Dimension(20, 20));
        stopwordsPathButton.setToolTipText(s3);
        stopwordsPathButton.setContentAreaFilled(false);
        stopwordsPathButton.addActionListener(evt -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(filesOnly);
            int option = fc.showOpenDialog(dialogPanel);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                stopwordsPath.setText(file.getPath());
            }
        });

        JPanel stopwrodsPathWrapper = new JPanel(new BorderLayout());
        stopwrodsPathWrapper.add(stopwordsPath);
        stopwrodsPathWrapper.add(stopwordsPathButton, BorderLayout.EAST);
        stopwordsPath.setText(stopwordsPath2);

        stopwordsPathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopwordsPathMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopwrodsPathWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        dialogPanel.add(stopwordsPathLabel);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(stopwordsPathMessage);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(stopwrodsPathWrapper);
    }

    @Override
    public void doCancelAction() {
        if (configurationComponent.isConfigured()) {
            super.doCancelAction();
        } else {
            FACERErrorDialog.showConfigurationCompleteError("Configuration Incomplete", "Please complete configuration to use FACER recommendations.");
        }
    }

    @Override
    protected void doOKAction() {
        if (databaseURL.getText().isEmpty()
                || stopwordsPath.getText().isEmpty()
                || luceneIndexPath.getText().isEmpty()
                || logFilePath.getText().isEmpty()) {
            FACERErrorDialog.showConfigurationCompleteError("Configuration Incomplete", "Please complete configuration to use FACER recommendations.");
        } else {
            configurationComponent.updateConfigurations(databaseURL.getText(), stopwordsPath.getText(), luceneIndexPath.getText(), logFilePath.getText());
            super.doOKAction();
        }
    }

    @Override
    public boolean shouldCloseOnCross() {
        FACERErrorDialog.showConfigurationCompleteError("Configuration Incomplete", "Please complete configuration to use FACER recommendations.");
        return true;
    }
}

