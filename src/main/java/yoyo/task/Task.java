package yoyo.task;

/**
 * Represents a task to be completed.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Instantiates a task which contains a task description and marks
     * the task as uncompleted.
     */
    public Task(String description) {
        this.description = description;
        isDone = false;
    }

    /**
     * Formats the completion status of the task.
     *
     * @return "X" if task is completed and " " if task is not completed.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " "; // mark done task with X
    }

    public String getDescription() {
        return description;
    }

    /**
     * Returns the completion status of the task.
     *
     * @return true if task is completed and false if task is not completed.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Sets the task as completed.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Sets the task as uncompleted.
     */
    public void unmarkAsDone() {
        isDone = false;
    }

    /**
     * Formats the task details into a string which will be used
     * for saving task data in the memory.txt file.
     */
    public String getSaveString() {
        return isDone + "|" + description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
