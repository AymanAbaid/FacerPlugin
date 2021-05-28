import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.*;

public class FACERConfigurationDialogWrapper extends DialogWrapper {

    JTextField databaseURL = new JTextField();
    JTextField stopwordsPath = new JTextField();
    JTextField luceneIndexPath = new JTextField();
    JTextField logFilePath = new JTextField();
    JTextField datasetRootPath = new JTextField();

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

        addFileFolderPathField(dialogPanel, "Stopwords File Path", "This path will be used to access stopwords file for FACER method recommendations.", "Choose file", JFileChooser.FILES_ONLY, stopwordsPath, configurationComponent.getStopwordsPath());
        dialogPanel.add(Box.createVerticalStrut(10));

        // lucene index folder information

        addFileFolderPathField(dialogPanel, "Lucene Index Path", "This path will be used to access Lucene Index folder for FACER method recommendations.", "Choose folder", JFileChooser.DIRECTORIES_ONLY, luceneIndexPath, configurationComponent.getLucenePath());
        dialogPanel.add(Box.createVerticalStrut(10));

        // Log file location information

        addFileFolderPathField(dialogPanel, "Log File Path", "This path will be used to log interaction events for FACER.", "Choose folder", JFileChooser.DIRECTORIES_ONLY, logFilePath, configurationComponent.getLogFilePath());
        dialogPanel.add(Box.createVerticalStrut(10));

        // Dataset folder root information
        addFileFolderPathField(dialogPanel, "Dataset Root Folder", "This path will be used to access the dataset for code recommendation.", "Choose folder", JFileChooser.DIRECTORIES_ONLY, datasetRootPath, configurationComponent.getDatasetRootPath());
        dialogPanel.add(Box.createVerticalStrut(10));

        return dialogPanel;
    }

    private void addFileFolderPathField(JPanel dialogPanel, String label, String message, String toolTip, int filesOnly, JTextField pathTextField, String pathTextFieldValue) {
        JLabel pathLabelField = new JLabel(label);
        pathLabelField.setFont(pathLabelField.getFont().deriveFont(Font.BOLD, 12f));
        JLabel pathMessageField = new JLabel(message);

        JButton pathBrowseButton = new JButton(AllIcons.Actions.Menu_open);
        pathBrowseButton.setBorder(BorderFactory.createEmptyBorder());
        pathBrowseButton.setPreferredSize(new Dimension(20, 20));
        pathBrowseButton.setToolTipText(toolTip);
        pathBrowseButton.setContentAreaFilled(false);
        pathBrowseButton.addActionListener(evt -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(filesOnly);
            int option = fc.showOpenDialog(dialogPanel);
            if (option == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                pathTextField.setText(file.getPath());
            }
        });

        JPanel stopwrodsPathWrapper = new JPanel(new BorderLayout());
        stopwrodsPathWrapper.add(pathTextField);
        stopwrodsPathWrapper.add(pathBrowseButton, BorderLayout.EAST);
        pathTextField.setText(pathTextFieldValue);

        pathLabelField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pathMessageField.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopwrodsPathWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        dialogPanel.add(pathLabelField);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(pathMessageField);
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
                || logFilePath.getText().isEmpty()
                || datasetRootPath.getText().isEmpty()) {
            FACERErrorDialog.showConfigurationCompleteError("Configuration Incomplete", "Please complete configuration to use FACER recommendations.");
        } else {
            if(!databaseURL.getText().equals(configurationComponent.getDatabaseURL())){
                configurationComponent.resetDatasetRootPath();
            }
            String oldDatasetRootPath = configurationComponent.getDatasetRootPath();
            String newDatasetRootPath = datasetRootPath.getText();
            if(!newDatasetRootPath.endsWith("\\")){
                newDatasetRootPath += "\\";
            }

            if (!configurationComponent.isDatasetRootPathConfigured() || !oldDatasetRootPath.equals(newDatasetRootPath)) {
                try {
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection(
                            databaseURL.getText());
                    PreparedStatement pstmtUpdateProject = con.prepareStatement("UPDATE project SET path = REPLACE(path, ?, ?);");
                    PreparedStatement pstmtUpdateFile = con.prepareStatement("UPDATE file SET file_name = REPLACE(file_name, ?, ?);");
                    if (configurationComponent.isDatasetRootPathConfigured()) {
                        pstmtUpdateProject.setString(1, oldDatasetRootPath);
                        pstmtUpdateFile.setString(1, oldDatasetRootPath);
                    } else {
                        pstmtUpdateProject.setString(1, "F:\\FACER_2020\\RawSourceCodeDataset\\ClonedNew\\");
                        pstmtUpdateFile.setString(1, "F:\\FACER_2020\\RawSourceCodeDataset\\ClonedNew\\");
                    }
                    pstmtUpdateProject.setString(2, newDatasetRootPath);
                    pstmtUpdateFile.setString(2, newDatasetRootPath);
                    pstmtUpdateProject.executeUpdate();
                    pstmtUpdateFile.executeUpdate();
                    con.close();
                    configurationComponent.updateConfigurations(databaseURL.getText(), stopwordsPath.getText(), luceneIndexPath.getText(), logFilePath.getText(), newDatasetRootPath);
                } catch (Exception e) {
                    System.out.println(e);
                }
            } else {
                configurationComponent.updateConfigurations(databaseURL.getText(), stopwordsPath.getText(), luceneIndexPath.getText(), logFilePath.getText(), newDatasetRootPath);
            }
            super.doOKAction();
        }
    }

    @Override
    public boolean shouldCloseOnCross() {
        FACERErrorDialog.showConfigurationCompleteError("Configuration Incomplete", "Please complete configuration to use FACER recommendations.");
        return true;
    }
}

