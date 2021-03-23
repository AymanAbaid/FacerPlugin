import com.intellij.openapi.wm.ToolWindow;
import com.intellij.ui.components.JBList;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;


// File Name: FACERForm.java
public class FACERForm {

    private JPanel mainPanel;
    private JTextField filterRecommendations;
    private JList recommendationsList;
    private JTextField filterRelatedMethods;
    private JList relatedMethodsList;
    private JTabbedPane codeViewer;
    private JToolBar facerToolbar;
    private JButton getRelatedMethodsButton;

    private static FACERForm instance = null;

    private FACERForm() {

        recommendationsList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList)evt.getSource();
                if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {
                    // Double-click detected
                    int index = list.getSelectedIndex();
                    int x = index+1;
                }
            }
        });

//        recommendationsList.addListSelectionListener();
//         recommendationsList.addListSelectionListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                getRelatedMethods(results.getSelectedItem());
//
//            }
//        });

    }

    public static FACERForm getInstance() {
        if (instance == null) {
            instance = new FACERForm();
        }
        return instance;
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
}
