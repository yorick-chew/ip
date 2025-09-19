package yoyo.task;

/**
 * Represents a task with a description.
 */
public class ToDo extends Task {
    /**
     * Instantiates a task that tracks the details of a
     * to-do.
     *
     * @param description Description details of the to-do.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the format used to save a ToDo in the
     * memory.txt file so that Yoyo will be able to retrieve it
     * when it restarts.
     *
     * @return Formatted string representing the ToDo.
     */
    @Override
    public String getSaveString() {
        return "T|" + super.getSaveString();
    }

    /**
     * Compares this ToDo object with another Object obj.
     * Returns true if obj is also a ToDo and shares the
     * same description as this ToDo object.
     *
     * @param obj The reference object with which to compare.
     * @return True if both objects are ToDos that share the same fields
     *         for their description.
     */
    @Override
    public boolean equals(Object obj) {
        // Solution below adapted from https://nus-cs2030s.github.io/2425-s1/14-polymorphism.html#the-equals-method
        if (obj instanceof ToDo) {
            ToDo toDo = (ToDo) obj;
            boolean isDescriptionEqual = toDo.getDescription().equals(this.getDescription());
            boolean isIsDoneEqual = (toDo.isDone() == this.isDone());
            return isDescriptionEqual && isIsDoneEqual;
        }
        return false;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
