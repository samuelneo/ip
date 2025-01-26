package carbon.utils;

import carbon.exceptions.InvalidArgumentException;
import carbon.exceptions.InvalidCommandException;
import carbon.task.TaskList;

import java.util.Scanner;

/**
 * Ui manages interactions with the user.
 */
public class Ui {
    private static final String LOGO = """
                   ____           _
                  / ___|__ _ _ __| |__   ___  _ __
                 | |   / _` | '__| '_ \\ / _ \\| '_ \\
                 | |__| (_| | |  | |_) | (_) | | | |
                  \\____\\__,_|_|  |_.__/ \\___/|_| |_|""";
    private static final String HORIZONTAL_LINE = "_".repeat(60);
    private final TaskList taskList;

    private Ui() {
        this.taskList = new TaskList();
    }

    /**
     * Starts a new Ui process.
     */
    public static void start() {
        new Ui().run();
    }

    private void run() {
        String loadDataMessage = Storage.loadDataFile(taskList);
        printMessage(LOGO, loadDataMessage + "Hello! What can I do for you?");
        inputLoop();
        printMessage("Goodbye!");
    }

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
     * Processes user commands until they enter "bye".
     */
    private void inputLoop() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine().trim();
            String[] words = input.split(" ", 2);
            String command = words[0].toLowerCase();
            String arg = words.length > 1 ? words[1].trim() : "";

            if (command.equals("bye")) {
                return;
            }

            try {
                // CHECKSTYLE.OFF: Indentation
                // Reason: checkstyle configuration does not support lambda-style switch statements
                String message = switch (command) {
                    case "list" -> taskList.listTasks();
                    case "mark" -> taskList.markTask(Integer.parseInt(arg) - 1);
                    case "unmark" -> taskList.unmarkTask(Integer.parseInt(arg) - 1);
                    case "todo" -> taskList.addTodo(arg);
                    case "deadline" -> taskList.addDeadline(arg);
                    case "event" -> taskList.addEvent(arg);
                    case "delete" -> taskList.deleteTask(Integer.parseInt(arg) - 1);
                    default -> throw new InvalidCommandException(
                            String.format("The command \"%s\" is not recognised", command));
                };
                // CHECKSTYLE.ON: Indentation
                printMessage(message);
            } catch (InvalidCommandException | InvalidArgumentException e) {
                printError(e.getMessage());
            } catch (NumberFormatException e) {
                printError(String.format("I expected a single integer after \"%s\"", command));
            } catch (IndexOutOfBoundsException e) {
                String message = taskList.isEmpty()
                        ? "You don't have any tasks!"
                        : "Tasks are numbered from 1 to " + taskList.size();
                printError(message);
            }
        }
    }
}
