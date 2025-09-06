package yoyo.exception;

public class InvalidFindException extends YoyoException {
    public InvalidFindException() {
        super("The user entered an incomplete command to find for tasks using keywords.");
    }
}
