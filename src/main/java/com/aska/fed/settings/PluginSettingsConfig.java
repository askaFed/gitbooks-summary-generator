package com.aska.fed.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "GitBookSummarryGeneratorConfig",
        storages = {@Storage("GitBookSummarryGeneratorConfig.xml")}
)
public class PluginSettingsConfig implements PersistentStateComponent<PluginSettingsConfig> {
    private static final String DEFAULT_FILE_EXTENSION = "md";
    private static final String DEFAULT_FILE_NAME = "SUMMARY.md";

    //persistent members should be public
    public String docRootPath;
    public String projectRootPath;
    public boolean enableAutoGeneration;
    public String fileExtension;
    public String ignoredFiles;
    public String fileName;

    private Project project;

    public PluginSettingsConfig(Project project) {
        this.project = project;
    }

    public PluginSettingsConfig() {
    }

    @Nullable
    @Override
    public PluginSettingsConfig getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull PluginSettingsConfig state) {
        XmlSerializerUtil.copyBean(state, this);
    }

    @Override
    public void initializeComponent() {
        loadState(this);
        if (projectRootPath == null) {
            setDefaults();
        }
    }

    private void setDefaults() {
        projectRootPath = project.getBasePath();
        docRootPath = project.getBasePath();
        ignoredFiles = "";
        fileExtension = DEFAULT_FILE_EXTENSION;
        fileName = DEFAULT_FILE_NAME;
    }

    public static PluginSettingsConfig getInstance(Project project) {
        return ServiceManager.getService(project, PluginSettingsConfig.class);
    }

}
