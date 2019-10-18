import com.intellij.openapi.components.ProjectComponent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class GitBookSummaryGenerator implements ProjectComponent {

    private static final String DEFAULT_FILE_NAME = "SUMMARY.md";

    public void generateSummaryFile(Path projectRoot, Path docRoot) {
        try {
            CustomVisitor visitor = new CustomVisitor();
            Files.walkFileTree(projectRoot, visitor);

            TreeSet<SummaryEntry> mdSummaries = visitor.getFiles().stream()
                    .filter(Objects::nonNull)
                    .filter(path -> !projectRoot.equals(path))
                    .filter(isNotSummarryFile)
                    .map(docRoot::relativize)
                    .map(this::toSummary)
                    .collect(Collectors.toCollection(TreeSet::new));

            Path pathToFile = Paths.get(projectRoot + File.separator + DEFAULT_FILE_NAME);
            String fileContent = getFileContent(mdSummaries);

            Files.write(pathToFile, fileContent.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Predicate<Path> isNotSummarryFile = file -> !DEFAULT_FILE_NAME.equals(file.getFileName().toString());

    private String getFileContent(TreeSet<SummaryEntry> mdSummaries) {
        String combined = mdSummaries.stream().map(SummaryEntry::getEntry).collect(Collectors.joining("\n"));
        return addHeader(combined);
    }

    private String addHeader(String content) {
        return "# Summary\n\n" + content;
    }

    private SummaryEntry toSummary(Path path) {
        int valueOfNesting = path.getNameCount();
        return new SummaryEntry(path.getFileName().toString(), path.toString(), FileUtils.isMdFile(path), valueOfNesting);
    }

}
