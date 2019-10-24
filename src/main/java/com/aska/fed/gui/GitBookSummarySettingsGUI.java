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
    PluginSettingsConfig settingsConfig;

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
        settingsConfig = PluginSettingsConfig.getInstance(project);
        Objects.requireNonNull(settingsConfig);

        enableAutoGenerationOnTreeChanges.setSelected(settingsConfig.enableAutoGeneration);
        fileExtension.setText(settingsConfig.fileExtension);
        fileName.setText(settingsConfig.fileName);

        FileChooserDescriptor singleFolderDescriptor = FileChooserDescriptorFactory.createSingleFolderDescriptor();
        docRoot.addBrowseFolderListener(new TextBrowseFolderListener(singleFolderDescriptor));
        docRoot.setText(settingsConfig.docRootPath);

        FileChooserDescriptor multipleFilesDescriptor = FileChooserDescriptorFactory.createMultipleFilesNoJarsDescriptor();
        ignoredFiles.addBrowseFolderListener(new TextBrowseFolderListener(multipleFilesDescriptor));
        ignoredFiles.setText(settingsConfig.ignoredFiles);
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
