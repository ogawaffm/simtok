package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

public class WhiteSpacesMatcher<T extends Comparable<T>> extends AbstractMatcher<T> {

    private static final char[] whiteSpace = {' ', '\t', '\n', '\u000B', '\f', '\r'};

    public WhiteSpacesMatcher(@NonNull final T type) {
        super(type);
    }

    @Override
    public int findIfAheadIn(@NonNull final CharSequence text) {

        int textIdx = 0;
        int whiteSpacesIdx;
        boolean match = true;

        while (match && textIdx < text.length()) {

            whiteSpacesIdx = 0;

            do {
                match = (text.charAt(textIdx) == whiteSpace[whiteSpacesIdx]);
                whiteSpacesIdx++;

            } while (!match && whiteSpacesIdx < whiteSpace.length);

            if (match) {
                textIdx++;
            }

        }

        return textIdx;
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

    @Override
    public String toString() {
        return "\uD83C\uDD66 ␣";
    }

}