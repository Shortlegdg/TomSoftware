package edu.epicode.tomSoftware.appmenu;

import java.util.Scanner;

/**
 * Reads the numbers typed by the waiter.
 */
public class ConsoleInput {

    /**
     * Utility class: it must not be instantiated.
     */
    private ConsoleInput() {
    }

    /**
     * Asks for a number until the waiter types one.
     * @param scanner The scanner reading the console.
     * @return The number typed by the waiter.
     */
    public static int readInt(Scanner scanner) {

        while (!scanner.hasNextInt()) {
            System.out.println("Invalid choice! Type a numeric input");
            scanner.next();
        }

        return scanner.nextInt();
    }
}
