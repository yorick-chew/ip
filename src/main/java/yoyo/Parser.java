package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Parses through the user's input in the command line and
 * interprets the corresponding command requested by the user.
 */
public class Parser {
    private final DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
            InvalidTaskException {
        Scanner commandScanner = new Scanner(userPrompt);
        String command;
        try {
            command = commandScanner.next();
        } catch (NoSuchElementException e) {
            throw new UnknownCommandException();
        }
        if (command.equals("bye")) {
            return new Command("bye");
        } else if (command.equals("list")) {
            return new Command("list");
        } else if (command.equals("mark") || command.equals("unmark")
                || command.equals("delete")) {
            try {
                int taskNum = commandScanner.nextInt();
                return new Command(command, taskNum);
            } catch (NoSuchElementException e) {
                throw new InvalidTaskException();
            }
        } else if (command.equals("todo")) {
            try {
                String description = commandScanner.nextLine().substring(1);
                if (description.isEmpty()) {
                    throw new InvalidToDoException();
                }
                return new Command("todo", description);
            } catch (NoSuchElementException e) {
                throw new InvalidToDoException();
            }
        } else if (command.equals("deadline")) {
            try {
                commandScanner.useDelimiter(" /by ");
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
                return new Command(command, description, byDate);
            } catch (NoSuchElementException e) {
                throw new InvalidDeadlineException();
            }
        } else if (command.equals("event")) {
            try {
                commandScanner.useDelimiter(" /from ");
                String description = commandScanner.next().substring(1);
                if (description.isEmpty()) {
                    throw new InvalidEventException();
                }
                commandScanner.reset();
                commandScanner.next();
                commandScanner.useDelimiter(" /to ");
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
                return new Command(command, description, fromDate, toDate);
            } catch (NoSuchElementException e) {
                throw new InvalidEventException();
            }
        } else {
            // The user's prompt is not preceded by a valid command
            throw new UnknownCommandException();
        }
    }
}
