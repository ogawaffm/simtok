package org.velohaven.simtok.syntax.sql;

/**
 * The type of token in a SQL statement. In case a match could be of multiple types, the type with the highest
 * priority is chosen. The highest priority is the first in the list because of .compareTo() in {@link SqlMatchType}.
 * is used to determine the priority of a match, therefor the order of the enum values is important.
 */
public enum SqlMatchType {
    RESERVED_WORD,          // A reserved word is pointed to be reserved in the SQL standard
    KEYWORD,                // A keyword is a word that is also known, but not reserved
    IDENTIFIER,             // An identifier is a name of a table, column, function etc.
    QUOTED_IDENTIFIER,      // An identifier that is quoted with double quotes, [] or `` (Backticks)
    WHITESPACE,             // A space, tab or newline
    BLOCK_COMMENT,          // A comment that starts with /* and ends with */
    LINE_COMMENT,           // A comment that starts with -- and ends with a newline or end of file
    QUOTED_LITERAL,         // A string that is quoted with single quotes
    NUMBER,                 // A number e.g. 123.45, 0.123, 123, -.123, +123e-4
    OPERATOR,               // An operator e.g. +, -, *, /, =, <>, <, >, <=, >=, LIKE, IN, BETWEEN, IS, NOT
    STATEMENT_TERMINATOR,   // A statement terminator e.g. ;
    BIND_VARIABLE,          // A bind variable: ? sql, ?1 JPA, :1 oracle pl/sql, :name postgresql, @var mysql/H2
    SUBSTITUTION_VARIABLE,  // A substitution variable e.g. &var in sqlplus/Db2 Command Line Processor, $(dept_id) in sql server sqlcmd
    DOT,                    // Separator for structure e.g. table.column
    ROUND_BRACKET,          // A round bracket e.g. (, )
    SQUARE_BRACKET,         // A square bracket e.g. [1] to access an array element
    CURLY_BRACKET,          // A curly bracket for jdbc escape syntax e.g. {fn curdate()} or  {t '10:11:22'}
    SEPARATOR,              // An argument separator e.g. ,
    UNRECOGNIZED            // A token that is not recognized
}
