package org.velohaven.simtok.syntax.java;

import org.junit.jupiter.api.Test;
import org.velohaven.simtok.syntax.AllFilesTest;
import org.velohaven.simtok.syntax.Utils;
import org.velohaven.simtok.tokenizer.matcher.Matcher;

import java.io.IOException;
import java.util.List;

class TestAllJavaFiles extends AllFilesTest<JavaMatchType> {

    public TestAllJavaFiles() {
        super(".java");
    }

    @Test
    void testAllJavaFiles() throws IOException {

        String[] reservedWords =
                JavaTest.readReservedWords(Utils.getAbsoluteTestResourcesPath("java/reserved_words.txt")
        );

        List<Matcher<JavaMatchType>> matchers = JavaMatcherFactory.create(reservedWords, 128);

//        testAllFiles("src/test/resources/java", matchers, JavaMatchType.UNRECOGNIZED);
        testAllFiles("../../Projects", matchers, JavaMatchType.UNRECOGNIZED, Integer.MAX_VALUE);
    }
}