import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VirtualFile;
import gherkin.lexer.Pa;
import org.jetbrains.annotations.SystemIndependent;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class GenerateAction extends AnAction {
    public GenerateAction() {
        super("Generate Summary File");
    }

    public void actionPerformed(AnActionEvent event) {
        Project project = event.getProject();

        @SystemIndependent String basePath = Objects.requireNonNull(project).getBasePath();

        //todo: doc root should be configurable
        Path docRoot = Paths.get(Objects.requireNonNull(basePath));
        Path projectRoot = Paths.get(Objects.requireNonNull(basePath));
        generateSummaryFile(projectRoot, docRoot);
        VirtualFile projectRootVF = LocalFileSystem.getInstance().findFileByIoFile(projectRoot.toFile());

    }

    private void generateSummaryFile(Path projectRoot, Path docRoot) {
        try {
            List<Path> mdFiles = Files.walk(projectRoot)
                    .map(docRoot::relativize)
                    .filter(this::isMdFile)
                    .collect(Collectors.toList());

            TreeSet<SummaryEntry> mdSummaries = mdFiles.stream()
                    .map(this::toSummaryLink)
                    .collect(Collectors.toCollection(TreeSet::new));

            List<SummaryEntry> sectionSummaries =
                    mdFiles.stream()
                            .map(Path::getParent)
                            .filter(Objects::nonNull)
                            .map(this::toSummary)
                            .collect(Collectors.toList());

            mdSummaries.addAll(sectionSummaries);
            String combined = mdSummaries.stream().map(SummaryEntry::getEntry).collect(Collectors.joining("\n"));
            String content = addHeader(combined);

            Files.write(Paths.get(projectRoot + File.separator + "SUMMARY.md"), content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String addHeader(String content) {
        return "# Summary\n" + content;
    }

    private boolean isMdFile(Path path) {
        return path.getFileName().toString().endsWith(".md");
    }

    private SummaryEntry toSummary(Path path) {
        int valueOfNesting = path.getNameCount();
        return new SummaryEntry(path.getFileName().toString(), path.toString(), true, valueOfNesting);
    }

    private SummaryEntry toSummaryLink(Path path) {
        int valueOfNesting = path.getNameCount();
        return new SummaryEntry(path.getFileName().toString(), path.toString(), false, valueOfNesting);
    }

    //todo: not tested
    public void enableAutoGeneration(Path projectRoot, Path docRoot) {
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
                        generateSummaryFile(projectRoot, docRoot);
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