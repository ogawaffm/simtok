package org.velohaven.simtok.syntax;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Utils {

    public static String getAbsoluteResourcesPath(String relativePath) {
        return getAbsolutePath(relativePath, false);
    }

    public static String getAbsoluteTestResourcesPath(String relativePath) {
        return getAbsolutePath(relativePath, true);
    }

    private static String getAbsolutePath(String relativePath, boolean isTestResource) {
        String basePath = Utils.class.getClassLoader().getResource("").getPath();
        if (isTestResource && basePath.contains("test-classes")) {
            basePath = basePath.replace("test-classes", "classes");
        }
        if (basePath.startsWith("/") && basePath.contains(":")) {
            // Remove the leading '/' of windows path e.g. from "/C:/path/to/resource"
            basePath = basePath.substring(1);
        }
        return basePath + relativePath;
    }

    public static String[] readLines(String fileName) throws IOException {
        String content = readString(fileName);
        return content.split("\\R");
    }

    public static String readString(String fileName) throws IOException {
        return Files.readString(Paths.get(fileName), StandardCharsets.UTF_8);
    }
}
