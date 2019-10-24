package com.aska.fed.settings;

import com.aska.fed.gui.GitBookSummarySettingsGUI;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GitbookSummaryConfigurable implements SearchableConfigurable {

    private GitBookSummarySettingsGUI settingsGUI;
    private Project project;
    private PluginSettingsConfig settingsConfig;

    public GitbookSummaryConfigurable(@NotNull Project project) {
        this.project = project;
        this.settingsConfig = PluginSettingsConfig.getInstance(project);
    }

    @NotNull
    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "GitBook Summary Generator";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        settingsGUI = new GitBookSummarySettingsGUI(project);
        return settingsGUI.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return isEnableAutoGenerationModified() ||
                isFileNameChanged() ||
                isDocRootModified() ||
                isFileExtModified() ||
                isIgnoredFilesModified();
    }

    private boolean isEnableAutoGenerationModified() {
        return !settingsConfig.enableAutoGeneration == settingsGUI.isAutoGenerationEnabled();
    }

    private boolean isFileExtModified() {
        return !settingsConfig.fileExtension.equals(settingsGUI.getFileExtension());
    }

    private boolean isDocRootModified() {
        return !settingsConfig.docRootPath.equals(settingsGUI.getDocRoot());
    }

    private boolean isIgnoredFilesModified() {
        return !settingsConfig.ignoredFiles.equals(settingsGUI.getIgnoredFiles());
    }

    private boolean isFileNameChanged() {
        return !settingsConfig.fileName.equals(settingsGUI.getFileName());
    }

    @Override
    public void apply() {
        settingsConfig.enableAutoGeneration = settingsGUI.isAutoGenerationEnabled();
        settingsConfig.ignoredFiles = settingsGUI.getIgnoredFiles();
        settingsConfig.fileExtension = settingsGUI.getFileExtension();
        settingsConfig.docRootPath = settingsGUI.getDocRoot(); //todo: to validate if it is within project dir
    }
}
