package yoyo.exception;

public class MissingMemoryException extends YoyoException {
    public MissingMemoryException() {
        super("The memory.txt file for the chatbot is missing or inaccessible"
                + "because of a missing data/ directory.");
    }
}
