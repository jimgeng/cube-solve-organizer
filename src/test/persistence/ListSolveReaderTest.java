package persistence;

import model.ListOfSolve;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

// Tests adapted from UBC CPSC210 JsonSerializationDemo Project
public class ListSolveReaderTest {

    @Test
    public void testReadNonExistentFile() {
        ListSolveReader reader = new ListSolveReader("./data/not_a_file.json");
        try {
            ListOfSolve los = reader.read();
            fail("IOException expected");
        } catch (IOException ioe) {
            // successful test
        }
    }

    @Test
    public void testEmptyListOfSolves() {
        ListSolveReader reader = new ListSolveReader("./data/test_read_empty_los.json");
        try {
            ListOfSolve los = reader.read();
            assertEquals(0, los.length());
        } catch (IOException ioe) {
            fail("IOException raised when it should not have.");
        }
    }

    @Test
    public void testRealListOfSolves() {
        ListSolveReader reader = new ListSolveReader("./data/test_read_non_empty_los.json");
        try {
            ListOfSolve los = reader.read();
            assertEquals(3, los.length());
        } catch (IOException ioe) {
            fail("IOException raised when it should not have.");
        }
    }
}
