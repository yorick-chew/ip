import java.util.Scanner;

public class Yoyo {
    private static String[] lst = new String[100];
    private static int currIdx = 0;

    private static void addToLst(String text) {
        lst[currIdx] = text;
        currIdx++;
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
            String userPrompt = scanner.nextLine();

            if (userPrompt.equals("bye")) {
                // Yoyo says bye before quitting
                System.out.println(tab + separator);
                System.out.println(tab + "Aww, bye! See ya later.");
                System.out.println(tab + separator);
                break;
            } else if (userPrompt.equals("list")) {
                // Yoyo lists out what it wrote in its list
                System.out.println(tab + separator);
                for (int idx = 0; idx < Yoyo.lst.length; idx++) {
                    if (idx < currIdx) {
                        System.out.println(tab + (idx + 1) + ". " + Yoyo.lst[idx]);
                    }
                }
                System.out.println(tab + separator);
            } else {
                // Yoyo adds the userPrompt to its list
                System.out.println(tab + separator);
                Yoyo.addToLst(userPrompt);
                System.out.println(tab + "added: " + userPrompt);
                System.out.println(tab + separator);
            }
        }
    }
}
