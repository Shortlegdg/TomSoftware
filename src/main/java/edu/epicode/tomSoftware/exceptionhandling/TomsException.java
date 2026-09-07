package edu.epicode.tomSoftware.exceptionhandling;

/**
 * Custom TomsException extending "Exception" to handle all the possible exception
 * like FileNotFound, or IOException.
 */
public class TomsException extends Exception {

    /**
     * Constructs the new handled exception with a specified message.
     *
     * @param message A descriptive message explaining tha cause of the exception.
     * @param cause The internal exception that triggered this exception.
     */
    public TomsException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Constructs the new handled exception with a message only.
     * @param message A descriptive message explaining the cause of the exception.
     */
    public TomsException(String message) {
        super(message);
    }
}
