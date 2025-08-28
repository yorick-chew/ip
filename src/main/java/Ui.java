public class Ui {
    private static final String SEPARATOR = "============================================================";
    private static final String TAB = "    ";

    public void displayBotMessage(String[] message) {
        System.out.println(TAB + SEPARATOR);
        for (String messageLine : message) {
            System.out.println(TAB + messageLine);
        }
        System.out.println(TAB + SEPARATOR);
    }
}
