package edu.epicode.tomSoftware;

import edu.epicode.tomSoftware.appmenu.AppMenu;
import edu.epicode.tomSoftware.appmenu.ConsoleInput;
import edu.epicode.tomSoftware.apptables.TableMapCollection;
import edu.epicode.tomSoftware.apptables.TablesBrowser;
import edu.epicode.tomSoftware.apptables.TableWriter;
import edu.epicode.tomSoftware.dish.DishService;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import edu.epicode.tomSoftware.order.AppendToOrder;
import edu.epicode.tomSoftware.order.DishSelector;
import edu.epicode.tomSoftware.order.MenuLoader;
import edu.epicode.tomSoftware.order.MenuShower;
import edu.epicode.tomSoftware.validation.InputValidator;

import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The Table Order Management Software Main Application.
 * A simple software to manage the tables of a restaurant
 * and create orders by selecting dishes right from the loaded menus.
 */
public class TomsApp {

    private static MenuShower shower = new MenuShower();
    private static DishSelector selector = new DishSelector();

    private static final Logger log = Logger.getLogger(TomsApp.class.getName());

    /**
     * The entry point of the application, and the single boundary where the
     * exceptions are shielded.
     */
    public static void main( String[] args ) {

        try {
            LoggingConfig.loggingSetup(); //Logging setup
        } catch (TomsException e) {
            System.out.println("The application could not start: " + e.getMessage());
            return;
        }

        try {
            run();
        } catch (TomsException e) {
            log.log(Level.SEVERE, "The application stopped after a handled error", e);
            System.out.println("\n" + e.getMessage());
        } catch (RuntimeException e) {
            log.log(Level.SEVERE, "Unexpected failure", e);
            System.out.println("\nAn unexpected error occurred. Please contact the support.");
        }
    }

