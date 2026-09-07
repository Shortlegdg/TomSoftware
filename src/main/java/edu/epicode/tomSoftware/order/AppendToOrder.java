package edu.epicode.tomSoftware.order;

import edu.epicode.tomSoftware.dish.DishService;
import edu.epicode.tomSoftware.validation.InputValidator;
import edu.epicode.tomSoftware.exceptionhandling.TomsExceptionHandler;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.logging.Logger;

public class AppendToOrder {

    private static final Logger log = Logger.getLogger(AppendToOrder.class.getName());

    /**
     * This method add the chosen dishes in a formatted way into the order fils.
     * It shows the category, the name and the price of the dish.
     * @param filePath The path of the selected Menù.
     * @param chosenDishes The list oh the chosen dishes.
     */
    public void addOrder(String filePath, List<? extends DishService> chosenDishes) {


        try (PrintWriter pw = new PrintWriter(new FileWriter(filePath, StandardCharsets.UTF_8, true))) {
            for (DishService dish : chosenDishes) {
                pw.println(dish.getCategory()
                        + InputValidator.FIELD_SEPARATOR + dish.getName()
                        + InputValidator.FIELD_SEPARATOR + dish.getPrice());
            }
        } catch (IOException e) {
            log.severe("Error appending order to table");
            TomsExceptionHandler.handledException(e);
        }

    }

}
