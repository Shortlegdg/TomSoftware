package edu.epicode.tomSoftware;

import edu.epicode.tomSoftware.exceptionhandling.TomsException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.*;

/**
 * The configuration of the Logging.
 * Registering logs exclusively on 5 rotating files placed in "logs/toms.log".
 */
public class LoggingConfig {

    private static final Logger rootLogger = Logger.getLogger("");

    /**
     * Logging setup exclusively by fileHandler (no console output).
     * Throwing custom TomsException.
     *
     * @throws TomsException If the log folder or the log file cannot be created.
     */
    public static void loggingSetup() throws TomsException {

        for (Handler h : rootLogger.getHandlers()) {
            rootLogger.removeHandler(h);
        }
        try {
            createLogFolder();

            FileHandler fileHandler = new FileHandler(
                    AppConfig.logFile(),
                    AppConfig.logMaxBytes(),
                    AppConfig.logFileCount(),
                    true);

            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);

            rootLogger.addHandler(fileHandler);

            rootLogger.setLevel(Level.ALL);

        } catch (IOException e) {
            throw new TomsException("Error configuring Logging", e);
        }
    }

    /**
     * Creates the folder the log file is written into.
     * @throws IOException If the folder cannot be created.
     */
    private static void createLogFolder() throws IOException {

        Path logFolder = Paths.get(AppConfig.logFile()).getParent();

        if (logFolder != null) {
            Files.createDirectories(logFolder);
        }
    }
}
