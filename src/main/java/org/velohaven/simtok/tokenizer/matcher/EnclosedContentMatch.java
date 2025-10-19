package org.velohaven.simtok.tokenizer.matcher;

import lombok.NonNull;

public class EnclosedContentMatch<T extends Comparable<T>> extends CaseAwareMatcher<T> implements InWordBoundaries {

    private final SequenceMatcher<T> prefixMacher;
    private final SequenceMatcher<T> suffixMacher;
    private final Matcher<T> nameMatcher;


    public EnclosedContentMatch(@NonNull final String prefix, @NonNull final String suffix,
                                @NonNull Matcher<T> nameMatcher, final boolean caseSensitive, @NonNull final T type) {
        super(caseSensitive, type);
        this.prefixMacher = prefix.isEmpty() ? null : new SequenceMatcher<>(prefix, caseSensitive, type);
        this.suffixMacher = suffix.isEmpty() ? null : new SequenceMatcher<>(suffix, caseSensitive, type);
        this.nameMatcher = nameMatcher;
    }

    @Override public int findIfAheadIn(@NonNull final CharSequence text) {

        int prefixFound = 0;

        if (prefixMacher != null) {
            prefixFound = prefixMacher.findIfAheadIn(text);
            if (prefixFound == 0) {
                return 0;
            }
        }

        int nameFound = nameMatcher.findIfAheadIn(text.subSequence(prefixFound, text.length()));

        if (nameFound == 0) {
            return 0;
        }

        int suffixFound = 0;

        if (suffixMacher != null) {
            suffixFound = suffixMacher.findIfAheadIn(text.subSequence(prefixFound + nameFound, text.length()));
            if (suffixFound == 0) {
                return 0;
            }
        }

        return prefixFound + nameFound + suffixFound;

    }

    @Override public int minLength() {
        return
            (prefixMacher == null ? 0 : prefixMacher.getSequence().length())
                + nameMatcher.minLength()
                + (suffixMacher == null ? 0 : suffixMacher.getSequence().length());
    }

    @Override public int maxLength() {
        int maxNameLength = Integer.MAX_VALUE
            - (prefixMacher == null ? 0 : prefixMacher.getSequence().length())
            - (suffixMacher == null ? 0 : suffixMacher.getSequence().length());

        if (nameMatcher.maxLength() <= maxNameLength) {
            return nameMatcher.maxLength()
                + (prefixMacher == null ? 0 : prefixMacher.getSequence().length())
                + (suffixMacher == null ? 0 : suffixMacher.getSequence().length());
        } else {
            return Integer.MAX_VALUE;
        }
    }

}
