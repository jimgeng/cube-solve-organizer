package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class TimerTest {
    private Timer testTimer;

    @BeforeEach
    public void setup() {
        testTimer = new Timer();
    }

    @Test
    public void testGetCurrentElapsed() throws InterruptedException {
        testTimer.startTimer();
        Thread.sleep(3000);
        Duration snapshot = testTimer.getCurrentElapsed();
        // accuracy test, the timer has a 5% error threshhold
        assertTrue(ErrorBelow(0.05, 3000, (int) snapshot.toMillis()));
    }

    @Test
    public void testGetResultingElapsed() throws InterruptedException {
        testTimer.startTimer();
        Thread.sleep(3000);
        Duration endTime = testTimer.stopTimer();
        Thread.sleep(1000);
        Duration secondEndTIme = testTimer.stopTimer();
        assertEquals(endTime, secondEndTIme);
    }

    private boolean ErrorBelow(double targetError, int expectedValue, int actualValue) {
        int absoluteDiff = actualValue - expectedValue;
        double percentError = ((double) absoluteDiff) / expectedValue;
        return percentError < targetError;
    }
}
