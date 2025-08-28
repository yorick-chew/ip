import java.util.ArrayList;

public class TaskList {
    private ArrayList<Task> taskLst = new ArrayList<>();

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

    public Task markAsDone(int taskNum) {
        int taskIdx = taskNum - 1;
        Task task = this.taskLst.get(taskIdx);
        task.markAsDone();
        return task;
    }

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
