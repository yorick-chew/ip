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

    @Override
    public String getSaveString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedFromDate = this.from.format(formatter);
        String formattedToDate = this.to.format(formatter);
        return "E|" + super.getSaveString() + "|" + formattedFromDate + "|" +
                formattedToDate;
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
