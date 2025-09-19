package yoyo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import yoyo.exception.YoyoException;
import yoyo.task.Deadline;
import yoyo.task.Event;
import yoyo.task.Task;
import yoyo.task.ToDo;

public class StorageTest {
    @Test
    public void parseTask_typicalInputLineToDo_success() {
        Storage testStorage = new Storage();
        try {
            Task parsedTask = testStorage.parseTask("T|false|Try and finish iP on time");
            ToDo newToDo = new ToDo("Try and finish iP on time");

            assertEquals(newToDo, parsedTask);
        } catch (YoyoException e) {
            // Skip past this task as the task type is invalid, which will not happen here
        }
    }

    @Test
    public void parseTask_typicalInputLineDeadline_success() {
        Storage testStorage = new Storage();
        try {
            Task parsedTask = testStorage.parseTask("D|false|Submit e-learning homework|2026-05-01 11:00");
            LocalDateTime expectedByDate = LocalDateTime.of(2026, 5, 1, 11, 0);
            Deadline newDeadline = new Deadline("Submit e-learning homework", expectedByDate);

            assertEquals(newDeadline, parsedTask);
        } catch (YoyoException e) {
            // Skip past this task as the task type is invalid, which will not happen here
        }
    }

    @Test
    public void parseTask_typicalInputLineEvent_success() throws Exception {
        Storage testStorage = new Storage();
        Task parsedTask = testStorage.parseTask("E|true|Networking Event @ NUS|2025-02-01 "
                + "16:00|2025-02-01 18:00");
        LocalDateTime dateFrom = LocalDateTime.of(2025, 2, 1, 16, 0);
        LocalDateTime dateTo = LocalDateTime.of(2025, 2, 1, 18, 0);
        Event newEvent = new Event("Networking Event @ NUS", dateFrom, dateTo);
        newEvent.markAsDone();

        assertEquals(newEvent, parsedTask);
    }

}
