package yoyo;

public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    public String getSaveString() {
        return "T|" + super.getSaveString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ToDo) {
            ToDo toDo = (ToDo) obj;
            return toDo.getDescription().equals(this.description);
        }
        return false;
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }
}
