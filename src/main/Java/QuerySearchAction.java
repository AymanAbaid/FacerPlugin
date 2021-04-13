import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;

public class QuerySearchAction extends AnAction implements GetRecommendations{

    Project projectRef = null;
    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {

        FACERConfigurationComponent instance = FACERConfigurationComponent.getInstance();
        if(!instance.isConfigured()){
            new FACERConfigurationDialogWrapper().showAndGet();
        } else {
        projectRef = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        //      event 0
        String query_text = editor.getSelectionModel().getSelectedText();
        if( query_text!= null)
            EventLoggerService.getInstance().log(0, new ArrayList<String>(Arrays.asList("query_text:" + query_text )));
        GetRecommendationsPopup.display(editor, this);
      }
    }

    private java.lang.String getToolWindowId() {
        return "FACER";
    }

    @Override
    public void getRecommendationsForQuery(String query) {
        if(query == null) {
            FACERErrorDialog.showConfigurationCompleteError("Error","No text selected. Please select text for recommendations.");

         } else {
            ToolWindow toolWindow = ToolWindowManager.getInstance(projectRef).getToolWindow(getToolWindowId());
            toolWindow.show();
            ArrayList recommendationsForQuery = FACERSearchService.getInstance().getRecommendationsForQuery(query);
            FACERForm.getInstance().populateRecommendations(query, recommendationsForQuery.toArray());
        }
    }

    @Override
    public void update(AnActionEvent e) {

    }
}