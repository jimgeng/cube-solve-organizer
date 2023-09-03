package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ui.CountDownObserver;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CountDownTest {

    private CountDown cd;
    private Thread cdThread;

    @BeforeEach
    public void setup() {
        cd = new CountDown(new CountDownObserver() {
            @Override
            public void onSecondUpdate(int secondsRemaining) {}
        });
    }

    @Test
    public void testNoCD() {
        cd.interrupt();
        assertEquals(0, cd.getStatus());
    }

    @Test
    public void testPre15CD() throws InterruptedException {
        cd.start();
        Thread.sleep(4000);
        cd.interrupt();
        assertEquals(1, cd.getStatus());
    }

    @Test
    public void testPre17CD() throws InterruptedException {
        cd.start();
        Thread.sleep(16000);
        cd.interrupt();
        assertEquals(2, cd.getStatus());
    }

    @Test
    public void testPost17CD() throws InterruptedException {
        cd.start();
        Thread.sleep(18000);
        cd.interrupt();
        assertEquals(3, cd.getStatus());
    }
}
