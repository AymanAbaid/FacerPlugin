import com.intellij.openapi.ui.DialogWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class FACERErrorDialogWrapper extends DialogWrapper {

    private final String title;
    private final String message;

    public FACERErrorDialogWrapper(String title, String message) {
        super(true); // use current window as parent
        this.title = title;
        this.message = message;
        init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel dialogPanel = new JPanel(new BorderLayout());
        setTitle(title);

        JLabel label = new JLabel(message);
        label.setPreferredSize(new Dimension(500, 100));
        dialogPanel.add(label, BorderLayout.CENTER);

        return dialogPanel;
    }

    @Override
    protected @NotNull Action[] createActions() {
        return new Action[]{getOKAction()};
    }
}

