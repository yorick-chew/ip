package yoyo;

/**
 * Formats and prints the chatbot's responses.
 */
public class Ui {
    private static final String SEPARATOR = "============================================================";
    private static final String TAB = "    ";

    /**
     * Formats and prints the chatbot's messages which
     * will then be displayed for the user to see.
     *
     * @param messageLines The response provided by the chatbot
     *                to be formatted, with each array element
     *                representing the text to be printed on
     *                each line.
     */
    public void displayBotMessage(String[] messageLines) {
        System.out.println(TAB + SEPARATOR);
        for (String messageLine : messageLines) {
            System.out.println(TAB + messageLine);
        }
        System.out.println(TAB + SEPARATOR);
    }
}
