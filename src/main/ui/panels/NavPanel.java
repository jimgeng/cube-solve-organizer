package ui.panels;

import ui.GraphicalApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Navigation bar for the GraphicalApp class
public class NavPanel extends JPanel {
    private final GraphicalApp parentFrame;

    // EFFECTS: creates a navagation panel that can let the user navigate between the two main panels
    public NavPanel(GraphicalApp pf, JPanel solvePanel) {
        parentFrame = pf;
        setPreferredSize(new Dimension(800, 50));
        JButton solveMode = new JButton("Solve Mode");
        JButton historyMode = new JButton("History Mode");


        ActionListener switchListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String panelName = ae.getActionCommand();
                parentFrame.switchPanel(panelName);
                if (panelName.equals("solve")) {
                    solvePanel.requestFocus();
                }
            }
        };

        solveMode.addActionListener(switchListener);
        solveMode.setActionCommand("solve");
        solveMode.setFocusable(false);
        historyMode.addActionListener(switchListener);
        historyMode.setActionCommand("history");
        historyMode.setFocusable(false);
        add(solveMode);
        add(historyMode);
    }
}
