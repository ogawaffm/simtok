package org.velohaven.simtok.tokenizer.matcher;

import lombok.Getter;
import lombok.NonNull;
import org.velohaven.simtok.tokenizer.Text;

@Getter
public class Match<T> extends Text {

    /**
     * The type of the match (as defined by the matcher that produced it).
     */
    private final T type;

    /**
     * Creates a new Match object.
     * @param text the full text from which the match was extracted
     * @param start the start index of the match in the text
     * @param end the end index of the match in the text
     * @param type the type of the match (as defined by the matcher that produced it)
     */
    public Match(@NonNull final CharSequence text, int start, int end, @NonNull final T type) {
        super(text, start, end);
        this.type = type;
    }

    /**
     * The content of the match.
     * @return the content of the match
     */
    public Text getContent() {
        return this.getBaseText();
    }

}
