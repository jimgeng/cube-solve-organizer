package model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.Duration;
import java.util.ArrayList;

// represents multiple solves that a user has done preivously.
public class ListOfSolve {

    private final ArrayList<Solve> solves;

    // EFFECTS: the new list of solves is initialized to be empty.
    public ListOfSolve() {
        solves = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: adds a new solve to the list of all solves.
    public void addSolve(Solve solve) {
        solves.add(solve);
        EventLog.getInstance().logEvent(new Event("New solve added to user solve list."));
    }

    // REQUIRES: 0 <= index < size of list
    // MODIFIES: this
    // EFFECTS: removes the specified solve at the specified index.
    public void removeSolve(int index) {
        solves.remove(index);
        EventLog.getInstance().logEvent(new Event("Solve deleted from user solve list."));
    }

    // REQUIRES: 0 <= index < size of list
    // EFFECTS: gets the specified solve at the specified index.
    public Solve getSolve(int index) {
        return solves.get(index);
    }

    // EFFECTS: returns the amount of solves in the current list
    public int length() {
        return solves.size();
    }

    // EFFECTS: returns the average duration of solve of the solves
    //          of the {count} most recent solves.
    //          IF the number of solves in the list is smaller than the
    //          count of the method, produce 0
    public Duration averageOfTime(int count) {
        if (solves.size() < count) {
            return Duration.ZERO;
        }
        Duration total = Duration.ZERO;
        for (int i = 0; i < count; i++) {
            total = total.plus(solves.get(solves.size() - i - 1).getSolveTime());
        }
        return total.dividedBy(count);
    }

    // REQUIRES: 0 <= startIndex < endIndex < size of list
    // EFFECTS: returns the average duration of solve of the solves
    //          specified by the starting and ending indexes.
    //          IF the number of solves in the list is smaller than the
    //          amount of solves of the method, produce 0
    public Duration averageOfTime(int startIndex, int endIndex) {
        Duration total = Duration.ZERO;
        for (int i = startIndex; i <= endIndex; i++) {
            total = total.plus(solves.get(i).getSolveTime());
        }
        return total.dividedBy(endIndex - startIndex + 1);
    }

    // EFFECTS: returns this list of solves as a JSON array
    public JSONArray toJson() {
        JSONArray list = new JSONArray();
        for (Solve s : solves) {
            list.put(s.toJson());
        }
        return list;
    }
}
