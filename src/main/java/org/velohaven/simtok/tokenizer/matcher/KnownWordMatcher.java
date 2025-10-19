package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A matcher that recognizes a word in a text. A word is a sequence of characters that is not preceded or followed by
 * a character that is part of a word.
 * @param <T>
 */
public class KnownWordMatcher<T extends Comparable<T>> extends SequenceMatcher<T> implements InWordBoundaries {

    public KnownWordMatcher(@NonNull final String knownWord, boolean caseSensitive, @NonNull final T type) {
        super(knownWord, caseSensitive, type);
    }

    public static <T extends Comparable<T>> List<KnownWordMatcher<T>> createKnownWordMatchers(
        @NonNull final String[] words,
        boolean caseSensitive,
        @NonNull final T type) {
        List<KnownWordMatcher<T>> matchers = new ArrayList<>(words.length);
        for (String word : words) {
            matchers.add(new KnownWordMatcher<>(word, caseSensitive, type));
        }
        return matchers;
    }

    @Override
    public String toString() {
        return "\uD83C\uDD5A '" + getSequence() + "' (" + (isCaseSensitive() ? "a≠A" : "a=A") + ")";
    }

}
