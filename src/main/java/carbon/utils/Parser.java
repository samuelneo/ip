package carbon.utils;

import carbon.exceptions.InvalidArgumentException;
import carbon.task.Deadline;
import carbon.task.Event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    public static Deadline parseDeadline(String text) {
        // Regex matches a String of the form "{A} /by {B}", where A and B each contain at least
        // one non-whitespace character
        Matcher matcher = Pattern.compile("^(.*\\S.*) /by (.*\\S.*)$").matcher(text);
        if (!matcher.find()) {
            throw new InvalidArgumentException(
                    "Deadline commands should be formatted as \"deadline [description] /by [due date/time]\"");
        }
        return new Deadline(matcher.group(1), matcher.group(2));
    }

    public static Event parseEvent(String text) {
        // Regex matches a String of the form "{A} /from {B} /to {C}", where A, B, and C each
        // contain at least one non-whitespace character
        Matcher matcher = Pattern.compile("^(.*\\S.*) /from (.*\\S.*) /to (.*\\S.*)$").matcher(text);
        if (!matcher.find()) {
            throw new InvalidArgumentException(
                    "Event commands should be formatted as \"event [description] /from [start] /to [end]\"");
        }
        return new Event(matcher.group(1), matcher.group(2), matcher.group(3));
    }
}
