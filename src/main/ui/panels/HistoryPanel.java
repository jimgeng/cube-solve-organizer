package ui.panels;

import model.ListOfSolve;
import model.Solve;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// History panel UI component for the GraphicalApp class
public class HistoryPanel extends JPanel {

    private final ListOfSolve solveList;
    private final JLabel errorMessage;
    private final HistoryTableModel tableModel;
    private JButton editButton;
    private JButton removeButton;
    private JTextField inputField;
    private JComboBox<String> dropdown;

    // EFFECTS: creates the history panel that shows times and editing controls
    public HistoryPanel(ListOfSolve los) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        solveList = los;
        tableModel = new HistoryTableModel();
        JTable table = new JTable(tableModel);
        JScrollPane scrollContainer = new JScrollPane(table);
        autoResizeTable(table);
        add(scrollContainer, constraints);
        JPanel controlPanel = createControlPanel();
        constraints.gridy = 1;
        add(controlPanel, constraints);
        constraints.gridy = 2;
        errorMessage = new JLabel();
        errorMessage.setForeground(Color.RED);
        add(errorMessage, constraints);
        setFunctionality();
    }

    public void updateTableData() {
        tableModel.fireTableDataChanged();
    }

    // EFFECTS: returns a JPanel of the control panel
    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new FlowLayout());
        editButton = new JButton("Edit");
        panel.add(editButton);
        removeButton = new JButton("Remove");
        panel.add(removeButton);
        inputField = new JTextField(10);
        panel.add(inputField);
        String[] dropdownOptions = {"None", "+2", "DNF"};
        dropdown = new JComboBox<>(dropdownOptions);
        panel.add(dropdown);
        return panel;
    }

    // MODIFIES: this
    // EFFECTS: adds functionality to the two editing buttons
    private void setFunctionality() {
        editButton.addActionListener(buttonListener);
        removeButton.addActionListener(buttonListener);
    }

    private final ActionListener buttonListener = new ActionListener() {
        // MODIFIES: this
        // EFFECTS: updates the list of solves taking into account the button, text and dropdown inputs
        @Override
        public void actionPerformed(ActionEvent e) {
            String stringIndex = inputField.getText();
            try {
                int index = Integer.parseInt(stringIndex);
                index--;
                if (index >= solveList.length() || index < 0) {
                    throw new IndexOutOfBoundsException();
                }
                if (((JButton) e.getSource()).getText().equals("Edit")) {
                    changeSolveStatus(index);
                    tableModel.fireTableDataChanged();
                } else if (((JButton) e.getSource()).getText().equals("Remove")) {
                    solveList.removeSolve(index);
                    tableModel.fireTableDataChanged();
                }
            } catch (NumberFormatException nfe) {
                showTimerMessage("ERROR: Please check your inputs.");
            } catch (IndexOutOfBoundsException ioobe) {
                showTimerMessage("ERROR: Solve at index " + stringIndex + " does not exist.");
            }
        }
    };

    // MODIFIES: this
    // EFFECTS: shows error message to the user for invalid editing inputs
    private void showTimerMessage(String msg) {
        errorMessage.setText(msg);
        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                errorMessage.setText("");
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    // REQUIRES: 0 < index < solveList.length()
    // MODIFIES: this
    // EFFECTS: updates the solve status of the specified solve
    private void changeSolveStatus(int index) {
        Solve s = solveList.getSolve(index);
        s.setStatus(dropdown.getSelectedIndex() + 1);
    }

    // MODIFIES: this
    // EFFECTS: resizes the columns of the table to fit contents
    private void autoResizeTable(JTable table) {
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable automatic resizing
        for (int i = 0; i < table.getColumnCount(); i++) {
            TableColumn column = table.getColumnModel().getColumn(i);
            int width = (int) table.getTableHeader().getDefaultRenderer()
                    .getTableCellRendererComponent(table, column.getHeaderValue(), false, false, -1, i)
                    .getPreferredSize().getWidth(); // Get the width of the column header
            // Iterate over the rows of the column to determine the maximum width of the cells
            for (int j = 0; j < table.getRowCount(); j++) {
                TableCellRenderer renderer = table.getCellRenderer(j, i);
                Component component = table.prepareRenderer(renderer, j, i);
                width = Math.max(width, component.getPreferredSize().width);
            }
            column.setPreferredWidth(width + 5); // Add some padding to the preferred width
        }
    }

    // table model used to describe the overall structure of the table
    private class HistoryTableModel extends AbstractTableModel {

        private final String[] colNames = {"Cube Type", "Time", "Scramble", "Penalties"};

        // EFFECTS: gets row count of the solve list table
        @Override
        public int getRowCount() {
            return solveList.length();
        }

        // EFFECTS: gets column count of the solve list table
        @Override
        public int getColumnCount() {
            return colNames.length;
        }

        // REQUIRES: column < 4
        // EFFECTS: returns the specified name of the column at given index position.
        @Override
        public String getColumnName(int column) {
            return colNames[column];
        }

        // EFFECTS: returns the object in the solve list table at the specified row & column
        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Solve solve = solveList.getSolve(rowIndex);
            switch (columnIndex) {
                case 0:
                    return solve.getCube().name();
                case 1:
                    return solve.getFormattedTime();
                case 2:
                    return solve.getScramble();
                case 3:
                    return getFormattedPenalty(solve.getStatus());
                default:
                    throw new IndexOutOfBoundsException();
            }
        }

        // EFFECTS: gets which penalty string to display in the table based on the status provided
        private String getFormattedPenalty(int status) {
            if (status == 2) {
                return "+2";
            } else if (status == 3) {
                return "DNF";
            } else {
                return "";
            }
        }
    }
}
