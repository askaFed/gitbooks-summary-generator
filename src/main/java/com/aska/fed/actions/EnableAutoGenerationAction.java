package com.aska.fed.actions;

import com.aska.fed.GitBookSummaryGenerator;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.SystemIndependent;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

public class EnableAutoGenerationAction extends AnAction {

    public EnableAutoGenerationAction() {
        super("Enable Auto Generation");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        @SystemIndependent String basePath = Objects.requireNonNull(project).getBasePath();

        //todo: doc root should be configurable
        Path docRoot = Paths.get(Objects.requireNonNull(basePath));
        Path projectRoot = Paths.get(Objects.requireNonNull(basePath));

        GitBookSummaryGenerator bookSummaryGenerator = project.getComponent(GitBookSummaryGenerator.class);
        enableAutoGeneration(projectRoot, docRoot, bookSummaryGenerator);
    }

    //todo: not tested
    public void enableAutoGeneration(Path projectRoot, Path docRoot, GitBookSummaryGenerator bookSummaryGenerator) {
        FileSystem fileSystem = docRoot.getFileSystem();
        try {
            WatchService watcher = fileSystem.newWatchService();
            WatchKey key = docRoot.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );
            while (key.isValid()) {
                WatchKey watchKey = watcher.take();
                List<WatchEvent<?>> events = watchKey.pollEvents();
                for (WatchEvent<?> event : events) {
                    if (event.kind() != StandardWatchEventKinds.OVERFLOW) {
                        bookSummaryGenerator.generateSummaryFile();
                    }
                }
                watchKey.reset();
            }
            System.out.println("Key is invalid!");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}