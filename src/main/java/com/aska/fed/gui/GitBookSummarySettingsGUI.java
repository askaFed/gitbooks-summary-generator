package com.aska.fed.gui;

import com.aska.fed.settings.PluginSettingsConfig;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextBrowseFolderListener;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;

import javax.swing.*;
import java.util.Objects;

public class GitBookSummarySettingsGUI {

    //panels
    private JPanel rootPanel;
    private JPanel autoGenerationPanel;
    private JPanel docRootPanel;
    private JPanel FileExtensionPanel;
    private JPanel filesToIgnorePanel;

    //labels
    private JLabel fileExtensionLabel;
    private JLabel filesToIgnoreLabel;
    private JLabel docRootLabel;
    private JLabel fileNameLabel;

    //fields
    private JCheckBox enableAutoGenerationOnTreeChanges;
    private JTextField fileExtension;
    private TextFieldWithBrowseButton ignoredFiles;
    private TextFieldWithBrowseButton docRoot;
    private JTextField fileName;

    public GitBookSummarySettingsGUI(Project project) {
        PluginSettingsConfig settingsConfig = PluginSettingsConfig.getInstance(project);

        FileChooserDescriptor singleFolderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        docRoot.addBrowseFolderListener(new TextBrowseFolderListener(singleFolderDescriptor));

        FileChooserDescriptor multipleFilesDescriptor = FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor();
        ignoredFiles.addBrowseFolderListener(new TextBrowseFolderListener(multipleFilesDescriptor));

        setFieldsFromSettings(Objects.requireNonNull(settingsConfig));
    }

    public void setFieldsFromSettings(PluginSettingsConfig settingsConfig) {
        enableAutoGenerationOnTreeChanges.setSelected(settingsConfig.isEnableAutoGeneration());
        fileExtension.setText(settingsConfig.getFileExtension());
        fileName.setText(settingsConfig.getFileName());
        docRoot.setText(settingsConfig.getDocRootPath());
        ignoredFiles.setText(settingsConfig.getIgnoredFiles());
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

    public String getFileName() {
        return fileName.getText();
    }

    public String getIgnoredFiles() {
        return ignoredFiles.getText();
    }

    public static GitBookSummarySettingsGUI getInstance(Project project) {
        return new GitBookSummarySettingsGUI(project);
    }
}
