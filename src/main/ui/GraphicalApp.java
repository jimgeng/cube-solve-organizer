package ui;

import model.Event;
import model.EventLog;
import model.ListOfSolve;
import persistence.ListSolveReader;
import persistence.ListSolveWriter;
import ui.panels.HistoryPanel;
import ui.panels.NavPanel;
import ui.panels.SolvePanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

// A graphical user interface for the rubiks cube timer application.
public class GraphicalApp extends JFrame {

    private ListOfSolve solveList;
    private JPanel cardContainerPanel;
    private CardLayout cardContainerLayout;
    private HistoryPanel historyPanel;
    private SolvePanel solvePanel;
    private NavPanel navPanel;

    // EFFECTS: creates a graphical app representation of the Rubik's cube timer
    public GraphicalApp() {
        super("Cube Timer");
        solveList = new ListOfSolve();
        showLoadingPrompt();
        initializeGraphics();
        initializeSavePrompt();
    }

    // EFFECTS: initializes and displays all graphical elements to user
    private void initializeGraphics() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        int width = 800;
        int height = 600;
        setSize(width, height);
        createComponents();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates graphical components of the app and creates nesting structure
    private void createComponents() {
        cardContainerLayout = new CardLayout();
        cardContainerPanel = new JPanel(cardContainerLayout);
        historyPanel = new HistoryPanel(solveList);
        solvePanel = new SolvePanel(historyPanel, solveList);
        navPanel = new NavPanel(this, solvePanel);
        cardContainerPanel.add(solvePanel, "solve");
        cardContainerPanel.add(historyPanel, "history");
        add(navPanel, BorderLayout.NORTH);
        add(cardContainerPanel, BorderLayout.CENTER);
    }

    // EFFECTS: switches the panels container to the specified panel
    public void switchPanel(String panelName) {
        cardContainerLayout.show(cardContainerPanel, panelName);
    }

    // MODIFIES: this
    // EFFECTS: prompts user to load their previous data on launch
    private void showLoadingPrompt() {
        String m = "Do you want to load previous solves?";
        String t = "Load Previous Solves";
        int response = JOptionPane.showConfirmDialog(this, m, t, JOptionPane.YES_NO_OPTION);
        if (response == JOptionPane.YES_OPTION) {
            try {
                ListSolveReader reader = new ListSolveReader("./data/solves.json");
                solveList = reader.read();
            } catch (IOException ioe) {
                m = "Error loading data, please make sure you have the solves.json file. Continuing without loading...";
                JOptionPane.showMessageDialog(this, m);
            }
        }
    }

    // EFFECTS: prompts user to save their app data on close
    private void initializeSavePrompt() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                String m = "Do you want to save current solves?";
                String t = "Save Current Solves";
                int response = JOptionPane.showConfirmDialog(GraphicalApp.this, m, t, JOptionPane.YES_NO_OPTION);
                if (response == JOptionPane.YES_OPTION) {
                    try {
                        ListSolveWriter writer = new ListSolveWriter("./data/solves.json");
                        writer.write(solveList);
                        writer.close();
                        printLog();
                        System.exit(0);
                    } catch (IOException ioe) {
                        m = "Error saving data, please make sure you have the solves.json file.";
                        JOptionPane.showMessageDialog(GraphicalApp.this, m);
                    }
                } else {
                    printLog();
                    System.exit(0);
                }
            }
        });
    }

    // EFFECTS: prints all logged actions to the console.
    private void printLog() {
        for (Event e : EventLog.getInstance()) {
            System.out.println(e.toString() + "\n");
        }
    }
}
