package carbon.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import carbon.exceptions.InvalidFileFormatException;
import carbon.task.Deadline;
import carbon.task.Event;
import carbon.task.Task;
import carbon.task.TaskList;
import carbon.task.Todo;

/**
 * Storage contains static methods that manage the storage of the TaskList.
 */
public class Storage {
    private static final String TASKS_FILE_PATH = "data/user/tasks.txt";

    /**
     * Writes the tasks into storage.
     *
     * @param tasks The list of tasks.
     */
    public static void updateDataFile(ArrayList<Task> tasks) {
        try (FileWriter writer = new FileWriter(TASKS_FILE_PATH)) {
            for (Task task : tasks) {
                writer.write(task.getStorageText() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads data from storage into the TaskList.
     *
     * @param taskList TaskList to load data into.
     * @return Message describing any abnormalities (empty if none).
     */
    public static String loadDataFile(TaskList taskList) {
        File dataFile = new File(TASKS_FILE_PATH);
        if (!dataFile.getParentFile().exists() && !dataFile.getParentFile().mkdirs()) {
            throw new RuntimeException("Unable to create data/user/ directory");
        }
        boolean hasFile;
        try {
            hasFile = !dataFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (!hasFile) {
            return "";
        }

        String message = "";

        try {
            readTasks(taskList, dataFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IndexOutOfBoundsException | NoSuchElementException | InvalidFileFormatException e) {
            message = "The data file was corrupted. Its contents are ignored and will be reset.\n";
            clearTasks(taskList);
        }
        return message;
    }

    private static void readTasks(TaskList taskList, File dataFile) throws FileNotFoundException {
        Scanner scanner = new Scanner(dataFile);
        while (scanner.hasNextLine()) {
            Task task = nextTask(scanner);
            if (task == null) {
                break;
            }
            taskList.add(task);
        }
    }

    private static Task nextTask(Scanner scanner) {
        String firstLine = scanner.nextLine();
        if (firstLine.isEmpty()) {
            return null;
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
        return task;
    }

    private static void clearTasks(TaskList taskList) {
        taskList.clear();
        try {
            // Clear tasks.txt contents
            new FileWriter(TASKS_FILE_PATH).close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }
}
