package carbon.task;

/**
 * Task is an entry in the task list.
 * Each Task has a description and a completion status (isDone).
 */
public abstract class Task implements Comparable<Task> {
    protected final char type;
    protected String description;
    protected boolean isDone;
    protected String warningMessage = "";

    /**
     * Creates an Task with the specified description.
     *
     * @param description Description of the Task.
     */
    public Task(char type, String description) {
        this.type = type;
        this.description = description;
        this.isDone = false;
    }

    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the Task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the Task as not done.
     */
    public void unmarkAsDone() {
        isDone = false;
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
     * Returns a String, formatted for text storage of the Task.
     *
     * @return Representation of the Task in text storage.
     */
    public String getStorageText() {
        return type + "\n" + (isDone ? "1" : "0") + "\n" + description;
    }

    /**
     * Returns a String representation of the Task.
     * This includes the label representing the type of Task, the completion status icon,
     * and the description of the Task.
     *
     * @return String representation of the Task.
     */
    @Override
    public String toString() {
        return String.format("[%s][%s] %s", type, getStatusIcon(), description);
    }

    /**
     * Returns a Temporal representing the datetime of the task.
     *
     * @return Temporal representing the datetime of the task.
     */
    public abstract Temporal toDateTime();

    /**
     * {@inheritDoc}
     * <p>
     * A Todo is considered to have a date/time greater than non-Todo tasks.
     * A Deadline is compared by its due date/time.
     * An Event is compared by its start date/time.
     */
    @Override
    public int compareTo(Task other) {
        return toDateTime().compareTo(other.toDateTime());
    }
}
