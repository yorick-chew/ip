public class MaxNumOfTasksException extends YoyoException {
    public MaxNumOfTasksException() {
        super("Unable to add more tasks to Yoyo as the user has reached" +
                "the maximum number of tasks trackable.");
    }
}
