package yoyo;

public class InvalidTaskException extends YoyoException {
    public InvalidTaskException() {
        super("The user entered an invalid number as parameter for" +
                "the mark, unmark or delete command.");
    }
}
