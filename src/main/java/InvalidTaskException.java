public class InvalidTaskException extends YoyoException {
    public InvalidTaskException() {
        super("The user entered an invalid number to mark or unmark" +
                "a task.");
    }
}
