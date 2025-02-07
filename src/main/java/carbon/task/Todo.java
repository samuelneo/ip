package carbon.task;

import java.time.LocalDateTime;

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

    /**
     * {@inheritDoc}
     * A Todo is considered to have the latest possible datetime.
     */
    @Override
    public Temporal toDateTime() {
        return Temporal.of(LocalDateTime.MAX);
    }
}
