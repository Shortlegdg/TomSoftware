package edu.epicode.tomSoftware.order;

import edu.epicode.tomSoftware.appmenu.ConsoleInput;
import edu.epicode.tomSoftware.dish.DishService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

public class DishSelector {

    private static final Logger log = Logger.getLogger(DishSelector.class.getName());

    /**
     * Represent the method that returns the chosen dishes to be printed into the file of the order.
     *
     * @param menu Refers to the Menù selected by category.
     * @param scanner The input choices to add a single dish to the order.
     *
     * @return The list of the chosen dishes to be added to the order of a table.
     */
    public List<DishService> chooseDishes(List<? extends DishService> menu, Scanner scanner) {

        List<DishService> chosenDishes = new ArrayList<>();

        System.out.println("\nType the number of the chosen dish or");
        System.out.println("type 0 to go back");

        while(true) {

            System.out.println("Selection N. : ");

            int choice;

            choice = ConsoleInput.readInt(scanner);

            if (choice == 0) {
                break;
            }
            if (choice < 1 || choice > menu.size()) {
                System.out.println("Invalid Input! Try again");
                continue;
            }

            chosenDishes.add(menu.get(choice -1));
            log.info("Dish \"" + menu.get(choice -1) + "\" added successfully");
        }
        return chosenDishes;

    }


}

