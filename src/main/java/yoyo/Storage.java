package yoyo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    private final String filePath = "data/memory.txt";
    private DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ArrayList<Task> load() throws MissingMemoryException, IOException {
        // Creates the data dir and memory.txt file if they do not exist
        File dataDir = new File("data");
        dataDir.mkdir();
        if (!(dataDir.exists())) {
            throw new MissingMemoryException();
        }

        File memory = new File(this.filePath);
        memory.createNewFile();

        // Fill up taskLst with saved memory
        ArrayList<Task> taskLst = new ArrayList<>();
        Scanner memoryScanner = new Scanner(memory);

        while (memoryScanner.hasNextLine()) {
            Task newTask = this.parseTask(memoryScanner.nextLine());

            taskLst.add(newTask);
        }
        return taskLst;
    }

    public Task parseTask(String taskLine) {
        String[] taskInfo = taskLine.split("\\|");
        String taskType = taskInfo[0];
        boolean isMarked = Boolean.parseBoolean(taskInfo[1]);
        String description = taskInfo[2];
        if (taskType.equals("T")) {
            ToDo newToDo = new ToDo(description);
            if (isMarked) {
                newToDo.markAsDone();
            }
            return newToDo;
        } else if (taskType.equals("D")) {
            String by = taskInfo[3];
            LocalDateTime byDate = LocalDateTime.parse(by, this.parseFormatter);
            Deadline newDeadline = new Deadline(description, byDate);
            if (isMarked) {
                newDeadline.markAsDone();
            }
            return newDeadline;
        } else {
            String from = taskInfo[3];
            String to = taskInfo[4];
            LocalDateTime fromDate = LocalDateTime.parse(from, this.parseFormatter);
            LocalDateTime toDate = LocalDateTime.parse(to, this.parseFormatter);
            Event newEvent = null;
            try {
                newEvent = new Event(description, fromDate, toDate);
            } catch (InvalidEventException e) {
                // Do nothing as a correct memory.txt file will have only recorded
                // valid events, assuming that no user edited this file.
            }
            return newEvent;
        }
    }

    public void updateMemory(TaskList taskLst) throws EditMemoryException {
        try {
            FileWriter fw = new FileWriter(this.filePath);
            for (int idx = 0; idx < taskLst.size(); idx++) {
                fw.write(taskLst.get(idx).getSaveString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new EditMemoryException();
        }
    }
}
