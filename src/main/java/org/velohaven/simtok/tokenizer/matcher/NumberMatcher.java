package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

public class NumberMatcher<T extends Comparable<T>> extends AbstractMatcher<T> {

    private final int decimalPoint;

    public NumberMatcher(char decimalPoint, @NonNull final T type) {
        super(type);
        this.decimalPoint = decimalPoint;
    }

    @Override
    public int findIfAheadIn(@NonNull final CharSequence text) {
        if (text.isEmpty()) {
            return 0;
        }

        int numLength = 0;
        int textLength = text.length();
        boolean decimalPointFound = false;
        boolean exponentFound = false;
        char c;

        // Check for optional sign at the beginning
        if (text.charAt(0) == '+' || text.charAt(0) == '-') {
            numLength++;
        }

        while (numLength < textLength) {

            c = text.charAt(numLength);

            if (Character.isDigit(c)) {
                numLength++;
            } else if (c == decimalPoint && !decimalPointFound) {
                decimalPointFound = true;
                numLength++;
                // Ensure there is at least one digit after the decimal point
                if (numLength >= textLength || !Character.isDigit(text.charAt(numLength))) {
                    return 0;
                }
            } else if ((c == 'e' || c == 'E') && !exponentFound) {
                exponentFound = true;
                numLength++;
                // Check for optional sign after the exponent
                if (numLength < textLength && (text.charAt(numLength) == '+' || text.charAt(numLength) == '-')) {
                    numLength++;
                }
                // Ensure there is at least one digit after the exponent
                if (numLength >= textLength || !Character.isDigit(text.charAt(numLength))) {
                    return 0;
                }
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
