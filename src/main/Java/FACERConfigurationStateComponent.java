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

        public State state = new State();

        static class State {
                public State() {}
                public String databaseURL;
                public String stopwordsPath;
                public String lucenePath;
                public String logPath;
                public String datasetRootPath;
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
        public void updateConfigurations(String databaseURL, String stopwordsPath, String lucenePath, String logPath, String datasetRootPath) {
                this.state.databaseURL = databaseURL;
                this.state.stopwordsPath = stopwordsPath;
                this.state.lucenePath = lucenePath;
                this.state.logPath = logPath;
                this.state.datasetRootPath = datasetRootPath;
        }

        public String getDatabaseURL() { return this.state.databaseURL; }
        public String getStopwordsPath() {
                return this.state.stopwordsPath;
        }
        public String getLucenePath() { return this.state.lucenePath; }
        public String getLogFilePath() { return this.state.logPath; }
        public String getDatasetRootPath() { return this.state.datasetRootPath; }

        public boolean isConfigured() {
                return isFieldConfigured(this.state.databaseURL)
                        && isFieldConfigured(this.state.stopwordsPath)
                        && isFieldConfigured(this.state.lucenePath)
                        && isFieldConfigured(this.state.logPath)
                        && isFieldConfigured(this.state.datasetRootPath);
        }

        public boolean isDatasetRootPathConfigured(){
                return isFieldConfigured(this.state.datasetRootPath);
        }

        private boolean isFieldConfigured(String field){
                return field != null && !field.isEmpty();
        }

}
