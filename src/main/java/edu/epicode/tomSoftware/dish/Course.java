package edu.epicode.tomSoftware.dish;

/**
 * A dish prepared by the kitchen: appetizers, first and second courses and side
 * dishes all fall into this type.
 */
public class Course extends Dish {

    /**
     * @param category The course the dish belongs to.
     * @param name     The name of the dish.
     * @param price    The price of the dish.
     */
    public Course(String category, String name, double price) {
        super(category, name, price);
    }

    /**
     * @return Always the kitchen.
     */
    @Override
    public Station getStation() {
        return Station.KITCHEN;
    }
}
