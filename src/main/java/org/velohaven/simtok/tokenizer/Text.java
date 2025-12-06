package org.velohaven.simtok.tokenizer;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@EqualsAndHashCode
@SuppressWarnings("unused") // Ignore unused warning for public API
public class Text implements CharSequence, Comparable<Text> {

    @EqualsAndHashCode.Exclude
    private final CharSequence baseText;
    @Getter
    private final int startIndex;
    private final int endIndex;

    public Text(CharSequence text) {
        this.baseText = text;
        this.startIndex = 0;
        this.endIndex = -1;
    }

    public Text(CharSequence baseText, int startIndex, int endIndex) {
        this.baseText = baseText;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        checkBeginAndEndIndex(startIndex, endIndex);
    }

    public Text createRegion(int startIndex, int endIndex) {
        checkBeginAndEndIndex(startIndex, endIndex);
        if (isBaseText() && startIndex == 0 && endIndex == this.endIndex) {
            return this;
        } else {
            return new Text(this, startIndex, endIndex);
        }
    }

    public boolean isBaseText() {
        return startIndex == 0 && endIndex == -1;
    }

    public boolean isSubText() {
        return !isBaseText();
    }

    public Text getBaseText() {
        if (isBaseText()) {
            return this;
        } else if (baseText instanceof Text) {
            return (Text) baseText;
        } else {
            return new Text(baseText);
        }
    }

    public int getEndIndex() {
        return endIndex == -1 ? baseText.length() : endIndex;
    }

    private void checkBeginAndEndIndex(int beginIndex, int endIndex) {
        if (beginIndex < 0 || endIndex > baseText.length() || beginIndex > endIndex) {
            throw new IndexOutOfBoundsException(
                "Invalid start or end index. Start: " + beginIndex + " End: " + endIndex + " Length: " + length());
        }
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= length()) {
            throw new IndexOutOfBoundsException("Invalid index. Index: " + index + " Length: " + length());
        }
    }

    public int indexOf(char ch, int start, int end) {
        checkBeginAndEndIndex(start, end);
        start += startIndex;
        end += startIndex;
        for (int i = start; i < end; i++) {
            if (baseText.charAt(i) == ch) {
                return i - startIndex;
            }
        }
        return -1;
    }

    public int indexOf(@NonNull CharSequence str) {
        return indexOf(str, 0, length());
    }

    public int indexOf(@NonNull CharSequence str, int start, int end) {
        checkBeginAndEndIndex(start, end);
        start += startIndex;
        end += startIndex;
        int strLength = str.length();
        for (int i = start; i <= end - strLength; i++) {
            if (baseText.subSequence(i, i + strLength).equals(str)) {
                return i - startIndex;
            }
        }
        return -1;
    }

    public int lastIndexOf(char ch, int start, int end) {
        checkBeginAndEndIndex(start, end);
        start += startIndex;
        end += startIndex;
        for (int i = end - 1; i >= start; i--) {
            if (baseText.charAt(i) == ch) {
                return i - startIndex;
            }
        }
        return -1;
    }

    public int lastIndexOf(CharSequence str) {
        return lastIndexOf(str, 0, length());
    }

    public int lastIndexOf(@NonNull CharSequence str, int start, int end) {
        checkBeginAndEndIndex(start, end);
        start += startIndex;
        end += startIndex;
        int strLength = str.length();
        for (int i = end - strLength; i >= start; i--) {
            if (baseText.subSequence(i, i + strLength).equals(str)) {
                return i - startIndex;
            }
        }
        return -1;
    }

    public int getLineNo(int index) {
        checkIndex(index);
        int lineNo = 1;
        int pos = 0;
        while ((pos = indexOf('\n', pos, index)) != -1) {
            lineNo++;
            pos++;
        }
        return lineNo;
    }

    public int getLineNo() {
        return getBaseText().getLineNo(startIndex);
    }

    public int getLineCount() {
        int lineCount = 1;
        int pos = 0;
        while ((pos = indexOf('\n', pos, length())) != -1) {
            lineCount++;
            pos++;
        }
        return lineCount;
    }

    public Text getLine(int lineNo) {
        int pos = 0;
        int line = 1;
        while (line < lineNo && (pos = indexOf('\n', pos, length())) != -1) {
            line++;
            pos++;
        }
        if (pos == -1) {
            throw new IndexOutOfBoundsException("Invalid line number: " + lineNo);
        }
        int start = pos;
        pos = indexOf('\n', pos, length());
        int end = pos == -1 ? length() : pos;
        return createRegion(start, end);
    }

    public int getColumnNo(int index) {
        checkIndex(index);
        int lastNewline = lastIndexOf('\n', 0, index);
        if (lastNewline == -1) {
            return index + 1;
        }
        return index - lastNewline;
    }

    public int getColumnNo() {
        return getBaseText().getColumnNo(startIndex);
    }

    public Text stripTrailing() {
        int end = getEndIndex();
        while (end > startIndex && Character.isWhitespace(baseText.charAt(end - 1))) {
            end--;
        }
        return new Text(baseText, startIndex, end);
    }

    public Text stripLeading() {
        int start = startIndex;
        while (start < getEndIndex() && Character.isWhitespace(baseText.charAt(start))) {
            start++;
        }
        return new Text(baseText, start, getEndIndex());
    }

    public Text strip() {
        return stripLeading().stripTrailing();
    }

    public boolean startsWith(@NonNull CharSequence prefix) {
        return baseText.subSequence(startIndex, startIndex + prefix.length()).equals(prefix);
    }

    public boolean endsWith(@NonNull CharSequence suffix) {
        return baseText.subSequence(getEndIndex() - suffix.length(), getEndIndex()).equals(suffix);
    }

    public boolean isBlank() {
        for (int i = startIndex; i < getEndIndex(); i++) {
            if (!Character.isWhitespace(baseText.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(CharSequence str) {
        return indexOf(str) != -1;
    }

    public Matcher match(CharSequence pattern) {
        Pattern compiledPattern = Pattern.compile(pattern.toString());
        return compiledPattern.matcher(this);
    }

    /* ************************************************************************************************************* */
    /* ************************************************ CharSequence *********************************************** */
    /* ************************************************************************************************************* */

    @Override
    public int length() {
        if (endIndex == -1) {
            return baseText.length() - startIndex;
        } else {
            return endIndex - startIndex;
        }
    }

    @Override
    public char charAt(final int index) {
        checkIndex(index);
        return baseText.charAt(startIndex + index);
    }

    @Override
    public @NonNull Text subSequence(final int start, final int end) {
        checkBeginAndEndIndex(start, end); // value of end cannot be -1 after the check
        if (start == 0 && startIndex == 0 && end == endIndex) {
            return this;
        } else {
            return new Text(baseText, startIndex + start, startIndex + end);
        }
    }

    @Override
    public int compareTo(@NonNull Text other) {
        int thisLength = this.length();
        int otherLength = other.length();
        int minLen = Math.min(thisLength, otherLength);

        char charOfThis;
        char charOfOther;

        for (int i = 0; i < minLen; i++) {

            charOfThis = this.charAt(i);
            charOfOther = other.charAt(i);
            if (charOfThis != charOfOther) {
                return charOfThis - charOfOther;
            }

        }
        return thisLength - otherLength;
    }

    @Override
    @NonNull public String toString() {
        StringBuilder sb = new StringBuilder(length());

        for (int i = 0; i < length(); i++) {
            sb.append(charAt(i));
        }
        return sb.toString();
    }

}