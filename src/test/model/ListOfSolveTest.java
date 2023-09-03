package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class ListOfSolveTest {

    private Solve s1;
    private Solve s2;
    private Solve s3;
    private ListOfSolve list;

    @BeforeEach
    public void setup() {
        s1 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(10));
        s2 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20));
        s3 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(30));
        s1.setStatus(1);
        s2.setStatus(1);
        s3.setStatus(1);
        list = new ListOfSolve();
    }

    @Test
    public void testAddSolve() {
        assertEquals(0, list.length());
        list.addSolve(s1);
        list.addSolve(s2);
        assertEquals(2, list.length());
        assertEquals(s1, list.getSolve(0));
        assertEquals(s2, list.getSolve(1));
    }

    @Test
    public void testRemoveSolve() {
        list.addSolve(s1);
        list.addSolve(s2);
        assertEquals(2, list.length());
        assertEquals(s2, list.getSolve(1));
        list.removeSolve(1);
        assertEquals(1, list.length());
        assertEquals(s1, list.getSolve(0));
    }

    @Test
    public void testAverageOfTimeCount() {
        list.addSolve(s1);
        list.addSolve(s2);
        list.addSolve(s3);
        assertEquals(Duration.ofSeconds(25), list.averageOfTime(2));
        assertEquals(Duration.ofSeconds(20), list.averageOfTime(3));
        assertEquals(Duration.ZERO, list.averageOfTime(4));
    }

    @Test
    public void testAverageOfTimeIndex() {
        list.addSolve(s1);
        list.addSolve(s2);
        list.addSolve(s3);
        assertEquals(Duration.ofSeconds(15), list.averageOfTime(0, 1));
        assertEquals(Duration.ofSeconds(20), list.averageOfTime(0, 2));
    }
}
