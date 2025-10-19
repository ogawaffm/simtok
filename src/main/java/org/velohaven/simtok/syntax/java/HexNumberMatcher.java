package org.velohaven.simtok.syntax.java;

import org.velohaven.simtok.tokenizer.matcher.WordMatcher;

public class HexNumberMatcher<T extends Comparable<T>> extends WordMatcher<T> {

    static final private String HEX_CHARS = "0123456789abcdefABCDEF";

    public HexNumberMatcher(int maxLength, T type) {
        super(HEX_CHARS, maxLength, type);
    }

}
