import java.time.LocalDateTime;

public class Command {
    private String command;
    private String description;
    private LocalDateTime dateOne;
    private LocalDateTime dateTwo;
    private int taskNum;

    // For bye, list, delete command
    public Command(String command) {
        this.command = command;
    }

    // For mark and unmark command
    public Command(String command, int taskNum) {
        this.command = command;
        this.taskNum = taskNum;
    }

    // For todo command
    public Command(String command, String description) {
        this.command = command;
        this.description = description;
    }

    // For deadline command
    public Command(String command, String description, LocalDateTime by) {
        this.command = command;
        this.description = description;
        this.dateOne = by;
    }

    // For event command
    public Command(String command, String description, LocalDateTime from, LocalDateTime to) {
        this.command = command;
        this.description = description;
        this.dateOne = from;
        this.dateTwo = to;
    }

    public String getCommand() {
        return this.command;
    }

    public int getTaskNum() {
        return this.taskNum;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalDateTime getDateOne() {
        return this.dateOne;
    }

    public LocalDateTime getDateTwo() {
        return this.dateTwo;
    }
}
