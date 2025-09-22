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
    public void parseTask_typicalToDoInputLine_success() {
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
    public void parseTask_typicalDeadlineInputLine_success() {
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
    public void parseTask_typicalEventInputLine_success() throws Exception {
        Storage testStorage = new Storage();
        Task parsedTask = testStorage.parseTask("E|true|Networking Event @ NUS|2025-02-01 "
                + "16:00|2025-02-01 18:00");
        LocalDateTime dateFrom = LocalDateTime.of(2025, 2, 1, 16, 0);
        LocalDateTime dateTo = LocalDateTime.of(2025, 2, 1, 18, 0);
        Event newEvent = new Event("Networking Event @ NUS", dateFrom, dateTo);
        newEvent.markAsDone();

        assertEquals(newEvent, parsedTask);
    }

    @Test
    public void parseTask_emptyInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown task type parsed.", e.getMessage());
        }
    }

    @Test
    public void parseTask_unformattedInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("todo");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown task type parsed.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("hello there");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown task type parsed.", e.getMessage());
        }
    }

    @Test
    public void parseTask_invalidTaskTypeInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("Q|false|Water Plants");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown task type parsed.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("M|false|Career Communication Workshop|2025-10-03 13:00|2025-10-03 15:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown task type parsed.", e.getMessage());
        }
    }

    @Test
    public void parseTask_invalidIsMarkedBooleanInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("T|f|Water Plants");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown isMarked boolean parsed.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("E|0|Career Communication Workshop|2025-10-03 13:00|2025-10-03 15:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Unknown isMarked boolean parsed.", e.getMessage());
        }
    }

    @Test
    public void parseTask_emptyDescriptionInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("T|false|");
            fail();
        } catch (YoyoException e) {
            assertEquals("Empty description parsed.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("D|false||");
            fail();
        } catch (YoyoException e) {
            assertEquals("Empty description parsed.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("E|true||2025-10-03 13:00|2025-10-03 15:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Empty description parsed.", e.getMessage());
        }
    }

    @Test
    public void parseTask_wrongDateTimeFormatInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("D|false|Complete calculus tutorial sheet|2025-9-3 13:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Datetime parsed is in the wrong format.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("E|false|Career Communication Workshop|2025-10-03 13:00|2025-10-04");
            fail();
        } catch (YoyoException e) {
            assertEquals("Datetime parsed is in the wrong format.", e.getMessage());
        }
    }

    @Test
    public void parseTask_incorrectNumOfParametersToDoInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("T|false|Complete calculus tutorial sheet|2025-10-03 13:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Wrong number of segments for a todo task.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("T|false|Career Communication Workshop|2025-10-03 13:00|2025-10-03 15:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Wrong number of segments for a todo task.", e.getMessage());
        }
    }

    @Test
    public void parseTask_incorrectNumOfParametersDeadlineInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("D|false|Water Plants");
            fail();
        } catch (YoyoException e) {
            assertEquals("Wrong number of segments for a deadline task.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("D|false|Career Communication Workshop|2025-10-03 13:00|2025-10-03 15:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Wrong number of segments for a deadline task.", e.getMessage());
        }
    }

    @Test
    public void parseTask_incorrectNumOfParametersEventInputLine_exceptionThrown() {
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("E|false|Water Plants");
            fail();
        } catch (YoyoException e) {
            assertEquals("Wrong number of segments for an event task.", e.getMessage());
        }
        try {
            Storage testStorage = new Storage();
            testStorage.parseTask("E|false|Complete calculus tutorial sheet|2025-10-03 13:00");
            fail();
        } catch (YoyoException e) {
            assertEquals("Wrong number of segments for an event task.", e.getMessage());
        }
    }


}
