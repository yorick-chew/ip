package yoyo;

import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> tasks;

    public TaskList() {
        tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void addTask(Task newTask) {
        tasks.add(newTask);
    }

    public Task get(int idx) {
        return tasks.get(idx);
    }

    public Task removeTask(int taskNum) {
        int taskIdx = taskNum - 1;
        return tasks.remove(taskIdx);
    }

    public Task markAsDone(int taskNum) {
        int taskIdx = taskNum - 1;
        Task task = tasks.get(taskIdx);
        task.markAsDone();
        return task;
    }

    public Task unmarkAsDone(int taskNum) {
        int taskIdx = taskNum - 1;
        Task task = tasks.get(taskIdx);
        task.unmarkAsDone();
        return task;
    }

    public int size() {
        return tasks.size();
    }
}
