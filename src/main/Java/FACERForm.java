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
import java.util.Arrays;

public class FACERForm {

    private JPanel mainPanel;
    private JTextField filterRecommendations;
    private JList recommendationsList;
    private JTextField filterRelatedMethods;
    private JList relatedMethodsList;
    private JTabbedPane codeViewer;


    private static FACERForm instance = null;

    private FACERForm() {
        recommendationsList.addMouseListener(queryMethodsListMouseAdapter);
        relatedMethodsList.addMouseListener(relatedMethodsListMouseAdapter);
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
    private MouseAdapter queryMethodsListMouseAdapter = new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList)evt.getSource();
            if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                // Double-click detected
                int index = list.getSelectedIndex();
                Method method = FACERSearchService.getInstance().getQueryResultMethod(index);
                // event 2
                EventLoggerService.getInstance().log(2,
                        new ArrayList<String>(Arrays.asList(
                                "method_id:" + method.id,
                                "method_name:" + method.name,
                                "rank:" + index)));
                if (method != null) {
                    showMethodBody(method, false);
                }
                getRelatedMethodsForMethod(method);
            }
        }
    };

    @NotNull
    private MouseAdapter relatedMethodsListMouseAdapter = new MouseAdapter() {
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList)evt.getSource();
            if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                // Double-click detected
                int index = list.getSelectedIndex();
                Method method = FACERSearchService.getInstance().getRelatedMethod(index);
                // event 6
                EventLoggerService.getInstance().log(6,
                        new ArrayList<String>(Arrays.asList(
                                "method_id:" + method.id,
                                "method_name:" + method.name,
                                "rank:"+index)));
                if (method != null){
                    showMethodBody(method, true);
                }
            }
        }
    };

    public JPanel getContent() {
        return mainPanel;
    }

    public void populateRecommendations(String query, Object[] results) {
        recommendationsList.setListData(results);
        EventLoggerService.getInstance().log(1, new ArrayList<String>(Arrays.asList("query_text:" + query,
                "result_count:" + results.length)));
    }

    public void populateRelatedMethods(Method queryMethod, Object[] results) {
        relatedMethodsList.setListData(results);
        // event 5
        EventLoggerService.getInstance().log(5,
                new ArrayList<String>(Arrays.asList(
                        "method_id:" + queryMethod.id,
                        "method_name:" + queryMethod.name,
                        "result_count:" + results.length)));
    }

    private void getRelatedMethodsForMethod(Method method) {
        //      event 4
        EventLoggerService.getInstance().log(4, new ArrayList<String>(Arrays.asList(
                "method_id:" + method.id,
                "method_name:" + method.name)));
        ArrayList relatedMethods = FACERSearchService.getInstance().getRelatedMethods(method.id);
        populateRelatedMethods(method, relatedMethods.toArray());
    }

    public void showMethodBody(Method method, boolean isRelatedMethodSearch){
        String tabTitle = method.id + ": " + method.name;
        int existingTabIndex = codeViewer.indexOfTab(tabTitle);
        if (existingTabIndex != -1){
            codeViewer.setSelectedIndex(existingTabIndex);
            return;
        }

        JPanel tabPanel = new JPanel();
        tabPanel.setLayout(new BorderLayout());

        JButton getRelatedMethodsButton = new JButton(AllIcons.Diff.MagicResolve);
        getRelatedMethodsButton.setBorder(BorderFactory.createEmptyBorder());
        getRelatedMethodsButton.setPreferredSize(new Dimension(20,20));
        getRelatedMethodsButton.setToolTipText("Search related methods");
        getRelatedMethodsButton.addActionListener(evt -> {
            getRelatedMethodsForMethod(method);
        });

        JButton copyMethodBodyButton = new JButton(AllIcons.Actions.Copy);
        copyMethodBodyButton.setBorder(BorderFactory.createEmptyBorder());
        copyMethodBodyButton.setPreferredSize(new Dimension(20,20));
        copyMethodBodyButton.setToolTipText("Add code to my file");
        copyMethodBodyButton.addActionListener(evt -> {
            // event 3
            if (!isRelatedMethodSearch)
            {
                EventLoggerService.getInstance().log(3,
                        new ArrayList<String>(Arrays.asList(
                                "method_id:" + method.id,
                                "method_name:" + method.name)));
            }
            else {
                // event 7
                EventLoggerService.getInstance().log(7,
                        new ArrayList<String>(Arrays.asList(
                                "method_id:" + method.id,
                                "method_name:" + method.name)));
            }
            final Project project = ProjectUtil.guessCurrentProject(mainPanel);
            @Nullable Editor editor = FileEditorManager.getInstance(project).getSelectedTextEditor();
            Document document = editor.getDocument();

            // Work off of the primary caret to get the selection info
            Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
            int end = primaryCaret.getSelectionEnd();

            // Replace the selection with a fixed string.
            // Must do this document change in a write action context.
            WriteCommandAction.runWriteCommandAction(project, () ->
                    document.insertString(end, "\n" + method.body)
            );

            // De-select the text range that was just replaced
            primaryCaret.removeSelection();
        });

        JTextArea textArea = new JTextArea(method.body);
//        textArea.setEditable(true);
        JScrollPane scrollableTextArea = new JBScrollPane(textArea);

        JPanel optionsWrapper = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        optionsWrapper.add(getRelatedMethodsButton);
        optionsWrapper.add(copyMethodBodyButton);

        if (isRelatedMethodSearch) {
            JButton upvoteMethodButton = new JButton(AllIcons.Actions.Commit);
            upvoteMethodButton.setBorder(BorderFactory.createEmptyBorder());
            upvoteMethodButton.setPreferredSize(new Dimension(20, 20));
            upvoteMethodButton.setToolTipText("Upvote method");
            upvoteMethodButton.addActionListener(evt -> {
                // event 8
                EventLoggerService.getInstance().log(8,
                        new ArrayList<String>(Arrays.asList(
                                "method_id:" + method.id,
                                "method_name:" + method.name)));
            });
            optionsWrapper.add(upvoteMethodButton);
        }

        optionsWrapper.setBackground(Color.white);

        tabPanel.add(optionsWrapper, BorderLayout.NORTH);
        tabPanel.add(scrollableTextArea, BorderLayout.CENTER);

        codeViewer.addTab(tabTitle, tabPanel);
        int index = codeViewer.indexOfTab(tabTitle);

        JPanel tabLabelPanel = new JPanel(new FlowLayout());
        tabLabelPanel.setOpaque(false);
        JLabel lblTitle = new JLabel(tabTitle);

        JButton btnClose = new JButton(AllIcons.Actions.Cancel);
        btnClose.setBorder(BorderFactory.createEmptyBorder());
        btnClose.setPreferredSize(new Dimension(8,8));

        btnClose.addActionListener(evt -> {
            int tabIndex = codeViewer.indexOfTab(tabTitle);
            if (tabIndex >= 0) {
                codeViewer.removeTabAt(tabIndex);
            }
        });

        tabLabelPanel.add(lblTitle);
        tabLabelPanel.add(btnClose);

        codeViewer.setTabComponentAt(index, tabLabelPanel);
        codeViewer.setSelectedIndex(index);
    }
}
