package yoyo;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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

    // The number of segments that each task type has in its save format, separated by delimiter '|'
    private static final int SAVED_TODO_SEGMENT_COUNT = 3;
    private static final int SAVED_DEADLINE_SEGMENT_COUNT = 4;
    private static final int SAVED_EVENT_SEGMENT_COUNT = 5;

    // The format for booleans in the memory.txt file
    private static final String TRUE_FORMAT = "true";
    private static final String FALSE_FORMAT = "false";

    // YoyoException error messages for incorrect inputs in memory.txt file
    private static final String UNKNOWN_TASK_TYPE_ERROR_MSG = "Unknown task type parsed.";
    private static final String UNKNOWN_ISMARKED_BOOLEAN_ERROR_MSG = "Unknown isMarked boolean parsed.";
    private static final String WRONG_DATETIME_FORMAT_ERROR_MSG = "Datetime parsed is in the wrong format.";
    private static final String EMPTY_DESCRIPTION_ERROR_MSG = "Empty description parsed.";
    private static final String WRONG_NUM_OF_SEGMENTS_TODO_ERROR_MSG = "Wrong number of segments "
            + "for a todo task.";
    private static final String WRONG_NUM_OF_SEGMENTS_DEADLINE_ERROR_MSG = "Wrong number of segments "
            + "for a deadline task.";
    private static final String WRONG_NUM_OF_SEGMENTS_EVENT_ERROR_MSG = "Wrong number of segments "
            + "for an event task.";

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
        // Solution for including trailing empty string in String.split inspired by
        // https://stackoverflow.com/questions/13939675/
        String[] taskDetails = taskLine.split(Storage.PARSE_DELIMITER, -1);
        String taskType = taskDetails[0];

        try {
            return switch (taskType) {
                case Storage.TODO_TYPE -> parseToDo(taskDetails);
                case Storage.DEADLINE_TYPE -> parseDeadline(taskDetails);
                case Storage.EVENT_TYPE -> parseEvent(taskDetails);
                default -> throw new YoyoException(Storage.UNKNOWN_TASK_TYPE_ERROR_MSG);
            };
        } catch (DateTimeParseException e) {
            throw new YoyoException(Storage.WRONG_DATETIME_FORMAT_ERROR_MSG);
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
            for (int idx = 0; idx < tasksToSave.getSize(); idx++) {
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
        // Fill up taskLst with saved memory
        ArrayList<Task> savedTasks = new ArrayList<>();
        Scanner memoryScanner;
        try {
            memoryScanner = new Scanner(memory);
        } catch (IOException e) {
            throw new MissingMemoryException();
        }

        while (memoryScanner.hasNextLine()) {
            try {
                Task newTask = parseTask(memoryScanner.nextLine());
                savedTasks.add(newTask);
            } catch (YoyoException e) {
                // Ignore the invalid task and continue parsing.
            }
        }
        return savedTasks;
    }

    private ToDo parseToDo(String[] taskDetails) throws YoyoException {
        checkNumOfTodoSegments(taskDetails);

        boolean isMarked = isMarked(taskDetails[1]);
        String description = taskDetails[2];

        if (description.isEmpty()) {
            throw new YoyoException(Storage.EMPTY_DESCRIPTION_ERROR_MSG);
        }

        ToDo newToDo = new ToDo(description);
        if (isMarked) {
            newToDo.markAsDone();
        }
        return newToDo;
    }

    private Deadline parseDeadline(String[] taskDetails) throws YoyoException {
        checkNumOfDeadlineSegments(taskDetails);

        boolean isMarked = isMarked(taskDetails[1]);
        String description = taskDetails[2];

        if (description.isEmpty()) {
            throw new YoyoException(Storage.EMPTY_DESCRIPTION_ERROR_MSG);
        }

        String by = taskDetails[3];
        LocalDateTime byDate = LocalDateTime.parse(by, parseFormatter);
        Deadline newDeadline = new Deadline(description, byDate);
        if (isMarked) {
            newDeadline.markAsDone();
        }
        return newDeadline;
    }

    private Event parseEvent(String[] taskDetails) throws YoyoException {
        checkNumOfEventSegments(taskDetails);

        boolean isMarked = isMarked(taskDetails[1]);
        String description = taskDetails[2];

        if (description.isEmpty()) {
            throw new YoyoException(Storage.EMPTY_DESCRIPTION_ERROR_MSG);
        }

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
            // (ChatGPT consulted regarding the appropriate exception to throw
            // when a checked exception is not expected to ever be thrown)
            throw new RuntimeException("This exception should not be reached unless a"
                    + "user edited the memory.txt file.", e);
        }
    }

    /**
     * Checks that the number of segments of a todo are correct and throws an error otherwise.
     */
    private void checkNumOfTodoSegments(String[] taskDetails) throws YoyoException {
        boolean isWrongNumOfSegments = taskDetails.length != Storage.SAVED_TODO_SEGMENT_COUNT;
        if (isWrongNumOfSegments) {
            throw new YoyoException(Storage.WRONG_NUM_OF_SEGMENTS_TODO_ERROR_MSG);
        }
    }

    /**
     * Checks that the number of segments of a deadline are correct and throws an error otherwise.
     */
    private void checkNumOfDeadlineSegments(String[] taskDetails) throws YoyoException {
        boolean isWrongNumOfSegments = taskDetails.length != Storage.SAVED_DEADLINE_SEGMENT_COUNT;
        if (isWrongNumOfSegments) {
            throw new YoyoException(Storage.WRONG_NUM_OF_SEGMENTS_DEADLINE_ERROR_MSG);
        }
    }

    /**
     * Checks that the number of segments of an event are correct and throws an error otherwise.
     */
    private void checkNumOfEventSegments(String[] taskDetails) throws YoyoException {
        boolean isWrongNumOfSegments = taskDetails.length != Storage.SAVED_EVENT_SEGMENT_COUNT;
        if (isWrongNumOfSegments) {
            throw new YoyoException(Storage.WRONG_NUM_OF_SEGMENTS_EVENT_ERROR_MSG);
        }
    }

    /**
     * Converts the markedStr which is either "true" or "false" into its corresponding boolean.
     */
    private boolean isMarked(String markedStr) throws YoyoException {
        // markedStr must be either "true" or "false"
        boolean isWrongFormat = !markedStr.equals(Storage.TRUE_FORMAT)
                && !markedStr.equals(Storage.FALSE_FORMAT);
        if (isWrongFormat) {
            throw new YoyoException(Storage.UNKNOWN_ISMARKED_BOOLEAN_ERROR_MSG);
        }
        return markedStr.equals(Storage.TRUE_FORMAT);
    }
}
