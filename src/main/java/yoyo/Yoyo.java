package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import yoyo.exception.EditMemoryException;
import yoyo.exception.InvalidDeadlineException;
import yoyo.exception.InvalidEventException;
import yoyo.exception.InvalidFindException;
import yoyo.exception.InvalidTaskException;
import yoyo.exception.InvalidToDoException;
import yoyo.exception.MissingMemoryException;
import yoyo.exception.UnknownCommandException;
import yoyo.task.Deadline;
import yoyo.task.Event;
import yoyo.task.Task;
import yoyo.task.ToDo;

/**
 * Manages a user's tasks (to-dos, deadlines and events)
 * through commands in the terminal.
 */
public class Yoyo {
    private static final String BYE_COMMAND = "bye";
    private static final String LIST_COMMAND = "list";
    private static final String MARK_COMMAND = "mark";
    private static final String UNMARK_COMMAND = "unmark";
    private static final String DELETE_COMMAND = "delete";
    private static final String FIND_COMMAND = "find";
    private static final String TODO_COMMAND = "todo";
    private static final String DEADLINE_COMMAND = "deadline";
    private static final String EVENT_COMMAND = "event";

    private final Storage storage;
    private final TaskList trackedTasks;
    // Detects the user's command and performs the appropriate action with formatting
    private final Parser parser;
    // Indicates if the chatbot program has been terminated by the user
    private boolean isTerminated = false;

    /**
     * Instantiates a chatbot that manages a user's tasks.
     *
     * @throws MissingMemoryException If there was an error accessing the memory file for saving
     *                                and retrieving saved data.
     */
    public Yoyo() throws MissingMemoryException {
        // Set up and check for chatbot memory
        storage = new Storage();
        parser = new Parser();
        trackedTasks = new TaskList(storage.load());
    }

