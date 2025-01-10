import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private JPanel contentPanel;
    private JLabel statusLabel;

    public MainWindow() {
        setTitle("Database Management System");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);

        contentPanel = new JPanel(new CardLayout());
        contentPanel.add(new UserPanel(this), "Users");
        contentPanel.add(new ChecklistPanel(), "Checklists");
        contentPanel.add(new OrganizationalUnitPanel(), "Organizational Units");
        contentPanel.add(new SearchMatchPanel(), "Search Matches");
        contentPanel.add(new CommunityDirectoryPanel(), "Community Directory");
        contentPanel.add(new PhonePanel(), "Phones");
        contentPanel.add(new EmergencyContactPanel(), "Emergency Contacts");
        contentPanel.add(new BiographicDetailsPanel(), "Biographic Details");
        add(contentPanel, BorderLayout.CENTER);

        statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        add(statusLabel, BorderLayout.SOUTH);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBackground(new Color(240, 240, 240));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(200, 200, 200)));

        String[] buttonLabels = {
            "Users", "Checklists", "Organizational Units", "Search Matches",
            "Community Directory", "Phones", "Emergency Contacts", "Biographic Details"
        };

        String[] iconNames = {
            "user", "checklist", "organization", "search",
            "community", "phone", "emergency", "biography"
        };

        for (int i = 0; i < buttonLabels.length; i++) {
            JButton button = createSidebarButton(buttonLabels[i], iconNames[i]);
            sidebar.add(button);
            sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return sidebar;
    }

    private JButton createSidebarButton(String label, String iconName) {
        JButton button = new JButton(label);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 40));
        button.setFocusPainted(false);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                CardLayout cl = (CardLayout) contentPanel.getLayout();
                cl.show(contentPanel, label);
                setStatus("Viewing " + label);
            }
        });
        return button;
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(new AppleInspiredLookAndFeel());
            } catch (UnsupportedLookAndFeelException e) {
                e.printStackTrace();
            }
            new MainWindow().setVisible(true);
        });
    }
}

