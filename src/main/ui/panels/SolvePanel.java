package ui.panels;

import model.ListOfSolve;
import model.Solve;
import org.worldcubeassociation.tnoodle.scrambles.PuzzleRegistry;
import ui.GraphicalApp;
import ui.GraphicalSolveManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

// Solve panel UI component for the GraphicalApp class
public class SolvePanel extends JPanel {
    private final ListOfSolve solveList;
    private final HistoryPanel tableData;
    private final GraphicalSolveManager gsm;
    private JTextArea scramble;
    private JLabel time;
    private JButton newSolveButton;
    private JComboBox<String> cubePicker;
    private Solve solve;
    private static Map<String, PuzzleRegistry> cubeMap;
    private boolean isSpaceDown;

    // EFFECTS: creates the history panel that allows the user to do solves.
    public SolvePanel(HistoryPanel tableData, ListOfSolve sl) {
        this.tableData = tableData;
        setFocusable(true);
        this.solveList = sl;
        initializeCubeMap();
        setLayout(new BorderLayout());
        solve = new Solve(cubeMap.get("3x3x3"));
        initializeGraphics();
        initializeCubePicker();
        initializeNewSolve();
        addKeyListener(new KeyHandler());
        requestFocus();
        gsm = new GraphicalSolveManager(this, 300, time);
        isSpaceDown = false;
    }

    // MODIFIES: this
    // EFFECTS: gives the new solve button functionality to generate a new solve
    private void initializeNewSolve() {
        newSolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(SolvePanel.this.isVisible());
                newSolve();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: gives the cube picker functionlaity to pick a different type of cube
    private void initializeCubePicker() {
        cubePicker.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                newSolve();
            }
        });
    }

    // MODIFIES: this
    // EFFECTS: generates a hash map of a string to cube map for utility purposes
    private void initializeCubeMap() {
        cubeMap = new HashMap<>();
        cubeMap.put("2x2x2", PuzzleRegistry.TWO);
        cubeMap.put("3x3x3", PuzzleRegistry.THREE);
        cubeMap.put("4x4x4", PuzzleRegistry.FOUR);
        cubeMap.put("5x5x5", PuzzleRegistry.FIVE);
        cubeMap.put("6x6x6", PuzzleRegistry.SIX);
        cubeMap.put("7x7x7", PuzzleRegistry.SEVEN);
        cubeMap.put("clock", PuzzleRegistry.CLOCK);
        cubeMap.put("skewb", PuzzleRegistry.SKEWB);
        cubeMap.put("pyraminx", PuzzleRegistry.PYRA);
        cubeMap.put("square-1", PuzzleRegistry.SQ1);
        cubeMap.put("megaminx", PuzzleRegistry.MEGA);
    }

    // MODIFIES: this
    // EFFECTS: creates the various visual components of the solve panel
    private void initializeGraphics() {
        scramble = new JTextArea(solve.getScramble());
        initializeScrambleTextArea();
        time = new JLabel("0:00.000");
        newSolveButton = new JButton("New Solve");
        newSolveButton.setFocusable(false);
        cubePicker = new JComboBox<>(new String[] {
                "2x2x2", "3x3x3", "4x4x4", "5x5x5", "6x6x6", "7x7x7",
                "clock", "skewb", "pyraminx", "square-1", "megaminx"
        });
        cubePicker.setSelectedItem("3x3x3");
        cubePicker.setFocusable(false);
        JPanel topFlowpanel = new JPanel(new BorderLayout());
        JPanel selectionGroup = new JPanel(new BorderLayout());
        selectionGroup.add(newSolveButton, BorderLayout.NORTH);
        selectionGroup.add(cubePicker, BorderLayout.SOUTH);
        topFlowpanel.add(scramble, BorderLayout.WEST);
        topFlowpanel.add(selectionGroup, BorderLayout.EAST);
        topFlowpanel.setMaximumSize(new Dimension(700, 32767));
        topFlowpanel.setBorder(BorderFactory.createEmptyBorder(0,50,0,50));
        time.setHorizontalAlignment(SwingConstants.CENTER);
        time.setFont(new Font("Sans Serif", Font.BOLD, 56));
        add(topFlowpanel, BorderLayout.NORTH);
        add(time, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: helper method to set the styling of the scramble display
    private void initializeScrambleTextArea() {
        scramble.setFont(new Font("Sans Serif", Font.PLAIN, 16));
        scramble.setLineWrap(true);
        scramble.setColumns(50);
        scramble.setEditable(false);
        scramble.setOpaque(false);
        scramble.setWrapStyleWord(true);
    }

    // MODIFIES: this
    // EFFECTS: updates the application with a new solve
    private void newSolve() {
        solve = new Solve(cubeMap.get((String) cubePicker.getSelectedItem()));
        scramble.setText(solve.getScramble());
    }

    // EFFECTS: gets the current solve displayed in the application
    public Solve getSolve() {
        return solve;
    }

    // special KeyHandler class (taken from UBC CPSC 210 SpaceInvader project)
    private class KeyHandler extends KeyAdapter {

        // MODIFIES: this
        // EFFECTS: notifies GraphicalSolveManager of a space bar press
        @Override
        public void keyPressed(KeyEvent e) {
            if (SolvePanel.this.isVisible() && e.getKeyCode() == KeyEvent.VK_SPACE && !isSpaceDown) {
                gsm.spaceDown();
                isSpaceDown = true;
            }
        }

        // MODIFIES: this
        // EFFECTS: notifies GraphicalSolveManager of a space bar release
        @Override
        public void keyReleased(KeyEvent e) {
            if (SolvePanel.this.isVisible() && e.getKeyCode() == KeyEvent.VK_SPACE && isSpaceDown) {
                gsm.spaceUp();
                isSpaceDown = false;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: updates table and internal model after GSM completes the solve
    public void gsmCompleteSolve() {
        solveList.addSolve(solve);
        tableData.updateTableData();
        newSolve();
    }
}

