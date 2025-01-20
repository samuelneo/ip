package carbon.task;

/**
 * Todo is a Task with no date/time attached
 */
public class Todo extends Task {
    /**
     * Creates a Todo with the specified description.
     *
     * @param description Description of the Todo.
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a String representation of the Todo.
     * This includes the label [T], the completion status icon,
     * followed by the description of the Todo.
     *
     * @return String representation of the Todo.
     */
    @Override
    public String toString() {
        return String.format("[T][%s] %s", getStatusIcon(), description);
    }
}
