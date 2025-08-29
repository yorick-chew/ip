package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

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

    @Override
    public String getSaveString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedFromDate = from.format(formatter);
        String formattedToDate = to.format(formatter);
        return "E|" + super.getSaveString() + "|" + formattedFromDate + "|"
                + formattedToDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Event) {
            Event event = (Event) obj;
            return event.getDescription().equals(this.getDescription())
                    && (event.getFrom().equals(from))
                    && (event.getTo().equals(to))
                    && (event.isDone() == this.isDone());
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
        String formattedFromDate = from.format(formatter);
        String formattedToDate = to.format(formatter);
        return "[E]" + super.toString() + " (from: " + formattedFromDate
                + " to: " + formattedToDate + ")";
    }
}
