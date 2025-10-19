package org.velohaven.simtok.syntax.sql;

import org.junit.jupiter.api.Test;
import org.velohaven.simtok.syntax.AbstractTest;
import org.velohaven.simtok.tokenizer.matcher.Matcher;

import java.util.List;

import static org.velohaven.simtok.syntax.Utils.getAbsoluteTestResourcesPath;

public class SqlTest extends AbstractTest<SqlMatchType> {

    final private static String source = getAbsoluteTestResourcesPath("sql/statements.sql");

    final private static String[] reservedWords = readReservedWords(
            getAbsoluteTestResourcesPath("sql/iso/reserved_words.txt")
    );

    @Test
    public void test() {

        List<Matcher<SqlMatchType>> matchers = SqlMatcherFactory.create(reservedWords, 128);
        test(matchers, SqlMatchType.UNRECOGNIZED, source, true);

    }
}
