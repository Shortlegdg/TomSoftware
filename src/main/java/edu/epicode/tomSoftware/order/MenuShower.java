package edu.epicode.tomSoftware.order;

import edu.epicode.tomSoftware.dish.DishService;

import java.util.List;


public class MenuShower {

    /**
     * The method that show all the menus with a formatting string dish by dish.
     * @param menu The category of the Menù to show.
     */
    public void showMenu(List<? extends DishService> menu) {

        System.out.println("\n============= MENU ==============");
        for (int i = 0; i < menu.size(); i++) {
            DishService dish = menu.get(i);

            System.out.println(i + 1 + ") " + dish.getCategory() + " - " + dish.getName()
                    + " - " + dish.getPrice() + "\u20ac   [" + dish.getStation() + "]");

        }
    }
}
