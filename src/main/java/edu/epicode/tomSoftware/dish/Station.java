package edu.epicode.tomSoftware.dish;

/**
 * The preparation point an ordered dish has to be routed to.
 */
public enum Station {

    /**
     * Prepared by the kitchen: appetizers, first and second courses, side dishes.
     */
    KITCHEN("Kitchen"),

    /**
     * Prepared at the bar: every beverage.
     */
    BAR("Bar"),

    /**
     * Prepared by the pastry section, and served after the other courses.
     */
    PASTRY("Pastry");

    /**
     * The name shown on screen.
     */
    private final String displayName;

    Station(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
