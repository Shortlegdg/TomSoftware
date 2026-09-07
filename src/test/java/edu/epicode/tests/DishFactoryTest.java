package edu.epicode.tests;

import edu.epicode.tomSoftware.dish.Dessert;
import edu.epicode.tomSoftware.dish.DishFactory;
import edu.epicode.tomSoftware.dish.DishService;
import edu.epicode.tomSoftware.dish.Drink;
import edu.epicode.tomSoftware.dish.Station;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Tests the Factory pattern and the sanitisation it applies to the menu files.
 */
public class DishFactoryTest {

    /**
     * The factory has to choose the concrete class, not always build the same
     * one: a beverage goes to the bar, a dessert to the pastry section.
     */
    @Test
    public void createsTheRightConcreteDish() throws TomsException {

        DishService drink = DishFactory.createDish("DRINKS", "Water;3.0");
        DishService dessert = DishFactory.createDish("DESSERTS", "Tiramisu;6.0");

        assertTrue("a beverage must be a Drink", drink instanceof Drink);
        assertEquals(Station.BAR, drink.getStation());

        assertTrue("a dessert must be a Dessert", dessert instanceof Dessert);
        assertEquals(Station.PASTRY, dessert.getStation());
    }

    /**
     * A malformed line must be reported as a TomsException, and never as the
     * technical exception it would have caused: without the validation this
     * would throw ArrayIndexOutOfBoundsException or NumberFormatException.
     */
    @Test
    public void rejectsAMalformedMenuEntry() {

        assertThrows(TomsException.class, () -> DishFactory.createDish("Drinks", "Water"));
        assertThrows(TomsException.class, () -> DishFactory.createDish("Drinks", "Water;free"));
        assertThrows(TomsException.class, () -> DishFactory.createDish("Drinks", "Water;-3.0"));
    }

    /**
     * Exception Shielding, asserted directly: the message that reaches the user
     * must not repeat the offending value, which belongs to the log file only.
     */
    @Test
    public void doesNotLeakTheOffendingValue() {

        TomsException thrown = assertThrows(TomsException.class,
                () -> DishFactory.createDish("Drinks", "Water;<script>alert(1)</script>"));

        assertFalse("the shielded message must not contain the raw input",
                thrown.getMessage().contains("script"));
    }
}
