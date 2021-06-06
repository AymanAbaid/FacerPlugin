import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "FACERConfigurationStateComponent",
        storages = {@Storage("FACERConfiguration.xml")})

class FACERConfigurationStateComponent implements PersistentStateComponent<FACERConfigurationStateComponent.State> {

        public final boolean isDbHardcoded = false;
        final String DB_URL_LIVE = "jdbc:mysql://203.135.63.70:3306/faceremserepopoint5?useSSL=false&user=shamsa&password=Mysql123!@#";

        public State state = new State();

        static class State {
                public State() {}
                public String databaseURL;
                public String resourcesRootPath;
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
        public static FACERConfigurationStateComponent getInstance() {
                return ApplicationManager.getApplication().getComponent(FACERConfigurationStateComponent.class);
        }
        public void updateConfigurations(String databaseURL, String resourcesFolderRootPath) {
                this.state.databaseURL = isDbHardcoded ? DB_URL_LIVE : databaseURL;
                this.state.resourcesRootPath = resourcesFolderRootPath;
        }

        public String getDatabaseURL() { return isDbHardcoded ? DB_URL_LIVE : this.state.databaseURL; }
        public String getResourcesFolderRootPath() {
                return this.state.resourcesRootPath;
        }

        public boolean isConfigured() {
                // either db is hardcoded or configured
                return (isDbHardcoded || isFieldConfigured(this.state.databaseURL))
                        && isFieldConfigured(this.state.resourcesRootPath);
        }

        private boolean isFieldConfigured(String field){
                return field != null && !field.isEmpty();
        }

}
