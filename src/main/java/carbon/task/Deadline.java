package carbon.task;

/**
 * Deadline is a Task with a due date/time.
 */
public class Deadline extends Task{
    protected String dueBy;

    /**
     * Creates a Deadline with the specified description and due date/time.
     *
     * @param description Description of the Deadline.
     * @param dueBy Due date/time of the Deadline.
     */
    public Deadline(String description, String dueBy) {
        super(description);
        this.dueBy = dueBy;
    }

    /**
     * Returns a String representation of the Deadline.
     * This includes the label [D], the completion status icon, followed by
     * the description of the Deadline and the due date/time.
     *
     * @return String representation of the Deadline.
     */
    @Override
    public String toString() {
        return String.format("[D][%s] %s (by: %s)", getStatusIcon(), description, dueBy);
    }
}
