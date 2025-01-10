import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class SearchMatchPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchIDField, criteriaField;
    private JButton addButton, updateButton, deleteButton;

    public SearchMatchPanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Search ID", "Criteria"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(3, 2));
        searchIDField = new JTextField(20);
        criteriaField = new JTextField(20);
        formPanel.add(new JLabel("Search ID:"));
        formPanel.add(searchIDField);
        formPanel.add(new JLabel("Criteria:"));
        formPanel.add(criteriaField);

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
        addButton.addActionListener(e -> addSearchMatch());
        updateButton.addActionListener(e -> updateSearchMatch());
        deleteButton.addActionListener(e -> deleteSearchMatch());

        // Load initial data
        loadSearchMatches();
    }

    private void loadSearchMatches() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT searchID, criteria FROM SearchMatch")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("searchID"),
                    rs.getString("criteria")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading search matches: " + e.getMessage());
        }
    }

    private void addSearchMatch() {
        String searchID = searchIDField.getText();
        String criteria = criteriaField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO SearchMatch (searchID, criteria) VALUES (?, ?)")) {
            pstmt.setString(1, searchID);
            pstmt.setString(2, criteria);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Search match added successfully");
            loadSearchMatches();
            clearFields();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding search match: " + e.getMessage());
        }
    }

    private void updateSearchMatch() {
        String searchID = searchIDField.getText();
        String criteria = criteriaField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE SearchMatch SET criteria = ? WHERE searchID = ?")) {
            pstmt.setString(1, criteria);
            pstmt.setString(2, searchID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Search match updated successfully");
                loadSearchMatches();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No search match found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating search match: " + e.getMessage());
        }
    }

    private void deleteSearchMatch() {
        String searchID = searchIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM SearchMatch WHERE searchID = ?")) {
            pstmt.setString(1, searchID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Search match deleted successfully");
                loadSearchMatches();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No search match found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting search match: " + e.getMessage());
        }
    }

    private void clearFields() {
        searchIDField.setText("");
        criteriaField.setText("");
    }
}

