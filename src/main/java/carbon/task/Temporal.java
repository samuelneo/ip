package carbon.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.stream.Stream;

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
    // US Locale is specified in order to be consistent across different operating systems
    // See https://stackoverflow.com/questions/70059067/is-datetimeformatter-operating-system-dependent
    // for more info
    private static final DateTimeFormatter[] DATE_FORMATTERS = Stream.of(
            "d/M/yyyy",
            "yyyy/M/d",
            "d-M-yyyy",
            "yyyy-M-d",
            "d MMM yyyy",
            "yyyy MMM d"
    ).map(x -> DateTimeFormatter.ofPattern(x).withLocale(Locale.US)).toArray(DateTimeFormatter[]::new);

    private static final DateTimeFormatter[] TIME_FORMATTERS = Stream.of(
            "H:mm",
            "h:mma",
            "ha"
    ).map(x -> DateTimeFormatter.ofPattern(x).withLocale(Locale.US)).toArray(DateTimeFormatter[]::new);

    private static final DateTimeFormatter[] DATE_TIME_FORMATTERS = Stream.of(
            "d/M/yyyy H:mm",
            "yyyy/M/d H:mm",
            "d-M-yyyy H:mm",
            "yyyy-M-d H:mm",
            "d MMM yyyy H:mm",
            "yyyy MMM d H:mm",
            "d/M/yyyy h:mma",
            "yyyy/M/d h:mma",
            "d-M-yyyy h:mma",
            "yyyy-M-d h:mma",
            "d MMM yyyy h:mma",
            "yyyy MMM d h:mma",
            "d/M/yyyy ha",
            "yyyy/M/d ha",
            "d-M-yyyy ha",
            "yyyy-M-d ha",
            "d MMM yyyy ha",
            "yyyy MMM d ha"
    ).map(x -> DateTimeFormatter.ofPattern(x).withLocale(Locale.US)).toArray(DateTimeFormatter[]::new);

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
     * Returns a new datetime-type Temporal object representing the specified datetime.
     *
     * @param dateTime Datetime of the Temporal object.
     * @return Datetime-type Temporal object.
     */
    public static Temporal of(LocalDateTime dateTime) {
        return new Temporal(dateTime);
    }

    /**
     * Returns the input String, capitalising "am"/"pm" at the end of the String (if applicable).
     * <p>
     * DateTimeFormatter looks for capital "AM"/"PM", hence this is necessary for parsing.
     *
     * @param text Input String, possibly ending in "am"/"pm".
     * @return String with ending "am"/"pm" capitalised.
     */
    private static String capitaliseAmPm(String text) {
        return text.endsWith("am") || text.endsWith("pm")
                ? text.substring(0, text.length() - 2) + text.substring(text.length() - 2).toUpperCase()
                : text;
    }

    private static String decapitaliseAmPm(String text) {
        return text.endsWith("AM") || text.endsWith("PM")
                ? text.substring(0, text.length() - 2) + text.substring(text.length() - 2).toLowerCase()
                : text;
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
        text = capitaliseAmPm(text);

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
        text = capitaliseAmPm(text);

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
            case TIME -> decapitaliseAmPm(time.format(DateTimeFormatter.ofPattern("h:mma")));
            case DATETIME -> decapitaliseAmPm(dateTime.format(DateTimeFormatter.ofPattern("d MMM yyyy h:mma")));
            case TEXT -> text;
        };
    }
}
