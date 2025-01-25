package carbon.utils;

import carbon.exceptions.InvalidArgumentException;
import carbon.task.Deadline;
import carbon.task.Event;
import carbon.task.Task;
import carbon.task.Todo;

import java.util.ArrayList;
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
                .mapToObj(i -> (i+1) + ". " + tasks.get(i).toString())
                .collect(Collectors.joining("\n"));
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
                ? String.format("Task #%d is already done (no changes made).", index+1)
                : "Marked as done:";
        task.markAsDone();
        Storage.updateDataFile(tasks);
        return message + "\n   " + tasks.get(index);
    }

    public String unmarkTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? "Marked as not done:"
                : String.format("Task #%d has not been done (no changes made).", index+1);
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
        return "Deleted the following task:\n" + task;
    }
}
