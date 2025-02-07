package carbon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * A Temporal stores an object that fits into one of the following categories:
 * <ul>
 *     <li>Date</li>
 *     <li>Time</li>
 *     <li>Datetime</li>
 *     <li>Text (none of the above)</li>
 * </ul>
 */
public class Temporal implements Comparable<Temporal> {
    enum TemporalType {
        DATE,
        TIME,
        DATETIME,
        TEXT
    }

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

    private final TemporalType type;
    private LocalDate date;
    private LocalTime time;
    private LocalDateTime dateTime;
    private String text;

    private Temporal(LocalDate date) {
        this.date = date;
        type = TemporalType.DATE;
    }

    private Temporal(LocalTime time) {
        this.time = time;
        type = TemporalType.TIME;
    }

    private Temporal(LocalDateTime dateTime) {
        this.dateTime = dateTime;
        type = TemporalType.DATETIME;
    }

    private Temporal(String text) {
        this.text = text;
        type = TemporalType.TEXT;
    }

    /**
     * Parses text into a date-type Temporal object.
     *
     * @param text Text to be parsed.
     * @return A date-type Temporal if possible, <code>null</code> otherwise.
     */
    public static Temporal parseToDate(String text) {
        for (DateTimeFormatter formatter : DATE_FORMATTERS) {
            try {
                LocalDate date = LocalDate.parse(text, formatter);
                return new Temporal(date);
            } catch (DateTimeParseException e) {
                // Text does not fit this format, continue to next formatter
            }
        }
        // Failed to parse
        return null;
    }

    /**
     * Parses text into a time-type Temporal object.
     *
     * @param text Text to be parsed.
     * @return A time-type Temporal if possible, <code>null</code> otherwise.
     */
    public static Temporal parseToTime(String text) {
        for (DateTimeFormatter formatter : TIME_FORMATTERS) {
            try {
                LocalTime time = LocalTime.parse(text, formatter);
                return new Temporal(time);
            } catch (DateTimeParseException e) {
                // Text does not fit this format, continue to next formatter
            }
        }
        // Failed to parse
        return null;
    }

    /**
     * Parses text into a datetime-type Temporal object.
     *
     * @param text Text to be parsed.
     * @return A datetime-type Temporal if possible, <code>null</code> otherwise.
     */
    public static Temporal parseToDateTime(String text) {
        for (DateTimeFormatter formatter : DATE_TIME_FORMATTERS) {
            try {
                LocalDateTime dateTime = LocalDateTime.parse(text, formatter);
                return new Temporal(dateTime);
            } catch (DateTimeParseException e) {
                // Text does not fit this format, continue to next formatter
            }
        }
        // Failed to parse
        return null;
    }

    /**
     * Returns a Temporal object of the appropriate type by parsing the input text.
     * <p>
     * First attempts to parse the text into a datetime-type Temporal.
     * On failure, attempts to parse into a date-type Temporal.
     * On failure, attempts to parse into a time-type Temporal.
     * On failure, returns a text-type Temporal.
     *
     * @param text Text to be parsed.
     * @return Temporal object obtained by parsing the text.
     */
    public static Temporal parse(String text) {
        // Try parsing to datetime, then date, then time
        Temporal temporal = parseToDateTime(text);
        if (temporal == null) {
            temporal = parseToDate(text);
        }
        if (temporal == null) {
            temporal = parseToTime(text);
        }
        if (temporal == null) {
            // Cannot be parsed
            temporal = new Temporal(text);
        }

        return temporal;
    }

    private LocalDateTime toDateTime() {
        return switch (type) {
            case DATE -> date.atStartOfDay();
            case TIME -> time.atDate(LocalDate.now());
            case DATETIME -> dateTime;
            case TEXT -> throw new IllegalStateException();
        };
    }

    /**
     * {@inheritDoc}
     * <p>
     * A text-type Temporal is considered to have a datetime later than a Temporal of any other type.
     * A date-type Temporal is considered to have a time of 00:00 (midnight).
     * A time-type Temporal is considered to have the current date.
     */
    @Override
    public int compareTo(Temporal other) {
        if (type == TemporalType.TEXT) {
            return other.type == TemporalType.TEXT ? 0 : 1;
        }
        if (other.type == TemporalType.TEXT) {
            return -1;
        }

        LocalDateTime thisDateTime = toDateTime();
        LocalDateTime otherDateTime = other.toDateTime();
        return thisDateTime.compareTo(otherDateTime);
    }

    /**
     * Returns a String representation of the Temporal.
     *
     * @return String representation of the Temporal.
     */
    @Override
    public String toString() {
        return switch (type) {
            case DATE -> date.format(DateTimeFormatter.ofPattern("d MMM yyyy"));
            case TIME -> time.format(DateTimeFormatter.ofPattern("h:mma"));
            case DATETIME -> dateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma"));
            case TEXT -> text;
        };
    }
}
