import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;

public class QuerySearchAction extends AnAction implements GetRecommendations{

    Project projectRef = null;
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        System.out.println("FACER invoked");
        projectRef = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        String selectedText = editor.getSelectionModel().getSelectedText();
        GetRecommendationsPopup.display(editor, this);
    }

    private java.lang.String getToolWindowId() {
        return "FACER";
    }

    @Override
    public void getRecommendationsForQuery(String query) {
        ToolWindow toolWindow = ToolWindowManager.getInstance(projectRef).getToolWindow(getToolWindowId());
        toolWindow.show();
        ArrayList recommendationsForQuery = FACERSearchService.getInstance().getRecommendationsForQuery(query);
        FACERForm.getInstance().populateRecommendations(recommendationsForQuery.toArray());
    }


    @Override
    public void update(AnActionEvent e) {

    }

    public void openTab(AnActionEvent e){
//        this.inputFile = inputFile;
//        VirtualFile.
//        VirtualFile.createChildData().contentsToByteArray("dfsf");
//                .setBinaryContent();
    }

    public void getRelatedMethods(String query) {

        List results=new List(5);
        results.setBounds(100,100, 75,75);
        results.add(query);
        results.add(query);
        results.add(query);
        results.add(query);
        results.add(query);
        createToolWindowForRelatedMethods(results);
    }

    private void createToolWindowForQuerySearch(List results) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(projectRef);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(getToolWindowId());

//        toolWindow.setIcon(getToolWindowIcon());
//        new ToolWindowFactory.FacerToolWindowFactory().createToolWindowContent(project, toolWindow);
        new ResultsTWFactory().createToolWindowContentForQuerySearch(projectRef, toolWindow, results);

        toolWindow.show();
    }

    private void createToolWindowForRelatedMethods(List results) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(projectRef);
        ToolWindow toolWindow = toolWindowManager.getToolWindow(getToolWindowId());

//        toolWindow.setIcon(getToolWindowIcon());
//        new ToolWindowFactory.FacerToolWindowFactory().createToolWindowContent(project, toolWindow);
        new ResultsTWFactory().createToolWindowContentForRelatedMethods(projectRef, toolWindow, results);
        toolWindow.show();
    }

}