package ui;

import model.CountDown;
import model.Solve;
import model.Timer;
import ui.panels.SolvePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

// Solve manager for the solve panel class, implemnents the main logic of the solve
public class GraphicalSolveManager implements CountDownObserver {

    private final AtomicBoolean heldLongEnough;
    private final javax.swing.Timer interactionTimer;
    private final JLabel timeDisplay;
    private final javax.swing.Timer visualTimer;
    private final SolvePanel solvePanel;
    private Solve solve;
    private Timer cubeTimer;
    private CountDown countDown;
    private int stage;

    // REQUIRES: holdtime > 0
    // EFFECTS: creates a GUI solving manager to manage keyboard interactions
    public GraphicalSolveManager(SolvePanel solvePanel, int holdTime, JLabel timeDisplay) {
        solve = getFromSolvePanel(solvePanel);
        this.stage = 0;
        this.timeDisplay = timeDisplay;
        this.solvePanel = solvePanel;
        heldLongEnough = new AtomicBoolean(false);
        interactionTimer = new javax.swing.Timer(holdTime, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GraphicalSolveManager.this.timeDisplay.setForeground(Color.GREEN);
                heldLongEnough.set(true);
            }
        });
        visualTimer = new javax.swing.Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                timeDisplay.setText(getFormattedTime(cubeTimer.getCurrentElapsed()));
            }
        });
    }

    // EFFECTS: returns the solve panel's current solve object
    private Solve getFromSolvePanel(SolvePanel solvePanel) {
        return solvePanel.getSolve();
    }

    // EFFECTS: gets the formatted time with a input Duration object
    private String getFormattedTime(Duration duration) {
        long minutes = duration.getSeconds() / 60;
        long seconds = duration.getSeconds() % 60;
        int millis = duration.getNano() / 1000000;
        if (minutes == 0) {
            return String.format("%d.%03d", seconds, millis);
        } else {
            return String.format("%d:%02d.%03d", minutes, seconds, millis);
        }
    }

    // MODIFIES: this
    // EFFECTS: handles state changes for when the spacebar is pressed down
    public void spaceDown() {
        if (stage == 0) {
            timeDisplay.setForeground(Color.GREEN);
        } else if (stage == 1) {
            timeDisplay.setForeground(Color.YELLOW);
            interactionTimer.start();
        } else if (stage == 2) {
            Duration finishTime = cubeTimer.stopTimer();
            solve.setSolveTime(finishTime);
            visualTimer.stop();
            timeDisplay.setText(getFormattedTime(finishTime));
            solve.setStatus(countDown.getStatus());
            solvePanel.gsmCompleteSolve();
        }
    }

    // MODIFIES: this
    // EFFECTS: handles state changes for when the spacebar is released
    public void spaceUp() {
        if (stage == 0) {
            stage0SpaceUp();
        } else if (stage == 1) {
            stage1SpaceUp();
        } else if (stage == 2) {
            stage = 0;
        }
    }

    // MODIFIES: this
    // EFFECTS: helper function for stage 1 of the solving process for when the spacebar is released
    private void stage1SpaceUp() {
        timeDisplay.setForeground(Color.BLACK);
        interactionTimer.stop();
        if (heldLongEnough.get() && countDown.getStatus() != 3) {
            countDown.interrupt();
            cubeTimer.startTimer();
            visualTimer.start();
            stage = 2;
        } else if (heldLongEnough.get()) {
            solve.setSolveTime(Duration.ZERO);
            solve.setStatus(3);
        }
    }

    // MODIFIES: this
    // EFFECTS: helper function for stage 2 of the solving process for when the spacebar is released
    private void stage0SpaceUp() {
        solve = getFromSolvePanel(solvePanel);
        countDown = new CountDown(this);
        cubeTimer = new Timer();
        heldLongEnough.set(false);
        timeDisplay.setForeground(Color.BLACK);
        timeDisplay.setText(String.valueOf(15));
        countDown.start();
        stage = 1;
    }

    // MODIFIES: this
    // EFFECTS: allows the inner countdown model to update the countdown on the GUI
    @Override
    public void onSecondUpdate(int secondsRemaining) {
        if (secondsRemaining == 0) {
            timeDisplay.setText("DNF");
        } else if (secondsRemaining <= 2) {
            timeDisplay.setText("+2");
        } else {
            timeDisplay.setText(String.valueOf(secondsRemaining - 2));
        }
    }
}
