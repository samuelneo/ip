package carbon.task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import carbon.exceptions.InvalidArgumentException;
import carbon.utils.Parser;
import carbon.utils.Storage;

/**
 * A TaskList manages a list of tasks.
 */
public class TaskList {
    private static final String NO_TASKS_MESSAGE = "You don't have any tasks! :)";
    private final ArrayList<Task> tasks;

    /**
     * Creates a TaskList object with no tasks.
     */
    public TaskList() {
        tasks = new ArrayList<>();
    }

    /**
     * Returns a String that lists all tasks in the TaskList.
     * <p>
     * Tasks are numbered, starting from 1.
     *
     * @return String representing the TaskList.
     */
    public String listTasks() {
        if (tasks.isEmpty()) {
            return NO_TASKS_MESSAGE;
        }
        return String.format("You have %d task%s:\n", tasks.size(), tasks.size() != 1 ? "s" : "")
                + IntStream.range(0, tasks.size())
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.joining("\n"));
    }

    /**
     * Returns a String that lists all tasks in the TaskList whose String representation
     * contains <code>filter</code> (case-insensitive).
     * <p>
     * Tasks are still numbered according to their original indices, rather than the
     * indices by which they appear in the filtered output. This is because the user
     * interacts with tasks based on their index in the TaskList, so presenting the
     * original index of the task avoids confusion.
     *
     * @param filter The text to filter by.
     * @return String representing the filtered TaskList.
     */
    public String listTasks(String filter) {
        if (filter.isEmpty()) {
            throw new InvalidArgumentException("I expected some text after \"find\"");
        }
        if (tasks.isEmpty()) {
            return NO_TASKS_MESSAGE;
        }

        return IntStream.range(0, tasks.size())
                .filter(i -> tasks.get(i).toString().toLowerCase().contains(filter.toLowerCase()))
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        results -> results.isEmpty()
                                ? String.format("You don't have any tasks that contain \"%s\".", filter)
                                : String.format("%d task%s contain%s \"%s\":\n",
                                results.size(),
                                results.size() != 1 ? "s" : "",
                                results.size() == 1 ? "s" : "",
                                filter) + String.join("\n", results)
                ));
    }

    /**
     * Sorts the TaskList, then returns a String that lists all tasks in the TaskList.
     *
     * @return String representing the TaskList.
     * @see Task
     * @see Temporal
     */
    public String sortTasks() {
        if (tasks.isEmpty()) {
            return NO_TASKS_MESSAGE;
        }

        Collections.sort(tasks);
        Storage.updateDataFile(tasks);

        return "Tasks sorted!\n" + listTasks();
    }

    /**
     * Returns <code>true</code> if the TaskList is empty,
     * <code>false</code> otherwise.
     *
     * @return Whether the TaskList is empty.
     */
    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /**
     * Returns the number of tasks in the TaskList.
     *
     * @return Number of tasks.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Adds a task to the TaskList.
     *
     * @param task Task to be added.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Clears the TaskList.
     */
    public void clear() {
        tasks.clear();
    }

    /**
     * Returns a String representing the input task, along with the
     * number of tasks currently in the TaskList.
     *
     * @param task Task to be formatted.
     * @return String representing the input task and TaskList size.
     */
    public String formatTask(Task task) {
        boolean isPlural = tasks.size() != 1;
        return "   " + task.toString() + "\n"
                + String.format("You now have %d task%s.", tasks.size(), isPlural ? "s" : "");
    }

    private String addTask(Task task) {
        tasks.add(task);
        Storage.updateDataFile(tasks);
        return "Added:\n" + formatTask(task);
    }

    /**
     * Marks a task as done.
     * <p>
     * Does nothing if the task has already been marked as done.
     *
     * @param index Index of the task in the TaskList to mark.
     * @return Message representing the changes made.
     * @throws IndexOutOfBoundsException If <code>index</code> is out of bounds.
     */
    public String markTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? String.format("Task #%d is already done (no changes made).", index + 1)
                : "Marked as done:";
        task.markAsDone();
        Storage.updateDataFile(tasks);
        return message + "\n   " + tasks.get(index);
    }

    /**
     * Unmarks a task as done (i.e., marks as not done).
     * <p>
     * Does nothing if the task has not been marked as done.
     *
     * @param index Index of the task in the TaskList to unmark.
     * @return Message representing the changes made.
     * @throws IndexOutOfBoundsException If <code>index</code> is out of bounds.
     */
    public String unmarkTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? "Marked as not done:"
                : String.format("Task #%d has not been done (no changes made).", index + 1);
        task.unmarkAsDone();
        Storage.updateDataFile(tasks);
        return message + "\n   " + tasks.get(index);
    }

    /**
     * Adds a Todo to the TaskList.
     *
     * @param arg Description of the Todo.
     * @return Message representing the changes made.
     */
    public String addTodo(String arg) {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("I expected a description after \"todo\"");
        }
        Todo todo = new Todo(arg);
        return addTask(todo);
    }

    /**
     * Adds a Deadline to the TaskList.
     * <p>
     * The input <code>arg</code> is the user input to be parsed into a Deadline
     * (excluding the word "deadline").
     * <p>
     * For example, if the user inputs "deadline assignment /by 1pm",
     * <code>arg</code> would be "assignment /by 1pm".
     *
     * @param arg Details of the Deadline.
     * @return Message representing the changes made.
     */
    public String addDeadline(String arg) {
        Deadline deadline = Parser.parseDeadline(arg);
        return addTask(deadline);
    }

    /**
     * Adds an Event to the TaskList.
     * <p>
     * The input <code>arg</code> is the user input to be parsed into an Event
     * (excluding the word "event").
     * <p>
     * For example, if the user inputs "event meeting /from 4pm /to 5pm",
     * <code>arg</code> would be "meeting /from 4pm /to 5pm".
     *
     * @param arg Details of the Event.
     * @return Message representing the changes made.
     */
    public String addEvent(String arg) {
        Event event = Parser.parseEvent(arg);
        return addTask(event);
    }

    /**
     * Deletes a task from the TaskList.
     *
     * @param index Index of the task in the TaskList to delete.
     * @return Message representing the changes made.
     * @throws IndexOutOfBoundsException If <code>index</code> is out of bounds.
     */
    public String deleteTask(int index) {
        Task task = tasks.remove(index);
        Storage.updateDataFile(tasks);
        return "Deleted the following task:\n" + formatTask(task);
    }
}
