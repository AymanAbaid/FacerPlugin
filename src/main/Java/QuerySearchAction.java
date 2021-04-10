import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class QuerySearchAction extends AnAction implements GetRecommendations{

    Project projectRef = null;
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
//      event 0
        projectRef = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        GetRecommendationsPopup.display(editor, this);
    }

    private java.lang.String getToolWindowId() {
        return "FACER";
    }

    @Override
    public void getRecommendationsForQuery(String query) {
        if(query == null)
            new FACERErrorDialogWrapper("Error", "No text selected. Please select text for recommendations.").showAndGet();
        ToolWindow toolWindow = ToolWindowManager.getInstance(projectRef).getToolWindow(getToolWindowId());
        toolWindow.show();
        ArrayList recommendationsForQuery = FACERSearchService.getInstance().getRecommendationsForQuery(query);
        FACERForm.getInstance().populateRecommendations(recommendationsForQuery.toArray());
    }


    @Override
    public void update(AnActionEvent e) {

    }
}