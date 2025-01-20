package carbon.task;

/**
 * Event is a Task with a start and end date/time.
 */
public class Event extends Task{
    protected String start;
    protected String end;

    /**
     * Creates an Event with the specified description, start date/time, and end date/time.
     *
     * @param description Description of the Event.
     * @param start Start date/time of the Event.
     * @param end End date/time of the Event.
     */
    public Event(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    /**
     * Returns a String representation of the Event.
     * This includes the label [E], the completion status icon, followed by
     * the description of the Event, the start date/time, and the end date/time.
     *
     * @return String representation of the Event.
     */
    @Override
    public String toString() {
        return String.format("[E][%s] %s (from: %s, to: %s)", getStatusIcon(), description, start, end);
    }
}
