package com.aska.fed.gui;

import com.aska.fed.settings.PluginSettingsConfig;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class GitBookSummarySettingsGUI {

    private PluginSettingsConfig settingsConfig;

    private JPanel autoGenerationPanel;
    private JCheckBox enableAutoGenerationOnTreeChanges;
    private JPanel rootPanel;
    private JPanel docRootPanel;
    private JLabel docRootL;
    private JTextField fileExtension;
    private JLabel fileExtL;
    private JPanel FileExtensionPanel;
    private JPanel filesToIgnore;
    private JLabel filesToIgnoreL;
    private JTextField ignoredFiles;
    private TextFieldWithBrowseButton docRoot;

    public GitBookSummarySettingsGUI(Project project) {
        settingsConfig = PluginSettingsConfig.getInstance(project);
        Objects.requireNonNull(settingsConfig);

        enableAutoGenerationOnTreeChanges.setSelected(settingsConfig.enableAutoGeneration);
        fileExtension.setText(settingsConfig.fileExtension);
        ignoredFiles.setText(settingsConfig.ignoredFiles.toString());

        FileChooserDescriptor singleFileDescriptor = FileChooserDescriptorFactory.createSingleFileDescriptor();
        docRoot.addBrowseFolderListener(new TextBrowseFolderListener(singleFileDescriptor));
        docRoot.setText(settingsConfig.docRootPath);
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
        return fileExtension.getText();
    }

    public List<String> getIgnoredFiles() {
        return Collections.singletonList(ignoredFiles.getText());
    }

    public static GitBookSummarySettingsGUI getInstance(Project project) {
        return new GitBookSummarySettingsGUI(project);
    }
}
