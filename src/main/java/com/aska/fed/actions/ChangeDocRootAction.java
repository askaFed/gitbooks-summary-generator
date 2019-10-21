package com.aska.fed.actions;

import com.aska.fed.GitBookSummaryGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.SystemIndependent;

import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ChangeDocRootAction extends AnAction {

    public ChangeDocRootAction() {
        super("Change Documentation Root");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        VirtualFile[] virtualFiles = FileChooser.chooseFiles(
                new FileChooserDescriptor(false, true, false, false, false, false),
                project,
                null
        );

        Path docRoot = Arrays.stream(virtualFiles).map(VirtualFile::getPath).findFirst()
                .map(p -> Path.of(p)).orElse(Path.of(Objects.requireNonNull(project).getBasePath()));

        project.getComponent(GitBookSummaryGenerator.class).setDocRoot(docRoot);
    }
}