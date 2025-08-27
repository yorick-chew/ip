public class EditMemoryException extends YoyoException {
    public EditMemoryException() {
        super("There was problem when editing or adding to memory.txt file to add" +
                "a task or mark a task.");
    }
}
