package edu.epicode.tomSoftware.dish;

/**
 * A dessert.
 */
public class Dessert extends Dish {

    /**
     * @param category The category the dessert belongs to.
     * @param name     The name of the dessert.
     * @param price    The price of the dessert.
     */
    public Dessert(String category, String name, double price) {
        super(category, name, price);
    }

    /**
     * @return Always the pastry section.
     */
    @Override
    public Station getStation() {
        return Station.PASTRY;
    }
}
