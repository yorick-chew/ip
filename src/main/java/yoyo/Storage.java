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
        try {
            Scanner memoryScanner = new Scanner(memory);
            while (memoryScanner.hasNextLine()) {
                String line = memoryScanner.nextLine();
                String[] taskInfo = line.split("\\|");
                String taskType = taskInfo[0];
                boolean isMarked = Boolean.parseBoolean(taskInfo[1]);
                String description = taskInfo[2];
                Task newTask;
                if (taskType.equals("T")) {
                    newTask = new ToDo(description);
                } else if (taskType.equals("D")) {
                    String by = taskInfo[3];
                    LocalDateTime byDate = LocalDateTime.parse(by, this.parseFormatter);
                    newTask = new Deadline(description, byDate);
                } else {
                    String from = taskInfo[3];
                    String to = taskInfo[4];
                    LocalDateTime fromDate = LocalDateTime.parse(from, this.parseFormatter);
                    LocalDateTime toDate = LocalDateTime.parse(to, this.parseFormatter);
                    newTask = new Event(description, fromDate, toDate);
                }
                if (isMarked) {
                    newTask.markAsDone();
                }
                taskLst.add(newTask);
            }
        } catch (InvalidEventException e) {
            // Do nothing as a correct memory.txt file will have only recorded
            // valid events, assuming that no user edited this file.
        }
        return taskLst;
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
