package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

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

    @Override
    public String getSaveString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedFromDate = this.from.format(formatter);
        String formattedToDate = this.to.format(formatter);
        return "E|" + super.getSaveString() + "|" + formattedFromDate + "|" +
                formattedToDate;
    }

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
