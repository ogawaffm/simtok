package org.velohaven.simtok.syntax.sql;

import org.velohaven.simtok.tokenizer.matcher.KnownWordMatcher;
import org.velohaven.simtok.tokenizer.matcher.Matcher;
import org.velohaven.simtok.tokenizer.matcher.NumberMatcher;
import org.velohaven.simtok.tokenizer.matcher.SequenceMatcher;
import org.velohaven.simtok.tokenizer.matcher.SignlessIntegerMatcher;
import org.velohaven.simtok.tokenizer.matcher.TagPairMatcher;
import org.velohaven.simtok.tokenizer.matcher.PrefixedWordMatcher;
import org.velohaven.simtok.tokenizer.matcher.WhiteSpacesMatcher;

import java.util.ArrayList;
import java.util.List;

public class SqlMatcherFactory {

    private final static String[] SYMBOL_OPERATORS = new String[]{
        "+", "-", "*", "/", "%", "=", "<", "<=", ">", ">=", "<>", "!=", "||",
    };

    public static List<Matcher<SqlMatchType>> create(String[] reservedWords, int maxIdentifierLength) {

        // Add known word matchers for reserved words
        List<Matcher<SqlMatchType>> matchers = new ArrayList<>(
            KnownWordMatcher.createKnownWordMatchers(reservedWords, false, SqlMatchType.RESERVED_WORD)
        );

        // Add tag pair matchers for comments and literals
        matchers.add(new TagPairMatcher<>("/*", "*/", "", false, false, SqlMatchType.BLOCK_COMMENT));
        matchers.add(new TagPairMatcher<>("--", "\n", "", true, false, SqlMatchType.LINE_COMMENT));

        // TODO quote escaping which can like 'fred''s house' or 'fred\'s house'
        matchers.add(new TagPairMatcher<>("'", "'", "'", false, false, SqlMatchType.QUOTED_LITERAL));
        matchers.add(new TagPairMatcher<>("\"", "\"", "\"", false, false, SqlMatchType.QUOTED_IDENTIFIER));

        // Add sequence matchers for various symbols
        matchers.add(new SequenceMatcher<>(";", false, SqlMatchType.STATEMENT_TERMINATOR));
        matchers.add(new SequenceMatcher<>(".", false, SqlMatchType.DOT));
        matchers.add(new SequenceMatcher<>(",", false, SqlMatchType.SEPARATOR));

        matchers.add(new SequenceMatcher<>("(", false, SqlMatchType.ROUND_BRACKET));
        matchers.add(new SequenceMatcher<>(")", false, SqlMatchType.ROUND_BRACKET));

        // Add bracket matchers for array access and object access
        matchers.add(new SequenceMatcher<>("[", false, SqlMatchType.SQUARE_BRACKET));
        matchers.add(new SequenceMatcher<>("]", false, SqlMatchType.SQUARE_BRACKET));

        // Add bracket matchers for jdbc escape syntax
        matchers.add(new SequenceMatcher<>("{", false, SqlMatchType.CURLY_BRACKET));
        matchers.add(new SequenceMatcher<>("}", false, SqlMatchType.CURLY_BRACKET));

        matchers.addAll(SequenceMatcher.createSequenceMatchers(false, SqlMatchType.OPERATOR, SYMBOL_OPERATORS));

        // Add identifier matcher
        SqlIdentifierMatcher<SqlMatchType> identifierMatcher = new SqlIdentifierMatcher<>(maxIdentifierLength,
            SqlMatchType.IDENTIFIER);
        matchers.add(identifierMatcher);

        // Add whitespace matcher
        matchers.add(new WhiteSpacesMatcher<>(SqlMatchType.WHITESPACE));

        // Add number matcher
        matchers.add(new NumberMatcher<>('.', SqlMatchType.NUMBER));

        // Add variable matchers
        matchers.add(new PrefixedWordMatcher<>("", new SequenceMatcher<>("?", false, SqlMatchType.BIND_VARIABLE), false,
            SqlMatchType.BIND_VARIABLE));
        matchers.add(new PrefixedWordMatcher<>("?", new SignlessIntegerMatcher<>(false, SqlMatchType.NUMBER), false,
            SqlMatchType.BIND_VARIABLE));
        matchers.add(new PrefixedWordMatcher<>(":", identifierMatcher, false, SqlMatchType.BIND_VARIABLE));
        matchers.add(new PrefixedWordMatcher<>("@", identifierMatcher, false, SqlMatchType.BIND_VARIABLE));
        matchers.add(new PrefixedWordMatcher<>("&", identifierMatcher, false, SqlMatchType.SUBSTITUTION_VARIABLE));
        matchers.add(new PrefixedWordMatcher<>("&&", identifierMatcher, false, SqlMatchType.SUBSTITUTION_VARIABLE));

        return matchers;
    }
}