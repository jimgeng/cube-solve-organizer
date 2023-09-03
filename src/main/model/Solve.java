package model;

//import org.worldcubeassociation.tnoodle.scrambles.Puzzle;

import org.json.JSONObject;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;

import java.time.Duration;
import java.util.Objects;

// represents a user's solve that they have done.
// includes metadata such as time taken, scramble used, type of solve, etc.
public class Solve {
    private final PuzzleRegistry cube;
    private Duration solveTime;
    private String scramble;

    // solve result status codes:
    //  3 = DNF penalty
    //  2 = +2 second penalty
    //  1 = Normally finished solve
    //  0 = Not yet finished / started solve
    private int status;

    // EFFECTS: cube is set to the type specified by cubeType.
    public Solve(PuzzleRegistry cube) {
        this.cube = cube;
        this.status = 0;
        this.solveTime = Duration.ZERO;
        this.scramble = cube.getScrambler().generateScramble();
    }

    // EFFECTS: cube is set ot the type sepcified by cubeType, and the solveTime is
    //          pre set to the solve time provided in the method parameter.
    public Solve(PuzzleRegistry cube, Duration solveTime) {
        this.cube = cube;
        this.status = 0;
        this.solveTime = solveTime;
        this.scramble = cube.getScrambler().generateScramble();
    }

    // REQUIRES: 1 <= status <= 3, scramble is syntactically correct.
    // EFFECTS: every single state of the solve is set to their pre-defined values
    //          in the constructor. Used currently only for persistence purposes.
    public Solve(PuzzleRegistry cube, Duration solveTime, int status, String scramble) {
        this.cube = cube;
        this.status = status;
        this.solveTime = solveTime;
        this.scramble = scramble;
    }

    // EFFECTS: returns a string representation of the scramble used in the solve.
    public String getScramble() {
        return scramble;
    }

    // EFFECTS: returns a PuzzleRegistry Enum of the cube type.
    public PuzzleRegistry getCube() {
        return cube;
    }

    // MODIFIES: this
    // EFFECTS: generates a new WCA approved cube scramble for this solve.
    public void generateScramble() {
        scramble = cube.getScrambler().generateScramble();
    }

    // REQUIRES: solveTime is not null.
    // EFFECTS: returns the amount of solve time taken as a Java Duration object.
    //          also takes into account any +2 second penalties.
    //          returns a 0 duration time if the solve has a DNF status.
    public Duration getSolveTime() {
        switch (status) {
            case 1:
                return solveTime;
            case 2:
                return solveTime.plus(Duration.ofSeconds(2));
            case 3:
                return Duration.ZERO;
            default:
                return null;
        }
    }

    // MODIFIES: this
    // EFFECTS: sets a custom solveTime for this solve.
    public void setSolveTime(Duration newTime) {
        solveTime = newTime;
    }

    // REQUIRES: newStatus must be either 0, 1, or 2.
    // MODIFIES: this
    // EFFECTS: sets the status of the solve for manual penalty application.
    public void setStatus(int newStatus) {
        status = newStatus;
        EventLog.getInstance().logEvent(new Event("Solve status of a solve changed"));
    }

    // EFFECTS: gets the current status of this solve.
    public int getStatus() {
        return status;
    }

    // EFFECTS: gets a string that prints out the solveTime in a nicely formatted style.
    public String getFormattedTime() {
        Duration actualTime = getSolveTime();
        long minutes = actualTime.getSeconds() / 60;
        long seconds = actualTime.getSeconds() % 60;
        int millis = actualTime.getNano() / 1000000;
        if (minutes == 0) {
            return String.format("%d.%03d", seconds, millis);
        } else {
            return String.format("%d:%02d.%03d", minutes, seconds, millis);
        }
    }

    // EFFECTS: returns this list of solves as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("cube", cube);
        json.put("status", status);
        json.put("time", solveTime);
        json.put("scramble", scramble);
        return json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Solve solve = (Solve) o;
        boolean timeEquals = Objects.equals(solveTime, solve.solveTime);
        boolean scrambleEquals = Objects.equals(scramble, solve.scramble);
        return status == solve.status && cube == solve.cube && timeEquals && scrambleEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cube, solveTime, scramble, status);
    }
}
