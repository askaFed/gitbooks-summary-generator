import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SummaryEntry implements Comparable {
    private String tittle;
    private String path;
    private boolean isSection;
    private int levelOfNesting;

    public String getPath() {
        return path;
    }

    public String getEntry() {
        String row = (isSection) ? tittle : String.format("[%s](%s)", tittle, path);
        return generateIdentations(levelOfNesting) + "* " + row;
    }

    private String generateIdentations(int levelOfNesting) {
        return Stream.generate(() -> "\t").limit(levelOfNesting - 1).collect(Collectors.joining());
    }

    public SummaryEntry(String tittle, String path, boolean isSection, int levelOfNesting) {
        this.tittle = tittle;
        this.path = path;
        this.isSection = isSection;
        this.levelOfNesting = levelOfNesting;
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.path.compareTo(((SummaryEntry) o).getPath());
    }
}
