package carbon.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import carbon.exceptions.InvalidArgumentException;
import carbon.task.Deadline;
import carbon.task.Event;

/**
 * Parser contains static methods that manage the parsing of user input.
 */
public class Parser {
    /**
     * Parses user input regarding a deadline into a Deadline.
     * <p>
     * The input <code>text</code> is the user input to be parsed into a Deadline
     * (excluding the word "deadline").
     * <p>
     * For example, if the user inputs "deadline assignment /by 1pm",
     * <code>text</code> would be "assignment /by 1pm".
     *
     * @param text Details of the Deadline.
     * @return The Deadline object.
     */
    public static Deadline parseDeadline(String text) {
        // Regex matches a String of the form "{A} /by {B}", where A and B each contain at least
        // one non-whitespace character
        Matcher matcher = Pattern.compile("^\\s*(\\S.*?)\\s+/by\\s+(\\S.*?)\\s*$").matcher(text);
        if (!matcher.find()) {
            throw new InvalidArgumentException(
                    "Deadline commands should be formatted as \"deadline [description] /by [due date/time]\"");
        }
        return new Deadline(matcher.group(1), matcher.group(2));
    }

    /**
     * Parses user input regarding an event into an Event.
     * <p>
     * The input <code>text</code> is the user input to be parsed into an Event
     * (excluding the word "event").
     * <p>
     * For example, if the user inputs "event meeting /from 4pm /to 5pm",
     * <code>text</code> would be "meeting /from 4pm /to 5pm".
     *
     * @param text Details of the Event.
     * @return The Event object.
     */
    public static Event parseEvent(String text) {
        // Regex matches a String of the form "{A} /from {B} /to {C}", where A, B, and C each
        // contain at least one non-whitespace character
        Matcher matcher = Pattern.compile("^\\s*(\\S.*?)\\s+/from\\s+(\\S.*?)\\s+/to\\s+(\\S.*?)\\s*$")
                .matcher(text);
        if (!matcher.find()) {
            throw new InvalidArgumentException(
                    "Event commands should be formatted as \"event [description] /from [start] /to [end]\"");
        }
        return new Event(matcher.group(1), matcher.group(2), matcher.group(3));
    }
}
