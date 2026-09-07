package edu.epicode.tomSoftware.dish;

/**
 * The dish created by the factory pattern, with all the information.
 */
public abstract class Dish implements DishService {

    /**
     * The category in terms of course of the dish
     */
    private String category;

    /**
     * The name oh the dish.
     */
    private String name;

    /**
     * The price of the dish
     */
    private double price;

    /**
     * @param category The course the dish belongs to.
     * @param name     The name of the dish.
     * @param price    The price of the dish.
     */
    protected Dish(String category, String name, double price) {

        this.category = category;
        this.name = name;
        this.price = price;
    }

    @Override
    public String getCategory() {
        return category;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return category + " - " + name + " - " + price + " - " + "€";
    }

}
