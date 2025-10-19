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
                if (m1 instanceof TagPairMatcher<T> tpm1 && m2 instanceof TagPairMatcher<T> tpm2) {
                    return tpm1.compareTo(tpm2);
                }
                if (m1 instanceof SequenceMatcher<?> sm1 && m2 instanceof SequenceMatcher<?> sm2) {
                    return sm1.getSequence().compareTo(sm2.getSequence());
                }
                int typePriority = m1.getType().compareTo(m2.getType());

                if (typePriority != 0) {
                    return typePriority;
                }

                if (m1 instanceof CaseAwareMatcher<?> m1Case && m2 instanceof CaseAwareMatcher<?> m2Case) {
                    return Boolean.compare(m2Case.isCaseSensitive(), m1Case.isCaseSensitive());
                }

                return 0;

            })
            .collect(Collectors.toList());
    }
}