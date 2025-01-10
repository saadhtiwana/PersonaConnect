import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class EmergencyContactPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField contactIDField, userIDField, nameField, relationshipField, phoneField, emailField;
    private JButton addButton, updateButton, deleteButton;

    public EmergencyContactPanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Contact ID", "User ID", "Name", "Relationship", "Phone", "Email"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(7, 2));
        contactIDField = new JTextField(20);
        userIDField = new JTextField(20);
        nameField = new JTextField(20);
        relationshipField = new JTextField(20);
        phoneField = new JTextField(20);
        emailField = new JTextField(20);
        formPanel.add(new JLabel("Contact ID:"));
        formPanel.add(contactIDField);
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIDField);
        formPanel.add(new JLabel("Name:"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Relationship:"));
        formPanel.add(relationshipField);
        formPanel.add(new JLabel("Phone:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);

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
        addButton.addActionListener(e -> addEmergencyContact());
        updateButton.addActionListener(e -> updateEmergencyContact());
        deleteButton.addActionListener(e -> deleteEmergencyContact());

        // Load initial data
        loadEmergencyContacts();
    }

    private void loadEmergencyContacts() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM EmergencyContact")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("contactID"),
                    rs.getString("userID"),
                    rs.getString("name"),
                    rs.getString("relationship"),
                    rs.getString("phone"),
                    rs.getString("email")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading emergency contacts: " + e.getMessage());
        }
    }

    private void addEmergencyContact() {
        if (!validateFields()) return;

        String contactID = contactIDField.getText();
        String userID = userIDField.getText();
        String name = nameField.getText();
        String relationship = relationshipField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO EmergencyContact (contactID, userID, name, relationship, phone, email) VALUES (?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, contactID);
            pstmt.setString(2, userID);
            pstmt.setString(3, name);
            pstmt.setString(4, relationship);
            pstmt.setString(5, phone);
            pstmt.setString(6, email);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Emergency contact added successfully");
            loadEmergencyContacts();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding emergency contact: " + e.getMessage());
        }
    }

    private void updateEmergencyContact() {
        if (!validateFields()) return;

        String contactID = contactIDField.getText();
        String userID = userIDField.getText();
        String name = nameField.getText();
        String relationship = relationshipField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE EmergencyContact SET userID = ?, name = ?, relationship = ?, phone = ?, email = ? WHERE contactID = ?")) {
            pstmt.setString(1, userID);
            pstmt.setString(2, name);
            pstmt.setString(3, relationship);
            pstmt.setString(4, phone);
            pstmt.setString(5, email);
            pstmt.setString(6, contactID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Emergency contact updated successfully");
                loadEmergencyContacts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No emergency contact found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating emergency contact: " + e.getMessage());
        }
    }

    private void deleteEmergencyContact() {
        String contactID = contactIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM EmergencyContact WHERE contactID = ?")) {
            pstmt.setString(1, contactID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Emergency contact deleted successfully");
                loadEmergencyContacts();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No emergency contact found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting emergency contact: " + e.getMessage());
        }
    }

    private void clearFields() {
        contactIDField.setText("");
        userIDField.setText("");
        nameField.setText("");
        relationshipField.setText("");
        phoneField.setText("");
        emailField.setText("");
    }

    private boolean validateFields() {
        if (contactIDField.getText().isEmpty() || userIDField.getText().isEmpty() ||
            nameField.getText().isEmpty() || relationshipField.getText().isEmpty() ||
            phoneField.getText().isEmpty() || emailField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields must be filled out");
            return false;
        }
        return true;
    }
}
