import java.util.Scanner;

public class Yoyo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String separator = "=========================" +
                "===============================";
        String tab = "    ";

        System.out.println(tab + separator);
        System.out.println(tab + "Yo! The name's Yoyo.\n"
                + tab + "What can I do for you?");
        System.out.println(tab + separator);

        while (true) {
            String userPrompt = scanner.nextLine();

            if (!userPrompt.equals("bye")) {
                System.out.println(tab + separator);
                System.out.println(tab + userPrompt);
                System.out.println(tab + separator);
            } else {
                break;
            }
        }

        System.out.println(tab + separator);
        System.out.println(tab + "Aww, bye! See ya later.");
        System.out.println(tab + separator);
    }
}
