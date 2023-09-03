package model;

import java.time.Duration;
import java.time.Instant;

// represents the timer to be used within the rubiks cube manager.
public class Timer {

    private Instant startTime;
    private Instant stopTime;
    private int status;

    // EFFECT: the timer is made
    public Timer() {

    }

    // MODIFIES: this
    // EFFECTS: starts a solve, and records the current instant of time that the scramble happens on.
    public void startTimer() {
        startTime = Instant.now();
    }

    // REQUIRES: startTime is not null.
    // EFFECT: returns the currently elapsed time of the active stopwatch.
    public Duration getCurrentElapsed() {
        return Duration.between(startTime, Instant.now());
    }

    // REQUIRES: startTime and stopTime is not null.
    // EFFECTS: calculates and returns the total time elapsed between
    //          the starting and stopping time.
    public Duration stopTimer() {
        if (stopTime == null) {
            stopTime = Instant.now();
        }
        return Duration.between(startTime, stopTime);
    }
}