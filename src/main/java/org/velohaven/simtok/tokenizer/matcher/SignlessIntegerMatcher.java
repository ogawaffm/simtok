package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

public class SignlessIntegerMatcher<T extends Comparable<T>> extends AbstractMatcher<T> {

    private final boolean allowLeadingZero;

    public SignlessIntegerMatcher(boolean allowLeadingZero, @NonNull final T type) {
        super(type);
        this.allowLeadingZero = allowLeadingZero;
    }

    @Override
    public int findIfAheadIn(@NonNull final CharSequence text) {

        if (text.isEmpty()) {
            return 0;
        }

        if (text.charAt(0) == '0' && !allowLeadingZero) {
            return 0;
        }

        int numLength = 0;
        int textLength = text.length();
        char c;

        while (numLength < textLength) {

            c = text.charAt(numLength);

            if (Character.isDigit(c)) {
                numLength++;
            } else {
                break;
            }
        }

        return numLength;
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
        return "\uD83C\uDD5D 0-9";
    }

}
