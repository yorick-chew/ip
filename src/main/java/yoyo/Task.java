package yoyo;

public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " "); // mark done task with X
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void unmarkAsDone() {
        this.isDone = false;
    }

    public String getSaveString() {
        return Boolean.toString(this.isDone) + "|" + description;
    }

    public String toString() {
        return "[" + this.getStatusIcon() + "] " + description;
    }
}
