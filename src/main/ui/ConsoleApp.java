package ui;

import model.CountDown;
import model.ListOfSolve;
import model.Solve;
import model.Timer;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;
import persistence.ListSolveReader;
import persistence.ListSolveWriter;

import java.io.IOException;
import java.time.Duration;
import java.util.InputMismatchException;
import java.util.Scanner;

// A console user interface for the rubiks cube timer application.
public class ConsoleApp implements CountDownObserver {

    private final Scanner userInput;
    private ListOfSolve solveList;

    // EFFECTS: all console apps will use a single scanner, and a single list of solves
    public ConsoleApp() {
        userInput = new Scanner(System.in);
        solveList = new ListOfSolve();
    }

    // EFFECTS: runs the setup, and loops the loop function until the loop triggers exit.
    public void run() {
        setup();
        while (true) {
            loop();
        }
    }

    // REQUIRES: secondsRemaining > 0
    // EFFECTS: prints to console when function is run with how many seconds remaining in countdown.
    @Override
    public void onSecondUpdate(int secondsRemaining) {
        if (secondsRemaining == 0) {
            System.out.println("\rYour solve has been marked as DNF. Press enter to continue. >> ");
        } else if (secondsRemaining <= 2) {
            System.out.printf("\rYou are %d seconds overtime, +2 penalty applied. >> ", - (secondsRemaining - 2));
        } else {
            System.out.printf("\rThere are %d seconds remaining. >> ", secondsRemaining - 2);
        }
    }

    // EFFECTS: setup method, prompting the user to choose if they want to load previous data
    private void setup() {
        System.out.println("Welcome ot the Console Based Rubik's Cube Timer");
        System.out.print("Would you like to load your previous data? (\"y\" to load) >> ");
        String selection = userInput.nextLine();
        try {
            if (selection.equalsIgnoreCase("y")) {
                ListSolveReader reader = new ListSolveReader("./data/solves.json");
                solveList = reader.read();
            }
        } catch (IOException ioe) {
            System.out.println("File failed to be read, exiting program...");
            System.exit(1);
        }
    }

    // EFFECTS: main loop function waits for user to select a mode, or to exit the application.
    private void loop() {
        System.out.println("Please selection your option:");
        System.out.print(" 1: New Solve\n 2: View History\n 3: Exit\n >> ");
        int selection = getValidSelectionInput();
        if (selection == 1) {
            solveMode();
        } else if (selection == 2) {
            historyMode();
        } else if (selection == 3) {
            promptSaveAndClose();

        } else {
            System.out.println("Selection not valid, please try again.");
        }
    }

    private void promptSaveAndClose() {
        System.out.print("Would you like to save your current list of times to file? (\"y\" to save) >> ");
        String selection = userInput.nextLine();
        try {
            if (selection.equalsIgnoreCase("y")) {
                ListSolveWriter writer = new ListSolveWriter("./data/solves.json");
                writer.write(solveList);
                writer.close();
            }
        } catch (IOException ioe) {
            System.out.println("File failed to be saved.");
            return;
        }
        userInput.close();
        System.exit(0);
    }

    // EFFECTS: Obtains valid numerical input from the user.
    private int getValidSelectionInput() {
        int choice;
        while (true) {
            try {
                choice = userInput.nextInt();
                userInput.nextLine();
                return choice;
            } catch (InputMismatchException ime) {
                System.out.print("Please input a number. >> ");
                userInput.next();
            }
        }

    }

    // MODIFIES: this
    // EFFECTS: solve mode method loop records new solves and saves them to solve
    private void solveMode() {
        System.out.println("Solve mode active.");
        String selection = printInstructionsAndGetUserSelection();
        while (!selection.equalsIgnoreCase("exit") && !selection.equalsIgnoreCase("e")) {
            if (selection.equalsIgnoreCase("help")) {
                selection = printPuzzleOptionsAndGetUserSelection();
                continue;
            }
            Solve newSolve;
            try {
                newSolve = pickSolve(selection);
            } catch (InvalidPuzzleException e) {
                System.out.println("You have not selected a valid puzzle. Please try again.");
                selection = printInstructionsAndGetUserSelection();
                continue;
            }
            solve(newSolve);
            printStats(newSolve);
            solveList.addSolve(newSolve);
            selection = printInstructionsAndGetUserSelection();
        }
    }

    // EFFECTS: prints through the solving process, including the inspection
    //          count down and the timer for the user.
    private void solve(Solve solve) {
        System.out.println("This is your scramble: " + solve.getScramble());
        System.out.print("Press enter to start the countdown. >> ");
        userInput.nextLine();
        CountDown cd = new CountDown(this);
        Timer t = new Timer();
        cd.start();
        userInput.nextLine();
        cd.interrupt();
        if (cd.getStatus() != 3) {
            t.startTimer();
            System.out.print("Timer started, press enter to stop. >> ");
            userInput.nextLine();
            solve.setSolveTime(t.stopTimer());
            solve.setStatus(cd.getStatus());
        } else {
            solve.setSolveTime(Duration.ZERO);
            solve.setStatus(3);
        }
    }

    // EFFECTS: prints out the results of a solve right after it is completed.
    private void printStats(Solve solve) {
        if (solve.getSolveTime().isZero()) {
            System.out.println("Result: DNF");
        } else if (solve.getStatus() == 2) {
            System.out.println("Result: " + solve.getFormattedTime() + " (+2 second penalty)");
        } else {
            System.out.println("Result: " + solve.getFormattedTime());
        }
    }

