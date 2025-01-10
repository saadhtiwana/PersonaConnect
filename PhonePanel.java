import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class PhonePanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField phoneIDField, numberField, typeField, userIDField;
    private JButton addButton, updateButton, deleteButton;

    public PhonePanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Phone ID", "Number", "Type", "User ID"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2));
        phoneIDField = new JTextField(20);
        numberField = new JTextField(20);
        typeField = new JTextField(20);
        userIDField = new JTextField(20);
        formPanel.add(new JLabel("Phone ID:"));
        formPanel.add(phoneIDField);
        formPanel.add(new JLabel("Number:"));
        formPanel.add(numberField);
        formPanel.add(new JLabel("Type:"));
        formPanel.add(typeField);
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIDField);

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
        addButton.addActionListener(e -> addPhone());
        updateButton.addActionListener(e -> updatePhone());
        deleteButton.addActionListener(e -> deletePhone());

        // Load initial data
        loadPhones();
    }

    private void loadPhones() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT phoneID, number_, type, userID FROM Phone")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("phoneID"),
                    rs.getString("number_"),
                    rs.getString("type"),
                    rs.getString("userID")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading phones: " + e.getMessage());
        }
    }

    private void addPhone() {
        String phoneID = phoneIDField.getText();
        String number = numberField.getText();
        String type = typeField.getText();
        String userID = userIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO Phone (phoneID, number_, type, userID) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, phoneID);
            pstmt.setString(2, number);
            pstmt.setString(3, type);
            pstmt.setString(4, userID);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Phone added successfully");
            loadPhones();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding phone: " + e.getMessage());
        }
    }

    private void updatePhone() {
        String phoneID = phoneIDField.getText();
        String number = numberField.getText();
        String type = typeField.getText();
        String userID = userIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE Phone SET number_ = ?, type = ?, userID = ? WHERE phoneID = ?")) {
            pstmt.setString(1, number);
            pstmt.setString(2, type);
            pstmt.setString(3, userID);
            pstmt.setString(4, phoneID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Phone updated successfully");
                loadPhones();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No phone found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating phone: " + e.getMessage());
        }
    }

    private void deletePhone() {
        String phoneID = phoneIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM Phone WHERE phoneID = ?")) {
            pstmt.setString(1, phoneID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Phone deleted successfully");
                loadPhones();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No phone found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting phone: " + e.getMessage());
        }
    }

    private void clearFields() {
        phoneIDField.setText("");
        numberField.setText("");
        typeField.setText("");
        userIDField.setText("");
    }
}

