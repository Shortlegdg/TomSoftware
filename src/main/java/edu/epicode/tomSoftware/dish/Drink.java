package edu.epicode.tomSoftware.dish;

/**
 * A beverage.
 */
public class Drink extends Dish {

    /**
     * @param category The category the beverage belongs to.
     * @param name     The name of the beverage.
     * @param price    The price of the beverage.
     */
    public Drink(String category, String name, double price) {
        super(category, name, price);
    }

    /**
     * @return Always the bar: a beverage is never prepared by the kitchen.
     */
    @Override
    public Station getStation() {
        return Station.BAR;
    }
}
