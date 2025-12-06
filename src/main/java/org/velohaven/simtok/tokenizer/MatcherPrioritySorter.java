package org.velohaven.simtok.tokenizer;

import org.velohaven.simtok.tokenizer.matcher.CaseAwareMatcher;
import org.velohaven.simtok.tokenizer.matcher.Matcher;
import org.velohaven.simtok.tokenizer.matcher.SequenceMatcher;
import org.velohaven.simtok.tokenizer.matcher.TagPairMatcher;

import java.util.List;
import java.util.stream.Collectors;

public class MatcherPrioritySorter<T extends Comparable<T>> {

    public List<Matcher<T>> sort(List<Matcher<T>> matchers) {
        return matchers.stream()
            .sorted((m1, m2) -> {

                int result = m1.compareTo(m2);
                if (result != 0) {
                    return result;
                }
                if (m1 instanceof TagPairMatcher && m2 instanceof TagPairMatcher) {
                    return ((TagPairMatcher<T>) m1).compareTo(((TagPairMatcher<T>) m2));
                }
                if (m1 instanceof SequenceMatcher<?> && m2 instanceof SequenceMatcher<?>) {
                    return ((SequenceMatcher<?>) m1).getSequence().compareTo(((SequenceMatcher<?>)m2).getSequence());
                }
                int typePriority = m1.getType().compareTo(m2.getType());

                if (typePriority != 0) {
                    return typePriority;
                }

                if (m1 instanceof CaseAwareMatcher<?> && m2 instanceof CaseAwareMatcher<?>) {
                    return Boolean.compare(
                            ((CaseAwareMatcher<?>) m2).isCaseSensitive(),
                            ((CaseAwareMatcher<?>) m1).isCaseSensitive()
                    );
                }

                return 0;

            })
            .collect(Collectors.toList());
    }
}