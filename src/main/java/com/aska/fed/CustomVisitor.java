package com.aska.fed;

import com.aska.fed.settings.PluginSettingsConfig;
import com.aska.fed.utils.FileUtils;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.TreeSet;
import java.util.function.BiPredicate;

public class CustomVisitor implements FileVisitor<Path> {
    private TreeSet<Path> files = new TreeSet<>();

    private PluginSettingsConfig settings;

    public CustomVisitor(PluginSettingsConfig settings) {
        this.settings = settings;
    }

    public TreeSet<Path> getFiles() {
        return files;
    }


    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
        if (isSupportedFile(file)) {
            files.add(file);
            files.add(file.getParent());
        }
        return FileVisitResult.CONTINUE;
    }

    private final BiPredicate<Path, String> notIgnoredFile =
            (file, fileToIgnore) -> !file.toString().equals(fileToIgnore);

    private final BiPredicate<Path, String> notOutputFile =
            (file, fileToIgnore) -> !file.getFileName().toString().equals(fileToIgnore);

    private boolean isSupportedFile(final Path file) {
        return FileUtils.isMdFile(file) &&
                notIgnoredFile.test(file, settings.ignoredFiles) &&
                notOutputFile.test(file, settings.fileName);
    }

    @Override
    public FileVisitResult visitFileFailed(Path file, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
        return FileVisitResult.CONTINUE;
    }

}
