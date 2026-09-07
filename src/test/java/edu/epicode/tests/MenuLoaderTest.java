package edu.epicode.tests;

import edu.epicode.tomSoftware.dish.DishService;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import edu.epicode.tomSoftware.order.MenuLoader;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * It tests that every menu file of the restaurant can actually be loaded.
 *
 */
public class MenuLoaderTest {

    /**
     * The categories exactly as the application asks for them.
     */
    private static final String[] CATEGORIES = {
            "APPETIZERS",
            "FIRST COURSES",
            "SECOND COURSES",
            "SIDE DISHES",
            "DRINKS",
            "DESSERTS"
    };

    /**
     * Every category must give back a menu with at least one dish, and every
     * dish must carry a name and a price that make sense.
     */
    @Test
    public void everyMenuLoads() throws TomsException {

        for (String category : CATEGORIES) {

            List<DishService> menu = MenuLoader.loadMenu(category);

            assertFalse("the " + category + " menu is empty", menu.isEmpty());

            for (DishService dish : menu) {
                assertFalse("a dish of " + category + " has no name",
                        dish.getName().trim().isEmpty());
                assertTrue("a dish of " + category + " has a negative price",
                        dish.getPrice() >= 0);
            }
        }
    }

}
