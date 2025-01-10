import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class CommunityDirectoryPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField directoryIDField, userIDField, userTypeField;
    private JButton addButton, updateButton, deleteButton;

    public CommunityDirectoryPanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Directory ID", "User ID", "User Type"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(4, 2));
        directoryIDField = new JTextField(20);
        userIDField = new JTextField(20);
        userTypeField = new JTextField(20);
        formPanel.add(new JLabel("Directory ID:"));
        formPanel.add(directoryIDField);
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIDField);
        formPanel.add(new JLabel("User Type:"));
        formPanel.add(userTypeField);

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
        addButton.addActionListener(e -> addCommunityDirectory());
        updateButton.addActionListener(e -> updateCommunityDirectory());
        deleteButton.addActionListener(e -> deleteCommunityDirectory());

        // Load initial data
        loadCommunityDirectories();
    }

    private void loadCommunityDirectories() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT directoryID, userID, userType FROM CommunityDirectory")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("directoryID"),
                    rs.getString("userID"),
                    rs.getString("userType")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading community directories: " + e.getMessage());
        }
    }

    private void addCommunityDirectory() {
        String directoryID = directoryIDField.getText();
        String userID = userIDField.getText();
        String userType = userTypeField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO CommunityDirectory (directoryID, userID, userType) VALUES (?, ?, ?)")) {
            pstmt.setString(1, directoryID);
            pstmt.setString(2, userID);
            pstmt.setString(3, userType);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Community directory added successfully");
            loadCommunityDirectories();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding community directory: " + e.getMessage());
        }
    }

    private void updateCommunityDirectory() {
        String directoryID = directoryIDField.getText();
        String userID = userIDField.getText();
        String userType = userTypeField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE CommunityDirectory SET userID = ?, userType = ? WHERE directoryID = ?")) {
            pstmt.setString(1, userID);
            pstmt.setString(2, userType);
            pstmt.setString(3, directoryID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Community directory updated successfully");
                loadCommunityDirectories();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No community directory found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating community directory: " + e.getMessage());
        }
    }

    private void deleteCommunityDirectory() {
        String directoryID = directoryIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM CommunityDirectory WHERE directoryID = ?")) {
            pstmt.setString(1, directoryID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Community directory deleted successfully");
                loadCommunityDirectories();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No community directory found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting community directory: " + e.getMessage());
        }
    }

    private void clearFields() {
        directoryIDField.setText("");
        userIDField.setText("");
        userTypeField.setText("");
    }
}

