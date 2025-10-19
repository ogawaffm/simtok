package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

import java.util.function.Predicate;

/**
 * A matcher that matches a word. The word is defined as a set of characters. The set of characters can
 * differ between the first character and the rest of the characters.
 *
 * @param <T>
 */
public class IdentifierMatcher<T extends Comparable<T>> extends WordMatcher<T> implements InWordBoundaries {

    public static final String A_TO_Z_LOWER = "abcdefghijklmnopqrstuvwxyz";
    public static final String A_TO_Z_UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String DIGITS = "0123456789";

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

    private final Predicate<Character> firstCharPredicate;
    private final int maxLength;

    protected IdentifierMatcher(String chars, int maxLength, T type) {
        this(new ContainsPredicate(chars), new ContainsPredicate(chars), maxLength, type);
    }

    protected IdentifierMatcher(String firstChars, String restOfChars, int maxLength, T type) {
        this(new ContainsPredicate(firstChars), new ContainsPredicate(restOfChars), maxLength, type);
    }

    protected IdentifierMatcher(@NonNull final Predicate<Character> firstCharPredicate,
                                @NonNull final Predicate<Character> restOfCharsPredicate, int maxLength, @NonNull final T type) {

        super(restOfCharsPredicate, maxLength, type);
        this.firstCharPredicate = firstCharPredicate;
        this.maxLength = maxLength;
    }


    @Override
    public int findIfAheadIn(final @NonNull CharSequence text) {

        if (text.isEmpty() || !firstCharPredicate.test(text.charAt(0))) {
            return 0;
        }

        return findIfAheadIn(text, 1);
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
