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
        super('E', description);
        this.start = start;
        this.end = end;
    }

    /**
     * {@inheritDoc}
     */
    public String getStorageText() {
        return super.getStorageText() + "\n" + start + "\n" + end;
    }

    /**
     * {@inheritDoc}
     * Additionally, the start and end date/time is included.
     */
    @Override
    public String toString() {
        return String.format("%s (from: %s, to: %s)", super.toString(), start, end);
    }
}
