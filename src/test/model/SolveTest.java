package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;

import java.time.Duration;

class SolveTest {
    private Solve threeByThreeSolve;

    @BeforeEach
    public void setup() {
        threeByThreeSolve = new Solve(PuzzleRegistry.THREE);
    }

    @Test
    public void testGenerateScramble() {
        String scramble = threeByThreeSolve.getScramble();
        // generate new scramble
        threeByThreeSolve.generateScramble();
        String newScramble = threeByThreeSolve.getScramble();
        // check scramble is now updated.
        assertNotEquals(scramble, newScramble);
    }

    @Test
    public void testGetCube() {
        assertEquals(PuzzleRegistry.THREE, threeByThreeSolve.getCube());
    }

    @Test
    public void testNotEqualsByNullAndClassType() {
        assertFalse(threeByThreeSolve.equals(null));
        assertFalse(threeByThreeSolve.equals(1));
    }

    @Test
    public void testEqualsAndHashCode() {
        Solve s1 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Specific Scramble");
        Solve s2 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Specific Scramble");
        assertEquals(s1, s2);
        assertNotSame(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    public void testNotEquals() {
        Solve s1 = new Solve(PuzzleRegistry.FOUR, Duration.ofSeconds(20), 1, "Specific Scramble");
        Solve s2 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Specific Scramble");
        assertNotEquals(s1, s2);
        s1 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(19), 1, "Specific Scramble");
        s2 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Specific Scramble");
        assertNotEquals(s1, s2);
        s1 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 2, "Specific Scramble");
        s2 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Specific Scramble");
        assertNotEquals(s1, s2);
        s1 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Other Scramble");
        s2 = new Solve(PuzzleRegistry.THREE, Duration.ofSeconds(20), 1, "Specific Scramble");
        assertNotEquals(s1, s2);
    }

    @Test
    public void testGetSolveTime() {
        // normal time
        threeByThreeSolve.setSolveTime(Duration.ofSeconds(15));
        threeByThreeSolve.setStatus(1);
        assertEquals(Duration.ofSeconds(15), threeByThreeSolve.getSolveTime());
        assertEquals(1, threeByThreeSolve.getStatus());
        // +2 second penalty
        threeByThreeSolve.setStatus(2);
        assertEquals(Duration.ofSeconds(17), threeByThreeSolve.getSolveTime());
        assertEquals(2, threeByThreeSolve.getStatus());
        // DNF penalty
        threeByThreeSolve.setStatus(3);
        assertEquals(Duration.ZERO, threeByThreeSolve.getSolveTime());
        assertEquals(3, threeByThreeSolve.getStatus());
        // erroneous status
        threeByThreeSolve.setStatus(0);
        assertNull(threeByThreeSolve.getSolveTime());
        assertEquals(0, threeByThreeSolve.getStatus());
    }

    @Test
    public void testGetFormattedTime() {
        threeByThreeSolve.setSolveTime(Duration.ofMillis(10500));
        threeByThreeSolve.setStatus(1);
        assertEquals("10.500", threeByThreeSolve.getFormattedTime());
        threeByThreeSolve.setStatus(2);
        assertEquals("12.500", threeByThreeSolve.getFormattedTime());
        threeByThreeSolve.setSolveTime(Duration.ofSeconds(75));
        threeByThreeSolve.setStatus(1);
        assertEquals("1:15.000", threeByThreeSolve.getFormattedTime());
    }
}