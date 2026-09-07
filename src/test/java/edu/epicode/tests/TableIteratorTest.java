package edu.epicode.tests;

import edu.epicode.tomSoftware.apptables.Iterator;
import edu.epicode.tomSoftware.apptables.TableIterator;
import edu.epicode.tomSoftware.compositeImpl.MenuComponent;
import edu.epicode.tomSoftware.compositeImpl.TableComposite;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

/**
 * Tests the Iterator pattern.
 */
public class TableIteratorTest {

    /**
     * @return A list holding two tables, used as the aggregate to walk through.
     */
    private List<MenuComponent> twoTables() {

        List<MenuComponent> tables = new ArrayList<>();
        tables.add(new TableComposite("Table1"));
        tables.add(new TableComposite("Table2"));

        return tables;
    }

    /**
     * The iterator visits every element of the aggregate, without the caller
     * knowing how the tables are stored.
     */
    @Test
    public void visitsEveryElement() {

        Iterator<MenuComponent> iterator = new TableIterator(twoTables());

        int visited = 0;
        while (iterator.hasNext()) {
            assertNotNull(iterator.next());
            visited++;
        }

        assertEquals(2, visited);
    }

    /**
     * Asking for an element past the last one must throw the exception the
     * contract of an iterator prescribes.
     */
    @Test
    public void refusesToGoPastTheLastElement() {

        Iterator<MenuComponent> iterator = new TableIterator(twoTables());
        iterator.next();
        iterator.next();

        assertThrows(NoSuchElementException.class, iterator::next);
    }
}
