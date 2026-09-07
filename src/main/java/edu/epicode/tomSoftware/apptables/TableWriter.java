package edu.epicode.tomSoftware.apptables;

import edu.epicode.tomSoftware.AppConfig;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;

/**
 * Represent the class for create the text file, containing the order, for a single table.
 */
public class TableWriter {

    private static final Logger log = Logger.getLogger(TableWriter.class.getName());
    /**
     * The path for each single text file of a table.
     */
    static String fileName;

    /**
     * The method that create a tex file with the Table ID as a name and containing also the number of peaople sitting that table.
     *
     * @param tableID The number of the created table.
     * @param peopleNumber The number of people sitting that table.
     */
    public static void createTableFile(int tableID, int peopleNumber) throws TomsException {

        String directory = AppConfig.tablesDirectory();
        int dirLenght = directory.length();

        fileName = directory + "table"+ tableID + ".txt";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTime time = LocalTime.now();
        log.info("Created file \"" + fileName + "\" at " + formatter.format(time) + " for " + peopleNumber + " people." );

        try {
            Files.createDirectories(Paths.get(directory));
        } catch (IOException e) {
            log.severe("Error creating directory: " + directory);
            throw new TomsException("Error creating file!", e);
        }

        try (PrintWriter pw = new PrintWriter(new FileWriter(fileName, StandardCharsets.UTF_8, true))) {

            pw.println("TABLE ID: " + tableID + " - People: " + peopleNumber + " - Open Time: " + formatter.format(time));
            pw.println("==============================================\n");

            System.out.println("File successfully created: " + fileName.substring(dirLenght) + "\n");

        } catch (IOException e) {
            log.severe("Error creating file: " + fileName);
            throw new TomsException("Error creating file!", e);
        }

    }

    /**
     * The method that returns the file path of a created table.
     *
     * @return The string representing the path of the created table.
     */
    public static String getFileName() {
        return fileName;
    }


}

