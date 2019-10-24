package com.aska.fed.gui;

import com.aska.fed.settings.PluginSettingsConfig;
import com.intellij.openapi.project.Project;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GitBookSummarySettingsGUI {

    private PluginSettingsConfig settingsConfig;

    private JPanel autoGenerationSetting;
    private JCheckBox enableAutoGenerationOnTreeChanges;
    private JPanel rootPanel;
    private JPanel docRootSettings;
    private JTextField docRoot;
    private JLabel docRootL;
    private JTextField fileExt;
    private JLabel fileExtL;
    private JPanel FileExtensionSettings;
    private JPanel filesToIgnore;
    private JLabel filesToIgnoreL;
    private JTextField ignoredFiles;

    public GitBookSummarySettingsGUI(Project project) {
        settingsConfig = PluginSettingsConfig.getInstance(project);
        Objects.requireNonNull(settingsConfig);

        enableAutoGenerationOnTreeChanges.setSelected(settingsConfig.enableAutoGeneration);
        docRoot.setText(settingsConfig.docRootPath);
        fileExt.setText(settingsConfig.fileExtension);
        ignoredFiles.setText(settingsConfig.ignoredFiles.toString());
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    public boolean isAutoGenerationEnabled() {
        return enableAutoGenerationOnTreeChanges.isSelected();
    }

    public String getDocRoot() {
        return docRoot.getText();
    }

    public String getFileExtension() {
        return fileExt.getText();
    }

    public List<String> getIgnoredFiles() {
        return Collections.singletonList(ignoredFiles.getText());
    }

    public static GitBookSummarySettingsGUI getInstance(Project project) {
        return new GitBookSummarySettingsGUI(project);
    }

}
