import java.util.NoSuchElementException;
import java.util.Scanner;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.format.DateTimeFormatter;

public class Yoyo {
    private Storage storage;
    private TaskList taskLst;
    private DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Yoyo() {
        // Set up and check for chatbot memory
        String separator = "=========================" +
                "===============================";
        String tab = "    ";
        this.storage = new Storage();
        try {
            this.taskLst = new TaskList(storage.load());
        } catch (IOException | MissingMemoryException e) {
            System.out.println(tab + separator);
            System.out.println(tab + "Uh-oh... I'm having amnesia! I can't save " +
                    "any past\n" + tab + "or future data you give me because of a " +
                    "system problem.\n" + tab + "I recommend that you reboot me and " +
                    "see if it fixes me!");
            System.out.println(tab + separator);
        }
    }

    public void run() {
        // Begin running Yoyo's interactions
        Scanner scanner = new Scanner(System.in);
        String separator = "=========================" +
                "===============================";
        String tab = "    ";

        System.out.println(tab + separator);
        System.out.println(tab + "Yo! The name's Yoyo.\n"
                + tab + "What can I do for you?");
        System.out.println(tab + separator);

        // Detects the user's command and performs the
        // appropriate action with formatting
        while (true) {
            try {
                String userPrompt = scanner.nextLine();
                Scanner commandScanner = new Scanner(userPrompt);

                String command = commandScanner.next();

                if (command.equals("bye")) {
                    // Yoyo says bye before quitting
                    System.out.println(tab + separator);
                    System.out.println(tab + "Aww, bye! See ya later.");
                    System.out.println(tab + separator);
                    break;
                } else if (command.equals("list")) {
                    // Yoyo lists out what it wrote in its list
                    System.out.println(tab + separator);
                    for (int idx = 0; idx < this.numOfTasks(); idx++) {
                        System.out.println(tab + (idx + 1) + ". " + this.taskLst.get(idx));
                    }
                    System.out.println(tab + separator);
                } else if (command.equals("mark")) {
                    try {
                        int taskNum = commandScanner.nextInt();
                        Task task = this.markAsDone(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Oh man, you're clearing them tasks" +
                                " like a pro!\n" + tab + "Marked it for you:\n" +
                                tab + "   " + task);
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidTaskException();
                    }
                } else if (command.equals("unmark")) {
                    try {
                        int taskNum = commandScanner.nextInt();
                        Task task = this.unmarkAsDone(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Bruh... Alright fine, I won't judge!\n" +
                                tab + "Unmarked it for you:\n" + tab + "   " +
                                task);
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidTaskException();
                    }
                } else if (command.equals("delete")) {
                    try {
                        int taskNum = commandScanner.nextInt();
                        Task task = this.removeTask(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Gotcha, it's gone! I've deleted this " +
                                "task:\n" + tab + task + "\n" + tab + "Now you have "
                                + this.numOfTasks() + " tasks in the list.");
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidTaskException();
                    }
                } else if (command.equals("todo")) {
                    // Yoyo adds a new to-do to its list
                    try {
                        String description = commandScanner.nextLine().substring(1);
                        if (description.isEmpty()) {
                            throw new InvalidToDoException();
                        }
                        Task task = this.addTask(description);

                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + task + "\n" + tab + "Now you have "
                                + this.numOfTasks() + " tasks in the list.");
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidToDoException();
                    }
                } else if (command.equals("deadline")) {
                    // Yoyo adds a new deadline to its list
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
                        LocalDateTime byDate = LocalDateTime.parse(by, this.parseFormatter);
                        Task task = this.addTask(description, byDate);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + task + "\n" + tab + "Now you have "
                                + this.numOfTasks() + " tasks in the list.");
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidDeadlineException();
                    }
                } else if (command.equals("event")) {
                    // Yoyo adds a new event to its list
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
                        LocalDateTime fromDate = LocalDateTime.parse(from, this.parseFormatter);
                        LocalDateTime toDate = LocalDateTime.parse(to, this.parseFormatter);
                        Task task = this.addTask(description, fromDate, toDate);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + task + "\n" + tab + "Now you have "
                                + this.numOfTasks() + " tasks in the list.");
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidEventException();
                    }
                } else {
                    // The user's prompt is not preceded by a valid command
                    throw new UnknownCommandException();
                }
            } catch (InvalidTaskException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... You've entered an invalid task " +
                        "number.");
                System.out.println(tab + separator);
            } catch (UnknownCommandException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... What did you just say? " +
                        "I genuinely don't\n" + tab + "understand...");
                System.out.println(tab + separator);
            } catch (InvalidToDoException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... The description of a " +
                        "todo cannot be empty.");
                System.out.println(tab + separator);
            } catch (InvalidDeadlineException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... The description and " +
                        "by date of a deadline\n" + tab + "cannot be empty.");
                System.out.println(tab + separator);
            } catch (InvalidEventException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... Your prompt doesn't sound right. " +
                        "Check that the description,\n" + tab + "from-date and to-date " +
                        "of your event is not empty.\n" + tab + "Oh, and double check that your " +
                        "to-date happens after your from-date!");
                System.out.println(tab + separator);
            } catch (NoSuchElementException e) {
                // User pressed enter without typing command. Wait for next
                // command and do nothing
            } catch (EditMemoryException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Uh-oh... My brain broke for a moment " +
                        "there.\n" + tab + "I had a problem keeping track of all " +
                        "your tasks.\n" + tab + "You may find that some tasks are not " +
                        "properly saved\n" + tab + "when you reboot me...");
                System.out.println(tab + separator);
            } catch (DateTimeParseException e) {
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... I didn't want to say this but you've " +
                        "entered\n" + tab + "the date and time in the wrong format or a date\n" +
                        tab + "that doesn't exist!\n" + tab + "Just remember: yyyy-MM-dd HH:mm.");
                System.out.println(tab + separator);
            }
        }
    }

    private Task addTask(String description) throws EditMemoryException {
        ToDo newToDo = new ToDo(description);
        this.taskLst.addTask(newToDo);
        this.storage.updateMemory(this.taskLst);
        return newToDo;
    }

    private Task addTask(String description, LocalDateTime by) throws EditMemoryException {
        Deadline newDeadline = new Deadline(description, by);
        this.taskLst.addTask(newDeadline);
        this.storage.updateMemory(this.taskLst);
        return newDeadline;
    }

    private Task addTask(String description, LocalDateTime from, LocalDateTime to) throws EditMemoryException, InvalidEventException {
        Event newEvent = new Event(description, from, to);
        this.taskLst.addTask(newEvent);
        this.storage.updateMemory(this.taskLst);
        return newEvent;
    }

    private Task removeTask(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        Task task = this.taskLst.removeTask(taskNum);
        this.storage.updateMemory(this.taskLst);
        return task;
    }

    private Task markAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        Task task = this.taskLst.markAsDone(taskNum);
        this.storage.updateMemory(this.taskLst);
        return task;
    }

    private Task unmarkAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        Task task = this.taskLst.unmarkAsDone(taskNum);
        this.storage.updateMemory(this.taskLst);
        return task;
    }

    private int numOfTasks() {
        return this.taskLst.size();
    }

    public static void main(String[] args) {
        new Yoyo().run();
    }
}
