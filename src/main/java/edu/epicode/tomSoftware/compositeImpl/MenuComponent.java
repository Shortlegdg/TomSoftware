package edu.epicode.tomSoftware.compositeImpl;

/**
 * Represent the Component Interface for define the composite pattern.
 *
 */
public abstract class MenuComponent {

    /**
     * Add a dish to the order
     *
     * @param dish The dish added to the order
     */
    public void add(MenuComponent dish) {
        throw new UnsupportedOperationException(
                getClass().getSimpleName() + " cannot contain other components.");
    }

    /**
     * The amount due for this component.
     *
     * @return The price of a single line, or the sum of the children of a table.
     */
    public abstract double getTotal();

    /**
     * Indicates the entire order of a table.
     */
    public void print() {
        throw new UnsupportedOperationException();
    }

}
