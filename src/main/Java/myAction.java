import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Caret;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.FileViewProvider;
import com.intellij.psi.PsiFile;
import org.jetbrains.annotations.NotNull;

public class myAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull final AnActionEvent e) {
        System.out.println("FACER invoked");
        final Project project = e.getProject();
        final Editor editor = e.getData(CommonDataKeys.EDITOR);
        final Document document = editor.getDocument();


        // Work off of the primary caret to get the selection info
        Caret primaryCaret = editor.getCaretModel().getPrimaryCaret();
        int start = primaryCaret.getSelectionStart();
        int end = primaryCaret.getSelectionEnd();
        String selected_text = editor.getSelectionModel().getSelectedText();
        System.out.println(selected_text);
        String selected_recommendation = Popup.display(editor);

        this.openTab(e);


        // Replace the selection with a fixed string.
        // Must do this document change in a write action context.
//        WriteCommandAction.runWriteCommandAction(project, () ->
//                document.replaceString(start, end, "editor_basics")
//        );

        // De-select the text range that was just replaced
//        primaryCaret.removeSelection();
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

}