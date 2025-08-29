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
    private final DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public ArrayList<Task> load() throws MissingMemoryException, IOException {
        // Creates the data dir and memory.txt file if they do not exist
        File dataDir = new File("data");
        dataDir.mkdir();
        if (!(dataDir.exists())) {
            throw new MissingMemoryException();
        }

        File memory = new File(filePath);
        memory.createNewFile();

        // Fill up taskLst with saved memory
        ArrayList<Task> savedTasks = new ArrayList<>();
        Scanner memoryScanner = new Scanner(memory);

        while (memoryScanner.hasNextLine()) {
            Task newTask = this.parseTask(memoryScanner.nextLine());

            savedTasks.add(newTask);
        }
        return savedTasks;
    }

    public Task parseTask(String taskLine) {
        String[] taskDetails = taskLine.split("\\|");
        String taskType = taskDetails[0];
        boolean isMarked = Boolean.parseBoolean(taskDetails[1]);
        String description = taskDetails[2];
        if (taskType.equals("T")) {
            ToDo newToDo = new ToDo(description);
            if (isMarked) {
                newToDo.markAsDone();
            }
            return newToDo;
        } else if (taskType.equals("D")) {
            String by = taskDetails[3];
            LocalDateTime byDate = LocalDateTime.parse(by, parseFormatter);
            Deadline newDeadline = new Deadline(description, byDate);
            if (isMarked) {
                newDeadline.markAsDone();
            }
            return newDeadline;
        } else {
            String from = taskDetails[3];
            String to = taskDetails[4];
            LocalDateTime fromDate = LocalDateTime.parse(from, parseFormatter);
            LocalDateTime toDate = LocalDateTime.parse(to, parseFormatter);
            try {
                return new Event(description, fromDate, toDate);
            } catch (InvalidEventException e) {
                // Do nothing as a correct memory.txt file will have only recorded
                // valid events, assuming that no user edited this file.
                throw new RuntimeException("This exception should not be reached unless a"
                        + "user edited the memory.txt file.", e);
            }
        }
    }

    public void updateMemory(TaskList tasksToSave) throws EditMemoryException {
        try {
            FileWriter fw = new FileWriter(filePath);
            for (int idx = 0; idx < tasksToSave.size(); idx++) {
                fw.write(tasksToSave.get(idx).getSaveString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new EditMemoryException();
        }
    }
}
