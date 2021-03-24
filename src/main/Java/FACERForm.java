import com.intellij.icons.AllIcons;
import com.intellij.ui.components.JBScrollPane;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


// File Name: FACERForm.java
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
//        codeViewer.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
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
                    showMethodBody(list.getSelectedValue().toString(), "5:34 PM\tTextMate bundle load error: Bundle ruby can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle rust can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle scss can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle search-result can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle shaderlab can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle shellscript can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle sql can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle swift can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle typescript-basics can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle vb can't be registered\n + 5:34 PM\tTextMate bundle load error: Bundle ruby can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle rust can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle scss can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle search-result can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle shaderlab can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle shellscript can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle sql can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle swift can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle typescript-basics can't be registered\n" +
                            "\n" +
                            "5:34 PM\tTextMate bundle load error: Bundle vb can't be registered\n");
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
        getRelatedMethodsButton.addActionListener(evt -> {
            ArrayList relatedMethods = FACERSearchService.getInstance().getRelatedMethods(methodSignature);
            populateRelatedMethods(relatedMethods.toArray());
        });

        JTextArea textArea = new JTextArea(methodBody);
        textArea.setEditable(true);
        JScrollPane scrollableTextArea = new JBScrollPane(textArea);

        JPanel optionsWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        optionsWrapper.add(getRelatedMethodsButton);
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
