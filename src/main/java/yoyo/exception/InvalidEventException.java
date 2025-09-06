package yoyo.exception;

public class InvalidEventException extends YoyoException {
    public InvalidEventException() {
        super("The user entered an incomplete command "
                + "to create an event.");
    }
}
