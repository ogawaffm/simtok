package org.velohaven.simtok.tokenizer.matcher;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Singular;

import java.util.Set;

@Builder(access = AccessLevel.PUBLIC)
@Getter
public class ParserDefinition<T extends Comparable<T>> {

    @NonNull
    @Singular
    private Set<InWordBoundaries> wordMatchers;

    @NonNull
    @Singular
    private Set<PrefixedWordMatcher<T>> variableMatchers;

    @NonNull
    @Singular
    private Set<SequenceMatcher<T>> sequenceMatchers;

    @NonNull
    @Singular
    private Set<TagPairMatcher<T>> tagPairMatchers;

    @NonNull
    private WhiteSpacesMatcher<T> whiteSpacesMatcher;

    @NonNull
    private NumberMatcher<T> numberMatcher;

}
