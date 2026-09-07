package edu.epicode.tomSoftware.dish;

import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import edu.epicode.tomSoftware.validation.InputValidator;

/**
 * The factory pattern that creates a dish from the text file wit all the dishes by category.
 */
public class DishFactory {

    /**
     * The method that create a dish for the Menù by reading a text file.
     *
     * @param category the category of the dish
     * @param line the line of the text with the information.
     *
     * @return a new dish object to be shown in the Menù.
     *
     * @throws TomsException If the line is malformed, or the name and the price
     *                       do not pass the validation.
     */
    public static DishService createDish(String category, String line) throws TomsException {

        if (category == null || line == null) {
            throw new TomsException("Malformed menu entry.");
        }

        String[] parts = line.split(String.valueOf(InputValidator.FIELD_SEPARATOR), -1);

        if (parts.length != 2) {
            throw new TomsException("Malformed menu entry.");
        }

        String name = InputValidator.sanitizeText(parts[0]);
        double price = InputValidator.parsePrice(parts[1]);

        String key = category.replaceAll("\\s", "").toUpperCase();

        switch (key) {
            case "DRINKS":
                return new Drink(category, name, price);
            case "DESSERTS":
                return new Dessert(category, name, price);
            default:
                return new Course(category, name, price);
        }
    }

}
