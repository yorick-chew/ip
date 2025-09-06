package yoyo.exception;

public class UnknownCommandException extends YoyoException {
    public UnknownCommandException() {
        super("The user entered an unknown command.");
    }
}
