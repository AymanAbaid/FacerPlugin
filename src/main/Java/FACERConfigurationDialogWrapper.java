import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.File;

public class FACERConfigurationDialogWrapper extends DialogWrapper {

    JTextField databaseURL = new JTextField();
    JTextField resourcesRootPath = new JTextField();

    FACERConfigurationStateComponent configurationComponent = FACERConfigurationStateComponent.getInstance();

    public FACERConfigurationDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("Configure FACER");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        dialogPanel.setLayout(new BoxLayout(dialogPanel, BoxLayout.Y_AXIS));

        SimpleAttributeSet normal = new SimpleAttributeSet();
//        StyleConstants.setFontFamily(normal, "SansSerif");
        StyleConstants.setFontSize(normal, 12);

        SimpleAttributeSet bold = new SimpleAttributeSet(normal);
        StyleConstants.setBold(bold, true);

        SimpleAttributeSet italic = new SimpleAttributeSet(normal);
        StyleConstants.setItalic(italic, true);

        // database url information

        if (!configurationComponent.isDbHardcoded) {
            JTextPane databaseUrlLabel = new JTextPane();
            StyledDocument databaseUrlLabelDoc = (StyledDocument) databaseUrlLabel.getDocument();

            try {
                databaseUrlLabelDoc.insertString(databaseUrlLabelDoc.getLength(), "Database URL Information\n", bold);
                databaseUrlLabelDoc.insertString(databaseUrlLabelDoc.getLength(), "This URL will be used to access FACER database for recommendations.\n", normal);
                databaseUrlLabelDoc.insertString(databaseUrlLabelDoc.getLength(), "URL can be of the format:\n", normal);
                databaseUrlLabelDoc.insertString(databaseUrlLabelDoc.getLength(), "\"jdbc:mysql://localhost<:port>/<database_name>?useSSL=false&user=<username>&password=<password>\"", normal);

            } catch (BadLocationException e) {
                e.printStackTrace();
            }

            databaseUrlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            dialogPanel.add(databaseUrlLabel);

            databaseURL.setAlignmentX(Component.LEFT_ALIGNMENT);
            databaseURL.setText(configurationComponent.getDatabaseURL());

            dialogPanel.add(databaseURL);
            dialogPanel.add(Box.createVerticalStrut(10));
        }

        // resources folder root path information

        JTextPane resourcesRootPathLabel = new JTextPane();
        StyledDocument resourcesRootPathLabelDoc = (StyledDocument) resourcesRootPathLabel.getDocument();

        try {
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "Resources file path\n", bold);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "This path will be used to for the following resources for FACER method recommendations:\n", normal);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "Input:\n", italic);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "\"stopwords.txt\" file\n", normal);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "\"LuceneIndex\" folder\n", normal);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "\"Dataset\" folder\n", normal);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "Output:\n", italic);
            resourcesRootPathLabelDoc.insertString(resourcesRootPathLabelDoc.getLength(), "\"log.csv\" file for user interaction logging\n", normal);

        } catch (BadLocationException e) {
            e.printStackTrace();
        }
        resourcesRootPathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(resourcesRootPathLabel);

        addFileFolderPathField(dialogPanel,"Choose file", JFileChooser.DIRECTORIES_ONLY, resourcesRootPath, configurationComponent.getResourcesFolderRootPath());
        dialogPanel.add(Box.createVerticalStrut(10));


        return dialogPanel;
    }

    private void addFileFolderPathField(JPanel dialogPanel, String toolTip, int filesOnly, JTextField pathTextField, String pathTextFieldValue) {

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

        JPanel pathWrapper = new JPanel(new BorderLayout());
        pathWrapper.add(pathTextField);
        pathWrapper.add(pathBrowseButton, BorderLayout.EAST);
        pathTextField.setText(pathTextFieldValue);

        pathTextField.setAlignmentX(Component.LEFT_ALIGNMENT);
        pathWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);
        dialogPanel.add(pathWrapper);
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
        // check db field for empty only if db is not hardcoded
        if ((!configurationComponent.isDbHardcoded && databaseURL.getText().isEmpty())
                || resourcesRootPath.getText().isEmpty()) {
            FACERErrorDialog.showConfigurationCompleteError("Configuration Incomplete", "Please complete configuration to use FACER recommendations.");
        } else {
            String newResourcesRootPath = resourcesRootPath.getText();
            if (newResourcesRootPath.endsWith("\\")) {
                newResourcesRootPath = newResourcesRootPath.substring(0, newResourcesRootPath.length() - 1);
            }
            configurationComponent.updateConfigurations(databaseURL.getText(), newResourcesRootPath);
            super.doOKAction();
        }
    }

    @Override
    public boolean shouldCloseOnCross() {
        return true;
    }
}

