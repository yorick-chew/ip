package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

import yoyo.exception.InvalidDeadlineException;
import yoyo.exception.InvalidEventException;
import yoyo.exception.InvalidFindException;
import yoyo.exception.InvalidTaskException;
import yoyo.exception.InvalidToDoException;
import yoyo.exception.UnknownCommandException;

/**
 * Parses through user inputs and interprets the corresponding command requested by the user.
 */
public class Parser {
    // No shortform bye command to prevent accidentally exiting the program
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String TODO_COMMAND = "todo";
    // No shortform delete command to prevent accidental deletion
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";

    private static final String SHORT_LIST_COMMAND = "l";
    private static final String SHORT_MARK_COMMAND = "m";
    private static final String SHORT_UNMARK_COMMAND = "um";
    private static final String SHORT_FIND_COMMAND = "f";
    private static final String SHORT_TODO_COMMAND = "t";
    private static final String SHORT_DEADLINE_COMMAND = "d";
    private static final String SHORT_EVENT_COMMAND = "e";


    private static final String BY_DELIMITER = " /by ";
    private static final String FROM_DELIMITER = " /from ";
    private static final String TO_DELIMITER = " /to ";

    private static final String INPUT_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    private final DateTimeFormatter parseFormatter = DateTimeFormatter
            .ofPattern(Parser.INPUT_DATETIME_FORMAT);

    /**
     * Interprets the given user command, userPrompt, and
     * produces a Command object that can be used by the
     * chatbot to quickly carry out actions.
     *
     * @param userPrompt The user's command.
     * @return A Command object with details about the command.
     * @throws InvalidToDoException If the userPrompt provides an incomplete
     *                              todo command.
     * @throws InvalidDeadlineException If the userPrompt provides an incomplete
     *                                  deadline command.
     * @throws InvalidEventException If the userPrompt provides an incomplete
     *                               event command.
     * @throws UnknownCommandException If the userPrompt provides an invalid command.
     * @throws InvalidTaskException If the userPrompt provides an incomplete
     *                              mark, unmark or delete command.
     */
    public Command interpretCommand(String userPrompt) throws InvalidToDoException,
            InvalidDeadlineException, InvalidEventException, UnknownCommandException,
            InvalidTaskException, InvalidFindException {
        Scanner commandScanner = new Scanner(userPrompt);
        try {
            String command = commandScanner.next();
            if (command.equals(Parser.BYE_COMMAND)) {
                return createBasicCommand(Parser.BYE_COMMAND);
            } else if (command.equals(Parser.LIST_COMMAND) || command.equals(Parser.SHORT_LIST_COMMAND)) {
                return createBasicCommand(Parser.LIST_COMMAND);
            } else if (command.equals(Parser.MARK_COMMAND) || command.equals(Parser.SHORT_MARK_COMMAND)) {
                return createNumberedCommand(Parser.MARK_COMMAND, commandScanner);
            } else if (command.equals(Parser.UNMARK_COMMAND) || command.equals(Parser.SHORT_UNMARK_COMMAND)) {
                return createNumberedCommand(Parser.UNMARK_COMMAND, commandScanner);
            } else if (command.equals(Parser.DELETE_COMMAND)) {
                return createNumberedCommand(Parser.DELETE_COMMAND, commandScanner);
            } else if (command.equals(Parser.FIND_COMMAND) || command.equals(Parser.SHORT_FIND_COMMAND)) {
                return createFindCommand(commandScanner);
            } else if (command.equals(Parser.TODO_COMMAND) || command.equals(Parser.SHORT_TODO_COMMAND)) {
                return createToDoCommand(commandScanner);
            } else if (command.equals(Parser.DEADLINE_COMMAND) || command.equals(Parser.SHORT_DEADLINE_COMMAND)) {
                return createDeadlineCommand(commandScanner);
            } else if (command.equals(Parser.EVENT_COMMAND) || command.equals(Parser.SHORT_EVENT_COMMAND)) {
                return createEventCommand(commandScanner);
            } else {
                // The user's prompt is not preceded by a valid command
                throw new UnknownCommandException();
            }
        } catch (NoSuchElementException e) {
            // The user inputted an empty command
            throw new UnknownCommandException();
        }
    }

    private Command createBasicCommand(String command) {
        return new Command(command);
    }

    private Command createNumberedCommand(String command, Scanner commandScanner) throws InvalidTaskException {
        try {
            int taskNum = commandScanner.nextInt();
            return new Command(command, taskNum);
        } catch (NoSuchElementException e) {
            throw new InvalidTaskException();
        }
    }

    private Command createFindCommand(Scanner commandScanner) throws InvalidFindException {
        try {
            String keyword = commandScanner.nextLine().substring(1);
            if (keyword.isEmpty()) {
                throw new InvalidFindException();
            }
            return new Command(Parser.FIND_COMMAND, keyword);
        } catch (NoSuchElementException e) {
            throw new InvalidFindException();
        }
    }

    private Command createToDoCommand(Scanner commandScanner) throws InvalidToDoException {
        try {
            String description = commandScanner.nextLine().substring(1);
            if (description.isEmpty()) {
                throw new InvalidToDoException();
            }
            return new Command(Parser.TODO_COMMAND, description);
        } catch (NoSuchElementException e) {
            throw new InvalidToDoException();
        }
    }

    private Command createDeadlineCommand(Scanner commandScanner) throws InvalidDeadlineException {
        try {
            commandScanner.useDelimiter(Parser.BY_DELIMITER);
            String description = commandScanner.next().substring(1);
            if (description.isEmpty()) {
                throw new InvalidDeadlineException();
            }
            commandScanner.reset();
            commandScanner.next();
            String by = commandScanner.nextLine().substring(1);
            if (by.isEmpty()) {
                throw new InvalidDeadlineException();
            }
            LocalDateTime byDate = LocalDateTime.parse(by, parseFormatter);
            return new Command(Parser.DEADLINE_COMMAND, description, byDate);
        } catch (NoSuchElementException e) {
            throw new InvalidDeadlineException();
        }
    }

    private Command createEventCommand(Scanner commandScanner) throws InvalidEventException {
        try {
            commandScanner.useDelimiter(Parser.FROM_DELIMITER);
            String description = commandScanner.next().substring(1);
            if (description.isEmpty()) {
                throw new InvalidEventException();
            }
            commandScanner.reset();
            commandScanner.next();
            commandScanner.useDelimiter(Parser.TO_DELIMITER);
            String from = commandScanner.next().substring(1);
            if (from.isEmpty()) {
                throw new InvalidEventException();
            }
            commandScanner.reset();
            commandScanner.next();
            String to = commandScanner.nextLine().substring(1);
            if (to.isEmpty()) {
                throw new InvalidEventException();
            }
            LocalDateTime fromDate = LocalDateTime.parse(from, parseFormatter);
            LocalDateTime toDate = LocalDateTime.parse(to, parseFormatter);
            return new Command(Parser.EVENT_COMMAND, description, fromDate, toDate);
        } catch (NoSuchElementException e) {
            throw new InvalidEventException();
        }
    }
}
