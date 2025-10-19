package org.velohaven.simtok.tokenizer.matcher;

import lombok.Getter;
import lombok.NonNull;

@Getter
public class TagPairMatcher_Old<T extends Comparable<T>> extends CaseAwareMatcher<T> {

    private final String openingTag;
    private final String closingTag;

    /**
     * If true and if the end of the file is reached before the closing tag is found, this is considered a match.
     * This is useful for tags that are not closed, like line comments.
     */
    private final boolean closesAtEndOfFile;

    public TagPairMatcher_Old(@NonNull final String openingTag, @NonNull final String closingTag, boolean closesAtEndOfFile,
                              boolean caseSensitive,
                              @NonNull final T type) {
        super(caseSensitive, type);
        if (caseSensitive) {
            this.openingTag = openingTag;
            this.closingTag = closingTag;
        } else {
            this.openingTag = openingTag.toUpperCase();
            this.closingTag = closingTag.toUpperCase();
        }
        this.closesAtEndOfFile = closesAtEndOfFile;
    }

    boolean isOpeningTagAhead(@NonNull final CharSequence text) {

        if (text.length() < openingTag.length()) {
            return false;
        }

        boolean match = true;
        char c;
        int i = 0;

        while (match && i < openingTag.length()) {

            if (caseSensitive) {
                c = text.charAt(i);
            } else {
                c = Character.toUpperCase(text.charAt(i));
            }

            match = c == openingTag.charAt(i);
            i++;
        }
        return match;

    }

    int searchForClosingTag(@NonNull final CharSequence text) {

        // start immediately after the opening tag
        int textIdx = openingTag.length();

        while (textIdx <= text.length() - closingTag.length()) {

            char c;
            boolean match = true;
            int closingTagIdx = 0;

            while (closingTagIdx < closingTag.length() && match) {
                if (caseSensitive) {
                    c = text.charAt(textIdx + closingTagIdx);
                } else {
                    c = Character.toUpperCase(text.charAt(textIdx + closingTagIdx));
                }
                match = c == closingTag.charAt(closingTagIdx);
                closingTagIdx++;
            }

            if (match) {
                return textIdx + closingTag.length();
            }
            textIdx++;
        }
        return -1;
    }

    @Override
    public int findIfAheadIn(@NonNull final CharSequence text) {

        int length = 0;
        if (isOpeningTagAhead(text)) {
            length = searchForClosingTag(text);
            if (length == -1 && closesAtEndOfFile) {
                length = text.length();
            }
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

    @Override
    public int compareTo(final @NonNull Matcher<T> other) {
        int result = super.compareTo(other);
        if (result != 0) {
            return result;
        }

        if (other instanceof TagPairMatcher_Old) {
            TagPairMatcher_Old<T> otherTagPairMatcher = (TagPairMatcher_Old<T>) other;

            // longest opening tag first
            result = Integer.compare(otherTagPairMatcher.getOpeningTag().length(), this.getOpeningTag().length());
            if (result != 0) {
                return result;
            }

            // longest closing tag first
            result = Integer.compare(otherTagPairMatcher.getClosingTag().length(), this.getClosingTag().length());
            if (result != 0) {
                return result;
            }

            // natural order of opening tags
            result = this.getOpeningTag().compareTo(otherTagPairMatcher.getOpeningTag());
            if (result != 0) {
                return result;
            }

            // natural order of closing tags
            result = this.getClosingTag().compareTo(otherTagPairMatcher.getClosingTag());
            if (result != 0) {
                return result;
            }
        }

        if (other instanceof CaseAwareMatcher<T> otherCaseAwareMatcher) {
            return Boolean.compare(otherCaseAwareMatcher.isCaseSensitive(), this.isCaseSensitive());
        }

        return 0;
    }
}
