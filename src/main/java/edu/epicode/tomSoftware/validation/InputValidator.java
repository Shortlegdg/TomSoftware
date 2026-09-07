package edu.epicode.tomSoftware.validation;

import edu.epicode.tomSoftware.AppConfig;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;

/**
 * The single point where every value entering the application is validated.
 */
public class InputValidator {

    /**
     * The character separating the fields inside the menu files.
     */
    public static final char FIELD_SEPARATOR = ';';

    public static final int MAX_TEXT_LENGTH = 60;

    private static final double MAX_PRICE = 1000.0;

    private InputValidator() {
    }

    /**
     * Checks that a table number lies inside the configured range.
     *
     * @param tableId The table number typed by the waiter.
     * @return The same number, when it is valid.
     * @throws TomsException If the number is outside the accepted range.
     */
    public static int validateTableId(int tableId) throws TomsException {

        int min = AppConfig.minTableId();
        int max = AppConfig.maxTableId();

        if (tableId < min || tableId > max) {
            throw new TomsException("The table number must be between " + min + " and " + max + ".");
        }
        return tableId;
    }

    /**
     * Checks that the number of guests lies inside the configured range.
     *
     * @param people The number of guests typed by the waiter.
     * @return The same number, when it is valid.
     * @throws TomsException If the number is outside the accepted range.
     */
    public static int validatePeopleCount(int people) throws TomsException {

        int min = AppConfig.minPeople();
        int max = AppConfig.maxPeople();

        if (people < min || people > max) {
            throw new TomsException("The number of guests must be between " + min + " and " + max + ".");
        }
        return people;
    }

    /**
     * Cleans a free text value read from a file or typed by the user.
     * @param raw The value to clean.
     * @return The cleaned value.
     * @throws TomsException If the value is empty or contains the field separator.
     */
    public static String sanitizeText(String raw) throws TomsException {

        if (raw == null) {
            throw new TomsException("A required text value is missing.");
        }

        String cleaned = raw.replaceAll("\\p{Cntrl}", "").trim();

        if (cleaned.isEmpty()) {
            throw new TomsException("A required text value is empty.");
        }
        if (cleaned.indexOf(FIELD_SEPARATOR) >= 0) {
            throw new TomsException("A text value contains an illegal character.");
        }
        if (cleaned.length() > MAX_TEXT_LENGTH) {
            cleaned = cleaned.substring(0, MAX_TEXT_LENGTH);
        }

        return cleaned;
    }

    /**
     * Parses and validates a price read from a menu file.
     * @param raw The textual price.
     * @return The parsed price.
     * @throws TomsException If the value is not a number, is negative, or is
     *                       implausibly large.
     */
    public static double parsePrice(String raw) throws TomsException {

        if (raw == null || raw.trim().isEmpty()) {
            throw new TomsException("A price value is missing.");
        }

        double price;

        try {
            price = Double.parseDouble(raw.trim());
        } catch (NumberFormatException e) {
            throw new TomsException("A price value is not a valid number.", e);
        }

        if (price < 0) {
            throw new TomsException("A price value cannot be negative.");
        }
        if (price > MAX_PRICE) {
            throw new TomsException("A price value is outside the accepted range.");
        }

        return price;
    }
}
