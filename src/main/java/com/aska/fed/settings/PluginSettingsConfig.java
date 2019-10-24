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

    private PluginSettingsConfig() {
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

    public static PluginSettingsConfig getInstance(Project project) {
        PluginSettingsConfig settings = ServiceManager.getService(project, PluginSettingsConfig.class);
        setDefaulSettings(project, settings);
        return settings;
    }

    private static void setDefaulSettings(Project project, PluginSettingsConfig settings) {
        settings.projectRootPath = project.getBasePath();
        settings.docRootPath = project.getBasePath();
        settings.ignoredFiles = "";
        settings.fileExtension = DEFAULT_FILE_EXTENSION;
        settings.fileName = DEFAULT_FILE_NAME;
    }

}
