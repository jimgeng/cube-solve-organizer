package ui;

public class InvalidPuzzleException extends Exception {
    public InvalidPuzzleException(String puzzle) {
        super("Invalid Puzzle: " + puzzle);
    }
}
