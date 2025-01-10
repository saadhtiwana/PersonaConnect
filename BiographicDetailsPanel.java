import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class BiographicDetailsPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField detailIDField, userIDField, workExperienceField, healthInfoField, languagesField, visaDetailsField, passportDetailsField, educationHistoryField;
    private JButton addButton, updateButton, deleteButton;

    public BiographicDetailsPanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Detail ID", "User ID", "Work Experience", "Health Info", "Languages", "Visa Details", "Passport Details", "Education History"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        detailIDField = new JTextField(20);
        userIDField = new JTextField(20);
        workExperienceField = new JTextField(20);
        healthInfoField = new JTextField(20);
        languagesField = new JTextField(20);
        visaDetailsField = new JTextField(20);
        passportDetailsField = new JTextField(20);
        educationHistoryField = new JTextField(20);
        formPanel.add(new JLabel("Detail ID:"));
        formPanel.add(detailIDField);
        formPanel.add(new JLabel("User ID:"));
        formPanel.add(userIDField);
        formPanel.add(new JLabel("Work Experience:"));
        formPanel.add(workExperienceField);
        formPanel.add(new JLabel("Health Info:"));
        formPanel.add(healthInfoField);
        formPanel.add(new JLabel("Languages:"));
        formPanel.add(languagesField);
        formPanel.add(new JLabel("Visa Details:"));
        formPanel.add(visaDetailsField);
        formPanel.add(new JLabel("Passport Details:"));
        formPanel.add(passportDetailsField);
        formPanel.add(new JLabel("Education History:"));
        formPanel.add(educationHistoryField);

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
        addButton.addActionListener(e -> addBiographicDetails());
        updateButton.addActionListener(e -> updateBiographicDetails());
        deleteButton.addActionListener(e -> deleteBiographicDetails());

        // Load initial data
        loadBiographicDetails();
    }

    private void loadBiographicDetails() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM BiographicDetails")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("detailID"),
                    rs.getString("userID"),
                    rs.getString("workExperience"),
                    rs.getString("healthInfo"),
                    rs.getString("languages"),
                    rs.getString("visaDetails"),
                    rs.getString("passportDetails"),
                    rs.getString("educationHistory")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading biographic details: " + e.getMessage());
        }
    }

    private void addBiographicDetails() {
        String detailID = detailIDField.getText();
        String userID = userIDField.getText();
        String workExperience = workExperienceField.getText();
        String healthInfo = healthInfoField.getText();
        String languages = languagesField.getText();
        String visaDetails = visaDetailsField.getText();
        String passportDetails = passportDetailsField.getText();
        String educationHistory = educationHistoryField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO BiographicDetails (detailID, userID, workExperience, healthInfo, languages, visaDetails, passportDetails, educationHistory) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setString(1, detailID);
            pstmt.setString(2, userID);
            pstmt.setString(3, workExperience);
            pstmt.setString(4, healthInfo);
            pstmt.setString(5, languages);
            pstmt.setString(6, visaDetails);
            pstmt.setString(7, passportDetails);
            pstmt.setString(8, educationHistory);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Biographic details added successfully");
            loadBiographicDetails();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding biographic details: " + e.getMessage());
        }
    }

    private void updateBiographicDetails() {
        String detailID = detailIDField.getText();
        String userID = userIDField.getText();
        String workExperience = workExperienceField.getText();
        String healthInfo = healthInfoField.getText();
        String languages = languagesField.getText();
        String visaDetails = visaDetailsField.getText();
        String passportDetails = passportDetailsField.getText();
        String educationHistory = educationHistoryField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE BiographicDetails SET userID = ?, workExperience = ?, healthInfo = ?, languages = ?, visaDetails = ?, passportDetails = ?, educationHistory = ? WHERE detailID = ?")) {
            pstmt.setString(1, userID);
            pstmt.setString(2, workExperience);
            pstmt.setString(3, healthInfo);
            pstmt.setString(4, languages);
            pstmt.setString(5, visaDetails);
            pstmt.setString(6, passportDetails);
            pstmt.setString(7, educationHistory);
            pstmt.setString(8, detailID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Biographic details updated successfully");
                loadBiographicDetails();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No biographic details found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating biographic details: " + e.getMessage());
        }
    }

    private void deleteBiographicDetails() {
        String detailID = detailIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM BiographicDetails WHERE detailID = ?")) {
            pstmt.setString(1, detailID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Biographic details deleted successfully");
                loadBiographicDetails();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No biographic details found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting biographic details: " + e.getMessage());
        }
    }

    private void clearFields() {
        detailIDField.setText("");
        userIDField.setText("");
        workExperienceField.setText("");
        healthInfoField.setText("");
        languagesField.setText("");
        visaDetailsField.setText("");
        passportDetailsField.setText("");
        educationHistoryField.setText("");
    }
}

