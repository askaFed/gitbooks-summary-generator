package com.aska.fed;

import com.aska.fed.model.SummaryEntry;
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

    public void generateSummaryFile(final Path docRoot, final Path projectRoot, final String fileName) {
        try {
            Path root = (docRoot == null) ? projectRoot : docRoot;
            CustomVisitor visitor = new CustomVisitor();
            Files.walkFileTree(projectRoot, visitor);

            TreeSet<SummaryEntry> mdSummaries = visitor.getFiles().stream()
                    .filter(Objects::nonNull)
                    .filter(path -> !root.equals(path))
                    .filter(file -> !fileName.equals(file.getFileName().toString()))
                    .map(root::relativize)
                    .map(this::toSummary)
                    .collect(Collectors.toCollection(TreeSet::new));

            Path pathToFile = Paths.get(projectRoot + File.separator + fileName);
            String fileContent = getFileContent(mdSummaries);

            Files.write(pathToFile, fileContent.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int valueOfNesting = path.getNameCount();
        String title = getFileNameWithoutExt(path);
        return new SummaryEntry(title, path.toString(), FileUtils.isMdFile(path), valueOfNesting);
    }

    private String getFileNameWithoutExt(Path file) {
        return FilenameUtils.removeExtension(file.getFileName().toString());
    }

}
