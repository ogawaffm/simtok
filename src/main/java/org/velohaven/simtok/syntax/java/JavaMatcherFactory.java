package org.velohaven.simtok.syntax.java;

import org.velohaven.simtok.tokenizer.matcher.EnclosedContentMatch;
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

public class JavaMatcherFactory {

    // Java operators but without < and > for generics
    private final static String[] SYMBOL_OPERATORS = new String[]{
        "+", "-", "*", "/", "%",
            "++", "--",
            "=", "==", "<=", ">=",
            "+=", "-=", "*=", "/=", "%=",
            "&=", "|=", "^=", "<<=", ">>=", ">>>=",
            "||", "&&", "!", "!="
            ,"|", "&", "^", "~", "<<", ">>", "--", "<<<", ">>>"
    };

    public static List<Matcher<JavaMatchType>> create(String[] reservedWords, int maxIdentifierLength) {

        // Add known word matchers for reserved words
        List<Matcher<JavaMatchType>> matchers = new ArrayList<>(
            KnownWordMatcher.createKnownWordMatchers(reservedWords, false, JavaMatchType.RESERVED_WORD)
        );

        // Add tag pair matchers for comments and literals
        matchers.add(new TagPairMatcher<>("/*", "*/", "", false, false, JavaMatchType.BLOCK_COMMENT));
        matchers.add(new TagPairMatcher<>("//", "\n", "", true, false, JavaMatchType.LINE_COMMENT));

        // TODO support quote escaping e.g. for 'fred''s house' or 'fred\'s house'
        matchers.add(new TagPairMatcher<>("'", "'", "\\", false, false, JavaMatchType.CHAR_LITERAL));
        matchers.add(new TagPairMatcher<>("\"", "\"", "\"", false, false, JavaMatchType.STRING_LITERAL));

        // Add sequence matchers for various symbols
        matchers.add(new SequenceMatcher<>("<", false, JavaMatchType.LESS_THAN_SYMBOL));
        matchers.add(new SequenceMatcher<>(">", false, JavaMatchType.GREATER_THAN_SYMBOL));
        matchers.add(new SequenceMatcher<>(";", false, JavaMatchType.SEMICOLON_SYMBOL));
        matchers.add(new SequenceMatcher<>(".", false, JavaMatchType.DOT_SYMBOL));
        matchers.add(new SequenceMatcher<>(",", false, JavaMatchType.COMMA_SYMBOL));
        matchers.add(new SequenceMatcher<>(":", false, JavaMatchType.COLON_SYMBOL));
        matchers.add(new SequenceMatcher<>("?", false, JavaMatchType.QUESTION_MARK_SYMBOL));

        matchers.add(new SequenceMatcher<>("::", false, JavaMatchType.METHOD_REFERENCE_OPERATOR));

        matchers.add(new SequenceMatcher<>("(", false, JavaMatchType.ROUND_BRACKET));
        matchers.add(new SequenceMatcher<>(")", false, JavaMatchType.ROUND_BRACKET));

        // Add bracket matchers for array access and object access
        matchers.add(new SequenceMatcher<>("[", false, JavaMatchType.SQUARE_BRACKET));
        matchers.add(new SequenceMatcher<>("]", false, JavaMatchType.SQUARE_BRACKET));

        // Add bracket matchers for jdbc escape syntax
        matchers.add(new SequenceMatcher<>("{", false, JavaMatchType.CURLY_BRACKET));
        matchers.add(new SequenceMatcher<>("}", false, JavaMatchType.CURLY_BRACKET));

        matchers.addAll(SequenceMatcher.createSequenceMatchers(false, JavaMatchType.OPERATOR, SYMBOL_OPERATORS));

        // Add identifier matcher
        JavaIdentifierMatcher<JavaMatchType> identifierMatcher = new JavaIdentifierMatcher<>(JavaMatchType.IDENTIFIER);
        matchers.add(identifierMatcher);

        matchers.add(new PrefixedWordMatcher<>("@", identifierMatcher, false, JavaMatchType.ANNOTATION_IDENTIFIER));

        // Add whitespace matcher
        matchers.add(new WhiteSpacesMatcher<>(JavaMatchType.WHITESPACE));

        // Add number matcher
        matchers.add(new NumberMatcher<>('.', JavaMatchType.NUMBER));
        matchers.add(new HexNumberMatcher<>(16, JavaMatchType.INTEGER_HEX_LITERAL));
        matchers.add(new HexNumberMatcher<>(32, JavaMatchType.LONG_HEX_LITERAL));
        HexNumberMatcher<JavaMatchType> hexIntegerMatcher = new HexNumberMatcher<>(16, JavaMatchType.INTEGER_HEX_LITERAL);
        HexNumberMatcher<JavaMatchType> hexLongMatcher = new HexNumberMatcher<>(32, JavaMatchType.LONG_HEX_LITERAL);

        matchers.add((new PrefixedWordMatcher<>("0x", hexIntegerMatcher, false, JavaMatchType.INTEGER_HEX_LITERAL)));

        matchers.add(new EnclosedContentMatch<>("0x", "L", hexLongMatcher, false, JavaMatchType.LONG_HEX_LITERAL));
        matchers.add(new EnclosedContentMatch<>("", "L", new SignlessIntegerMatcher<>(false, JavaMatchType.NUMBER), false, JavaMatchType.LONG_LITERAL));

        matchers.add(new EnclosedContentMatch<>("", "D", new NumberMatcher<>('.', JavaMatchType.NUMBER), false, JavaMatchType.DOUBLE_LITERAL));

        matchers.add(new PrefixedWordMatcher<>("@", identifierMatcher, false, JavaMatchType.ANNOTATION_IDENTIFIER));

        return matchers;
    }
}