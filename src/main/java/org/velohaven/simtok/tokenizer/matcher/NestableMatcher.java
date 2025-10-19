package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

public class NestableMatcher<T extends Comparable<T>> extends CaseAwareMatcher<T> {

    NestableMatcher(final boolean caseSensitive, @NonNull final T type) {
        super(caseSensitive, type);
    }

    @Override
    public int findIfAheadIn(@NonNull CharSequence text) {
        return 0;
    }

    @Override
    public int minLength() {
        return 1;
    }

    @Override
    public int maxLength() {
        return Integer.MAX_VALUE;
    }

}
