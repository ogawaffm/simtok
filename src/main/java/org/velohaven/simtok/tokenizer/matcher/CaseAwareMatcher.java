package org.velohaven.simtok.tokenizer.matcher;

import lombok.Getter;
import lombok.NonNull;

/**
 * A parser that looks ahead in the text to find a certain pattern.
 * @param <T> the type of the parser's result
 */
@Getter
public abstract class CaseAwareMatcher<T extends Comparable<T>> extends AbstractMatcher<T> {

    final boolean caseSensitive;

    CaseAwareMatcher(boolean caseSensitive, @NonNull final T type) {
        super(type);
        this.caseSensitive = caseSensitive;
    }

}
