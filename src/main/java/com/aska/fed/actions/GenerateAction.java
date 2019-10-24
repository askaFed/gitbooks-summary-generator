package com.aska.fed.actions;

import com.aska.fed.GitBookSummaryGenerator;
import com.aska.fed.settings.PluginSettingsConfig;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.nio.file.*;
import java.util.*;

public class GenerateAction extends AnAction {
    public GenerateAction() {
        super("Generate Summary File");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        PluginSettingsConfig settings = PluginSettingsConfig.getInstance(project);
        Path docRootPath = Path.of(settings.docRootPath);
        Path projectRootPath = Path.of(settings.projectRootPath);

        Objects.requireNonNull(project)
                .getComponent(GitBookSummaryGenerator.class)
                .generateSummaryFile(docRootPath, projectRootPath, settings.fileName);
    }
}