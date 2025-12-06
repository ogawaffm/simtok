package org.velohaven.simtok.syntax.sql;

import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TemporalLiterals {

    private TemporalLiterals() {}

    private static final String DATE_PATTERN_STRING
        = "^(?<year>\\d{4})-(?<month>0[1-9]|1[0-2])-(?<day>0[1-9]|[12]\\d|3[01])$";

    private static final Pattern DATE_PATTERN = Pattern.compile(DATE_PATTERN_STRING);

    private static final String TIME_PATTERN_STRING =
        "^(?<hour>[01]\\d|2[0-3]):(?<minute>[0-5]\\d):(?<second>[0-5]\\d)(\\.(?<fraction>\\d+))?$";

    private static final Pattern TIME_PATTERN = Pattern.compile(TIME_PATTERN_STRING);

    private static final Pattern TIMESTAMP_PATTERN = Pattern.compile(
        DATE_PATTERN_STRING + " " + TIME_PATTERN_STRING
    );

    public static boolean isDateLiteral(String input) {
        return toLocalDate(input) != null;
    }

    public static boolean isTimeLiteral(String input) {
        return toLocalTime(input) != null;
    }

    public static boolean isTimestampLiteral(String input) {
        return toLocalDateTime(input) != null;
    }

    private static LocalDate LocalDateFromMatcher(Matcher matcher) {
        if (matcher.matches()) {
            try {
                return LocalDate.of(
                    Integer.parseInt(matcher.group("year")),
                    Integer.parseInt(matcher.group("month")),
                    Integer.parseInt(matcher.group("day"))
                );
            } catch (Exception twentyNineFebruaryInNonLeapYear) {
                return null;
            }
        }
        return null;
    }

    private static LocalTime LocalTimeFromMatcher(Matcher matcher) {
        if (matcher.matches()) {
            return LocalTime.of(
                Integer.parseInt(matcher.group("hour")),
                Integer.parseInt(matcher.group("minute")),
                Integer.parseInt(matcher.group("second")),
                matcher.group("fraction") != null ? Integer.parseInt(matcher.group("fraction")) * 1_000_000 : 0
            );
        }
        return null;
    }

    public static LocalDate toLocalDate(@NonNull String input) {
        return LocalDateFromMatcher(DATE_PATTERN.matcher(input));
    }

    public static LocalTime toLocalTime(@NonNull String input) {
        return LocalTimeFromMatcher(TIME_PATTERN.matcher(input));
    }

    public static LocalDateTime toLocalDateTime(@NonNull String input) {
        Matcher matcher = TIMESTAMP_PATTERN.matcher(input);
        if (matcher.matches()) {
            try {
                //noinspection DataFlowIssue
                return LocalDateTime.of(LocalDateFromMatcher(matcher), LocalTimeFromMatcher(matcher));
            } catch (Exception twentyNineFebruaryInNonLeapYear) {
                return null;
            }
        }
        return null;
    }
}