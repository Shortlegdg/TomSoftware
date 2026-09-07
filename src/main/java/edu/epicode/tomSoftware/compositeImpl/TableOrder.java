package edu.epicode.tomSoftware.compositeImpl;

import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import edu.epicode.tomSoftware.validation.InputValidator;

/**
 * Represent the leaf Class of the composite pattern.
 */
public class TableOrder extends MenuComponent {

    /**
     * The number of fields of an order record: category, name and price.
     */
    private static final int RECORD_FIELDS = 3;

    /**
     * The line as it is shown to the waiter.
     */
    private final String order;

    /**
     * The price of the line, zero when the line is not an order record.
     */
    private final double price;

    /**
     * Indicates an order dish by dish of a single table.
     *
     * @param order The dish added to that table.
     */
    public TableOrder(String order) {
        this(order, 0.0);
    }

    /**
     * @param order The line as it is shown to the waiter.
     * @param price The price of the line.
     */
    private TableOrder(String order, double price) {
        this.order = order;
        this.price = price;
    }

    /**
     * Rebuilds a leaf out of a line read from the file of a table.
     * @param line The line read from the file.
     * @return The leaf representing that line.
     */
    public static TableOrder fromLine(String line) {

        if (line == null) {
            return new TableOrder("");
        }

        String[] parts = line.split(String.valueOf(InputValidator.FIELD_SEPARATOR), -1);

        if (parts.length != RECORD_FIELDS) {
            return new TableOrder(line);
        }

        try {
            double parsed = InputValidator.parsePrice(parts[2]);
            String text = parts[0] + ": " + parts[1] + " - " + parsed + "\u20ac";

            return new TableOrder(text, parsed);

        } catch (TomsException e) {
            return new TableOrder(line);
        }
    }

    /**
     * @return The price of this single line.
     */
    @Override
    public double getTotal() {
        return price;
    }

    /**
     *Indicates the entire order of a table.
     */
    @Override
    public void print() {
        System.out.println(" " + order);
    }

}
