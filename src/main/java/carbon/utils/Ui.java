package carbon.utils;

import carbon.exceptions.InvalidArgumentException;
import carbon.exceptions.InvalidCommandException;
import carbon.task.TaskList;

/**
 * Ui manages interactions with the user.
 */
public class Ui {
    private final TaskList taskList;
    private String mostRecentCommand;
    private final String welcomeMessage;

    /**
     * Creates a new UI process.
     */
    public Ui() {
        this.taskList = new TaskList();
        welcomeMessage = Storage.loadDataFile(taskList) + "Hello! What can I do for you?";
    }

    private String formatError(String message) {
        mostRecentCommand = "error";
        return String.format("Oops! %s :(", message);
    }

    /**
     * Returns the reply to the user input.
     *
     * @param input User input.
     * @return Reply.
     */
    public String reply(String input) {
        String[] words = input.trim().split(" ", 2);
        mostRecentCommand = words[0].toLowerCase();
        String arg = words.length > 1 ? words[1].trim() : "";

        try {
            return switch (mostRecentCommand) {
                case "start" -> welcomeMessage;
                case "bye" -> "Goodbye!";
                case "list" -> taskList.listTasks();
                case "find" -> taskList.listTasks(arg);
                case "mark" -> taskList.markTask(Integer.parseInt(arg) - 1);
                case "unmark" -> taskList.unmarkTask(Integer.parseInt(arg) - 1);
                case "todo" -> taskList.addTodo(arg);
                case "deadline" -> taskList.addDeadline(arg);
                case "event" -> taskList.addEvent(arg);
                case "delete" -> taskList.deleteTask(Integer.parseInt(arg) - 1);
                case "sort" -> taskList.sortTasks();
                default -> throw new InvalidCommandException(
                        String.format("The command \"%s\" is not recognised", mostRecentCommand));
            };
        } catch (InvalidCommandException | InvalidArgumentException e) {
            assert !e.getMessage().isBlank() : "Exception should have a message";
            return formatError(e.getMessage());
        } catch (NumberFormatException e) {
            return formatError(String.format("I expected a single integer after \"%s\"", mostRecentCommand));
        } catch (IndexOutOfBoundsException e) {
            String message = taskList.isEmpty()
                    ? "You don't have any tasks!"
                    : "Tasks are numbered from 1 to " + taskList.size();
            return formatError(message);
        }
    }

    /**
     * Returns the most recent command as a String in lowercase.
     * The command is the first word of the user's message.
     * If user's message triggered an error, "error" is returned.
     * If no command has been entered, "start" is returned.
     *
     * @return Most recent command.
     */
    public String getMostRecentCommand() {
        return mostRecentCommand;
    }
}
