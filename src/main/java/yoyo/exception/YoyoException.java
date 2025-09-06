package yoyo.exception;

/**
 * Represents exceptions that are specific to the Yoyo chatbot.
 */
public class YoyoException extends Exception {
    public YoyoException(String errorMessage) {
        super(errorMessage);
    }
}
