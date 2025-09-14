package yoyo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import yoyo.exception.InvalidEventException;

/**
 * Represents a task with both a start date and time, and
 * end date and time.
 */
public class Event extends Task {
    private static final String DISPLAY_DATETIME_FORMAT = "MMM dd yyyy, h:mm a";
    private static final String STORAGE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    protected LocalDateTime from;
    protected LocalDateTime to;
    
    /**
     * Instantiates a task that tracks the details of an
     * event.
     *
     * @param description Description details of the event.
     * @param from The starting date and time of the event.
     * @param to The ending date and time of the event.
     * @throws InvalidEventException If from begins after to.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to)
            throws InvalidEventException {
        super(description);
        if (from.isAfter(to) || from.isEqual(to)) {
            throw new InvalidEventException();
        }
        this.from = from;
        this.to = to;
    }

    protected LocalDateTime getFrom() {
        return from;
    }

    protected LocalDateTime getTo() {
        return to;
    }

    /**
     * Returns the format used to save an Event in the
     * memory.txt file so that Yoyo will be able to retrieve it
     * when it restarts.
     *
     * @return Formatted string representing the event.
     */
    @Override
    public String getSaveString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(STORAGE_DATETIME_FORMAT);
        String formattedFromDate = from.format(formatter);
        String formattedToDate = to.format(formatter);
        return "E|" + super.getSaveString() + "|" + formattedFromDate + "|"
                + formattedToDate;
    }

    /**
     * Compares this Event object with another Object obj.
     * Returns true if obj is also an Event and shares the
     * same description, from and to fields as this Event object.
     *
     * @param obj   The reference object with which to compare.
     * @return True if both objects are Events that share the same fields
     *         for description, from and to.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            Event event = (Event) obj;
            boolean isDescriptionEqual = event.getDescription().equals(this.getDescription());
            boolean isFromEqual = event.getFrom().equals(from);
            boolean isToEqual = event.getTo().equals(to);
            boolean isIsDoneEqual = (event.isDone() == this.isDone());
            return  isDescriptionEqual && isFromEqual && isToEqual && isIsDoneEqual;
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Event.DISPLAY_DATETIME_FORMAT);
        String formattedFromDate = from.format(formatter);
        String formattedToDate = to.format(formatter);
        return "[E]" + super.toString() + " (from: " + formattedFromDate
                + " to: " + formattedToDate + ")";
    }
}
