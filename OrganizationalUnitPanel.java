import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class OrganizationalUnitPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField orgUnitIDField, unitNameField, unitTypeField, codeField, statusField, descriptionField;
    private JFormattedTextField effectiveDateField, createdAtField;
    private JButton addButton, updateButton, deleteButton;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public OrganizationalUnitPanel() {
        setLayout(new BorderLayout());

        // Create table
        tableModel = new DefaultTableModel(new String[]{"Org Unit ID", "Unit Name", "Unit Type", "Code", "Status", "Effective Date", "Description", "Created At"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create form panel
        JPanel formPanel = new JPanel(new GridLayout(9, 2));
        orgUnitIDField = new JTextField(20);
        unitNameField = new JTextField(20);
        unitTypeField = new JTextField(20);
        codeField = new JTextField(20);
        statusField = new JTextField(20);
        effectiveDateField = new JFormattedTextField(dateFormat);
        descriptionField = new JTextField(20);
        createdAtField = new JFormattedTextField(dateFormat);

        formPanel.add(new JLabel("Org Unit ID:"));
        formPanel.add(orgUnitIDField);
        formPanel.add(new JLabel("Unit Name:"));
        formPanel.add(unitNameField);
        formPanel.add(new JLabel("Unit Type:"));
        formPanel.add(unitTypeField);
        formPanel.add(new JLabel("Code:"));
        formPanel.add(codeField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Effective Date (yyyy-MM-dd):"));
        formPanel.add(effectiveDateField);
        formPanel.add(new JLabel("Description:"));
        formPanel.add(descriptionField);
        formPanel.add(new JLabel("Created At (yyyy-MM-dd):"));
        formPanel.add(createdAtField);

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
        addButton.addActionListener(e -> addOrganizationalUnit());
        updateButton.addActionListener(e -> updateOrganizationalUnit());
        deleteButton.addActionListener(e -> deleteOrganizationalUnit());

        // Load initial data
        loadOrganizationalUnits();
    }

    private void loadOrganizationalUnits() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM OrganizationalUnit")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("orgUnitID"),
                    rs.getString("unitName"),
                    rs.getString("unitType"),
                    rs.getString("code"),
                    rs.getString("status"),
                    rs.getDate("effectiveDate"),
                    rs.getString("description"),
                    rs.getDate("createdAt")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading organizational units: " + e.getMessage());
        }
    }

    private void addOrganizationalUnit() {
        try {
            String orgUnitID = orgUnitIDField.getText();
            String unitName = unitNameField.getText();
            String unitType = unitTypeField.getText();
            String code = codeField.getText();
            String status = statusField.getText();
            Date effectiveDate = new Date(dateFormat.parse(effectiveDateField.getText()).getTime());
            String description = descriptionField.getText();
            Date createdAt = new Date(dateFormat.parse(createdAtField.getText()).getTime());

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "INSERT INTO OrganizationalUnit (orgUnitID, unitName, unitType, code, status, effectiveDate, description, createdAt) VALUES (?, ?, ?, ?, ?, ?, ?, ?)")) {
                pstmt.setString(1, orgUnitID);
                pstmt.setString(2, unitName);
                pstmt.setString(3, unitType);
                pstmt.setString(4, code);
                pstmt.setString(5, status);
                pstmt.setDate(6, effectiveDate);
                pstmt.setString(7, description);
                pstmt.setDate(8, createdAt);
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Organizational unit added successfully");
                loadOrganizationalUnits();
                clearFields();
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding organizational unit: " + e.getMessage());
        }
    }

    private void updateOrganizationalUnit() {
        try {
            String orgUnitID = orgUnitIDField.getText();
            String unitName = unitNameField.getText();
            String unitType = unitTypeField.getText();
            String code = codeField.getText();
            String status = statusField.getText();
            Date effectiveDate = new Date(dateFormat.parse(effectiveDateField.getText()).getTime());
            String description = descriptionField.getText();
            Date createdAt = new Date(dateFormat.parse(createdAtField.getText()).getTime());

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(
                     "UPDATE OrganizationalUnit SET unitName = ?, unitType = ?, code = ?, status = ?, effectiveDate = ?, description = ?, createdAt = ? WHERE orgUnitID = ?")) {
                pstmt.setString(1, unitName);
                pstmt.setString(2, unitType);
                pstmt.setString(3, code);
                pstmt.setString(4, status);
                pstmt.setDate(5, effectiveDate);
                pstmt.setString(6, description);
                pstmt.setDate(7, createdAt);
                pstmt.setString(8, orgUnitID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "Organizational unit updated successfully");
                    loadOrganizationalUnits();
                    clearFields();
                } else {
                    JOptionPane.showMessageDialog(this, "No organizational unit found with the given ID");
                }
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating organizational unit: " + e.getMessage());
        }
    }

    private void deleteOrganizationalUnit() {
        String orgUnitID = orgUnitIDField.getText();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM OrganizationalUnit WHERE orgUnitID = ?")) {
            pstmt.setString(1, orgUnitID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "Organizational unit deleted successfully");
                loadOrganizationalUnits();
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "No organizational unit found with the given ID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error deleting organizational unit: " + e.getMessage());
        }
    }

    private void clearFields() {
        orgUnitIDField.setText("");
        unitNameField.setText("");
        unitTypeField.setText("");
        codeField.setText("");
        statusField.setText("");
        effectiveDateField.setText("");
        descriptionField.setText("");
        createdAtField.setText("");
    }
}

