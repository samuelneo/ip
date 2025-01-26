package carbon.task;

import carbon.exceptions.InvalidArgumentException;
import carbon.utils.Parser;
import carbon.utils.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public String listTasks() {
        if (tasks.isEmpty()) {
            return "You don't have any tasks! :)";
        }
        return "Tasks:\n" + IntStream.range(0, tasks.size())
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
            return "You don't have any tasks! :)";
        }
        List<String> results = IntStream.range(0, tasks.size())
                .filter(i -> tasks.get(i).toString().toLowerCase().contains(filter.toLowerCase()))
                .mapToObj(i -> (i + 1) + ". " + tasks.get(i))
                .toList();
        boolean isPlural = results.size() != 1;
        return results.isEmpty()
                ? String.format("You don't have any tasks that contain \"%s\"", filter)
                : String.format("%d task%s contain%s \"%s\":\n", results.size(), isPlural ? "s" : "",
                isPlural ? "" : "s", filter) + String.join("\n", results);
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public int size() {
        return tasks.size();
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public void clear() {
        tasks.clear();
    }

    public String formatTask(Task task) {
        boolean pluralise = tasks.size() != 1;
        return "   " + task.toString() + "\n"
                + String.format("You now have %d task%s.", tasks.size(), pluralise ? "s" : "");
    }

    private String addTask(Task task) {
        tasks.add(task);
        Storage.updateDataFile(tasks);
        return "Added:\n" + formatTask(task);
    }

    public String markTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? String.format("Task #%d is already done (no changes made).", index + 1)
                : "Marked as done:";
        task.markAsDone();
        Storage.updateDataFile(tasks);
        return message + "\n   " + tasks.get(index);
    }

    public String unmarkTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? "Marked as not done:"
                : String.format("Task #%d has not been done (no changes made).", index + 1);
        task.unmarkAsDone();
        Storage.updateDataFile(tasks);
        return message + "\n   " + tasks.get(index);
    }

    public String addTodo(String arg) {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("I expected a description after \"todo\"");
        }
        Todo todo = new Todo(arg);
        return addTask(todo);
    }

    public String addDeadline(String arg) {
        Deadline deadline = Parser.parseDeadline(arg);
        return addTask(deadline);
    }

    public String addEvent(String arg) {
        Event event = Parser.parseEvent(arg);
        return addTask(event);
    }

    public String deleteTask(int index) {
        Task task = tasks.remove(index);
        Storage.updateDataFile(tasks);
        return "Deleted the following task:\n" + formatTask(task);
    }
}
