package carbon;

import carbon.exceptions.InvalidArgumentException;
import carbon.exceptions.InvalidCommandException;
import carbon.exceptions.InvalidFileFormatException;
import carbon.task.Deadline;
import carbon.task.Event;
import carbon.task.Task;
import carbon.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileSystemException;
import java.nio.file.NoSuchFileException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Carbon {
    private static final String HORIZONTAL_LINE = "_".repeat(60);
    private static final String TASKS_FILE_PATH = "data/user/tasks.txt";

    private static ArrayList<Task> tasks;
    private static boolean hasTasklistChanged = false;

    /**
     * Prints the message in between two horizontal lines.
     * The lines are indented by 4 spaces, and the message is indented by 5 spaces.
     *
     * @param message Message to be printed.
     */
    private static void printMessage(String... message) {
        System.out.println("    " + HORIZONTAL_LINE);
        for (String s : message) {
            System.out.println("     " + s.replace("\n", "\n     "));
        }
        System.out.println("    " + HORIZONTAL_LINE + "\n");
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
        hasTasklistChanged = true;
        printTask("Added:", task);
    }

    private static void markTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? String.format("Task #%d is already done (no changes made).", index+1)
                : "Marked as done:";
        task.markAsDone();
        hasTasklistChanged = true;
        printMessage(message, "   " + tasks.get(index).toString());
    }

    private static void unmarkTask(int index) {
        Task task = tasks.get(index);
        String message = task.isDone()
                ? "Marked as not done:"
                : String.format("Task #%d has not been done (no changes made).", index+1);
        task.unmarkAsDone();
        hasTasklistChanged = true;
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
        // Regex matches a String of the form "{A} /by {B}", where A and B each contain at least
        // one non-whitespace character
        Matcher matcher = Pattern.compile("^(.*\\S.*) /by (.*\\S.*)$").matcher(arg);
        if (!matcher.find()) {
            throw new InvalidArgumentException(
                    "Deadline commands should be formatted as \"deadline [description] /by [due date/time]\"");
        }
        Deadline deadline = new Deadline(matcher.group(1), matcher.group(2));
        printAndAddTask(deadline);
    }

    private static void addEvent(String arg) {
        // Regex matches a String of the form "{A} /from {B} /to {C}", where A, B, and C each
        // contain at least one non-whitespace character
        Matcher matcher = Pattern.compile("^(.*\\S.*) /from (.*\\S.*) /to (.*\\S.*)$").matcher(arg);
        if (!matcher.find()) {
            throw new InvalidArgumentException(
                    "Event commands should be formatted as \"event [description] /from [start] /to [end]\"");
        }
        Event event = new Event(matcher.group(1), matcher.group(2), matcher.group(3));
        printAndAddTask(event);
    }

    private static void deleteTask(int index) {
        Task task = tasks.remove(index);
        hasTasklistChanged = true;
        printTask("Deleted the following task:", task);
    }

    /**
     * Processes user commands until they enter "bye".
     */
    private static void inputLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            hasTasklistChanged = false;

            String input = scanner.nextLine().trim();
            String[] words = input.split(" ", 2);
            String command = words[0].toLowerCase();
            String arg = words.length > 1 ? words[1].trim() : "";

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

            if (hasTasklistChanged) {
                updateDataFile();
            }
        }
    }

    private static void updateDataFile() {
        try (FileWriter writer = new FileWriter(TASKS_FILE_PATH)) {
            for (Task task : tasks) {
                writer.write(task.getStorageText() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String loadDataFile() {
        String message = "";

        File dataFile = new File(TASKS_FILE_PATH);
        if (!dataFile.getParentFile().exists() && !dataFile.getParentFile().mkdirs()) {
            throw new RuntimeException("Unable to create data/user/ directory");
        }
        boolean fileExists;
        try {
            fileExists = !dataFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        tasks = new ArrayList<>(); // initialise empty carbon.task list
        if (fileExists) {
            try {
                Scanner scanner = new Scanner(dataFile);
                while (scanner.hasNextLine()) {
                    String firstLine = scanner.nextLine();
                    if (firstLine.isEmpty()) {
                        break;
                    }
                    char type = firstLine.charAt(0);
                    boolean isDone = scanner.nextLine().charAt(0) == '1';
                    String description = scanner.nextLine().trim();
                    Task task = switch (type) {
                        case 'T' -> new Todo(description);
                        case 'D' -> {
                            String dueBy = scanner.nextLine().trim();
                            yield new Deadline(description, dueBy);
                        }
                        case 'E' -> {
                            String from = scanner.nextLine().trim();
                            String to = scanner.nextLine().trim();
                            yield new Event(description, from, to);
                        }
                        default -> throw new InvalidFileFormatException();
                    };
                    if (isDone) {
                        task.markAsDone();
                    }
                    tasks.add(task);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IndexOutOfBoundsException | NoSuchElementException | InvalidFileFormatException e) {
                message += "The data file was corrupted. Its contents are ignored and will be reset.\n";
                tasks.clear();
                try {
                    // Clear tasks.txt contents
                    new FileWriter(TASKS_FILE_PATH).close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
        return message;
    }

    public static void main(String[] args) {
        String logo = """
                   ____           _
                  / ___|__ _ _ __| |__   ___  _ __
                 | |   / _` | '__| '_ \\ / _ \\| '_ \\
                 | |__| (_| | |  | |_) | (_) | | | |
                  \\____\\__,_|_|  |_.__/ \\___/|_| |_|""";
        String loadDataMessage = loadDataFile();
        printMessage(logo, loadDataMessage + "Hello! What can I do for you?");
        inputLoop();
        printMessage("Goodbye!");
    }
}
