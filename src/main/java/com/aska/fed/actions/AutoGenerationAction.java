package com.aska.fed.actions;

import com.aska.fed.GitBookSummaryGenerator;
import com.aska.fed.settings.PluginSettingsConfig;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Objects;

//todo: not implemented yet
public class AutoGenerationAction extends AnAction {

    public AutoGenerationAction() {
        super("Enable Auto Generation");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();
        PluginSettingsConfig settings = PluginSettingsConfig.getInstance(project);
        Path docRootPath = Path.of(settings.docRootPath);

        FileSystem fileSystem = docRootPath.getFileSystem();
        try {
            WatchService watcher = fileSystem.newWatchService();
            WatchKey key = docRootPath.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY
            );
            while (key.isValid()) {
                WatchKey watchKey = watcher.take();
                List<WatchEvent<?>> watchEvents = watchKey.pollEvents();
                for (WatchEvent<?> watchEvent : watchEvents) {
                    if (watchEvent.kind() != StandardWatchEventKinds.OVERFLOW) {
                        Objects.requireNonNull(project)
                                .getComponent(GitBookSummaryGenerator.class)
                                .generateSummaryFile(settings);
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