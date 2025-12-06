package org.velohaven.simtok.syntax;

import org.velohaven.simtok.tokenizer.matcher.Matcher;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AllFilesTest<T extends Comparable<T>> extends AbstractTest<T> {

    private final String fileExtension;
    private final Set<String> unrecognizedMatches = new HashSet<>();

    public AllFilesTest(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public void testAllFiles(String directory, List<Matcher<T>> matchers, T noMatchType, int limit) throws IOException {
        final int[] fileCount = {0};
        Date startTime = new Date();

        Files.walkFileTree(Paths.get(directory), new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (fileCount[0] >= limit) {
                    return FileVisitResult.TERMINATE;
                }

                if (file.toString().endsWith(fileExtension)) {
                    System.out.println("Testing file: " + file);
                    try {
                        unrecognizedMatches.addAll(test(matchers, noMatchType, file.toString(), false));
                        fileCount[0]++;
                    } catch (Exception e) {
                        System.err.println("Error reading file: " + file + "\n" + e);
                    }
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.err.println("Failed to visit file: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return FileVisitResult.CONTINUE;
            }
        });

        if (!unrecognizedMatches.isEmpty()) {
            System.out.println("Unrecognized matches found:");
            for (String token : unrecognizedMatches) {
                System.out.println(token);
            }
        } else {
            System.out.println("All files passed the test.");
        }
        System.out.println(fileCount[0] + " files processed in " + (new Date().getTime() - startTime.getTime()) + " ms.");
    }
}