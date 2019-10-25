package com.aska.fed.settings;

import com.aska.fed.gui.GitBookSummarySettingsGUI;
import com.aska.fed.utils.FileUtils;
import com.intellij.openapi.options.ConfigurationException;
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
                isFileNameModified() ||
                isDocRootModified() ||
                isFileExtModified() ||
                isIgnoredFilesModified();
    }

    private boolean isEnableAutoGenerationModified() {
        return !settingsConfig.isEnableAutoGeneration() == settingsGUI.isAutoGenerationEnabled();
    }

    private boolean isFileExtModified() {
        return !settingsConfig.getFileExtension().equals(settingsGUI.getFileExtension());
    }

    private boolean isDocRootModified() {
        return !settingsConfig.getDocRootPath().equals(settingsGUI.getDocRoot());
    }

    private boolean isIgnoredFilesModified() {
        return !settingsConfig.getIgnoredFiles().equals(settingsGUI.getIgnoredFiles());
    }

    private boolean isFileNameModified() {
        return !settingsConfig.getFileName().equals(settingsGUI.getFileName());
    }

    @Override
    public void apply() throws ConfigurationException {
        validateInput();

        settingsConfig.setEnableAutoGeneration(settingsGUI.isAutoGenerationEnabled());
        settingsConfig.setIgnoredFiles(settingsGUI.getIgnoredFiles());
        settingsConfig.setFileExtension(settingsGUI.getFileExtension());
        settingsConfig.setFileName(settingsGUI.getFileName());
        settingsConfig.setDocRootPath(settingsGUI.getDocRoot());
    }

    @Override
    public void reset() {
        settingsGUI.setFieldsFromSettings(settingsConfig);
    }

    private void validateInput() throws ConfigurationException {
        if (!isProjectFile(settingsGUI.getDocRoot())) {
            throw new ConfigurationException("Documentation directory could not be outside project root directory");
        }

        if (!FileUtils.isMdFile(settingsGUI.getFileName())) {
            throw new ConfigurationException("Output file could only be a markdown file");
        }
    }

    private boolean isProjectFile(final String path) {
        return path.contains(settingsConfig.getProjectRootPath());
    }
}
