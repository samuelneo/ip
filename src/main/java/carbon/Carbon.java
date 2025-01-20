package carbon;

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
     * Prints the list of tasks in between two horizontal lines.
     * The lines are indented by 4 spaces, and the tasks are indented by 5 spaces.
     */
    private static void printTasks() {
        String taskList = IntStream.range(0, tasks.size())
                .mapToObj(i -> (i+1) + ". " + tasks.get(i).toString())
                .collect(Collectors.joining("\n"));
        printMessage("Tasks:", taskList);
    }

    private static void printAndAddTask(Task task) {
        tasks.add(task);
        boolean pluralise = tasks.size() != 1;
        printMessage("Added: ",
                "   " + task.toString(),
                String.format("You now have %d task%s.", tasks.size(), pluralise ? "s" : ""));
    }

    private static void addTodo(String arg) {
        Todo todo = new Todo(arg);
        printAndAddTask(todo);
    }

    private static void addDeadline(String arg) {
        String[] args = arg.split(" /by ");
        Deadline deadline = new Deadline(args[0], args[1]);
        printAndAddTask(deadline);
    }

    private static void addEvent(String arg) {
        String[] args = arg.split(" /(from|to) ");
        Event event = new Event(args[0], args[1], args[2]);
        printAndAddTask(event);
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

            switch (command) {
                case "bye" -> {
                    return;
                }
                case "list" -> printTasks();
                case "mark" -> {
                    int index = Integer.parseInt(arg) - 1;
                    tasks.get(index).markAsDone();
                    printMessage("Marked as done:", tasks.get(index).toString());
                }
                case "unmark" -> {
                    int index = Integer.parseInt(arg) - 1;
                    tasks.get(index).markAsNotDone();
                    printMessage("Marked as not done:", tasks.get(index).toString());
                }
                case "todo" -> addTodo(arg);
                case "deadline" -> addDeadline(arg);
                case "event" -> addEvent(arg);
                default -> printMessage(String.format("Command \"%s\" not recognised.", command));
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
