package carbon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A DateTime object stores a date, time, datetime, or text.
 */
public class DateTime {
    enum DateTimeType {
        DATE,
        TIME,
        DATETIME,
        INVALID
    }

    private final DateTimeType type;
    private LocalDate date;
    private LocalTime time;
    private LocalDateTime dateTime;
    private String text;

    private static final DateTimeFormatter[] DATE_FORMATTERS = {
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("yyyy/M/d"),
            DateTimeFormatter.ofPattern("d-M-yyyy"),
            DateTimeFormatter.ofPattern("yyyy-M-d"),
            DateTimeFormatter.ofPattern("d MMM yyyy"),
            DateTimeFormatter.ofPattern("yyyy MMM d")
    };
    private static final DateTimeFormatter[] TIME_FORMATTERS = {
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("h:mma"),
            DateTimeFormatter.ofPattern("ha")
    };
    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = {
            DateTimeFormatter.ofPattern("d/M/yyyy H:mm"),
            DateTimeFormatter.ofPattern("yyyy/M/d H:mm"),
            DateTimeFormatter.ofPattern("d-M-yyyy H:mm"),
            DateTimeFormatter.ofPattern("yyyy-M-d H:mm"),
            DateTimeFormatter.ofPattern("d MMM yyyy H:mm"),
            DateTimeFormatter.ofPattern("yyyy MMM d H:mm"),
            DateTimeFormatter.ofPattern("d/M/yyyy h:mma"),
            DateTimeFormatter.ofPattern("yyyy/M/d h:mma"),
            DateTimeFormatter.ofPattern("d-M-yyyy h:mma"),
            DateTimeFormatter.ofPattern("yyyy-M-d h:mma"),
            DateTimeFormatter.ofPattern("d MMM yyyy h:mma"),
            DateTimeFormatter.ofPattern("yyyy MMM d h:mma"),
            DateTimeFormatter.ofPattern("d/M/yyyy ha"),
            DateTimeFormatter.ofPattern("yyyy/M/d ha"),
            DateTimeFormatter.ofPattern("d-M-yyyy ha"),
            DateTimeFormatter.ofPattern("yyyy-M-d ha"),
            DateTimeFormatter.ofPattern("d MMM yyyy ha"),
            DateTimeFormatter.ofPattern("yyyy MMM d ha")
    };

    private DateTime(LocalDate date) {
        this.date = date;
        type = DateTimeType.DATE;
    }

    private DateTime(LocalTime time) {
        this.time = time;
        type = DateTimeType.TIME;
    }

    private DateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        type = DateTimeType.DATETIME;
    }

    private DateTime(String text) {
        this.text = text;
        type = DateTimeType.INVALID;
    }

    public static DateTime parse(String text) {
        text = text.trim();

        // Try parsing to datetime
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
                return new DateTime(dateTime);
            } catch (DateTimeParseException e) {
                // Text does not fit this format, continue to next formatter
            }
        }

        // Try parsing to date
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(text, formatter);
                return new DateTime(date);
            } catch (DateTimeParseException e) {
                // Text does not fit this format, continue to next formatter
            }
        }

        // Try parsing to time
        for (DateTimeFormatter formatter : TIME_FORMATTERS) {
            try {
                LocalTime time = LocalTime.parse(text, formatter);
                return new DateTime(time);
            } catch (DateTimeParseException e) {
                // Text does not fit this format, continue to next formatter
            }
        }

        // Cannot be parsed
        return new DateTime(text);
    }

    /**
     * Returns a String representation of the DateTime.
     *
     * @return String representation of the Task.
     */
    @Override
    public String toString() {
        return switch (type) {
            case DATE -> date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
            case TIME -> time.format(DateTimeFormatter.ofPattern("h:mma"));
            case DATETIME -> dateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma"));
            case INVALID -> text;
        };
    }
}
