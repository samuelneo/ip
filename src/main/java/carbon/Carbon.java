package carbon;

import carbon.task.Task;

import java.util.ArrayList;
import java.util.Scanner;

public class Carbon {
    private static final String horizontalLine = "_".repeat(60);

    private static ArrayList<Task> tasks;

    /**
     * Prints the message in between two horizontal lines.
     * The lines are indented by 4 spaces, and the message is indented by 5 spaces.
     *
     * @param message Message to be printed.
     */
    private static void printMessage(String message) {
        System.out.println("    " + horizontalLine);
        System.out.println("     " + message.replace("\n", "\n     "));
        System.out.println("    " + horizontalLine + "\n");
    }

    /**
     * Prints the list of tasks in between two horizontal lines.
     * The lines are indented by 4 spaces, and the tasks are indented by 5 spaces.
     */
    private static void printTasks() {
        System.out.println("    " + horizontalLine);
        System.out.println("     Tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.printf("     %d. %s\n", i+1, tasks.get(i));
        }
        System.out.println("    " + horizontalLine + "\n");
    }

    /**
     * Processes user commands until they enter "bye".
     */
    private static void inputLoop() {
        tasks = new ArrayList<>(); // initialise empty carbon.task list
        Scanner scanner = new Scanner(System.in);

        while (true) {
            String input = scanner.nextLine();
            switch (input) {
            case "bye":
                return;
            case "list":
                printTasks();
                break;
            default:
                tasks.add(new Task(input));
                printMessage("added: " + input);
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
