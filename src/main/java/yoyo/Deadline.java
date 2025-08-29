package yoyo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Deadline extends Task {
    protected LocalDateTime by;

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    protected LocalDateTime getBy() {
        return by;
    }

    @Override
    public String getSaveString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDate = by.format(formatter);
        return "D|" + super.getSaveString() + "|" + formattedDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Deadline) {
            Deadline deadline = (Deadline) obj;
            return deadline.getDescription().equals(this.getDescription())
                    && (deadline.getBy().equals(by))
                    && (deadline.isDone()) == this.isDone();
        }
        return false;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mm a");
        String formattedDate = by.format(formatter);
        return "[D]" + super.toString() + " (by: " + formattedDate + ")";
    }
}
