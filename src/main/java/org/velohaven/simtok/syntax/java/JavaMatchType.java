package org.velohaven.simtok.syntax.java;

/**
 * JavaMatchType is an enumeration of the different types of tokens that can be matched in Java code.
 * Instances ending on _SYMBOL are not unique and can be used in different contexts.
 */
public enum JavaMatchType {
    RESERVED_WORD,               // A reserved word is pointed to be reserved in the SQL standard
    IDENTIFIER,                  // An identifier is a name of a table, column, function etc.
    ANNOTATION_IDENTIFIER,       // An annotation identifier is a name an @annotation
    WHITESPACE,                  // A space, tab or newline
    BLOCK_COMMENT,               // A comment that starts with /* and ends with */
    LINE_COMMENT,                // A comment that starts with -- and ends with a newline or end of file
    STRING_LITERAL,              // A string that is quoted with single quotes
    CHAR_LITERAL,                // A string that is quoted with single quotes
    INTEGER_HEX_LITERAL,         // A hex integer literal e.g. 0x1234
    LONG_HEX_LITERAL,            // A hex long literal e.g. 0x1234L
    NUMBER,                      // A number e.g. 123.45, 0.123, 123, -.123, +123e-4
    INTEGER_LITERAL,             // A integer literal e.g. 12345
    LONG_LITERAL,                // A integer literal e.g. 12345
    FLOAT_LITERAL,               // A integer literal e.g. 12345
    DOUBLE_LITERAL,              // A integer literal e.g. 12345
    OPERATOR,                    // An operator e.g. +, -, *, /, =, <>, <=, >=, but not <, > which are "symbols"
    GREATER_THAN_SYMBOL,         // > can be an operator or a generic type List<String>
    LESS_THAN_SYMBOL,            // < can be an operator or a generic type List<String>
    SEMICOLON_SYMBOL,            // A statement terminator e.g. ;
    DOT_SYMBOL,                         // Separator for structure e.g. table.column
    ROUND_BRACKET,               // A round bracket e.g. (, )
    SQUARE_BRACKET,              // A square bracket e.g. [1] to access an array element
    CURLY_BRACKET,               // A curly bracket for jdbc escape syntax e.g. {fn curdate()} or  {t '10:11:22'}
    COLON_SYMBOL,                // a < b ? -1 : 1; or in for (ElementType element : collection) -- the colon
    QUESTION_MARK_SYMBOL,        // a < b ? -1 : 1; tenary operator or List<?> list;
    METHOD_REFERENCE_OPERATOR,   // :: operator
    COMMA_SYMBOL,             // A function argument: f(a, b), element separator {a, b} etc
    UNRECOGNIZED                 // A token that is not recognized
}
