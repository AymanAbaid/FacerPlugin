import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;

public class GetRecommendationsPopup {

    static String queryPopupOptions[]= {"Get Recommendations"};

    static void display(Editor editor, GetRecommendations getRecommendationsCallback){
        final JBList<String> list = new JBList<>(queryPopupOptions);

        JBPopupFactory.getInstance().createListPopupBuilder(list)
                .setTitle("FACER Actions")
                .setItemChoosenCallback(() -> getRecommendationsCallback.getRecommendationsForQuery(editor.getSelectionModel().getSelectedText()))
                .createPopup()
                .showInBestPositionFor(editor);
    }
}
