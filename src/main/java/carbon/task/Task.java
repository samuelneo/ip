package carbon.task;

/**
 * Task is an entry in the task list.
 * Each Task has a description and a completion status (isDone).
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates an Task with the specified description and a negative completion status.
     *
     * @param description Description of the Task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns an icon representing the completion status of the Task.
     * This is "X" if the task has been completed,
     * or a space " " if the task has not been completed.
     *
     * @return Completion status icon.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done carbon.task with X
    }

    /**
     * Returns a String representation of the Task.
     * This includes the completion status icon enclosed in square brackets,
     * followed by the description of the Task.
     *
     * @return String representation of the Task.
     */
    @Override
    public String toString() {
        return String.format("[%s] %s", getStatusIcon(), description);
    }
}
