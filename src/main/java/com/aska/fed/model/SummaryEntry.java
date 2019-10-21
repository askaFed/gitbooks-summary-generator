package com.aska.fed.model;

import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SummaryEntry implements Comparable {

    private String tittle;
    private String path;
    private boolean isMdFile;
    private int levelOfNesting;

    public SummaryEntry(String tittle, String path, boolean isMdFile, int levelOfNesting) {
        this.tittle = tittle;
        this.path = path;
        this.isMdFile = isMdFile;
        this.levelOfNesting = levelOfNesting;
    }

    public String getPath() {
        return path;
    }

    public String getEntry() {
        String row = (isMdFile) ? String.format("[%s](%s)", tittle, path) : tittle;
        return generateIdentations(levelOfNesting) + "* " + row;
    }

    private String generateIdentations(int levelOfNesting) {
        return Stream.generate(() -> "\t").limit(levelOfNesting - 1).collect(Collectors.joining());
    }

    @Override
    public int compareTo(@NotNull Object o) {
        return this.path.compareTo(((SummaryEntry) o).getPath());
    }

}
