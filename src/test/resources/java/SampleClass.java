package org.velohaven.simtok.tokenizer;

import lombok.NonNull;
import org.velohaven.simtok.tokenizer.matcher.InWordBoundaries;
import org.velohaven.simtok.tokenizer.matcher.Match;
import org.velohaven.simtok.tokenizer.matcher.CaseAwareMatcher;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class Tokenizer<T extends Comparable<T>> {

    private final List<CaseAwareMatcher<T>> matchers;
    private final T noMatchType;

    public Tokenizer(List<CaseAwareMatcher<T>> matchers, T noMatchType) {
        this.matchers = new MatcherPrioritySorter<T>().sort(matchers);
        this.noMatchType = noMatchType;
    }

    void handlePendingUnrecognizedInput(int inputIdx, CharSequence input, int length,
        @NonNull final Consumer<Match<T>> matchConsumer) {
        if (length > 0) {
            matchConsumer.accept(new Match<>(input, inputIdx, inputIdx + length, this.noMatchType));
        }
    }

    public Stream<Match<T>> stream(@NonNull final CharSequence input) {
        Stream.Builder<Match<T>> builder = Stream.builder();
        this.tokenize(input, builder::add);
        return builder.build();
    }

    @SafeVarargs
    public final CharSequence hide(CharSequence input, @NonNull final T... typesToHide) {
        StringBuilder sb = new StringBuilder();
        Set<T> typesToHideSet = new HashSet<>(Arrays.asList(typesToHide));
        Consumer<Match<T>> hiddingMatchConsumer = match -> {
            if (typesToHideSet.contains(match.getType())) {
                for (int i = 0; i < match.length(); i++) {
                    char c = match.charAt(i);
                    if (Character.isWhitespace(c)) {
                        sb.append(c);
                    } else {
                        sb.append(' ');
                    }
                }
            } else {
                sb.append(match);
            }
        };
        this.tokenize(input, hiddingMatchConsumer);
        return sb;
    }

    public void tokenize(@NonNull final CharSequence source, @NonNull final Consumer<Match<T>> matchConsumer) {

        int inputIdx = 0;
        int noMatchStart = 0;
        int noMatchLength = 0;

        Text input = new Text(source);

        while (inputIdx < input.length()) {
            int foundMatchSize = 0;
            Match<T> longestMatch = null;

            Text subInput = input.subSequence(inputIdx, input.length());

            for (CaseAwareMatcher<T> matcher : matchers) {
                if (foundMatchSize > matcher.maxLength()) {
                    break;
                }

                if (!(matcher instanceof InWordBoundaries) || inputIdx == 0 || !isWordChar(
                    input.charAt(inputIdx - 1))) {
                    //CharSequence subInput = input.subSequence(inputIdx, input.length());
                    foundMatchSize = matcher.findIfAheadIn(subInput);

                    if (foundMatchSize > 0 &&
                        (!(matcher instanceof InWordBoundaries)
                            || inputIdx + foundMatchSize == input.length()
                            || !isWordChar(input.charAt(inputIdx + foundMatchSize))) &&
                        (longestMatch == null || foundMatchSize > longestMatch.length() || (foundMatchSize
                            == longestMatch.length() && matcher.getType().compareTo(longestMatch.getType()) < 0))) {
                        longestMatch = new Match<>(input, inputIdx, inputIdx + foundMatchSize, matcher.getType());
                    }
                }
            }

            if (longestMatch == null) {
                if (noMatchStart == 0) {
                    noMatchStart = inputIdx;
                    noMatchLength = 0;
                }
                noMatchLength++;
                inputIdx++;
            } else {
                handlePendingUnrecognizedInput(noMatchStart, input, noMatchLength, matchConsumer);
                noMatchLength = 0;
                noMatchStart = 0;
                matchConsumer.accept(longestMatch);
                inputIdx += longestMatch.length();
            }
        }

        handlePendingUnrecognizedInput(noMatchStart, input, noMatchLength, matchConsumer);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    static boolean isWordChar(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }
}