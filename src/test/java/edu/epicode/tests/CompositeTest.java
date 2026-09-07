package edu.epicode.tests;

import edu.epicode.tomSoftware.compositeImpl.MenuComponent;
import edu.epicode.tomSoftware.compositeImpl.TableComposite;
import edu.epicode.tomSoftware.compositeImpl.TableOrder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Tests the Composite pattern.
 */
public class CompositeTest {

    /**
     * The tolerance used when comparing two amounts, since the prices are
     * stored as double.
     */
    private static final double DELTA = 0.001;

    /**
     * The operation that justifies the pattern: the total is computed by
     * walking the structure, and a table nested inside another one is treated
     * exactly like a single order line, without the caller knowing which is
     * which.
     */
    @Test
    public void theTotalIsComputedRecursively() {

        TableComposite shared = new TableComposite("Shared");
        shared.add(TableOrder.fromLine("First Courses;Carbonara;12.0"));
        shared.add(TableOrder.fromLine("First Courses;Amatriciana;11.0"));

        TableComposite table = new TableComposite("Table1");
        table.add(TableOrder.fromLine("Drinks;Wine;5.0"));
        table.add(shared);

        assertEquals(28.0, table.getTotal(), DELTA);
    }

    /**
     * The leaf shares the interface of the composite but refuses to contain
     * other components: it is the known trade-off of the transparent Composite.
     */
    @Test
    public void aLeafRefusesChildren() {

        MenuComponent leaf = TableOrder.fromLine("Drinks;Water;3.0");

        assertThrows(UnsupportedOperationException.class,
                () -> leaf.add(TableOrder.fromLine("Drinks;Beer;5.0")));
    }
}
