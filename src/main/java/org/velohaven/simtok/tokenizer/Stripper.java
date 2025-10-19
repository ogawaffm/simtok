package org.velohaven.simtok.tokenizer;

import org.velohaven.simtok.tokenizer.matcher.Match;
import org.velohaven.simtok.syntax.sql.SqlMatchType;

public class Stripper {

    private final Tokenizer<SqlMatchType> parser;
    private StringBuilder sb;

    public Stripper(Tokenizer<SqlMatchType> parser) {
        this.parser = parser;
    }

    public CharSequence hide(CharSequence input) {
        sb = new StringBuilder(input.length());
        parser.tokenize(input, this::matchConsumer);
        return sb;
    }

    public void matchConsumer(Match<SqlMatchType> match) {
        if (match.getType() == SqlMatchType.BLOCK_COMMENT || match.getType() == SqlMatchType.LINE_COMMENT) {
            char c;
            for (int i = 0; i < match.length(); i++) {
                c = match.charAt(i);
                if (Character.isWhitespace(c)) {
                    // keep whitespace, especially newlines
                    sb.append(c);
                } else {
                    sb.append(' ');
                }
            }
        } else {
            sb.append(match);
        }
    }

}