package carbon;

import carbon.exceptions.InvalidArgumentException;
import carbon.exceptions.InvalidCommandException;
import carbon.task.Deadline;
import carbon.task.Event;
import carbon.task.Task;
import carbon.task.Todo;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Carbon {
    private static final String horizontalLine = "_".repeat(60);

    private static ArrayList<Task> tasks;

    /**
     * Prints the message in between two horizontal lines.
     * The lines are indented by 4 spaces, and the message is indented by 5 spaces.
     *
     * @param message Message to be printed.
     */
    private static void printMessage(String... message) {
        System.out.println("    " + horizontalLine);
        for (String s : message) {
            System.out.println("     " + s.replace("\n", "\n     "));
        }
        System.out.println("    " + horizontalLine + "\n");
    }

    /**
     * Prints the error message in between two horizontal lines.
     * The lines are indented by 4 spaces, and the message is indented by 5 spaces,
     * preceded with "Oops!" and followed with ":(".
     *
     * @param message Error message to be printed.
     */
    private static void printError(String message) {
        printMessage(String.format("Oops! %s :(", message));
    }

    /**
     * Prints the list of tasks in between two horizontal lines.
     * The lines are indented by 4 spaces, and the tasks are indented by 5 spaces.
     */
    private static void printTasks() {
        if (tasks.isEmpty()) {
            printMessage("You don't have any tasks! :)");
            return;
        }
        String taskList = IntStream.range(0, tasks.size())
                .mapToObj(i -> (i+1) + ". " + tasks.get(i).toString())
                .collect(Collectors.joining("\n"));
        printMessage("Tasks:", taskList);
    }

    private static void printTask(String precedingMessage, Task task) {
        boolean pluralise = tasks.size() != 1;
        printMessage(precedingMessage,
                "   " + task.toString(),
                String.format("You now have %d task%s.", tasks.size(), pluralise ? "s" : ""));
    }

    private static void printAndAddTask(Task task) {
        tasks.add(task);
        printTask("Added:", task);
    }

    private static void markTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? String.format("Task #%d is already done (no changes made).", index+1)
                : "Marked as done:";
        task.markAsDone();
        printMessage(message, "   " + tasks.get(index).toString());
    }

    private static void unmarkTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? "Marked as not done:"
                : String.format("Task #%d has not been done (no changes made).", index+1);
        task.unmarkAsDone();
        printMessage(message, "   " + tasks.get(index).toString());
    }

    private static void addTodo(String arg) {
        if (arg.isEmpty()) {
            throw new InvalidArgumentException("I expected a description after \"todo\"");
        }
        Todo todo = new Todo(arg);
        printAndAddTask(todo);
    }

    private static void addDeadline(String arg) {
        String[] args = arg.split(" /by ");
        if (args.length != 2) {
            throw new InvalidArgumentException(
                    "Deadline commands should be formatted as \"deadline [description] /by [due date/time]\"");
        }
        Deadline deadline = new Deadline(args[0], args[1]);
        printAndAddTask(deadline);
    }

    private static void addEvent(String arg) {
        String[] args = arg.split(" /from ");
        // This allows validity to be verified just by checking the length of the duration array
        String[] duration = (args.length == 2) ? args[1].split(" /to ") : new String[0];
        if (duration.length != 2) {
            throw new InvalidArgumentException(
                    "Event commands should be formatted as \"event [description] /from [start] /to [end]\"");
        }
        Event event = new Event(args[0], duration[0], duration[1]);
        printAndAddTask(event);
    }

    private static void deleteTask(int index) {
        Task task = tasks.remove(index);
        printTask("Deleted the following task:", task);
    }

    /**
     * Processes user commands until they enter "bye".
     */
    private static void inputLoop() {
        tasks = new ArrayList<>(); // initialise empty carbon.task list
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();
            String[] words = input.split(" ", 2);
            String command = words[0].toLowerCase();
            String arg = words.length > 1 ? words[1] : "";

            try {
                switch (command) {
                    case "bye" -> {
                        return;
                    }
                    case "list" -> printTasks();
                    case "mark" -> markTask(Integer.parseInt(arg) - 1);
                    case "unmark" -> unmarkTask(Integer.parseInt(arg) - 1);
                    case "todo" -> addTodo(arg);
                    case "deadline" -> addDeadline(arg);
                    case "event" -> addEvent(arg);
                    case "delete" -> deleteTask(Integer.parseInt(arg) - 1);
                    default -> throw new InvalidCommandException(
                            String.format("The command \"%s\" is not recognised", command));
                }
            } catch (InvalidCommandException | InvalidArgumentException e) {
                printError(e.getMessage());
            } catch (NumberFormatException e) {
                printError(String.format("I expected a single integer after \"%s\"", command));
            } catch (IndexOutOfBoundsException e) {
                String message = tasks.isEmpty()
                        ? "You don't have any tasks!"
                        : "Tasks are numbered from 1 to " + tasks.size();
                printError(message);
            }
        }
    }

    public static void main(String[] args) {
        String logo = """
                   ____           _
                  / ___|__ _ _ __| |__   ___  _ __
                 | |   / _` | '__| '_ \\ / _ \\| '_ \\
                 | |__| (_| | |  | |_) | (_) | | | |
                  \\____\\__,_|_|  |_.__/ \\___/|_| |_|""";
        printMessage(logo + "\nHello! What can I do for you?");
        inputLoop();
        printMessage("Goodbye!");
    }
}
