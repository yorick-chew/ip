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
import yoyo.exception.YoyoException;
import yoyo.task.Deadline;
import yoyo.task.Event;
import yoyo.task.Task;
import yoyo.task.ToDo;

/**
 * Handles saving and loading of tasks inputted into the chatbot.
 */
public class Storage {
    private static final String TODO_TYPE = "T";
    private static final String DEADLINE_TYPE = "D";
    private static final String EVENT_TYPE = "E";

    private static final String DIRECTORY_NAME = "data";
    private static final String SAVEFILE_NAME = "memory.txt";
    private static final String FILEPATH = Storage.DIRECTORY_NAME + "/" + Storage.SAVEFILE_NAME;

    private static final String PARSE_DELIMITER = "\\|";

    private static final String STORAGE_DATETIME_FORMAT = "yyyy-MM-dd HH:mm";

    private final DateTimeFormatter parseFormatter = DateTimeFormatter
            .ofPattern(Storage.STORAGE_DATETIME_FORMAT);


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
        createDirectory();
        File memory = createMemoryFile();
        return populateTaskList(memory);
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
    public Task parseTask(String taskLine) throws YoyoException {
        String[] taskDetails = taskLine.split(Storage.PARSE_DELIMITER);
        String taskType = taskDetails[0];

        if (taskType.equals(Storage.TODO_TYPE)) {
            return parseToDo(taskDetails);
        } else if (taskType.equals(Storage.DEADLINE_TYPE)) {
            return parseDeadline(taskDetails);
        } else if (taskType.equals(Storage.EVENT_TYPE)) {
            return parseEvent(taskDetails);
        } else {
            throw new YoyoException("Unknown task type parsed.");
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
            FileWriter fw = new FileWriter(FILEPATH);
            for (int idx = 0; idx < tasksToSave.size(); idx++) {
                Task taskToSave = tasksToSave.get(idx);
                fw.write(taskToSave.getSaveString() + System.lineSeparator());
            }
            fw.close();
        } catch (IOException e) {
            throw new EditMemoryException();
        }
    }

    private void createDirectory() throws MissingMemoryException {
        File dataDir = new File(Storage.DIRECTORY_NAME);
        dataDir.mkdir();
        if (!(dataDir.exists())) {
            throw new MissingMemoryException();
        }
    }

    private File createMemoryFile() throws MissingMemoryException {
        File memory = new File(FILEPATH);
        try {
            memory.createNewFile();
        } catch (IOException e) {
            throw new MissingMemoryException();
        }
        return memory;
    }

    private ArrayList<Task> populateTaskList(File memory) throws MissingMemoryException {
        try {
            // Fill up taskLst with saved memory
            ArrayList<Task> savedTasks = new ArrayList<>();
            Scanner memoryScanner = new Scanner(memory);

            while (memoryScanner.hasNextLine()) {
                try {
                    Task newTask = parseTask(memoryScanner.nextLine());
                    savedTasks.add(newTask);
                } catch (YoyoException e) {
                    // Ignore the invalid task and continue parsing.
                }
            }
            return savedTasks;
        } catch (IOException e) {
            throw new MissingMemoryException();
        }
    }

    private ToDo parseToDo(String[] taskDetails) {
        boolean isMarked = Boolean.parseBoolean(taskDetails[1]);
        String description = taskDetails[2];

        ToDo newToDo = new ToDo(description);
        if (isMarked) {
            newToDo.markAsDone();
        }
        return newToDo;
    }

    private Deadline parseDeadline(String[] taskDetails) {
        boolean isMarked = Boolean.parseBoolean(taskDetails[1]);
        String description = taskDetails[2];
        String by = taskDetails[3];

        LocalDateTime byDate = LocalDateTime.parse(by, parseFormatter);
        Deadline newDeadline = new Deadline(description, byDate);
        if (isMarked) {
            newDeadline.markAsDone();
        }
        return newDeadline;
    }

    private Event parseEvent(String[] taskDetails) {
        boolean isMarked = Boolean.parseBoolean(taskDetails[1]);
        String description = taskDetails[2];
        String from = taskDetails[3];
        String to = taskDetails[4];

        LocalDateTime fromDate = LocalDateTime.parse(from, parseFormatter);
        LocalDateTime toDate = LocalDateTime.parse(to, parseFormatter);
        try {
            Event newEvent = new Event(description, fromDate, toDate);
            if (isMarked) {
                newEvent.markAsDone();
            }
            return newEvent;
        } catch (InvalidEventException e) {
            // Do nothing as a correct memory.txt file will have only recorded
            // valid events, assuming that no user edited this file.
            throw new RuntimeException("This exception should not be reached unless a"
                    + "user edited the memory.txt file.", e);
        }
    }
}
