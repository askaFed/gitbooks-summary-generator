package com.aska.fed.utils;

import java.nio.file.Path;

public class FileUtils {

    public static boolean isMdFile(Path path) {
        return path.getFileName().toString().endsWith(".md");
    }

}
