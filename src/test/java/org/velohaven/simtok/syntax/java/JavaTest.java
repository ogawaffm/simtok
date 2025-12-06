package org.velohaven.simtok.syntax.java;

import org.junit.jupiter.api.Test;
import org.velohaven.simtok.syntax.AbstractTest;
import org.velohaven.simtok.tokenizer.matcher.Matcher;

import java.util.List;

import static org.velohaven.simtok.syntax.Utils.getAbsoluteTestResourcesPath;

public class JavaTest extends AbstractTest<JavaMatchType> {

    private static final String SOURCE = readSource(
            getAbsoluteTestResourcesPath("java/SampleClass.java")
    );

    private static final String[] reservedWords = readReservedWords(
            getAbsoluteTestResourcesPath("java/reserved_words.txt")
    );

    @Test
    public void test() {

        List<Matcher<JavaMatchType>> matchers = JavaMatcherFactory.create(reservedWords, 128);
        test(matchers, JavaMatchType.UNRECOGNIZED, SOURCE, false);

    }
}
