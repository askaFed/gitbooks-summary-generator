package com.aska.fed;

import com.aska.fed.model.SummaryEntry;
import com.aska.fed.settings.PluginSettingsConfig;
import com.aska.fed.utils.FileUtils;
import com.intellij.openapi.components.ProjectComponent;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class GitBookSummaryGenerator implements ProjectComponent {
    private static final String HEADER = "# Summary\n\n";

    public void generateSummaryFile(final PluginSettingsConfig settings) {
        try {
            Path root = Path.of(settings.getDocRootPath());
            CustomVisitor visitor = new CustomVisitor(settings);
            Files.walkFileTree(root, visitor);

            TreeSet<SummaryEntry> mdSummaries = visitor.getFiles().stream()
                    .filter(Objects::nonNull)
                    .filter(path -> !root.equals(path))
                    .map(root::relativize)
                    .map(this::toSummary)
                    .collect(Collectors.toCollection(TreeSet::new));

            String fileContent = getFileContent(mdSummaries);
            Path pathToFile = getOutputFilePath(settings);

            Files.write(pathToFile, fileContent.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Path getOutputFilePath(PluginSettingsConfig settings) {
        return Paths.get(settings.getProjectRootPath() + File.separator + settings.getFileName());
    }

    private String getFileContent(TreeSet<SummaryEntry> mdSummaries) {
        String combined = mdSummaries.stream()
                .map(SummaryEntry::getEntry)
                .collect(Collectors.joining("\n"));

        return addHeader(combined);
    }

    private String addHeader(String content) {
        return HEADER + content;
    }

    private SummaryEntry toSummary(Path path) {
        int nestingLvl = path.getNameCount();
        String title = getFileNameWithoutExt(path);
        return new SummaryEntry(title, path.toString(), FileUtils.isMdFile(path), nestingLvl);
    }

    private String getFileNameWithoutExt(Path file) {
        return FilenameUtils.removeExtension(file.getFileName().toString());
    }

}
