package edu.epicode.tomSoftware.apptables;

import edu.epicode.tomSoftware.AppConfig;
import edu.epicode.tomSoftware.compositeImpl.MenuComponent;
import edu.epicode.tomSoftware.compositeImpl.TableComposite;
import edu.epicode.tomSoftware.compositeImpl.TableOrder;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import edu.epicode.tomSoftware.exceptionhandling.TomsExceptionHandler;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The definition of the HashMap collection for all the tables inside the application.
 * It implements the interface Aggregate for the iterator pattern.
 */
public class TableMapCollection implements Aggregate<MenuComponent>, Repository<Integer, MenuComponent> {

    private static final Logger log = Logger.getLogger(TableMapCollection.class.getName());

    /**
     * The string representation of the directory path of all the tables.
     */
    private final String directory = AppConfig.tablesDirectory();

    /**
     * The file folder assigned to the directory path of the tables.
     */
    private final File folder = new File(directory);

    /**
     * The initialization of the HashMap.
     */
    private Map<Integer, MenuComponent> tablesMap = new HashMap<>();


    public TableMapCollection() {

        log.info("Initializing table collection...");
        loadTables();
    }

    /**
     * Loads all the table files inside the assigned folder, and creates the table object
     * with its ID number and add all the tables inside the map.
     */
    private void loadTables() {

        log.fine("Loading tables from directory " + directory);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        File[] files = folder.listFiles((dir, name) -> name.startsWith("table") && name.endsWith(".txt"));

        if (files != null) {
            for (File f : files) {

                int id = parseTableId(f.getName());

                if (id < 0) {
                    log.warning("Ignoring unrecognised file name: " + f.getName());
                    continue;
                }

                TableComposite table = new TableComposite("Table" + id);

                try (BufferedReader br = new BufferedReader(new FileReader(f, StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        table.add(TableOrder.fromLine(line));
                    }
                } catch (IOException e) {
                    log.severe("Error reading file " + f.getName());
                    TomsExceptionHandler.handledException(e);



                }
                tablesMap.put(id, table);
                log.info("Loaded " + tablesMap.size() + " tables.");

            }
        }
    }

    /**
     * Extracts the table number out of a file name.
     *
     * @param fileName The name of a file found inside the tables folder.
     * @return The table number, or -1 when the name does not match.
     */
    private int parseTableId(String fileName) {

        String digits = fileName.replace("table", "").replace(".txt", "");

        if (!digits.matches("\\d{1,9}")) {
            return -1;
        }

        return Integer.parseInt(digits);
    }

    /**
     * For each table file present inside the folder, returns a list view of all the table opened.
     */
    public void getOpenedTables() {

        File[] files = folder.listFiles((dir, name) -> name.startsWith("table") && name.endsWith(".txt"));

        if (files != null && files.length != 0) {
            System.out.println("\n================ Opened Tables ================\n");
            for (File f : files) {
                String openedTable =  f.getName().replace(".txt", "");
                System.out.println(openedTable);
            }
        } else {
            System.out.println("No tables are opened.");
        }
    }

    @Override
    public boolean exists(Integer id) {
        return id != null && tablesMap.containsKey(id);
    }

    /**
     * Returns a table object of the specified ID.
     *
     * @param id The ID number of the table we want to get.
     * @return The object element representing the selected table inside the Map.
     * @throws TomsException If no table has that number.
     */
    @Override
    public MenuComponent findById(Integer id) throws TomsException {

        if (!exists(id)) {
            throw new TomsException("The table " + id + " is not opened.");
        }

        return tablesMap.get(id);
    }

    /**
     * Closes the table with the given identifier.
     *
     * @param id The table to close.
     * @return true if the table existed and has been closed.
     * @throws TomsException If the file of the table cannot be deleted.
     */
    @Override
    public boolean deleteById(Integer id) throws TomsException {

        if (id == null) {
            return false;
        }

        return closeTable(id.intValue());
    }

    @Override
    public Iterator<MenuComponent> createIterator() {
        return new TableIterator(new ArrayList<>(tablesMap.values()));
    }

    /**
     * It is responsible to close the table and delete its own file.
     *
     * @param id The ID number of table to close.
     * @return The boolean result of the cancellation of the table.
     */
    public boolean closeTable(int id) throws TomsException {

        log.info("Request closing table " + id);

        MenuComponent removed = tablesMap.remove(id);

        if (removed == null) {
            return false;
        }

        File file = new File(directory + "table" + id + ".txt");

        if (!file.exists()) {
            throw new TomsException("The table " + id + " has no file to delete.");
        }

        if (!file.delete()) {
            log.severe("Could not delete the file of table " + id);
            throw new TomsException("The table could not be closed. Please try again later.");
        }

        log.info("Table " + id + " closed correctly");

        return true;
    }
}
