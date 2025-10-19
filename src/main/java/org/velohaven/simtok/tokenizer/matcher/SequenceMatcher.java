package org.velohaven.simtok.tokenizer.matcher;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class SequenceMatcher<T extends Comparable<T>> extends CaseAwareMatcher<T> {

    /**
     * The keywords to be found in the text, ordered by length in descending and then by natural order. This is to
     * ensure that the longest keyword is found first. In case of case-sensitive parsing, the keywords are stored in
     * upper case to avoid the need for case conversion during parsing.
     */
    final String sequence;

    public SequenceMatcher(@NonNull final String sequence, boolean caseSensitive, @NonNull final T type) {
        super(caseSensitive, type);
        this.sequence = caseSensitive ? sequence : sequence.toUpperCase();
    }

    @Override
    public int findIfAheadIn(final @NonNull CharSequence text) {

        int i;
        char c;
        boolean match;

        // Any chance of a match?
        if (sequence.length() <= text.length()) {

            i = 0;
            match = true;

            // through the whole keyword while it matches
            while (i < sequence.length() && match) {

                if (caseSensitive) {
                    // use the original case against the keyword, which also is stored in original case
                    c = text.charAt(i);
                } else {
                    // use the upper-case as the keywords are stored in upper case for case-insensitive parsing
                    c = Character.toUpperCase(text.charAt(i));
                }

                match = c == sequence.charAt(i);

                i++;

            }
            if (match) {
                return sequence.length();
            }
        }
        return 0;

    }

    @Override
    public int minLength() {
        return sequence.length();
    }

    @Override
    public int maxLength() {
        return sequence.length();
    }

    public static <T extends Comparable<T>> List<SequenceMatcher<T>> createSequenceMatchers(
        @NonNull final String[] sequences,
        boolean caseSensitive, @NonNull final T type) {
        List<SequenceMatcher<T>> matchers = new ArrayList<>(sequences.length);
        for (String word : sequences) {
            matchers.add(new SequenceMatcher<>(word, caseSensitive, type));
        }
        return matchers;
    }

    @Override
    public String toString() {
        return "\uD83C\uDD62 '" + getSequence() + "' (" + (isCaseSensitive() ? "a≠A" : "a=A") + ")";
    }

    public static <T extends Comparable<T>> List<SequenceMatcher<T>> createSequenceMatchers(boolean caseSensitive,
        @NonNull final T type,
        @NonNull final String... sequences) {
        return createSequenceMatchers(sequences, caseSensitive, type);
    }

    @Override
    public int compareTo(final @NonNull Matcher<T> other) {
        int result = super.compareTo(other);
        if (result != 0) {
            return result;
        }

        if (other instanceof final SequenceMatcher<T> otherSequenceMatcher) {
            result = sequence.compareTo(otherSequenceMatcher.sequence);
            if (result != 0) {
                return result;
            }
        }

        if (other instanceof CaseAwareMatcher<T> otherCaseAwareMatcher) {
            return Boolean.compare(otherCaseAwareMatcher.isCaseSensitive(), isCaseSensitive());
        }

        return 0;
    }

}
