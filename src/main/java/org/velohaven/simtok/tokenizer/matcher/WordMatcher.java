package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

import java.util.function.Predicate;

/**
 * A matcher that matches a word. The word is defined as a set of characters. The set of characters can
 * differ between the first character and the rest of the characters.
 *
 * @param <T>
 */
public class WordMatcher<T extends Comparable<T>> extends AbstractMatcher<T> implements InWordBoundaries {

    public static class ContainsPredicate implements Predicate<Character> {
        private final String chars;

        public ContainsPredicate(@NonNull final String chars) {
            this.chars = chars;
        }

        @Override
        public boolean test(@NonNull final Character c) {
            return chars.indexOf(c) >= 0;
        }
    }

    private final Predicate<Character> predicate;
    private final int maxLength;

    protected WordMatcher(String chars, int maxLength, T type) {
        this(new ContainsPredicate(chars), maxLength, type);
    }

    protected WordMatcher(@NonNull final Predicate<Character> predicate, int maxLength, @NonNull final T type) {
        super(type);
        this.predicate = predicate;
        this.maxLength = maxLength;
    }


    @Override
    public int findIfAheadIn(final @NonNull CharSequence text) {
        return findIfAheadIn(text, 0);
    }

    int findIfAheadIn(final @NonNull CharSequence text, int length) {

        while (length < text.length() && length < maxLength && predicate.test(text.charAt(length))) {
            length++;
        }

        return length;
    }

    @Override public int minLength() {
        return 1;
    }

    @Override public int maxLength() {
        return maxLength;
    }

    @Override
    public String toString() {
        return "\uD83C\uDD58 (" + minLength() + "…" + maxLength() + ")";
    }

}
