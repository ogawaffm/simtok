package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

public interface Matcher<T extends Comparable<T>> extends Comparable<Matcher<T>> {
    /**
     * Checks if the parser finds what he is looking for exactly at position 0 in the text ahead.
     * If no match is found, the parser should return 0.
     * If a match is found, the parser should return the length of the match.
     * In case of a search for a Tag-Pair, if only the opening tag is found, the parser should return -1.
     * -1 can then be interpreted that the parser is still looking for the closing tag (e.g. for a line comment
     * which has no new-line but an end-of-file as closing tag).
     *
     * @param text the text to search in
     * @return 0 if no match is found, the length of the match if a match is found or -1 if only the opening tag is found.
     */
    int findIfAheadIn(@NonNull CharSequence text);

    /**
     * Minimum length of a match for this matcher.
     * @return the minimum length of a match for this matcher (>=0).
     */
    int minLength();

    /**
     * Maximum length of a match for this matcher.
     * @return the maximum length of a match for this matcher (>=minLength()).
     */
    int maxLength();

    /**
     * The type of the match.
     * @return the type of the match.
     */
    T getType();

    /**
     * Compares this matcher with another matcher to determine their relative order of application.
     * The main criteria for comparison is the maximum length of the match.
     * @param other the object to be compared.
     * @return -1 if this matcher should be applied before the other matcher, 1 if it should be applied after,
     * 0 if they are equal.
     */
    @Override
    int compareTo(Matcher<T> other);
}
