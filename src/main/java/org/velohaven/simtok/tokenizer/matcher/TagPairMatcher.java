package org.velohaven.simtok.tokenizer.matcher;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class TagPairMatcher<T extends Comparable<T>> extends CaseAwareMatcher<T> {

    private final String openingTag;
    private final String closingTag;
    private final String escapeString;

    private final boolean closesAtEndOfFile;

    public TagPairMatcher(@NonNull final String openingTag, @NonNull final String closingTag,
                          @NonNull final String escapeString, boolean closesAtEndOfFile,
                          boolean caseSensitive, @NonNull final T type) {
        super(caseSensitive, type);
        if (caseSensitive) {
            this.openingTag = openingTag;
            this.closingTag = closingTag;
            this.escapeString = escapeString;
        } else {
            this.openingTag = openingTag.toUpperCase();
            this.closingTag = closingTag.toUpperCase();
            this.escapeString = escapeString.toUpperCase();
        }
        this.closesAtEndOfFile = closesAtEndOfFile;
    }

    int searchForClosingTag(@NonNull final CharSequence text) {
        int textIdx = openingTag.length();
        boolean escaped = false;

        while (textIdx <= text.length() - closingTag.length()) {
            if (!escaped && textIdx <= text.length() - escapeString.length() &&
                    text.subSequence(textIdx, textIdx + escapeString.length()).toString().equals(escapeString)) {
                escaped = true;
                textIdx += escapeString.length();
                continue;
            }

            if (escaped) {
                escaped = false;
                textIdx++;
                continue;
            }

            boolean match = true;
            for (int i = 0; i < closingTag.length(); i++) {
                char c = caseSensitive ? text.charAt(textIdx + i) : Character.toUpperCase(text.charAt(textIdx + i));
                if (c != closingTag.charAt(i)) {
                    match = false;
                    break;
                }
            }

            if (match) {
                return textIdx + closingTag.length();
            }

            textIdx++;
        }

        return closesAtEndOfFile ? text.length() : -1;
    }
    @Override
    public int findIfAheadIn(@NonNull final CharSequence text) {
        if (text.length() < openingTag.length()) {
            return 0;
        }

        // Überprüfen, ob der openingTag am Anfang steht
        boolean match = true;
        for (int i = 0; i < openingTag.length(); i++) {
            char c = caseSensitive ? text.charAt(i) : Character.toUpperCase(text.charAt(i));
            if (c != openingTag.charAt(i)) {
                match = false;
                break;
            }
        }

        if (!match) {
            return 0;
        }

        // Nach dem closingTag suchen
        int length = searchForClosingTag(text);
        if (length == -1 && closesAtEndOfFile) {
            return text.length();
        }

        return length;
    }
    @Override
    public int minLength() {
        return openingTag.length() + closingTag.length();
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "\uD83C\uDD63 '"
                + getOpeningTag() + "'…'" + getClosingTag()
                + "' (" + minLength() + "…" + maxLength() + ", " + (isCaseSensitive() ? "a≠A" : "a=A") + ")";
    }
}