package edu.epicode.tests;

import edu.epicode.tomSoftware.AppConfig;
import edu.epicode.tomSoftware.apptables.TableMapCollection;
import edu.epicode.tomSoftware.apptables.TableWriter;
import edu.epicode.tomSoftware.exceptionhandling.TomsException;
import org.junit.After;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the file based persistence: opening a table writes its file, closing it
 * removes both the file and the entry from the collection.
 */
public class TableFileTest {

    /**
     * A table number reserved for these tests.
     */
    private static final int TEST_TABLE_ID = 999;

    /**
     * @return The file the tested table is written into.
     */
    private File tableFile() {
        return new File(AppConfig.tablesDirectory() + "table" + TEST_TABLE_ID + ".txt");
    }

    /**
     * Opening a table has to create its file, with the header written in.
     */
    @Test
    public void openingATableCreatesItsFile() throws TomsException {

        TableWriter.createTableFile(TEST_TABLE_ID, 5);

        assertTrue("the table file must be created", tableFile().exists());
        assertTrue("the table file must contain its header", tableFile().length() > 0);
    }

    /**
     * Closing a table has to remove it from the collection and delete its file.
     */
    @Test
    public void closingATableDeletesItsFile() throws TomsException {

        TableWriter.createTableFile(TEST_TABLE_ID, 4);

        TableMapCollection collection = new TableMapCollection();

        assertTrue("closeTable must report the table as closed",
                collection.deleteById(TEST_TABLE_ID));
        assertFalse("the file must be gone after the table is closed", tableFile().exists());
    }

    /**
     * Removes the file if a test failed before deleting it.
     */
    @After
    public void removeTheCreatedFile() {

        File file = tableFile();

        if (file.exists()) {
            file.delete();
        }
    }
}
