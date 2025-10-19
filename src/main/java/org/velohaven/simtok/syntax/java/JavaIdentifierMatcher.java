package org.velohaven.simtok.syntax.java;

import org.velohaven.simtok.tokenizer.matcher.IdentifierMatcher;

public class JavaIdentifierMatcher<T extends Comparable<T>> extends IdentifierMatcher<T> {

    public JavaIdentifierMatcher(T type) {
        super(Character::isJavaIdentifierStart, Character::isJavaIdentifierPart, Integer.MAX_VALUE, type);
    }

    public JavaIdentifierMatcher(String firstChars, String restOfChars, int maxLength, T type) {
        super(firstChars, restOfChars, maxLength, type);
    }

}
