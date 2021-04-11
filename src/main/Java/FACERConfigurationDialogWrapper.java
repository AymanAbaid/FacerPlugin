import com.intellij.icons.AllIcons;
import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.List;

public class FACERConfigurationDialogWrapper extends DialogWrapper {

    JTextField databaseURL = new JTextField();
    JTextField stopwordsPath = new JTextField();
    JTextField luceneIndexPath = new JTextField();
    FACERConfigurationComponent configurationComponent = FACERConfigurationComponent.getInstance();

    public FACERConfigurationDialogWrapper() {
        super(true); // use current window as parent
        init();
        setTitle("Test DialogWrapper");
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
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

        JLabel stopwordsPathLabel = new JLabel("Stopwords File Path");
        stopwordsPathLabel.setFont(stopwordsPathLabel.getFont().deriveFont(Font.BOLD, 12f));
        JLabel stopwordsPathMessage = new JLabel("This path will be used to access stopwords file for FACER method recommendations.");

        JButton stopwordsPathButton = new JButton(AllIcons.Actions.Menu_open);
        stopwordsPathButton.setBorder(BorderFactory.createEmptyBorder());
        stopwordsPathButton.setPreferredSize(new Dimension(20,20));
        stopwordsPathButton.setToolTipText("Choose file");
        stopwordsPathButton.setContentAreaFilled(false);
        stopwordsPathButton.addActionListener(evt -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int option = fc.showOpenDialog(dialogPanel);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                stopwordsPath.setText(file.getPath());
            }
        });

        JPanel stopwrodsPathWrapper = new JPanel(new BorderLayout());
        stopwrodsPathWrapper.add(stopwordsPath);
        stopwrodsPathWrapper.add(stopwordsPathButton, BorderLayout.EAST);
        stopwordsPath.setText(configurationComponent.getStopwordsPath());

        stopwordsPathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopwordsPathMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        stopwrodsPathWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        dialogPanel.add(stopwordsPathLabel);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(stopwordsPathMessage);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(stopwrodsPathWrapper);

        dialogPanel.add(Box.createVerticalStrut(10));

        // lucene index folder information

        JLabel luceneIndexPathLabel = new JLabel("Lucene Index Path");
        luceneIndexPathLabel.setFont(luceneIndexPathLabel.getFont().deriveFont(Font.BOLD, 12f));
        JLabel luceneIndexPathMessage = new JLabel("This path will be used to access Lucene Index folder for FACER method recommendations.");

        JButton lucenePathButton = new JButton(AllIcons.Actions.Menu_open);
        lucenePathButton.setBorder(BorderFactory.createEmptyBorder());
        lucenePathButton.setPreferredSize(new Dimension(20,20));
        lucenePathButton.setToolTipText("Choose folder");
        lucenePathButton.setContentAreaFilled(false);
        lucenePathButton.addActionListener(evt -> {
            final JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int option = fc.showOpenDialog(dialogPanel);
            if(option == JFileChooser.APPROVE_OPTION){
                File file = fc.getSelectedFile();
                luceneIndexPath.setText(file.getPath());
            }
        });

        JPanel lucenePathWrapper = new JPanel(new BorderLayout());
        lucenePathWrapper.add(luceneIndexPath);
        lucenePathWrapper.add(lucenePathButton, BorderLayout.EAST);
        luceneIndexPath.setText(configurationComponent.getLucenePath());

        luceneIndexPathLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        luceneIndexPathMessage.setAlignmentX(Component.LEFT_ALIGNMENT);
        lucenePathWrapper.setAlignmentX(Component.LEFT_ALIGNMENT);

        dialogPanel.add(luceneIndexPathLabel);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(luceneIndexPathMessage);
        dialogPanel.add(Box.createVerticalStrut(5));
        dialogPanel.add(lucenePathWrapper);

        return dialogPanel;
    }

    @Override
    public void doCancelAction() {
        if(configurationComponent.isConfigured()) {
            super.doCancelAction();
        } else {
            showConfigurationCompleteError();
        }
    }

    @Override
    protected void doOKAction() {
        if(databaseURL.getText().isEmpty() || stopwordsPath.getText().isEmpty() || luceneIndexPath.getText().isEmpty()){
            showConfigurationCompleteError();
        } else {
            configurationComponent.updateConfigurations(databaseURL.getText(), stopwordsPath.getText(), luceneIndexPath.getText());
            super.doOKAction();
        }
    }


    @Override
    public boolean shouldCloseOnCross() {
        showConfigurationCompleteError();
        return true;
    }

    private void showConfigurationCompleteError() {
        final JComponent[] inputs = new JComponent[]{
                new JLabel("Please complete configuration to use FACER recommendations."),
        };
        JOptionPane.showMessageDialog(null, inputs, "Configuration Incomplete", JOptionPane.ERROR_MESSAGE);
    }

}

