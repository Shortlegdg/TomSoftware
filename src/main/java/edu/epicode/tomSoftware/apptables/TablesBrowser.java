package edu.epicode.tomSoftware.apptables;

import edu.epicode.tomSoftware.compositeImpl.MenuComponent;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;

/**
 * The class that provide the method to browse through the tables.
 * It iterates through the Map Collection
 */
public class TablesBrowser {

    /**
     * The collection the browser works on.
     *
     */
    private static TableMapCollection collection = new TableMapCollection();

    /**
     * Reloads the tables from the disk, so that every operation of the browser
     * works on the current situation.
     */
    private static void refresh() {
        collection = new TableMapCollection();
    }

    /**
     * This method lists all the opened tables and their orders.
     */
    public static void listTables() {

        refresh();

        Iterator<MenuComponent> iterator = collection.createIterator();

        if (iterator.hasNext()) {

            System.out.println("\n================ Opened Tables ================");

            while (iterator.hasNext()) {
                MenuComponent table = iterator.next();
                table.print();
            }
        } else {
            System.out.println("No tables are opened.");
        }

    }

    /**
     * This permits to visit a selected table and shows its order.
     *
     * @param tableID The number of the table to get shown, received by input.
     * @throws TomsException If the table is not opened.
     */
    public static void enterTable(int tableID) throws TomsException {

        refresh();

        if (!collection.exists(tableID)) {
            System.out.println("The table " + tableID + " does not exist.");
            return;
        }

        MenuComponent table = collection.findById(tableID);

        System.out.println("\n============ Order of the table " + tableID + " ============" );
        table.print();
    }

    /**
     * This method throws the request to close a selected table, then to delete its file.
     *
     * @param id The table to get closed.
     * @throws TomsException If the file of the table cannot be deleted.
     */
    public static void closeTable(int id) throws TomsException {

        refresh();

        boolean closed = collection.deleteById(id);

        if (closed) {
            System.out.println("\nThe table " + id + " has been closed successfully");
        } else {
            System.out.println("\nThe table " + id + " does not exist");
        }
    }

}
