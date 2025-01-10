import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ChecklistPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField checklistIDField, typeField, descriptionField, orgUnitIDField;
    private JButton addButton, updateButton, deleteButton;

    public ChecklistPanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Checklist ID", "Type", "Description", "Org Unit ID"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        checklistIDField = new JTextField(20);
        typeField = new JTextField(20);
        descriptionField = new JTextField(20);
        orgUnitIDField = new JTextField(20);
        formPanel.add(new JLabel("Checklist ID:"));
        formPanel.add(checklistIDField);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Org Unit ID:"));
        formPanel.add(orgUnitIDField);

        // Create buttons
        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(buttonPanel);
        add(formPanel, BorderLayout.SOUTH);

        // Add action listeners
        addButton.addActionListener(e -> addChecklist());
        updateButton.addActionListener(e -> updateChecklist());
        deleteButton.addActionListener(e -> deleteChecklist());

        // Load initial data
        loadChecklists();
    }

    private void loadChecklists() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT checklistID, type, description, orgUnitID FROM Checklist")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("checklistID"),
                    rs.getString("type"),
                    rs.getString("description"),
                    rs.getString("orgUnitID")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading checklists: " + e.getMessage());
        }
    }

    private void addChecklist() {
        String checklistID = checklistIDField.getText();
        String type = typeField.getText();
        String description = descriptionField.getText();
        String orgUnitID = orgUnitIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO Checklist (checklistID, type, description, orgUnitID) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, checklistID);
            pstmt.setString(2, type);
            pstmt.setString(3, description);
            pstmt.setString(4, orgUnitID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Checklist added successfully");
            loadChecklists();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding checklist: " + e.getMessage());
        }
    }

    private void updateChecklist() {
        String checklistID = checklistIDField.getText();
        String type = typeField.getText();
        String description = descriptionField.getText();
        String orgUnitID = orgUnitIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE Checklist SET type = ?, description = ?, orgUnitID = ? WHERE checklistID = ?")) {
            pstmt.setString(1, type);
            pstmt.setString(2, description);
            pstmt.setString(3, orgUnitID);
            pstmt.setString(4, checklistID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Checklist updated successfully");
                loadChecklists();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No checklist found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating checklist: " + e.getMessage());
        }
    }

    private void deleteChecklist() {
        String checklistID = checklistIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Checklist WHERE checklistID = ?")) {
            pstmt.setString(1, checklistID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Checklist deleted successfully");
                loadChecklists();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No checklist found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting checklist: " + e.getMessage());
        }
    }

    private void clearFields() {
        checklistIDField.setText("");
        typeField.setText("");
        descriptionField.setText("");
        orgUnitIDField.setText("");
    }
}

