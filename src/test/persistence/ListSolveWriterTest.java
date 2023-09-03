package persistence;

import model.ListOfSolve;
import model.Solve;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;

import java.io.IOException;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

// Tests adapted from UBC CPSC210 JsonSerializationDemo Project
public class ListSolveWriterTest {

    @Test
    public void testWriterInvalidFile() {
        ListSolveWriter writer = new ListSolveWriter("./data/not_a_file.json");
        try {
            writer.open();
            fail("IOException expected.");
        } catch (IOException ioe) {
            // test passes
        }
    }

    @Test
    public void testWriterEmptyList() {
        ListSolveWriter writer = new ListSolveWriter("./data/test_write_empty_list.json");
        try {
            ListOfSolve los = new ListOfSolve();
            writer.write(los);
            writer.close();

            ListSolveReader reader = new ListSolveReader("./data/test_write_empty_list.json");
            ListOfSolve los2 = reader.read();
            assertEquals(0, los.length());
        } catch (IOException ioe) {
            fail("IOException not expected: " + ioe.getMessage());
        }
    }

    @Test
    public void testWriterNonEmptyList() {
        ListSolveWriter writer = new ListSolveWriter("./data/test_write_non_empty_list.json");
        try {
            ListOfSolve los = new ListOfSolve();
            // Create sample solves
            Solve three = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(30), 1, "Test1");
            Solve four = new Solve(PuzzleRegistry.FOUR, Duration.ofSeconds(40), 2, "Test2");
            Solve five = new Solve(PuzzleRegistry.FIVE, Duration.ofSeconds(50), 3, "Test3");
            // Add to list
            los.addSolve(three);
            los.addSolve(four);
            los.addSolve(five);
            // Main writing stage
            writer.open();
            writer.write(los);
            writer.close();

            ListSolveReader reader = new ListSolveReader("./data/test_write_non_empty_list.json");
            ListOfSolve los2 = reader.read();
            // check with original objects
            assertEquals(los2.getSolve(0), three);
            assertEquals(los2.getSolve(1), four);
            assertEquals(los2.getSolve(2), five);
        } catch (IOException ioe) {
            fail("IOException not expected: " + ioe.getMessage());
        }
    }
}
