package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with both a start date and time, and
 * end date and time.
 */
public class Event extends Task {
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
    public Event(String description, LocalDateTime from, LocalDateTime to) throws InvalidEventException {
        super(description);
        if (from.isAfter(to) || from.isEqual(to)) {
            throw new InvalidEventException();
        }
        this.from = from;
        this.to = to;
    }

    protected LocalDateTime getFrom() {
        return this.from;
    }

    protected LocalDateTime getTo() {
        return this.to;
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedFromDate = this.from.format(formatter);
        String formattedToDate = this.to.format(formatter);
        return "E|" + super.getSaveString() + "|" + formattedFromDate + "|" +
                formattedToDate;
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
            return event.getDescription().equals(this.description) &&
                    (event.getFrom().equals(this.from)) &&
                    (event.getTo().equals(this.to));
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
        String formattedFromDate = this.from.format(formatter);
        String formattedToDate = this.to.format(formatter);
        return "[E]" + super.toString() + " (from: " + formattedFromDate
                + " to: " + formattedToDate + ")";
    }
}
