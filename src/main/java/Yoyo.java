import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import java.io.IOException;
import java.io.File;
import java.io.FileWriter;

public class Yoyo {
    private ArrayList<Task> taskLst = new ArrayList<>();
    private String filePath = "data/memory.txt";

    public Yoyo() {
        // Set up and check for chatbot memory
        String separator = "=========================" +
                "===============================";
        String tab = "    ";
        try {
            File dataDir = new File("data");
            dataDir.mkdir();
            if (!(dataDir.exists())) {
                throw new MissingMemoryException();
            }
            File memory = new File(this.filePath);
            memory.createNewFile();

            // Fill up taskLst with saved memory
            Scanner memoryScanner = new Scanner(memory);
            memoryScanner.useDelimiter("\\|");
            while (memoryScanner.hasNextLine()) {
                String line = memoryScanner.nextLine();
                String[] taskInfo = line.split("\\|");
                String taskType = taskInfo[0];
                boolean isMarked = Boolean.parseBoolean(taskInfo[1]);
                String description = taskInfo[2];
                Task newTask;
                if (taskType.equals("T")) {
                    newTask = new ToDo(description);
                } else if (taskType.equals("D")) {
                    String by = taskInfo[3];
                    newTask = new Deadline(description, by);
                } else {
                    String from = taskInfo[3];
                    String to = taskInfo[4];
                    newTask = new Event(description, from, to);
                }
                if (isMarked) {
                    newTask.markAsDone();
                }
                this.taskLst.add(newTask);
            }
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
                        String taskString = this.markAsDone(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Oh man, you're clearing them tasks" +
                                " like a pro!\n" + tab + "Marked it for you:\n" +
                                tab + "   " + taskString);
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidTaskException();
                    }
                } else if (command.equals("unmark")) {
                    try {
                        int taskNum = commandScanner.nextInt();
                        String taskString = this.unmarkAsDone(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Bruh... Alright fine, I won't judge!\n" +
                                tab + "Unmarked it for you:\n" + tab + "   " +
                                taskString);
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidTaskException();
                    }
                } else if (command.equals("delete")) {
                    try {
                        int taskNum = commandScanner.nextInt();
                        String taskString = this.removeTask(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Gotcha, it's gone! I've deleted this " +
                                "task:\n" + tab + taskString + "\n" + tab + "Now you have "
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
                        String taskString = this.addTask(description);

                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + taskString + "\n" + tab + "Now you have "
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
                        String taskString = this.addTask(description, by);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + taskString + "\n" + tab + "Now you have "
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
                        String taskString = this.addTask(description, from, to);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + taskString + "\n" + tab + "Now you have "
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
                System.out.println(tab + "Umm... The description, from date " +
                        "and to date\n" + tab + "of an event cannot be empty.");
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
            }
        }
    }

    private void updateMemory() throws EditMemoryException {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            for (Task task : this.taskLst) {
                fw.write(task.getSaveString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new EditMemoryException();
        }
    }

    private String addTask(String description) throws EditMemoryException {
        ToDo newToDo = new ToDo(description);
        this.taskLst.add(newToDo);
        this.updateMemory();
        return newToDo.toString();
    }

    private String addTask(String description, String by) throws EditMemoryException {
        Deadline newDeadline = new Deadline(description, by);
        this.taskLst.add(newDeadline);
        this.updateMemory();
        return newDeadline.toString();
    }

    private String addTask(String description, String from, String to) throws EditMemoryException {
        Event newEvent = new Event(description, from, to);
        this.taskLst.add(newEvent);
        this.updateMemory();
        return newEvent.toString();
    }

    private String removeTask(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        Task task = this.taskLst.remove(taskIdx);
        this.updateMemory();
        return task.toString();
    }

    private String markAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        Task task = this.taskLst.get(taskIdx);
        task.markAsDone();
        this.updateMemory();
        return task.toString();
    }

    private String unmarkAsDone(int taskNum) throws InvalidTaskException, EditMemoryException {
        if (taskNum <= 0 || taskNum > this.numOfTasks()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        Task task = this.taskLst.get(taskIdx);
        task.unmarkAsDone();
        this.updateMemory();
        return task.toString();
    }

    private int numOfTasks() {
        return this.taskLst.size();
    }

    public static void main(String[] args) {
        new Yoyo().run();
    }
}
