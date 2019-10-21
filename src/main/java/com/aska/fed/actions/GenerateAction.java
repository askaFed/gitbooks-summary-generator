package com.aska.fed.actions;

import com.aska.fed.GitBookSummaryGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.SystemIndependent;

import java.nio.file.*;
import java.util.*;

public class GenerateAction extends AnAction {
    public GenerateAction() {
        super("Generate Summary File");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        @SystemIndependent String basePath = Objects.requireNonNull(project).getBasePath();
        Path projectRoot = Paths.get(Objects.requireNonNull(basePath));

        GitBookSummaryGenerator component = project.getComponent(GitBookSummaryGenerator.class);
        component.setProjectRoot(projectRoot);
        component.generateSummaryFile();
    }
}