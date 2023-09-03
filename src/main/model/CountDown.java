package model;

import ui.CountDownObserver;

import java.time.Duration;
import java.time.Instant;

public class CountDown {

    private final int duration = 17;
    private final Thread updateThread;

    private Instant dnfTime;
    private Instant plusTwoTime;
    private int status;
    // status codes:
    //  3 = DNF penalty for interrupting after 17 seconds
    //  2 = +2 second penalty for interrupting after 15 seconds
    //  1 = Normally interrupted timer
    //  0 = Not yet finished / started timer

    // EFFECTS: Creates a new countdown object with a thread to update a UI component
    public CountDown(CountDownObserver ui) {
        this.status = 0;
        updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = duration; i >= 0; i--) {
                    try {
                        ui.onSecondUpdate(i);
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        return;
                    }
                }
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: starts the thread that provides info to any CountDownObserver object
    //          to be implemented by some UI interface to recieve information on the
    //          countdown status. also records the future ending time of the countdown
    //          to be used for DNF and +2 penalty calculation.
    public void start() {
        dnfTime = Instant.now().plus(Duration.ofSeconds(duration));
        plusTwoTime = Instant.now().plus(Duration.ofSeconds(duration - 2));
        updateThread.start();
    }

    // MODIFIES: this
    // EFFECTS: stops the countdown if it running (and stops givin info to the UI),
    //          and depending on when it is stopped sets the correct status based
    //          on amount of time left when stopping. If the countdown isn't running
    //          do nothing.
    public void interrupt() {
        Instant stopTime = Instant.now();
        if (dnfTime == null) {
            return;
        }
        if (stopTime.isAfter(dnfTime)) {
            status = 3;
        } else {
            updateThread.interrupt();
            if (stopTime.isAfter(plusTwoTime)) {
                status = 2;
            } else {
                status = 1;
            }
        }
    }

    // EFFECTS: gets the current status of the countdown
    public int getStatus() {
        return status;
    }
}
