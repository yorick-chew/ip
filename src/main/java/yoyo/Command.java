package yoyo;

import java.time.LocalDateTime;

/**
 * Details the command that should be carried out by the
 * chatbot.
 */
public class Command {
    private String command;
    private String description;
    private LocalDateTime dateOne;
    private LocalDateTime dateTwo;
    private int taskNum;

    /**
     * Instantiates either a bye, list or delete command depending
     * parameter.
     *
     * @param command A string that identifies the command.
     */
    public Command(String command) {
        this.command = command;
    }

    /**
     * Instantiates either a mark or unmark command depending
     * parameter.
     *
     * @param command A string that identifies the command.
     * @param taskNum The index of the selected task, starting from 1.
     */
    public Command(String command, int taskNum) {
        this.command = command;
        this.taskNum = taskNum;
    }

    /**
     * Instantiates a todo command.
     *
     * @param command A string that identifies the command.
     * @param description The details of the todo task.
     */
    public Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    /**
     * Instantiates a deadline command.
     *
     * @param command A string that identifies the command.
     * @param description The details of the deadline task.
     * @param by The due date and time of the deadline.
     */
    public Command(String command, String description, LocalDateTime by) {
        this.command = command;
        this.description = description;
        this.dateOne = by;
    }

    /**
     * Instantiates an event command.
     *
     * @param command A string that identifies the command.
     * @param description The details of the event task.
     * @param from The starting date and time of the event.
     * @param to The ending date and time of the event.
     */
    public Command(String command, String description, LocalDateTime from, LocalDateTime to) {
        this.command = command;
        this.description = description;
        this.dateOne = from;
        this.dateTwo = to;
    }

    public String getCommand() {
        return command;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getDateOne() {
        return dateOne;
    }

    public LocalDateTime getDateTwo() {
        return dateTwo;
    }
}
