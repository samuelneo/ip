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
        super('T', description);
    }
}
