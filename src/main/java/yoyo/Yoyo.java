package yoyo;

import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Manages a user's tasks (to-dos, deadlines and events)
 * through commands in the terminal.
 */
public class Yoyo {
    private final Storage storage;
    private TaskList trackedTasks;
    private Ui ui;

    /**
     * Yoyo class
     *
     * Instantiates a chatbot that manages a user's tasks.
     */
    public Yoyo() {
        // Set up and check for chatbot memory
        storage = new Storage();
        ui = new Ui();
        try {
            trackedTasks = new TaskList(storage.load());
        } catch (IOException | MissingMemoryException e) {
            String[] loadingErrorMessageLines = {"Uh-oh... I'm having amnesia! I can't save any past",
                    "or future data you give me because of a system problem.",
                    "I recommend that you reboot me and see if it fixes me!"};
            ui.displayBotMessage(loadingErrorMessageLines);
        }
    }

    /**
     * Runs the chatbot so that users can begin interacting with it.
     * Users can input commands to create, manage and delete tasks.
     */
    public void run() {
        // Begin running Yoyo's interactions
        ui.displayBotMessage(new String[]{"Yo! The name's Yoyo.", "What can I do for you?"});

        // Detects the user's command and performs the
        // appropriate action with formatting
        Parser parser = new Parser();
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                String inputLine = scanner.nextLine();
                Command userCommand = parser.interpretCommand(inputLine);
                String commandType = userCommand.getCommand();

                if (commandType.equals("bye")) {
                    // Yoyo says bye before quitting
                    ui.displayBotMessage(new String[]{"Aww, bye! See ya later."});
                    break;
                } else if (commandType.equals("list")) {
                    // Yoyo lists out what it wrote in its list
                    String[] tasksToPrint = new String[this.numOfTasks()];
                    for (int idx = 0; idx < this.numOfTasks(); idx++) {
                        tasksToPrint[idx] = (idx + 1) + ". " + trackedTasks.get(idx);
                    }
                    ui.displayBotMessage(tasksToPrint);
                } else if (commandType.equals("mark")) {
                    // Yoyo marks the task
                    int taskNum = userCommand.getTaskNum();
                    Task task = this.markAsDone(taskNum);
                    ui.displayBotMessage(new String[]{"Oh man, you're clearing them tasks like a pro!",
                            "Marked it for you:", "   " + task});
                } else if (commandType.equals("unmark")) {
                    // Yoyo unmarks the task
                    int taskNum = userCommand.getTaskNum();
                    Task task = this.unmarkAsDone(taskNum);
                    ui.displayBotMessage(new String[]{"Bruh... Alright fine, I won't judge!",
                            "Unmarked it for you:", "   " + task});
                } else if (commandType.equals("delete")) {
                    // Yoyo deletes the task from taskList
                    int taskNum = userCommand.getTaskNum();
                    Task task = this.removeTask(taskNum);
                    ui.displayBotMessage(new String[]{"Gotcha, it's gone! I've deleted this task:",
                    task.toString(), "Now you have " + this.numOfTasks() + " tasks in the list."});
                } else if (commandType.equals("todo")) {
                    // Yoyo adds a new to-do to its list
                    String description = userCommand.getDescription();
                    Task task = this.addTask(description);
                    ui.displayBotMessage(new String[]{"Alright-y, I've added your task:",
                            "   " + task, "Now you have " + this.numOfTasks() + " tasks in the list."});
                } else if (commandType.equals("deadline")) {
                    // Yoyo adds a new deadline to its list
                    String description = userCommand.getDescription();
                    LocalDateTime byDate = userCommand.getDateOne();
                    Task task = this.addTask(description, byDate);
                    ui.displayBotMessage(new String[]{"Alright-y, I've added your task:",
                            "   " + task, "Now you have " + this.numOfTasks() + " tasks in the list."});
                } else if (commandType.equals("event")) {
                    // Yoyo adds a new event to its list
                    String description = userCommand.getDescription();
                    LocalDateTime fromDate = userCommand.getDateOne();
                    LocalDateTime toDate = userCommand.getDateTwo();
                    Task task = this.addTask(description, fromDate, toDate);
                    ui.displayBotMessage(new String[]{"Alright-y, I've added your task:",
                            "   " + task, "Now you have " + this.numOfTasks() + " tasks in the list."});
                } else {
                    // The user's prompt is not preceded by a valid command
                    throw new UnknownCommandException();
                }
            } catch (InvalidTaskException e) {
                ui.displayBotMessage(new String[]{"Umm... You've entered an invalid task number."});
            } catch (UnknownCommandException e) {
                ui.displayBotMessage(new String[]{"Umm... What did you just say? I genuinely don't",
                        "understand..."});
            } catch (InvalidToDoException e) {
                ui.displayBotMessage(new String[]{"Umm... The description of a todo "
                        + "cannot be empty."});
            } catch (InvalidDeadlineException e) {
                ui.displayBotMessage(new String[]{"Umm... The description and by date "
                        + "of a deadline", "cannot be empty."});
            } catch (InvalidEventException e) {
                ui.displayBotMessage(new String[]{"Umm... Your prompt doesn't sound right. Check "
                        + "that the description,", "from-date and to-date of your event is not empty.",
                        "Oh, and double check that your to-date happens after your from-date!"});
            } catch (EditMemoryException e) {
                ui.displayBotMessage(new String[]{"Uh-oh... My brain broke for a moment there.",
                        "I had a problem keeping track of all your tasks.", "You may find that "
                        + "some tasks are not properly saved", "when you reboot me..."});
            } catch (DateTimeParseException e) {
                ui.displayBotMessage(new String[]{"Umm... I didn't want to say this but "
                        + "you've entered", "the date and time in the wrong format or a date",
                        "that doesn't exist!", "Just remember: yyyy-MM-dd HH:mm."});
            }
        }
    }

    private Task addTask(String description) throws EditMemoryException {
        ToDo newToDo = new ToDo(description);
        trackedTasks.addTask(newToDo);
        storage.updateMemory(trackedTasks);
        return newToDo;
    }

    private Task addTask(String description, LocalDateTime by) throws EditMemoryException {
        Deadline newDeadline = new Deadline(description, by);
        trackedTasks.addTask(newDeadline);
        storage.updateMemory(trackedTasks);
        return newDeadline;
    }

    private Task addTask(String description, LocalDateTime from, LocalDateTime to)
            throws EditMemoryException, InvalidEventException {
        Event newEvent = new Event(description, from, to);
        trackedTasks.addTask(newEvent);
        storage.updateMemory(trackedTasks);
        return newEvent;
    }

    private Task removeTask(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        Task task = trackedTasks.removeTask(taskNum);
        storage.updateMemory(trackedTasks);
        return task;
    }

    private Task markAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        Task task = trackedTasks.markAsDone(taskNum);
        storage.updateMemory(trackedTasks);
        return task;
    }

    private Task unmarkAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        Task task = trackedTasks.unmarkAsDone(taskNum);
        storage.updateMemory(trackedTasks);
        return task;
    }

    private int numOfTasks() {
        return trackedTasks.size();
    }

    public static void main(String[] args) {
        new Yoyo().run();
    }
}
