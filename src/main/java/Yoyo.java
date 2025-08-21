import java.util.Scanner;

public class Yoyo {
    private static Task[] taskLst = new Task[100];
    private static int currIdx = 0;

    private static Task addTask(String description) {
        ToDo newToDo = new ToDo(description);
        taskLst[currIdx] = newToDo;
        currIdx++;
        return newToDo;
    }

    private static Task addTask(String description, String by) {
        Deadline newDeadline = new Deadline(description, by);
        taskLst[currIdx] = newDeadline;
        currIdx++;
        return newDeadline;
    }

    private static Task addTask(String description, String from, String to) {
        Event newEvent = new Event(description, from, to);
        taskLst[currIdx] = newEvent;
        currIdx++;
        return newEvent;
    }

    private static int numOfTasks() {
        return currIdx;
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
            String userPrompt = scanner.next();

            if (userPrompt.equals("bye")) {
                // Yoyo says bye before quitting
                System.out.println(tab + separator);
                System.out.println(tab + "Aww, bye! See ya later.");
                System.out.println(tab + separator);
                break;
            } else if (userPrompt.equals("list")) {
                // Yoyo lists out what it wrote in its list
                System.out.println(tab + separator);
                for (int idx = 0; idx < Yoyo.taskLst.length; idx++) {
                    if (idx < currIdx) {
                        System.out.println(tab + (idx + 1) + ". " + Yoyo.taskLst[idx]);
                    }
                }
                System.out.println(tab + separator);
            } else if (userPrompt.equals("mark")) {
                int taskIdx = scanner.nextInt() - 1;
                Yoyo.taskLst[taskIdx].markAsDone();
                System.out.println(tab + separator);
                System.out.println(tab + "Oh man, you're clearing them tasks" +
                                " like a pro!\n" + tab + "Marked it for you:\n" +
                                tab + "   " + Yoyo.taskLst[taskIdx]);
                System.out.println(tab + separator);
            } else if (userPrompt.equals("unmark")) {
                int taskIdx = scanner.nextInt() - 1;
                Yoyo.taskLst[taskIdx].unmarkAsDone();
                System.out.println(tab + separator);
                System.out.println(tab + "Bruh... Alright fine, I won't judge!\n" +
                                tab + "Unmarked it for you:\n" + tab + "   " +
                                Yoyo.taskLst[taskIdx]);
                System.out.println(tab + separator);
            } else if (userPrompt.equals("todo")) {
                // Yoyo adds a new to-do to its list
                String description = scanner.nextLine().substring(1);
                Task newTask = Yoyo.addTask(description);
                System.out.println(tab + separator);
                System.out.println(tab + "Alright-y, I've added your task:\n" +
                        tab + "   " + newTask + "\n" + tab + "Now you have "
                        + Yoyo.numOfTasks() + " tasks in the list.");
                System.out.println(tab + separator);
            } else if (userPrompt.equals("deadline")) {
                // Yoyo adds a new deadline to its list
                scanner.useDelimiter(" /by ");
                String description = scanner.next().substring(1);
                scanner.reset();
                scanner.next();
                String by = scanner.nextLine().substring(1);
                Task newTask = Yoyo.addTask(description, by);
                System.out.println(tab + separator);
                System.out.println(tab + "Alright-y, I've added your task:\n" +
                        tab + "   " + newTask + "\n" + tab + "Now you have "
                        + Yoyo.numOfTasks() + " tasks in the list.");
                System.out.println(tab + separator);
            } else if (userPrompt.equals("event")) {
                // Yoyo adds a new event to its list
                scanner.useDelimiter(" /from ");
                String description = scanner.next().substring(1);
                scanner.reset();
                scanner.next();
                scanner.useDelimiter(" /to ");
                String from = scanner.next().substring(1);
                scanner.reset();
                scanner.next();
                String to = scanner.nextLine().substring(1);
                Task newTask = Yoyo.addTask(description, from, to);
                System.out.println(tab + separator);
                System.out.println(tab + "Alright-y, I've added your task:\n" +
                        tab + "   " + newTask + "\n" + tab + "Now you have "
                        + Yoyo.numOfTasks() + " tasks in the list.");
                System.out.println(tab + separator);
            } else {
                // The user's prompt is not preceded by a valid command
                System.out.println(tab + separator);
                System.out.println(tab + "Umm... What did you just say? " +
                        "I genuinely don't\n" + tab + "understand...");
                System.out.println(tab + separator);
            }
        }
    }
}
