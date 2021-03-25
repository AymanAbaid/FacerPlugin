import com.intellij.icons.AllIcons;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileEditorManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FACERForm {

    private JPanel mainPanel;
    private JTextField filterRecommendations;
    private JList recommendationsList;
    private JTextField filterRelatedMethods;
    private JList relatedMethodsList;
    private JTabbedPane codeViewer;

    private static FACERForm instance = null;

    private FACERForm() {
        recommendationsList.addMouseListener(getMethodsListMouseAdapter());
        relatedMethodsList.addMouseListener(getMethodsListMouseAdapter());
        codeViewer.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        codeViewer.removeAll();
    }

    public static FACERForm getInstance() {
        if (instance == null) {
            instance = new FACERForm();
        }
        return instance;
    }

    @NotNull
    private MouseAdapter getMethodsListMouseAdapter() {
        return new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                    // Double-click detected
                    int index = list.getSelectedIndex();
                    showMethodBody(list.getSelectedValue().toString(),"@NotNull\n" +
                            "    private MouseAdapter getMethodsListMouseAdapter() {\n" +
                            "        return new MouseAdapter() {\n" +
                            "            public void mouseClicked(MouseEvent evt) {\n" +
                            "                JList list = (JList)evt.getSource();\n" +
                            "                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {\n" +
                            "                    // Double-click detected\n" +
                            "                    int index = list.getSelectedIndex();\n" +
                            "                    showMethodBody(list.getSelectedValue().toString(),\"public void testSomething(){}\");\n" +
                            "\n" +
                            "                }\n" +
                            "            }\n" +
                            "        };\n" +
                            "    }");

                }
            }
        };
    }

    public JPanel getContent() {
        return mainPanel;
    }

    public void populateRecommendations(Object[] results) {
        recommendationsList.setListData(results);
    }

    public void populateRelatedMethods(Object[] results) {
        relatedMethodsList.setListData(results);
    }

    public void showMethodBody(String methodSignature, String methodBody){
        int existingTabIndex = codeViewer.indexOfTab(methodSignature);
        if (existingTabIndex != -1){
            codeViewer.setSelectedIndex(existingTabIndex);
            return;
        }

        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BoxLayout(tabPanel, BoxLayout.Y_AXIS));

        JButton getRelatedMethodsButton = new JButton(AllIcons.Diff.MagicResolve);
        getRelatedMethodsButton.setBorder(BorderFactory.createEmptyBorder());
        getRelatedMethodsButton.setPreferredSize(new Dimension(20,20));
        getRelatedMethodsButton.setToolTipText("Search related methods");
        getRelatedMethodsButton.addActionListener(evt -> {
            ArrayList relatedMethods = FACERSearchService.getInstance().getRelatedMethods(methodSignature);
            populateRelatedMethods(relatedMethods.toArray());
        });

        JButton copyMethodBodyButton = new JButton(AllIcons.Actions.Copy);
        copyMethodBodyButton.setBorder(BorderFactory.createEmptyBorder());
        copyMethodBodyButton.setPreferredSize(new Dimension(20,20));
        copyMethodBodyButton.setToolTipText("Add code to my file");
        copyMethodBodyButton.addActionListener(evt -> {
            final Project project = ProjectUtil.guessCurrentProject(mainPanel);
            @Nullable Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            Document document = editor.getDocument();

            // Work off of the primary caret to get the selection info
            Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
            int start = primaryCaret.getSelectionStart();
            int end = primaryCaret.getSelectionEnd();

            // Replace the selection with a fixed string.
            // Must do this document change in a write action context.
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.replaceString(start, end, methodBody)
            );

            // De-select the text range that was just replaced
            primaryCaret.removeSelection();

            // Copy to clipboard
//            StringSelection stringSelection = new StringSelection(methodBody);
//            Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
//            clpbrd.setContents (stringSelection, null);
        });

        JTextArea textArea = new JTextArea(methodBody);
//        textArea.setEditable(true);
        JScrollPane scrollableTextArea = new JBScrollPane(textArea);

        JPanel optionsWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        optionsWrapper.add(getRelatedMethodsButton);
        optionsWrapper.add(copyMethodBodyButton);
        optionsWrapper.setBackground(Color.white);

        tabPanel.add(optionsWrapper);
        tabPanel.add(scrollableTextArea);

        codeViewer.addTab(methodSignature, tabPanel);
        int index = codeViewer.indexOfTab(methodSignature);

        JPanel tabLabelPanel = new JPanel(new FlowLayout());
        tabLabelPanel.setOpaque(false);
        JLabel lblTitle = new JLabel(methodSignature);

        JButton btnClose = new JButton(AllIcons.Actions.Cancel);
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        btnClose.setPreferredSize(new Dimension(8,8));

        btnClose.addActionListener(evt -> {
            if (index >= 0) {
                codeViewer.removeTabAt(index);
            }
        });

        tabLabelPanel.add(lblTitle);
        tabLabelPanel.add(btnClose);

        codeViewer.setTabComponentAt(index, tabLabelPanel);
        codeViewer.setSelectedIndex(index);
    }

}
