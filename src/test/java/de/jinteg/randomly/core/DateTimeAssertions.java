package de.jinteg.randomly.core;

import static org.assertj.core.api.Assertions.assertThat;

public class DateTimeAssertions {

    private static final String ISO_TIME_REGEX = "^([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";
    private static final String ISO_DATE_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$";
    private static final String ISO_DATETIME_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])T([01]\\d|2[0-3]):[0-5]\\d:[0-5]\\d$";

    public static void assertThatIsIsoDateTime(String actual) {
        assertThat(actual)
                .as("Check if '%s' matches ISO combined format YYYY-MM-DDTHH:mm:ss", actual)
                .matches(ISO_DATETIME_REGEX);
    }

    public static void assertThatIsIsoTime(String actual) {
        assertThat(actual)
                .as("Check if '%s' matches ISO time format HH:mm:ss", actual)
                .matches(ISO_TIME_REGEX);
    }

    public static void assertThatIsIsoDate(String actual) {
        assertThat(actual)
                .as("Check if '%s' matches ISO date format YYYY-MM-DD", actual)
                .matches(ISO_DATE_REGEX);
    }

}