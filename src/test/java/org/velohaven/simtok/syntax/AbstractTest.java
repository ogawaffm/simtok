package org.velohaven.simtok.syntax;

import org.velohaven.simtok.tokenizer.Tokenizer;
import org.velohaven.simtok.tokenizer.matcher.Match;
import org.velohaven.simtok.tokenizer.matcher.Matcher;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AbstractTest<T extends Comparable<T>> {

    private int tokenNo = 0;
    private final Set<String> unrecognizedMatches = new HashSet<>();
    boolean withTokenOutput;

    private Tokenizer<T> tokenizer;

    public static String readSource(String sourceFilename) {
        try {
            return Utils.readString(sourceFilename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String[] readReservedWords(String reservedWordsFilename) {
        try {
            return Utils.readLines(reservedWordsFilename);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String indent(char c, int indention) {
        if (indention < 0) {
            return "";
        }
        return repeat(c, indention);
    }

    public void matchConsumer(Match<T> match) {
        if (withTokenOutput) {
            System.out.print(indent('…', match.getColumnNo() - 1));
        }
        tokenNo = tokenNo + 1;
        if (withTokenOutput) {
            System.out.print(match);
        }
        if (match.getType().equals(tokenizer.getNoMatchType())) {
            unrecognizedMatches.add(match.toString());
            if (withTokenOutput) {
                System.out.print("⭠ ⚠ ⚠ ⚠ " + tokenizer.getNoMatchType() + " ⚠ ⚠ ⚠ '" + unrecognizedMatches.size());
            }
        } else {
            if (withTokenOutput) {
                System.out.print("⭠ " + match.getType());
            }
        }
        if (withTokenOutput) {
            System.out.println(" #" + tokenNo);
        }
    }

    public Set<String> test(List<Matcher<T>> matchers, T noMatchType, String sourceFilename, boolean withTokenOutput) {

        tokenNo = 0;
        this.withTokenOutput = withTokenOutput;
        tokenizer = new Tokenizer<>(matchers, noMatchType);

        LocalDateTime start = LocalDateTime.now();
        tokenizer.tokenize(readSource(sourceFilename), this::matchConsumer);

        if (! unrecognizedMatches.isEmpty()) {
            System.out.println(repeat('-', 80));
            System.out.println("Unrecognized matches:");
            for (String token : unrecognizedMatches) {
                System.out.println(token);
            }
        } else {
            System.out.println("No unrecognized matches");
        }

        LocalDateTime end = LocalDateTime.now();
        System.out.println("Elapsed time: " + Duration.between(start, end).toMillis() + " ms");
        return unrecognizedMatches;

    }

    private static String repeat(char c, int count) {
        StringBuilder sb = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

}
