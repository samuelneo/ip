package carbon.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;

public class TaskListTest {
    @Test
    public void listTasks_noTasks_success() {
        TaskList taskList = new TaskList();
        assertEquals("You don't have any tasks! :)", taskList.listTasks());
    }

    @Test
    public void deleteTask_indexOutOfBounds_exceptionThrown() {
        try {
            new TaskList().deleteTask(1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            new TaskList().deleteTask(0);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            new TaskList().deleteTask(-1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
        try {
            TaskList taskList = new TaskList();
            taskList.add(new Todo("test"));
            taskList.deleteTask(2);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }
}
