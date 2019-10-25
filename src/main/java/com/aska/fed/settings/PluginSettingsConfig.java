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

    private Project project;

    private String docRootPath;
    private String projectRootPath;
    private boolean enableAutoGeneration;
    private String fileExtension;
    private String ignoredFiles;
    private String fileName;

    public PluginSettingsConfig(Project project) {
        this.project = project;
    }

    //Default constructor is needed for serialization
    public PluginSettingsConfig() {
    }

    public static PluginSettingsConfig getInstance(Project project) {
        return ServiceManager.getService(project, PluginSettingsConfig.class);
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


    public String getDocRootPath() {
        return docRootPath;
    }

    public void setDocRootPath(String docRootPath) {
        this.docRootPath = docRootPath;
    }

    public String getProjectRootPath() {
        return projectRootPath;
    }

    public void setProjectRootPath(String projectRootPath) {
        this.projectRootPath = projectRootPath;
    }

    public boolean isEnableAutoGeneration() {
        return enableAutoGeneration;
    }

    public void setEnableAutoGeneration(boolean enableAutoGeneration) {
        this.enableAutoGeneration = enableAutoGeneration;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public String getIgnoredFiles() {
        return ignoredFiles;
    }

    public void setIgnoredFiles(String ignoredFiles) {
        this.ignoredFiles = ignoredFiles;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