    // prints solve mode instructions to the user and prompts them for a input.
    private String printInstructionsAndGetUserSelection() {
        System.out.print("Enter \"exit\" or \"e\" to exit, or enter a puzzle name to do"
                + "solves for that specific puzzle (enter \"help\" to print out names). Otherwise press en"
                + "ter to start a 3x3 solve. >> ");
        return userInput.nextLine();
    }

    // prints all of the available options (after the user enters a help command in solve mode)
    private String printPuzzleOptionsAndGetUserSelection() {
        System.out.println("The following names can be used to do solves.\n"
                + " - 2x2\n - 3x3\n - 4x4\n - 5x5\n - 6x6\n - 7x7\n"
                + " - clock\n - skewb\n - pyra (Pyraminx)\n - sq1 (Square-1)\n - mega (Megaminx)\n");
        return printInstructionsAndGetUserSelection();
    }

    // picks the according PuzzleRegistry enum type with a given string input.
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private Solve pickSolve(String selection) throws InvalidPuzzleException {
        switch (selection) {
            case "2x2":
                return new Solve(PuzzleRegistry.TWO);
            case "":
            case "3x3":
                return new Solve(PuzzleRegistry.THREE);
            case "4x4":
                return new Solve(PuzzleRegistry.FOUR);
            case "5x5":
                return new Solve(PuzzleRegistry.FIVE);
            case "6x6":
                return new Solve(PuzzleRegistry.SIX);
            case "7x7":
                return new Solve(PuzzleRegistry.SEVEN);
            case "clock":
                return new Solve(PuzzleRegistry.CLOCK);
            case "skewb":
                return new Solve(PuzzleRegistry.SKEWB);
            case "pyra":
                return new Solve(PuzzleRegistry.PYRA);
            case "sq1":
                return new Solve(PuzzleRegistry.SQ1);
            case "mega":
                return new Solve(PuzzleRegistry.MEGA);
            default:
                throw new InvalidPuzzleException(selection);
        }
    }

    // EFFECTS: history mode method loop prints out history and asks user for edits.
    private void historyMode() {
        System.out.println("History mode active.");
        String selection = printHistoryAndGetUserSelection();
        while (!selection.equalsIgnoreCase("exit") && !selection.equalsIgnoreCase("e")) {
            if (selection.split(" ")[0].equalsIgnoreCase("edit")) {
                editSolveChoice(selection);
            } else if (selection.split(" ")[0].equalsIgnoreCase("remove")) {
                removeSolveChoice(selection);
            } else {
                System.out.println("That is not a valid option. Please try again.");
            }
            selection = printHistoryAndGetUserSelection();
        }
    }

    // MODIFIES: this
    // EFFECTS: one of the solve history editing methods, it removes the solve
    //          the list of solves.
    private void removeSolveChoice(String selection) {
        String[] splitArgs = selection.split(" ");
        try {
            int index = Integer.parseInt(splitArgs[1]);
            if (index > solveList.length() || index < 1) {
                throw new NullPointerException();
            }
            solveList.removeSolve(index - 1);
        } catch (NumberFormatException nfe) {
            System.out.println("That is not a valid number. Please try again.");
        } catch (NullPointerException npe) {
            System.out.println("That is not a valid index position. Please try again.");
        }
    }

    // EFFECTS: one of the solve history editing methods, it edits the solve
    //          status of one of the solves on the list of solves.
    private void editSolveChoice(String selection) {
        String[] splitArgs = selection.split(" ");
        try {
            int index = Integer.parseInt(splitArgs[1]);
            if (index > solveList.length() || index < 1) {
                throw new NullPointerException();
            }
            changeSolveStatus(index - 1, splitArgs[2]);
        } catch (NumberFormatException nfe) {
            System.out.println("That is not a valid number. Please try again.");
        } catch (NullPointerException npe) {
            System.out.println("That is not a valid index position. Please try again.");
        } catch (InvalidChoiceException ice) {
            System.out.println("That is not a valid status to update to. Please try again.");
        }
    }

    // MODIFIES: this
    // EFFECTS: helper method for the editSolveChoice function to do the final manipulation.
    private void changeSolveStatus(int index, String choice) throws InvalidChoiceException {
        Solve s = solveList.getSolve(index);
        if (choice.equalsIgnoreCase("none")) {
            s.setStatus(1);
        } else if (choice.equalsIgnoreCase("+2")) {
            s.setStatus(2);
        } else if (choice.equalsIgnoreCase("dnf")) {
            s.setStatus(3);
        } else {
            throw new InvalidChoiceException(choice);
        }
    }

    // EFFECTS: prints out the history of all solves (in solveList) and returns a user input.
    private String printHistoryAndGetUserSelection() {
        System.out.println("Here are your past solves ordered from least to most recent.");
        for (int i = 0; i < solveList.length(); i++) {
            Solve s = solveList.getSolve(i);
            System.out.print(" " + (i + 1) + ". " + s.getFormattedTime());
            if (s.getStatus() == 2) {
                System.out.print(" (+2 second penalty)");
            } else if (s.getStatus() == 3) {
                System.out.print(" (DNF penalty)");
            }
            System.out.print("\n");
        }
        System.out.println("Enter \"exit\" or \"e\" to exit, otherwise:");
        System.out.println(" 1. Enter \"remove {index}\" to remove that specific solve.");
        System.out.println(" 2. Enter \"edit {index} {none|+2|dnf}\" to change the status of that specific solve.");
        System.out.print(" >> ");
        return userInput.nextLine();
    }
}
