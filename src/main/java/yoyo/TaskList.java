package yoyo;

import java.util.ArrayList;

import yoyo.exception.InvalidTaskException;
import yoyo.task.Task;

/**
 * Holds a list of Task objects.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Instantiates a list of tasks containing the tasks in the input array list.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds the given task into the task list.
     */
    public void addTask(Task newTask) {
        tasks.add(newTask);
    }

    public Task get(int idx) {
        assert idx >= 0 && idx < getSize() : "Methods calling get must pass in a valid idx";
        return tasks.get(idx);
    }

    /**
     * Removes and returns the taskNum-th task from the task list.
     *
     * @param taskNum The index of the selected task, starting from 1.
     * @return The removed task.
     * @throws InvalidTaskException If the taskNum-th task is not in the task list.
     */
    public Task removeTask(int taskNum) throws InvalidTaskException {
        if (taskNum <= 0 || taskNum > getSize()) {
            throw new InvalidTaskException();
        }
        int taskIdx = taskNum - 1;
        return tasks.remove(taskIdx);
    }

    /**
     * Finds the taskNum-th Task in the list and marks
     * the Task as done.
     *
     * @param taskNum The index of the selected task, starting from 1.
     * @return The taskNum-th task that was marked as done.
     * @throws InvalidTaskException If the taskNum-th task is not in the task list.
     */
    public Task markAsDone(int taskNum) throws InvalidTaskException {
        if (taskNum <= 0 || taskNum > getSize()) {
            throw new InvalidTaskException();
        }
        Task task = getTaskFromNum(taskNum);
        task.markAsDone();
        return task;
    }

    /**
     * Finds the taskNum-th Task in the list and unmarks
     * the Task as done.
     *
     * @param taskNum The index of the selected task, starting from 1.
     * @return The taskNum-th task that was unmarked as done.
     * @throws InvalidTaskException If the taskNum-th task is not in the task list.
     */
    public Task unmarkAsDone(int taskNum) throws InvalidTaskException {
        if (taskNum <= 0 || taskNum > getSize()) {
            throw new InvalidTaskException();
        }
        Task task = getTaskFromNum(taskNum);
        task.unmarkAsDone();
        return task;
    }

    /**
     * Filters and returns a task list that contains only tasks
     * with descriptions that include the keyword string.
     *
     * @param keyword The string that all tasks in the returned TaskList must
     *                contain in their description.
     * @return The filtered TaskList consisting of only Tasks that contain
     *         the keyword string.
     */
    public TaskList filterTasks(String keyword) {
        ArrayList<Task> filteredTasks = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getDescription().contains(keyword)) {
                filteredTasks.add(task);
            }
        }
        return new TaskList(filteredTasks);
    }

    /**
     * Gets the number of tasks in the task list.
     */
    public int getSize() {
        return tasks.size();
    }

    /**
     * Gets the taskNum-th task in the task list.
     */
    private Task getTaskFromNum(int taskNum) {
        int taskIdx = taskNum - 1;
        return tasks.get(taskIdx);
    }
}
