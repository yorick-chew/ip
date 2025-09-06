package yoyo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Scanner;

import yoyo.exception.EditMemoryException;
import yoyo.exception.InvalidEventException;
import yoyo.exception.MissingMemoryException;
import yoyo.task.Deadline;
import yoyo.task.Event;
import yoyo.task.Task;
import yoyo.task.ToDo;

/**
 * Handles saving and loading of tasks inputted into the chatbot.
 */
public class Storage {
    private final String filePath = "data/memory.txt";
    private final DateTimeFormatter parseFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Loads data of tasks inputted into the chatbot during previous
     * sessions into the current session.
     *
     * @return A list of Tasks from the previous chatbot session.
     * @throws MissingMemoryException If there is a system error while creating
     *                                the data directory for the memory.txt file.
     */
    public ArrayList<Task> load() throws MissingMemoryException {
        // Creates the data dir and memory.txt file if they do not exist
        File dataDir = new File("data");
        dataDir.mkdir();
        if (!(dataDir.exists())) {
            throw new MissingMemoryException();
        }

        File memory = new File(filePath);
        try {
            memory.createNewFile();

            // Fill up taskLst with saved memory
            ArrayList<Task> savedTasks = new ArrayList<>();
            Scanner memoryScanner = new Scanner(memory);

            while (memoryScanner.hasNextLine()) {
                Task newTask = this.parseTask(memoryScanner.nextLine());

                savedTasks.add(newTask);
            }
            return savedTasks;
        } catch (IOException e) {
            throw new MissingMemoryException();
        }
    }

    /**
     * Parses each line of the memory.txt file to retrieve the Task
     * saved.
     *
     * @param taskLine A line of formatted text on the memory.txt file
     *                 which represents a Task object that was saved from
     *                 a previous session.
     * @return A Task that was saved from the previous session with the chatbot.
     */
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

    /**
     * Updates the memory.txt file with the given TaskList.
     *
     * @param tasksToSave The latest list of Tasks that the chatbot will save
     *                for future sessions.
     * @throws EditMemoryException If there is a system error while writing into
     *                             memory.txt for saving purposes.
     */
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
