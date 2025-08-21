import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Yoyo {
    private static ArrayList<Task> taskLst = new ArrayList<>();

    private static String addTask(String description) {
        ToDo newToDo = new ToDo(description);
        taskLst.add(newToDo);
        return newToDo.toString();
    }

    private static String addTask(String description, String by) {
        Deadline newDeadline = new Deadline(description, by);
        taskLst.add(newDeadline);
        return newDeadline.toString();
    }

    private static String addTask(String description, String from, String to) {
        Event newEvent = new Event(description, from, to);
        taskLst.add(newEvent);
        return newEvent.toString();
    }

    private static String removeTask(int taskNum) throws InvalidTaskException {
        if (taskNum <= 0 || taskNum > Yoyo.numOfTasks()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        Task task = Yoyo.taskLst.remove(taskIdx);
        return task.toString();
    }

    private static String markAsDone(int taskNum) throws InvalidTaskException {
        if (taskNum <= 0 || taskNum > Yoyo.numOfTasks()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        Task task = Yoyo.taskLst.get(taskIdx);
        task.markAsDone();
        return task.toString();
    }

    private static String unmarkAsDone(int taskNum) throws InvalidTaskException {
        if (taskNum <= 0 || taskNum > Yoyo.numOfTasks()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        Task task = Yoyo.taskLst.get(taskIdx);
        task.unmarkAsDone();
        return task.toString();
    }

    private static int numOfTasks() {
        return Yoyo.taskLst.size();
    }

    public static void main(String[] args) {
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
                    for (int idx = 0; idx < Yoyo.numOfTasks(); idx++) {
                        System.out.println(tab + (idx + 1) + ". " + Yoyo.taskLst.get(idx));
                    }
                    System.out.println(tab + separator);
                } else if (command.equals("mark")) {
                    try {
                        int taskNum = commandScanner.nextInt();
                        String taskString = Yoyo.markAsDone(taskNum);
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
                        String taskString = Yoyo.unmarkAsDone(taskNum);
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
                        String taskString = Yoyo.removeTask(taskNum);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Gotcha, it's gone! I've deleted this " +
                                "task:\n" + tab + taskString + "\n" + tab + "Now you have "
                                + Yoyo.numOfTasks() + " tasks in the list.");
                        System.out.println(tab + separator);
                    } catch (NoSuchElementException e) {
                        throw new InvalidTaskException();
                    }
                } else if (command.equals("todo")) {
                    // Yoyo adds a new to-do to its list
                    try {
                        String description = commandScanner.nextLine().substring(1);
                        String taskString = Yoyo.addTask(description);
                        if (description.isEmpty()) {
                            throw new InvalidToDoException();
                        }
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + taskString + "\n" + tab + "Now you have "
                                + Yoyo.numOfTasks() + " tasks in the list.");
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
                        String taskString = Yoyo.addTask(description, by);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + taskString + "\n" + tab + "Now you have "
                                + Yoyo.numOfTasks() + " tasks in the list.");
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
                        String taskString = Yoyo.addTask(description, from, to);
                        System.out.println(tab + separator);
                        System.out.println(tab + "Alright-y, I've added your task:\n" +
                                tab + "   " + taskString + "\n" + tab + "Now you have "
                                + Yoyo.numOfTasks() + " tasks in the list.");
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
            }
        }
    }
}
