package yoyo;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    @Test
    public void interpretCommand_byeInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("bye");
        assertEquals("bye", command.getCommand());
    }

    @Test
    public void interpretCommand_listInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("list");
        assertEquals("list", command.getCommand());
    }

    @Test
    public void interpretCommand_shortListInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("l");
        assertEquals("list", command.getCommand());
    }

    @Test
    public void interpretCommand_markInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("mark 3");
        assertEquals("mark", command.getCommand());
        assertEquals(3, command.getTaskNum());
    }

    @Test
    public void interpretCommand_shortMarkInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("m 3");
        assertEquals("mark", command.getCommand());
        assertEquals(3, command.getTaskNum());
    }

    @Test
    public void interpretCommand_unmarkInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("unmark 4");
        assertEquals("unmark", command.getCommand());
        assertEquals(4, command.getTaskNum());
    }

    @Test
    public void interpretCommand_shortUnmarkInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("um 4");
        assertEquals("unmark", command.getCommand());
        assertEquals(4, command.getTaskNum());
    }

    @Test
    public void interpretCommand_deleteInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("delete 5");
        assertEquals("delete", command.getCommand());
        assertEquals(5, command.getTaskNum());
    }

    @Test
    public void interpretCommand_findInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("find books today");
        assertEquals("find", command.getCommand());
        assertEquals("books today", command.getDescription());
    }

    @Test
    public void interpretCommand_shortFindInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("f books today");
        assertEquals("find", command.getCommand());
        assertEquals("books today", command.getDescription());
    }

    @Test
    public void interpretCommand_todoInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("todo Important work");
        assertEquals("todo", command.getCommand());
        assertEquals("Important work", command.getDescription());
    }

    @Test
    public void interpretCommand_shortTodoInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand("t Important work");
        assertEquals("todo", command.getCommand());
        assertEquals("Important work", command.getDescription());
    }

    @Test
    public void interpretCommand_deadlineInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand(
                "deadline iP Week 3 /by 2025-08-29 16:00");
        assertEquals("deadline", command.getCommand());
        assertEquals("iP Week 3", command.getDescription());
        assertEquals(LocalDateTime.of(2025, 8, 29,
                16, 0), command.getDateOne());
    }

    @Test
    public void interpretCommand_shortDeadlineInput_success() throws Exception {
        Parser testParser = new Parser();
        Command command = testParser.interpretCommand(
                "d iP Week 3 /by 2025-08-29 16:00");
        assertEquals("deadline", command.getCommand());
        assertEquals("iP Week 3", command.getDescription());
        assertEquals(LocalDateTime.of(2025, 8, 29,
                16, 0), command.getDateOne());
    }

    @Test
    public void interpretCommand_eventInput_success() throws Exception {

        Parser testParser = new Parser();
        Command command = testParser.interpretCommand(
                "event iP Week 3 /from 2025-08-25 00:00 "
                        + "/to 2025-08-29 16:00");
        assertEquals("event", command.getCommand());
        assertEquals("iP Week 3", command.getDescription());
        assertEquals(LocalDateTime.of(2025, 8, 25,
                0, 0), command.getDateOne());
        assertEquals(LocalDateTime.of(2025, 8, 29,
                16, 0), command.getDateTwo());
    }

    @Test
    public void interpretCommand_shortEventInput_success() throws Exception {

        Parser testParser = new Parser();
        Command command = testParser.interpretCommand(
                "e iP Week 3 /from 2025-08-25 00:00 "
                        + "/to 2025-08-29 16:00");
        assertEquals("event", command.getCommand());
        assertEquals("iP Week 3", command.getDescription());
        assertEquals(LocalDateTime.of(2025, 8, 25,
                0, 0), command.getDateOne());
        assertEquals(LocalDateTime.of(2025, 8, 29,
                16, 0), command.getDateTwo());
    }

    @Test
    public void interpretCommand_unknownInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("blah");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an unknown command.", e.getMessage());
        }
    }

    @Test
    public void interpretCommand_invalidInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("mark");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an invalid number as parameter for"
                    + "the mark, unmark or delete command.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("mark ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an invalid number as parameter for"
                    + "the mark, unmark or delete command.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("unmark");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an invalid number as parameter for"
                    + "the mark, unmark or delete command.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("unmark ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an invalid number as parameter for"
                    + "the mark, unmark or delete command.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("delete");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an invalid number as parameter for"
                    + "the mark, unmark or delete command.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("delete ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an invalid number as parameter for"
                    + "the mark, unmark or delete command.", e.getMessage());
        }
    }

    @Test
    public void interpretCommand_invalidFindInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("find");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command to "
                    + "find for tasks using keywords.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("find ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command to "
                    + "find for tasks using keywords.", e.getMessage());
        }
    }

    @Test
    public void interpretCommand_invalidToDoInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("todo");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a todo.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("todo ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a todo.", e.getMessage());
        }
    }

    @Test
    public void interpretCommand_invalidDeadlineInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("deadline");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("deadline ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("deadline  ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("deadline /by");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand(
                    "deadline /by 2025-08-29 18:00");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("deadline x /by");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("deadline /by /by 2025-08-29 18:00");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create a deadline.", e.getMessage());
        }
    }
    @Test
    public void interpretCommand_invalidEventInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event  ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event /by 2025-08-29 16:00");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event /from");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event / from");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event /from /from /to 2025-08-29 16:00");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event x /from ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event x /from  ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event x /from 2025-08-29 16:00 /to");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand("event x /from 2025-08-29 16:00 /to ");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
    }

    @Test
    public void interpretCommand_eventFromAfterToInput_exceptionThrown() {
        try {
            Parser testParser = new Parser();
            Command command = testParser.interpretCommand(
                    "event /from 2025-08-29 16:00 /to 2025-08-29 15:00");
            fail();
        } catch (Exception e) {
            assertEquals("The user entered an incomplete command "
                    + "to create an event.", e.getMessage());
        }
    }
}
