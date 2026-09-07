package edu.epicode.tomSoftware.apptables;

import edu.epicode.tomSoftware.compositeImpl.MenuComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * The table Iterator class.
 */
public class TableIterator implements Iterator<MenuComponent> {

    private final List<MenuComponent> tables;
    private int index = 0;

    /**
     * @param tables The tables to walk through. The list is copied, so opening
     *               or closing a table while the iteration is running does not
     *               break the iterator.
     */
    public TableIterator(List<MenuComponent> tables) {
        this.tables = new ArrayList<>();
        if (tables != null) {
            this.tables.addAll(tables);
        }
    }

    @Override
    public boolean hasNext() {
        return index < tables.size();
    }

    /**
     * @return The next table of the iteration.
     * @throws NoSuchElementException If the iteration is over. This is the
     *         contract every iterator has to respect: before, the method threw
     *         IndexOutOfBoundsException, which says nothing to the caller.
     */
    @Override
    public MenuComponent next() {

        if (!hasNext()) {
            throw new NoSuchElementException("There are no more tables to iterate.");
        }

        return tables.get(index++);
    }

}