    /** Returns Yoyo's welcome greeting when the user begins using Yoyo. */
    public String getGreeting() {
        return formatResponse("Yo! The name's Yoyo.", "What can I do for you?");
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param userInput The user's inputted chat message from the GUI.
     * @return Yoyo's response to the user's input message.
     */
    public String getResponse(String userInput) {
        assert userInput != null : "userInput should not be null";
        try {
            Command userCommand = parser.interpretCommand(userInput);
            return executeCommand(userCommand);
        } catch (InvalidTaskException e) {
            return formatResponse("Umm... You've entered an invalid task number.");
        } catch (UnknownCommandException e) {
            return formatResponse("Umm... What did you just say? I genuinely don't understand...");
        } catch (InvalidToDoException e) {
            return formatResponse("Umm... The description of a todo cannot be empty.");
        } catch (InvalidFindException e) {
            return formatResponse("Umm... The keywords for the find command cannot be empty.");
        } catch (InvalidDeadlineException e) {
            return formatResponse("Umm... The description and by date of a deadline cannot be empty.");
        } catch (InvalidEventException e) {
            return formatResponse("Umm... Your prompt doesn't sound right. Check that the description, "
                    + "from-date and to-date of your event is not empty. Oh, and double check that "
                    + "your to-date happens after your from-date!");
        } catch (EditMemoryException e) {
            return formatResponse("Uh-oh... My brain broke for a moment there. I had a problem "
                    + "keeping track of all your tasks. You may find that some tasks are not properly saved when "
                    + "you reboot me...");
        } catch (DateTimeParseException e) {
            return formatResponse("Umm... I didn't want to say this but you've entered the date "
                    + "and time in the wrong format or a date that doesn't exist!",
                    "Just remember: yyyy-MM-dd HH:mm.");
        }
    }

    /** Indicates if the chatbot program has been terminated by the user's command 'bye'. */
    public boolean isTerminated() {
        return isTerminated;
    }

    /** Executes the corresponding userCommand and generates Yoyo's response **/
    private String executeCommand(Command userCommand) throws InvalidTaskException, EditMemoryException,
            InvalidEventException, UnknownCommandException {
        String commandType = userCommand.getCommand();
        return switch (commandType) {
            case Yoyo.BYE_COMMAND -> executeByeCommand();
            case Yoyo.LIST_COMMAND -> executeListCommand();
            case Yoyo.MARK_COMMAND -> executeMarkCommand(userCommand);
            case Yoyo.UNMARK_COMMAND -> executeUnmarkCommand(userCommand);
            case Yoyo.DELETE_COMMAND -> executeDeleteCommand(userCommand);
            case Yoyo.FIND_COMMAND -> executeFindCommand(userCommand);
            case Yoyo.TODO_COMMAND -> executeToDoCommand(userCommand);
            case Yoyo.DEADLINE_COMMAND -> executeDeadlineCommand(userCommand);
            case Yoyo.EVENT_COMMAND -> executeEventCommand(userCommand);
            default -> throw new UnknownCommandException();
        };
    }

    private String executeByeCommand() {
        // Terminates the program
        isTerminated = true;
        return formatResponse("Aww, bye! See ya later.");
    }

    private String executeListCommand() {
        return getListedResponse(trackedTasks);
    }

    private String executeMarkCommand(Command userCommand) throws InvalidTaskException, EditMemoryException {
        // Yoyo marks the task
        int taskNum = userCommand.getTaskNum();
        Task task = markAsDone(taskNum);
        assert task != null : "Marked task should not be null";
        return formatResponse("Oh man, you're clearing them tasks like a pro!", "Marked it for you:",
                "   " + task);
    }

    private String executeUnmarkCommand(Command userCommand) throws InvalidTaskException, EditMemoryException {
        // Yoyo unmarks the task
        int taskNum = userCommand.getTaskNum();
        Task task = unmarkAsDone(taskNum);
        assert task != null : "Unmarked task should not be null";
        return formatResponse("Bruh... Alright fine, I won't judge!", "Unmarked it for you:",
                "   " + task);
    }

    private String executeDeleteCommand(Command userCommand) throws InvalidTaskException, EditMemoryException {
        // Yoyo deletes the task from taskList
        int taskNum = userCommand.getTaskNum();
        Task task = removeTask(taskNum);
        assert task != null : "Removed task should not be null";
        return formatResponse("Gotcha, it's gone! I've deleted this task:", "   " + task,
                "Now you have " + numOfTasks() + " task(s) in the list.");
    }

    private String executeFindCommand(Command userCommand) {
        // Yoyo finds all the tasks in its list that matches its keywords
        String keyword = userCommand.getDescription();
        TaskList filteredTasks = trackedTasks.filterTasks(keyword);
        assert keyword != null : "keyword should not be null";
        assert filteredTasks != null : "filteredTasks should not be null";
        return getListedResponse(filteredTasks);
    }

    private String executeToDoCommand(Command userCommand) throws EditMemoryException {
        // Yoyo adds a new to-do to its list
        String description = userCommand.getDescription();
        Task task = addTask(description);
        return formatResponse("Alright-y, I've added your task:", "   " + task, "Now you have "
                + numOfTasks() + " task(s) in the list.");
    }

    private String executeDeadlineCommand(Command userCommand) throws EditMemoryException {
        // Yoyo adds a new deadline to its list
        String description = userCommand.getDescription();
        LocalDateTime byDate = userCommand.getDateOne();
        Task task = addTask(description, byDate);
        return formatResponse("Alright-y, I've added your task:", "   " + task, "Now you have "
                + numOfTasks() + " task(s) in the list.");
    }

    private String executeEventCommand(Command userCommand) throws EditMemoryException, InvalidEventException {
        // Yoyo adds a new event to its list
        String description = userCommand.getDescription();
        LocalDateTime fromDate = userCommand.getDateOne();
        LocalDateTime toDate = userCommand.getDateTwo();
        Task task = addTask(description, fromDate, toDate);
        return formatResponse("Alright-y, I've added your task:", "   " + task, "Now you have "
                + numOfTasks() + " task(s) in the list.");
    }

    /** Formats the given list into Yoyo's response format **/
    private String getListedResponse(TaskList tasksToList) {
        // Yoyo lists out what it wrote in the list
        String response = "";
        for (int idx = 0; idx < tasksToList.size(); idx++) {
            response += (idx + 1) + ". " + tasksToList.get(idx) + "\n";
        }
        return response;
    }

    /** Formats each string in responseLines into its own line in Yoyo's response **/
    private String formatResponse(String... responseLines) {
        String response = "";
        for (String responseLine : responseLines) {
            response += responseLine + "\n";
        }
        return response;
    }

    private Task addTask(String description) throws EditMemoryException {
        assert description != null : "description should not be null";
        ToDo newToDo = new ToDo(description);
        trackedTasks.addTask(newToDo);
        storage.updateMemory(trackedTasks);
        return newToDo;
    }

    private Task addTask(String description, LocalDateTime by) throws EditMemoryException {
        assert description != null && by != null : "description and by should not be null";
        Deadline newDeadline = new Deadline(description, by);
        trackedTasks.addTask(newDeadline);
        storage.updateMemory(trackedTasks);
        return newDeadline;
    }

    private Task addTask(String description, LocalDateTime from, LocalDateTime to)
            throws EditMemoryException, InvalidEventException {
        assert description != null && from != null && to != null : "description, from and to should not be null";
        Event newEvent = new Event(description, from, to);
        trackedTasks.addTask(newEvent);
        storage.updateMemory(trackedTasks);
        return newEvent;
    }

    private Task removeTask(int taskNum) throws InvalidTaskException, EditMemoryException {
        Task task = trackedTasks.removeTask(taskNum);
        storage.updateMemory(trackedTasks);
        return task;
    }

    private Task markAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        Task task = trackedTasks.markAsDone(taskNum);
        storage.updateMemory(trackedTasks);
        return task;
    }

    private Task unmarkAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        Task task = trackedTasks.unmarkAsDone(taskNum);
        storage.updateMemory(trackedTasks);
        return task;
    }

    private int numOfTasks() {
        return trackedTasks.size();
    }
}
