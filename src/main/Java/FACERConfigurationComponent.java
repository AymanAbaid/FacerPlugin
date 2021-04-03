import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "FACERConfigurationComponent",
        storages = {@Storage("FACERConfiguration.xml")})

class FACERConfigurationComponent implements PersistentStateComponent<FACERConfigurationComponent.State> {

        public State state = new State();

         static class State {
                public State() {}
                public String lucenePath;
                public boolean configured;
        }

        @Override
        public @Nullable State getState() {
                return state;
        }

        @Override
        public void loadState(@NotNull State state) {
                XmlSerializerUtil.copyBean(state, this.state);
        }

        @Nullable
        public static FACERConfigurationComponent getInstance() {
                return ApplicationManager.getApplication().getComponent(FACERConfigurationComponent.class);

        }
        public void setLucenePath(String lucenePath) {
                this.state.lucenePath = lucenePath;
        }
        public String getLucenePath() {
                return this.state.lucenePath;
        }
        public boolean isConfigured() {
                return this.state.configured;
        }

}
