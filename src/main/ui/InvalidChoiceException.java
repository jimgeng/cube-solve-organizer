package ui;

public class InvalidChoiceException extends Exception {
    public InvalidChoiceException(String puzzle) {
        super("Invalid Choice: " + puzzle);
    }
}
