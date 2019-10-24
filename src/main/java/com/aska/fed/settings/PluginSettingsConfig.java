package com.aska.fed.settings;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@State(
        name = "GitBookSummarryGeneratorConfig",
        storages = {@Storage("GitBookSummarryGeneratorConfig.xml")}
)
public class PluginSettingsConfig implements PersistentStateComponent<PluginSettingsConfig> {

    //persistent member should be public
    public String docRootPath;
    public String projectRootPath;
    public boolean enableAutoGeneration;
    public String fileExtension = "md";
    public List<String> ignoredFiles = Collections.emptyList();

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
        settings.projectRootPath = project.getBasePath();
        settings.docRootPath = project.getBasePath();
        return settings;
    }
}
