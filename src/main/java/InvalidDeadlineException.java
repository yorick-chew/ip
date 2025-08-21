public class InvalidDeadlineException extends YoyoException {
    public InvalidDeadlineException() {
        super("The user entered an incomplete command to create a deadline.");
    }
}
