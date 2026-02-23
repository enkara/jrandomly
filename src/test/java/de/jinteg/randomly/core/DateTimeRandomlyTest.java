
package de.jinteg.randomly.core;

import de.jinteg.randomly.JRandomly;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.*;

import static de.jinteg.randomly.core.DateTimeAssertions.*;
import static org.assertj.core.api.Assertions.assertThat;

class DateTimeRandomlyTest {

    @AfterEach
    void cleanup() {
        System.clearProperty("jrandomly.seed");
        System.clearProperty("jrandomly.runStartTime");
    }

    @Test
    void localDate_returnsAnchorDate() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:00Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#anchor");
        LocalDate d = r.dateTime().localDate();

        assertThat(d).isEqualTo(LocalDate.of(2026, 6, 15));
    }

    @Test
    void localDateBefore_isBeforeAnchor() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:00Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#before");
        LocalDate d = r.dateTime().localDateBefore(10);

        assertThat(d).isBefore(LocalDate.of(2026, 6, 15))
                .isAfterOrEqualTo(LocalDate.of(2026, 6, 5));
    }

    @Test
    void localDateTimeAsIso_hasCorrectFormat() {
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:05Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#localTimeString");
        String combinedString = r.dateTime().localDateTimeAsIso(r.dateTime().localDateTime());

        assertThatIsIsoDateTime(combinedString);
    }

    @Test
    void localDateAsIso_hasCorrectFormat() {
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:05Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#localTimeString");
        String localTimeString = r.dateTime().localDateAsIso(r.dateTime().localDate());

        assertThatIsIsoDate(localTimeString);
    }

    @Test
    void localTimeAsIso_hasCorrectFormat() {
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:05Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#localTimeString");
        String localTimeString = r.dateTime().localTimeAsIso(r.dateTime().localTime());

        assertThatIsIsoTime(localTimeString);
    }

    @Test
    void localDateBetween_isInRange() {
        System.setProperty("jrandomly.seed", "1");

        JRandomly r = JRandomly.randomly("DateTimeTest#between");
        LocalDate from = LocalDate.of(2026, 1, 1);
        LocalDate to = LocalDate.of(2026, 12, 31);

        LocalDate d = r.dateTime().localDateBetween(from, to);

        assertThat(d).isAfterOrEqualTo(from).isBeforeOrEqualTo(to);
    }

    @Test
    void localDateTime_isDeterministic() {
        System.setProperty("jrandomly.seed", "42");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:00Z");

        LocalDateTime a = JRandomly.randomly("DateTimeTest#det").dateTime().localDateTimeBefore(30);
        LocalDateTime b = JRandomly.randomly("DateTimeTest#det").dateTime().localDateTimeBefore(30);

        assertThat(a).isEqualTo(b);
    }

    @Test
    void localTime_isValidTime() {
        System.setProperty("jrandomly.seed", "1");

        JRandomly r = JRandomly.randomly("DateTimeTest#time");
        LocalTime t = r.dateTime().localTime();

        assertThat(t.getHour()).isBetween(0, 23);
        assertThat(t.getMinute()).isBetween(0, 59);
        assertThat(t.getSecond()).isBetween(0, 59);
    }

    @Test
    void zoneId_canBeOverridden() {
        System.setProperty("jrandomly.seed", "1");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T22:30:00Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#zone");

        // UTC: 22:30 → date is June 15
        LocalDate utcDate = r.dateTime(ZoneId.of("UTC")).localDate();
        assertThat(utcDate).isEqualTo(LocalDate.of(2026, 6, 15));

        // Tokyo: 22:30 UTC = next day 07:30 JST
        LocalDate tokyoDate = r.dateTime(ZoneId.of("Asia/Tokyo")).localDate();
        assertThat(tokyoDate).isEqualTo(LocalDate.of(2026, 6, 16));
    }

    @Test
    void instantInPast_isInExpectedRange() {
        System.setProperty("jrandomly.seed", "42");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:00Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#past");
        Instant anchor = r.getRunStartTime();
        Instant result = r.dateTime().instantInPast(5, 10);

        assertThat(result)
                .isAfterOrEqualTo(anchor.minus(Duration.ofDays(10)))
                .isBeforeOrEqualTo(anchor.minus(Duration.ofDays(5)));
    }

    @Test
    void instantInFuture_isInExpectedRange() {
        System.setProperty("jrandomly.seed", "42");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:00Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#future");
        Instant anchor = r.getRunStartTime();
        Instant result = r.dateTime().instantInFuture(1, 30);

        assertThat(result)
                .isAfterOrEqualTo(anchor.plus(Duration.ofDays(1)))
                .isBeforeOrEqualTo(anchor.plus(Duration.ofDays(30)));
    }

    @Test
    void localDateTimeInPast_isBeforeAnchor() {
        System.setProperty("jrandomly.seed", "42");
        System.setProperty("jrandomly.runStartTime", "2026-06-15T10:30:00Z");

        JRandomly r = JRandomly.randomly("DateTimeTest#ldtPast");
        LocalDateTime anchor = r.dateTime().localDateTime();
        LocalDateTime result = r.dateTime().localDateTimeInPast(1, 30);

        assertThat(result).isBefore(anchor);
    }
}