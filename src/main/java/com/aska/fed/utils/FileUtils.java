package com.aska.fed.utils;

import java.nio.file.Path;

public class FileUtils {

    public static boolean isMdFile(Path path) {
        return isMdFile(path.getFileName().toString());
    }

    public static boolean isMdFile(String path) {
        return path.endsWith(".md");
    }

}
