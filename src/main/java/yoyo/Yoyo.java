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
    private final Storage storage;
    private final TaskList trackedTasks;
    // Detects the user's command and performs the appropriate action with formatting
    private final Parser parser;
    // Indicates if the chatbot program has been terminated by the user
    private boolean isTerminated = false;

    /**
     * Yoyo class
     *
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
        return "Yo! The name's Yoyo.\nWhat can I do for you?";
    }

    /**
     * Generates a response for the user's chat message.
     *
     * @param userInput The user's inputted chat message from the GUI.
     * @return Yoyo's response to the user's input message.
     */
    public String getResponse(String userInput) {
        try {
            Command userCommand = parser.interpretCommand(userInput);
            String commandType = userCommand.getCommand();

            if (commandType.equals("bye")) {
                // Yoyo says bye before quitting
                isTerminated = true;
                return "Aww, bye! See ya later.";
            } else if (commandType.equals("list")) {
                // Yoyo lists out what it wrote in its list
                String response = "";
                for (int idx = 0; idx < this.numOfTasks(); idx++) {
                    response += (idx + 1) + ". " + trackedTasks.get(idx) + "\n";
                }
                return response;
            } else if (commandType.equals("mark")) {
                // Yoyo marks the task
                int taskNum = userCommand.getTaskNum();
                Task task = this.markAsDone(taskNum);
                return "Oh man, you're clearing them tasks like a pro!\nMarked it for you:\n   " + task;
            } else if (commandType.equals("unmark")) {
                // Yoyo unmarks the task
                int taskNum = userCommand.getTaskNum();
                Task task = this.unmarkAsDone(taskNum);
                return "Bruh... Alright fine, I won't judge!\nUnmarked it for you:\n   " + task;
            } else if (commandType.equals("delete")) {
                // Yoyo deletes the task from taskList
                int taskNum = userCommand.getTaskNum();
                Task task = this.removeTask(taskNum);
                return "Gotcha, it's gone! I've deleted this task:\n   " + task + "\nNow you have "
                        + this.numOfTasks() + " task(s) in the list.";
            } else if (commandType.equals("find")) {
                // Yoyo finds all the tasks in its list that matches its keywords
                String keyword = userCommand.getDescription();
                TaskList filteredTasks = trackedTasks.filterTasks(keyword);
                String response = "";
                for (int idx = 0; idx < filteredTasks.size(); idx++) {
                    response += (idx + 1) + ". " + filteredTasks.get(idx) + "\n";
                }
                return response;
            } else if (commandType.equals("todo")) {
                // Yoyo adds a new to-do to its list
                String description = userCommand.getDescription();
                Task task = this.addTask(description);
                return "Alright-y, I've added your task:\n   " + task + "\nNow you have "
                        + this.numOfTasks() + " task(s) in the list.";
            } else if (commandType.equals("deadline")) {
                // Yoyo adds a new deadline to its list
                String description = userCommand.getDescription();
                LocalDateTime byDate = userCommand.getDateOne();
                Task task = this.addTask(description, byDate);
                return "Alright-y, I've added your task:\n   " + task + "\nNow you have "
                        + this.numOfTasks() + " task(s) in the list.";
            } else if (commandType.equals("event")) {
                // Yoyo adds a new event to its list
                String description = userCommand.getDescription();
                LocalDateTime fromDate = userCommand.getDateOne();
                LocalDateTime toDate = userCommand.getDateTwo();
                Task task = this.addTask(description, fromDate, toDate);
                return "Alright-y, I've added your task:\n   " + task + "\nNow you have "
                        + this.numOfTasks() + " task(s) in the list.";
            } else {
                // The user's prompt is not preceded by a valid command
                throw new UnknownCommandException();
            }
        } catch (InvalidTaskException e) {
            return "Umm... You've entered an invalid task number.";
        } catch (UnknownCommandException e) {
            return "Umm... What did you just say? I genuinely don't understand...";
        } catch (InvalidToDoException e) {
            return "Umm... The description of a todo cannot be empty.";
        } catch (InvalidFindException e) {
            return "Umm... The keywords for the find command cannot be empty.";
        } catch (InvalidDeadlineException e) {
            return "Umm... The description and by date of a deadline cannot be empty.";
        } catch (InvalidEventException e) {
            return "Umm... Your prompt doesn't sound right. Check that the description, from-date and to-date "
                    + "of your event is not empty. Oh, and double check that your to-date happens "
                    + "after your from-date!";
        } catch (EditMemoryException e) {
            return "Uh-oh... My brain broke for a moment there. I had a problem keeping track of all your tasks."
                    + "You may find that some tasks are not properly saved when you reboot me...";
        } catch (DateTimeParseException e) {
            return "Umm... I didn't want to say this but you've entered the date and time in the wrong format"
                    + " or a date that doesn't exist!\nJust remember: yyyy-MM-dd HH:mm.";
        }
    }

    /** Indicates if the chatbot program has been terminated by the user's command 'bye'. */
    public boolean isTerminated() {
        return isTerminated;
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
}
