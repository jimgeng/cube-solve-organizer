package persistence;

import model.ListOfSolve;
import model.Solve;
import org.json.JSONArray;
import org.json.JSONObject;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;

// A reader that can read a list of times from disk
// Citation: idea modeled from UBC CPSC210 JsonSerializationDemo Project
public class ListSolveReader {

    private final String path;

    // EFFECT: Creates a ListOfSolves data reader with the corresponding path;
    public ListSolveReader(String path) {
        this.path = path;
    }

    // EFFECT: reads source json file and imports the ListOfSolves data from it
    // will throw IOException if there are any errors in reading the file.
    public ListOfSolve read() throws IOException {
        String jsonString = readFile(path);
        JSONArray jsonArray = new JSONArray(jsonString);
        ListOfSolve los = new ListOfSolve();
        parseSolves(los, jsonArray);
        return los;
    }

    // EFFECTS: reads file and returns all content as one string
    private String readFile(String path) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            return builder.toString();
        }
    }

    // MODIFIES: los
    // EFFECTS: parses json and addes them to the list of solves
    private void parseSolves(ListOfSolve los, JSONArray json) {
        for (Object o : json) {
            JSONObject solveJson = (JSONObject) o;
            parseSolve(los, solveJson);
        }
    }

    // MODIFIES: los
    // EFFECTS: parses individual solve and adds it to the list of solves
    private void parseSolve(ListOfSolve los, JSONObject solveJson) {
        int status = solveJson.getInt("status");
        Duration duration = Duration.parse(solveJson.getString("time"));
        String scramble = solveJson.getString("scramble");
        PuzzleRegistry cube = PuzzleRegistry.valueOf(solveJson.getString("cube"));
        los.addSolve(new Solve(cube, duration, status, scramble));
    }
}
