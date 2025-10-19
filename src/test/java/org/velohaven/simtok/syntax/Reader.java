package org.velohaven.simtok.syntax;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Reader implements CharSequence {

    private final String content;

    public Reader(Path filePath, List<Charset> charsets) throws IOException {
            byte[] fileBytes = Files.readAllBytes(filePath);
            this.content = decodeContent(fileBytes, charsets);
    }

    private String decodeContent(byte[] fileBytes, List<Charset> charsets) {
        for (Charset charset : charsets) {
            try {
                return new String(fileBytes, charset);
            } catch (Exception ignored) {
                // Try next encoding
            }
        }
        throw new IllegalStateException("No applicable encoding found.");
    }

    @Override
    public int length() {
        return content.length();
    }

    @Override
    public char charAt(int index) {
        return content.charAt(index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return content.subSequence(start, end);
    }

    @Override
    public String toString() {
        return content;
    }

    public static void main(String[] args) throws IOException {
        List<Charset> encodings = List.of(StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, StandardCharsets.US_ASCII);
        Reader reader = new Reader(Path.of("example.txt"), encodings);
        System.out.println(reader);
    }

}
