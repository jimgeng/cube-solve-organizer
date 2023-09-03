package persistence;

import model.ListOfSolve;
import org.json.JSONArray;

import java.io.*;

// A writer that can write a list of times to disk
// Citation: idea modeled from UBC CPSC210 JsonSerializationDemo Project
public class ListSolveWriter {
    private static final int TAB = 4;
    private final String path;
    private BufferedWriter writer;

    // EFFECT: Creates a ListOfSolves data writer with the corresponding path
    public ListSolveWriter(String path) {
        this.path = path;
    }

    // MODIFIES: this
    // EFFECTS: attaches file on disk to this writer, prepares it to be written to
    // if the destination file is not found, or cannot be opened due to other issues, throw IOException
    public void open() throws IOException {
        if (!new File(path).isFile()) {
            throw new FileNotFoundException();
        }
        writer = new BufferedWriter(new FileWriter(path));
    }

    // MODIFIES: this
    // EFFECT: writes the JSON representation of the list of solves to file
    // if the file has not been attached, also attempts to first attach the file with open
    public void write(ListOfSolve los) throws IOException {
        if (writer == null) {
            open();
        }
        JSONArray json = los.toJson();
        writer.write(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECT: flushes and closes the writer
    public void close() throws IOException {
        writer.close();
    }
}