package edu.epicode.tomSoftware.appmenu;

/**
 * Represents a class to call everytime we want to display the two most important menus.
 */
public class AppMenu {

    /**
     * Main menu is called to show the first Menù of the entire App.
     */
    public void mainMenu() {
        System.out.println("\n====== Table Order Management Software ======");
        System.out.println("-                                           -");
        System.out.println("-------- Type 1 to insert a new order -------");
        System.out.println("-                                           -");
        System.out.println("---- Type 2 to display an existing order ----");
        System.out.println("-                                           -");
        System.out.println("----- Type 3 to display all the orders ------");
        System.out.println("-                                           -");
        System.out.println("----- Type 4 to close an existing table -----");
        System.out.println("-                                           -");
        System.out.println("-                                           -");
        System.out.println("========= Type 0 to EXIT the program ========");
    }

    /**
     * Sub menu is called to show the categories of the courses available on the Menù.
     */
    public void subMenu() {
        System.out.println("\nType the menu to open: ");
        System.out.println("- 1) Appetizers");
        System.out.println("- 2) First Courses ");
        System.out.println("- 3) Second Courses ");
        System.out.println("- 4) Side dishes ");
        System.out.println("- 5) Drinks ");
        System.out.println("- 6) Desserts ");
        System.out.println("\n");
        System.out.println("- 0) RETURN TO MAIN MENU ");
    }

}


