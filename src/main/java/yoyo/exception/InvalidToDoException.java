package yoyo.exception;

public class InvalidToDoException extends YoyoException {
    public InvalidToDoException() {
        super("The user entered an incomplete command to create a todo.");
    }
}
