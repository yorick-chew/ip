package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Parser {
    private final DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        } else if (command.equals("mark") || command.equals("unmark") || command.equals("delete")) {
            int taskNum;
            try {
                taskNum = commandScanner.nextInt();
            } catch (NoSuchElementException e) {
                throw new InvalidTaskException();
            }
            return new Command(command, taskNum);
        } else if (command.equals("todo")) {
            String description;
            try {
                description = commandScanner.nextLine().substring(1);
                if (description.isEmpty()) {
                    throw new InvalidToDoException();
                }
            } catch (NoSuchElementException e) {
                throw new InvalidToDoException();
            }
            return new Command("todo", description);
        } else if (command.equals("deadline")) {
            String description;
            LocalDateTime byDate;
            try {
                commandScanner.useDelimiter(" /by ");
                description = commandScanner.next().substring(1);
                if (description.isEmpty()) {
                    throw new InvalidDeadlineException();
                }
                commandScanner.reset();
                commandScanner.next();
                String by = commandScanner.nextLine().substring(1);
                if (by.isEmpty()) {
                    throw new InvalidDeadlineException();
                }
                byDate = LocalDateTime.parse(by, this.parseFormatter);
            } catch (NoSuchElementException e) {
                throw new InvalidDeadlineException();
            }
            return new Command(command, description, byDate);
        } else if (command.equals("event")) {
            String description;
            LocalDateTime fromDate;
            LocalDateTime toDate;
            try {
                commandScanner.useDelimiter(" /from ");
                description = commandScanner.next().substring(1);
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
                fromDate = LocalDateTime.parse(from, this.parseFormatter);
                toDate = LocalDateTime.parse(to, this.parseFormatter);
            } catch (NoSuchElementException e) {
                throw new InvalidEventException();
            }
            return new Command(command, description, fromDate, toDate);
        } else {
            // The user's prompt is not preceded by a valid command
            throw new UnknownCommandException();
        }
    }
}
