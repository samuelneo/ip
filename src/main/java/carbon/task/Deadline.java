package carbon.task;

/**
 * Deadline is a Task with a due date/time.
 */
public class Deadline extends Task {
    private final Temporal dueBy;

    /**
     * Creates a Deadline with the specified description and due date/time.
     *
     * @param description Description of the Deadline.
     * @param dueBy Due date/time of the Deadline.
     */
    public Deadline(String description, String dueBy) {
        super('D', description);
        this.dueBy = Temporal.parse(dueBy);

        if (this.dueBy.getType() == TemporalType.TEXT) {
            warningMessage = "\n" + Temporal.TEMPORAL_PARSE_WARNING;
        }
    }

    /**
     * {@inheritDoc}
     * A Deadline's datetime is that of its due date/time.
     */
    @Override
    public Temporal toDateTime() {
        return dueBy;
    }

    public String getStorageText() {
        return super.getStorageText() + "\n" + dueBy;
    }

    /**
     * {@inheritDoc}
     * Additionally, the due date/time is included.
     */
    @Override
    public String toString() {
        return String.format("%s (by: %s)", super.toString(), dueBy);
    }
}