    private static void run() throws TomsException {

        Scanner scanner = new Scanner(System.in);

        AppMenu m = new AppMenu();

        m.mainMenu(); //Loading the main Menù

        int menuChoice;

        menuChoice = ConsoleInput.readInt(scanner);

        while (menuChoice != 0) {

            if (menuChoice > 4) {
                System.out.println("Invalid Input! Try again");
                m.mainMenu();

                menuChoice = ConsoleInput.readInt(scanner);
            }

            if (menuChoice == 1) { //Menù to open a new table and creating its own file

                System.out.println("============= Insert a New Order ============");
                System.out.println("\nIndicate the table ID or");
                System.out.println("type 0 to return to main menu ");

                int waiterInput;

                waiterInput = ConsoleInput.readInt(scanner);

                if (waiterInput == 0) {
                    m.mainMenu();
                    menuChoice = ConsoleInput.readInt(scanner);

                    continue;

                }else {
                    int tableID = InputValidator.validateTableId(waiterInput);
                    System.out.println("\nTableID: " + tableID + " created successfully!");
                    System.out.println("\nIndicate the number of people seating this table: ");

                    int peopleNumber; //Indicating how many people are sitting to this table

                    peopleNumber = InputValidator.validatePeopleCount(ConsoleInput.readInt(scanner));
                    TableWriter.createTableFile(tableID, peopleNumber); //Table opening and file creation
                }

                m.subMenu();
                menuChoice = ConsoleInput.readInt(scanner);

                while (menuChoice != 0) {

                    if (menuChoice == 1) { //Menù for the Appetizers

                        List<DishService> menu = MenuLoader.loadMenu("APPETIZERS");
                        shower.showMenu(menu);
                        List<DishService> chosenDishes = selector.chooseDishes(menu, scanner);
                        String filePath = TableWriter.getFileName();
                        AppendToOrder append = new AppendToOrder();
                        append.addOrder(filePath, chosenDishes);

                        System.out.println("Order Printed!");

                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else if (menuChoice ==2) { //Menù for the First Courses

                        List<DishService> menu = MenuLoader.loadMenu("FIRST COURSES");
                        shower.showMenu(menu);
                        List<DishService> chosenDishes = selector.chooseDishes(menu, scanner);
                        String filePath = TableWriter.getFileName();
                        AppendToOrder append = new AppendToOrder();
                        append.addOrder(filePath, chosenDishes);

                        System.out.println("Order Printed!");

                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else if (menuChoice == 3) { //Menù for the Second Courses

                        List<DishService> menu = MenuLoader.loadMenu("SECOND COURSES");
                        shower.showMenu(menu);
                        List<DishService> chosenDishes = selector.chooseDishes(menu, scanner);
                        String filePath = TableWriter.getFileName();
                        AppendToOrder append = new AppendToOrder();
                        append.addOrder(filePath, chosenDishes);

                        System.out.println("Order Printed!");

                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else if (menuChoice == 4) { //Menù for the Side Dishes

                        List<DishService> menu = MenuLoader.loadMenu("SIDE DISHES");
                        shower.showMenu(menu);
                        List<DishService> chosenDishes = selector.chooseDishes(menu, scanner);
                        String filePath = TableWriter.getFileName();
                        AppendToOrder append = new AppendToOrder();
                        append.addOrder(filePath, chosenDishes);

                        System.out.println("Order Printed!");

                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else if (menuChoice == 5) { //Menù for the Drinks

                        List<DishService> menu = MenuLoader.loadMenu("DRINKS");
                        shower.showMenu(menu);
                        List<DishService> chosenDishes = selector.chooseDishes(menu, scanner);
                        String filePath = TableWriter.getFileName();
                        AppendToOrder append = new AppendToOrder();
                        append.addOrder(filePath, chosenDishes);

                        System.out.println("Order Printed!");

                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else if (menuChoice == 6) { //Menù for the Desserts

                        List<DishService> menu = MenuLoader.loadMenu("DESSERTS");
                        shower.showMenu(menu);
                        List<DishService> chosenDishes = selector.chooseDishes(menu, scanner);
                        String filePath = TableWriter.getFileName();
                        AppendToOrder append = new AppendToOrder();
                        append.addOrder(filePath, chosenDishes);

                        System.out.println("Order Printed!");

                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else {
                        System.out.println("Invalid Choice! Please try again");
                        m.subMenu();
                        menuChoice = ConsoleInput.readInt(scanner);
                    }
                }
                m.mainMenu();
                menuChoice = ConsoleInput.readInt(scanner);

            } else if (menuChoice == 2) { //Menu choice to display the tables opened and to watch its own order

                TableMapCollection collection = new TableMapCollection();

                collection.getOpenedTables();
               
                System.out.println("\nType the number of the table you want to visit");
                System.out.println("or 0 to go back to main menu");

                int tableID;

                tableID = ConsoleInput.readInt(scanner);

                if (tableID != 0) {
                    InputValidator.validateTableId(tableID);
                    TablesBrowser.enterTable(tableID);
                    System.out.println("\nType 0 to go back to main menu");

                    int waiterInput;

                    waiterInput = ConsoleInput.readInt(scanner);
                    if (waiterInput == 0) {
                        m.mainMenu();
                        menuChoice = ConsoleInput.readInt(scanner);
                    } else {
                        System.out.println("Invalid input! Try again");
                    }
                } else if (tableID == 0) {
                    m.mainMenu();
                    menuChoice = ConsoleInput.readInt(scanner);
                } else{
                    System.out.println("Invalid input! Try again");
                }

            } else if (menuChoice == 3) { //Menu choice to see all the orders for all the tables

                TablesBrowser.listTables();

                System.out.println("\nType 0 to go back to Main Menu: ");

                int waiterInput;

                waiterInput = ConsoleInput.readInt(scanner);
                if (waiterInput == 0) {
                    m.mainMenu();
                    menuChoice = ConsoleInput.readInt(scanner);

                }else {
                    System.out.println("Invalid input, try again");

                }
            } else if (menuChoice == 4) { //Menu choice to close a desired table and deleting its file

                TableMapCollection collection = new TableMapCollection();

                collection.getOpenedTables();

                System.out.println("\nType the number of the table you want to CLOSE or 0");
                System.out.println("to go back to main menu");

                int tableID;

                tableID = ConsoleInput.readInt(scanner);

                if (tableID != 0) {

                    InputValidator.validateTableId(tableID);

                    System.out.println("\nAre you sure to close the table " + tableID + "?");
                    System.out.println("\nType YES or NO");

                    String waiterInput = scanner.next();

                    if (waiterInput.equalsIgnoreCase("yes")) {

                            TablesBrowser.closeTable(tableID);

                    } else if (waiterInput.equalsIgnoreCase("no")) {
                        m.mainMenu();
                        menuChoice = ConsoleInput.readInt(scanner);

                    } else {
                        System.out.println("Invalid input! Try again");
                    }
                } else if (tableID == 0) {
                    m.mainMenu();
                    menuChoice = ConsoleInput.readInt(scanner);

                } else {
                    System.out.println("Invalid input! Try again");
                }
            }
        }
    }
}

