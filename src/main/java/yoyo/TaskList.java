package yoyo;

import java.util.ArrayList;

/**
 * Holds a list of Task objects.
 */
public class TaskList {
    private ArrayList<Task> taskLst;

    public TaskList() {
        this.taskLst = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> taskLst) {
        this.taskLst = taskLst;
    }

    public void addTask(Task newTask) {
        this.taskLst.add(newTask);
    }

    public Task get(int idx) {
        return this.taskLst.get(idx);
    }

    public Task removeTask(int taskNum) {
        int taskIdx = taskNum - 1;
        return this.taskLst.remove(taskIdx);
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
        Task task = this.taskLst.get(taskIdx);
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
        Task task = this.taskLst.get(taskIdx);
        task.unmarkAsDone();
        return task;
    }

    public int size() {
        return this.taskLst.size();
    }
}
