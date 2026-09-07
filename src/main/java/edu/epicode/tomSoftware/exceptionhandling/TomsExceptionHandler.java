package edu.epicode.tomSoftware.exceptionhandling;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles exception by logging them internally.
 * Throws a user-friendly RuntimeException.
 */
public class TomsExceptionHandler {

    /**
     * The logger writing to the rotating files.
     */
    private static final Logger log = Logger.getLogger(TomsExceptionHandler.class.getName());

    /**
     * Handles Exception by logging it internally and throwing a shielded
     * runtime exception with generic message.
     * @param e The original exception thrown.
     * @throws RuntimeException shielded exception.
     */
    public static void handledException(Exception e) {
        logException(e);
        throw new RuntimeException("Error occurred. Please try again later", e);
    }

    /**
     * Logs the details of the Exception internally, on the log files.
     *
     * @param e the exception to be logged
     */
    private static void logException(Exception e) {
        log.log(Level.SEVERE, "Internal error logged", e);
    }

}
