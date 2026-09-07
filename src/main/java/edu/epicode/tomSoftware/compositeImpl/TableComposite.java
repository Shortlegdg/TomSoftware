package edu.epicode.tomSoftware.compositeImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the composite element of the composite pattern.
 */
public class TableComposite extends MenuComponent {

    /**
     * The ID number of a table.
     */
    private String tableID;

    /**
     * The list of dishes of the Menù.
     */
    private List<MenuComponent> lines = new ArrayList<>();

    /**
     * The application of the ID number to a specific table.
     *
     * @param tableID The ID number of a table.
     */
    public TableComposite(String tableID) {
        this.tableID = tableID;
    }

    /**
     * The operation that add a new selected dish to the order of a table.
     * @param dish The dish added to the order
     */
    @Override
    public void add(MenuComponent dish) {
        lines.add(dish);
    }

    /**
     * Sums the totals of the children, recursively.
     * @return The amount due for this table.
     */
    @Override
    public double getTotal() {

        double total = 0.0;

        for (MenuComponent c : lines) {
            total += c.getTotal();
        }

        return total;
    }

    /**
     * The representation of the Table with the entire order.
     */
    @Override
    public void print() {

        System.out.println("\n=================== " + tableID + " ===================");

        for (MenuComponent c : lines) {
            c.print();
        }

        System.out.println("-----------------------------------------------");
        System.out.printf(" TOTAL: %.2f\u20ac%n", getTotal());
    }

}
