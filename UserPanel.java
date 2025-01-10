import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class UserPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField userIDField, firstNameField, lastNameField, emailField;
    private JButton addButton, updateButton, deleteButton;
    private MainWindow mainWindow;

    public UserPanel(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        tableModel = new DefaultTableModel(new String[]{"User ID", "First Name", "Last Name", "Email"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new CustomTableHeaderRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane, BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        userIDField = createStyledTextField();
        firstNameField = createStyledTextField();
        lastNameField = createStyledTextField();
        emailField = createStyledTextField();
        formPanel.add(createStyledLabel("User ID:"));
        formPanel.add(userIDField);
        formPanel.add(createStyledLabel("First Name:"));
        formPanel.add(firstNameField);
        formPanel.add(createStyledLabel("Last Name:"));
        formPanel.add(lastNameField);
        formPanel.add(createStyledLabel("Email:"));
        formPanel.add(emailField);

        addButton = createStyledButton("Add");
        updateButton = createStyledButton("Update");
        deleteButton = createStyledButton("Delete");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        formPanel.add(new JLabel(""));
        formPanel.add(buttonPanel);
        add(formPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addUser());
        updateButton.addActionListener(e -> updateUser());
        deleteButton.addActionListener(e -> deleteUser());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    userIDField.setText(table.getValueAt(selectedRow, 0).toString());
                    firstNameField.setText(table.getValueAt(selectedRow, 1).toString());
                    lastNameField.setText(table.getValueAt(selectedRow, 2).toString());
                    emailField.setText(table.getValueAt(selectedRow, 3).toString());
                }
            }
        });

        loadUsers();
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Helvetica", Font.BOLD, 12));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setPreferredSize(new Dimension(200, 30));
        return textField;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 30));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(230, 230, 230));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(UIManager.getColor("Button.background"));
            }
        });

        return button;
    }

    private void loadUsers() {
        tableModel.setRowCount(0);
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT userID, firstName, lastName, email FROM User_")) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                    rs.getString("userID"),
                    rs.getString("firstName"),
                    rs.getString("lastName"),
                    rs.getString("email")
                });
            }
            mainWindow.setStatus("Users loaded successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading users: " + e.getMessage());
            mainWindow.setStatus("Error loading users");
        }
    }

    private void addUser() {
        String userID = userIDField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        if (userID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "INSERT INTO User_ (userID, firstName, lastName, email) VALUES (?, ?, ?, ?)")) {
            pstmt.setString(1, userID);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "User added successfully");
            loadUsers();
            clearFields();
            mainWindow.setStatus("User added successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding user: " + e.getMessage());
            mainWindow.setStatus("Error adding user");
        }
    }

    private void updateUser() {
        String userID = userIDField.getText();
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();

        if (userID.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(
                 "UPDATE User_ SET firstName = ?, lastName = ?, email = ? WHERE userID = ?")) {
            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, email);
            pstmt.setString(4, userID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                JOptionPane.showMessageDialog(this, "User updated successfully");
                loadUsers();
                clearFields();
                mainWindow.setStatus("User updated successfully");
            } else {
                JOptionPane.showMessageDialog(this, "No user found with the given ID");
                mainWindow.setStatus("User update failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating user: " + e.getMessage());
            mainWindow.setStatus("Error updating user");
        }
    }

    private void deleteUser() {
        String userID = userIDField.getText();

        if (userID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "User ID is required for deletion");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this user?", "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement("DELETE FROM User_ WHERE userID = ?")) {
                pstmt.setString(1, userID);
                int affectedRows = pstmt.executeUpdate();
                if (affectedRows > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully");
                    loadUsers();
                    clearFields();
                    mainWindow.setStatus("User deleted successfully");
                } else {
                    JOptionPane.showMessageDialog(this, "No user found with the given ID");
                    mainWindow.setStatus("User deletion failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting user: " + e.getMessage());
                mainWindow.setStatus("Error deleting user");
            }
        }
    }

    private void clearFields() {
        userIDField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        emailField.setText("");
    }
}

