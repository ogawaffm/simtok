package org.velohaven.simtok.tokenizer.matcher;

import lombok.Getter;
import lombok.NonNull;

/**
 * An abstract base class for matchers that provides common functionality.
 * @param <T> the type of the match
 */
@Getter
public abstract class AbstractMatcher<T extends Comparable<T>> implements Matcher<T> {

    final private T type;

    AbstractMatcher(@NonNull final T type) {
        this.type = type;
    }

    /**
     * Compares this matcher to another matcher for ordering.
     * The comparison is based on the maximum length of the match, then on the minimum length of the match,
     * to avoid overlapping matches (e.g. "any" and "anything"),
     * and finally on the type of the match for a deterministic ordering.
     * @param other the other matcher to compare to
     * @return a negative integer, zero, or a positive integer as this matcher is less than, equal to,
     * or greater than the specified matcher.
     */

    @Override
    public int compareTo(Matcher<T> other) {

        // the longer the match, the higher the priority
        // (we want to match the longest possible sequence, not to overlap e.g. "any" and "anything")
        int result = Integer.compare(other.maxLength(), this.maxLength());
        if (result != 0) {
            return result;
        }

        // if max lengths are equal, prefer the one with the greater minimum length
        result = Integer.compare(other.minLength(), this.minLength());
        if (result != 0) {
            return result;
        }

        // if we reach here, the matchers are considered equal in terms of length,
        // but we still need a consistent ordering, so we compare by type
        return other.getType().compareTo(this.getType());

    }

}
