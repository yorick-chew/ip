package yoyo;

import java.util.ArrayList;

/**
 * Holds a list of Task objects.
 */
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

    /**
     * Finds the taskNum-th Task in the list and marks
     * the Task as done.
     *
     * @param taskNum The index of the selected task, starting from 1.
     * @return The taskNum-th task that was marked as done.
     */
    public Task markAsDone(int taskNum) {
        int taskIdx = taskNum - 1;
        Task task = tasks.get(taskIdx);
        task.markAsDone();
        return task;
    }

    /**
     * Finds the taskNum-th Task in the list and unmarks
     * the Task as done.
     *
     * @param taskNum The index of the selected task, starting from 1.
     * @return The taskNum-th task that was unmarked as done.
     */
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
