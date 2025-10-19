package org.velohaven.simtok.syntax.sql;

import org.velohaven.simtok.tokenizer.matcher.IdentifierMatcher;

public class SqlIdentifierMatcher<T extends Comparable<T>> extends IdentifierMatcher<T> {

    public static final String FIRST_CHARS = A_TO_Z_LOWER + A_TO_Z_UPPER + "_";
    public static final String REST_OF_CHARS = FIRST_CHARS + DIGITS;

    public SqlIdentifierMatcher(String firstChars, String restOfChars, int maxLength, T type) {
        super(firstChars, restOfChars, maxLength, type);
    }

    public SqlIdentifierMatcher(int maxLength, T type) {
        this(FIRST_CHARS, REST_OF_CHARS, maxLength, type);
    }

    public SqlIdentifierMatcher(T type) {
        this(Integer.MAX_VALUE, type);
    }

}
