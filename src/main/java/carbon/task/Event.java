package carbon.task;

/**
 * Event is a Task with a start and end date/time.
 */
public class Event extends Task {
    private final Temporal start;
    private final Temporal end;

    /**
     * Creates an Event with the specified description, start date/time, and end date/time.
     *
     * @param description Description of the Event.
     * @param start Start date/time of the Event.
     * @param end End date/time of the Event.
     */
    public Event(String description, String start, String end) {
        super('E', description);
        this.start = Temporal.parse(start);
        this.end = Temporal.parse(end);

        if (this.start.compareTo(this.end) > 0) {
            warningMessage = "\nWARNING: Start date/time is after end date/time.";
        }

        if (this.start.getType() == TemporalType.TEXT || this.end.getType() == TemporalType.TEXT) {
            warningMessage += "\n" + Temporal.TEMPORAL_PARSE_WARNING;
        }
    }

    public String getStorageText() {
        return super.getStorageText() + "\n" + start + "\n" + end;
    }

    /**
     * {@inheritDoc}
     * An Event's datetime is that of its start date/time.
     */
    @Override
    public Temporal toDateTime() {
        return start;
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
