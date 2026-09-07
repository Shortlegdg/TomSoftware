package edu.epicode.tomSoftware.dish;

/**
 * The interface to identify a dish for the order.
 */
public interface DishService {

    /**
     * The getter for the name of the dish.
     * @return the name of the dish ordered
     */
    String getName();

    /**
     * The getter for the price of the dish.
     * @return the price of the dish ordered.
     */
    double getPrice();

    /**
     * The category in terms of course of the dish.
     * @return the type of course of that dish.
     */
    String getCategory();

    /**
     * The preparation point the comanda of this dish has to be routed to.
     * @return the station preparing this dish.
     */
    Station getStation();

}
