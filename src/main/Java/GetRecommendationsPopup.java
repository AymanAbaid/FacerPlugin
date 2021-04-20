import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;

public class GetRecommendationsPopup {

    static String queryPopupOptions[]= {"Get FACER recommendations"};

    static void display(Editor editor, GetRecommendations getRecommendationsCallback){
        final JBList<String> list = new JBList<>(queryPopupOptions);

        JBPopupFactory.getInstance().createListPopupBuilder(list)
                .setItemChoosenCallback(() -> getRecommendationsCallback.getRecommendationsForQuery(editor.getSelectionModel().getSelectedText().trim()))
                .createPopup()
                .showInBestPositionFor(editor);
    }
}
