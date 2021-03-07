import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.components.JBList;
import com.intellij.openapi.editor.Editor;

import java.util.concurrent.atomic.AtomicReference;

public class Popup {

    static String r_l[]= {"Reommendation1","Reommendation2","Reommendation3"};
    static String selected_String;

    static String display(Editor editor){
        AtomicReference<String> selected_String= new AtomicReference<String>();
        final JBList<String> list = new JBList<>(r_l);

        JBPopupFactory.getInstance().createListPopupBuilder(list)
                .setTitle("Recommendation List").setItemChoosenCallback(() -> {
            final String name = (String)list.getSelectedValue();
            selected_String.set(name);

        }).createPopup().showInBestPositionFor(editor);

        return selected_String.get();
    }
}
