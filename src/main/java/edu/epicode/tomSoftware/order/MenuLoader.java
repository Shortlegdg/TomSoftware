package edu.epicode.tomSoftware.order;

import edu.epicode.tomSoftware.AppConfig;
import edu.epicode.tomSoftware.dish.DishFactory;
import edu.epicode.tomSoftware.dish.DishService;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Represent the class that execute operation on th  menu files.
 */
public class MenuLoader {

    private static final Logger log = Logger.getLogger(MenuLoader.class.getName());

    /**
     * The method displays to the user the selected Menù, for the chosen category.
     *
     * @param category The category of the Menù to be displayed.
     *
     * @return The list of the selected Menù by category.
     */
    public static List<DishService> loadMenu(String category) throws TomsException {

        List<DishService> menu = new ArrayList<>();

        String menuCategory = category.replaceAll("\\s", "").toLowerCase();

        menu.addAll(loadFromFile(AppConfig.menuDirectory() + menuCategory + ".txt", category));

        return menu;

    }

    /**
     * Create the list of dishes on the selected Menù.
     *
     * @param filePath The path of the selected category Menù.
     * @param category The category, based on the courses, of the Menù.
     * @return Line by line the dishes of the selected Menù.
     */
    private static List<DishService> loadFromFile(String filePath, String category) throws TomsException {

        List<DishService> list = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8)) {
            String line;
            int lineNumber = 0;

            while ((line = br.readLine()) != null){
                lineNumber++;

                if (line.trim().isEmpty()) {
                    continue;
                }

                try {
                    DishService dish = DishFactory.createDish(category, line);
                    list.add(dish);

                } catch (TomsException e) {
                    log.warning("Skipping invalid entry at " + filePath + ":" + lineNumber
                            + " (" + e.getMessage() + ")");
                }
            }
        }catch(IOException e) {
            log.severe("Error reading file: " + filePath);
            throw new TomsException("Error! File not found", e);
        }

        if (list.isEmpty()) {
            throw new TomsException("The " + category + " menu is not available at the moment.");
        }

        return list;

    }

}






