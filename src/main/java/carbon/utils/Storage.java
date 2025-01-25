package carbon.utils;

import carbon.exceptions.InvalidFileFormatException;
import carbon.task.Deadline;
import carbon.task.Event;
import carbon.task.Task;
import carbon.task.Todo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Storage {
    private static final String TASKS_FILE_PATH = "data/user/tasks.txt";

    public static void updateDataFile(ArrayList<Task> tasks) {
        try (FileWriter writer = new FileWriter(TASKS_FILE_PATH)) {
            for (Task task : tasks) {
                writer.write(task.getStorageText() + "\n");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String loadDataFile(TaskList taskList) {
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
                    taskList.add(task);
                }
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            } catch (IndexOutOfBoundsException | NoSuchElementException | InvalidFileFormatException e) {
                message += "The data file was corrupted. Its contents are ignored and will be reset.\n";
                taskList.clear();
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
}
