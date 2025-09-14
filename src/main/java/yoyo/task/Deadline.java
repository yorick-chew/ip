package yoyo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a due date and time.
 */
public class Deadline extends Task {
    private static final String DISPLAY_DATETIME_FORMAT = "MMM dd yyyy, h:mm a";
    private static final String STORAGE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    protected LocalDateTime by;

    /**
     * Instantiates a task that tracks the details of a
     * deadline.
     *
     * @param description Description details of the deadline.
     * @param by The due date and time of the deadline.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    protected LocalDateTime getBy() {
        return by;
    }

    /**
     * Returns the format used to save a Deadline in the
     * memory.txt file so that Yoyo will be able to retrieve it
     * when it restarts.
     *
     * @return Formatted string representing the deadline.
     */
    @Override
    public String getSaveString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Deadline.STORAGE_DATETIME_FORMAT);
        String formattedDate = by.format(formatter);
        return "D|" + super.getSaveString() + "|" + formattedDate;
    }

    /**
     * Compares this Deadline object with another Object obj.
     * Returns true if obj is also a Deadline and shares the
     * same description and by field as this Deadline object.
     *
     * @param obj The reference object with which to compare.
     * @return True if both objects are Deadlines that share the same fields
     *         for description and by.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Deadline) {
            Deadline deadline = (Deadline) obj;
            boolean isDescriptionEqual = deadline.getDescription().equals(this.getDescription());
            boolean isByEqual = deadline.getBy().equals(by);
            boolean isIsDoneEqual = (deadline.isDone() == this.isDone());
            return isDescriptionEqual && isByEqual && isIsDoneEqual;
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Deadline.DISPLAY_DATETIME_FORMAT);
        String formattedDate = by.format(formatter);
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}
