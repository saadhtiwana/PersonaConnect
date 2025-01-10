import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class CustomTableHeaderRenderer extends DefaultTableCellRenderer {
    public CustomTableHeaderRenderer() {
        setHorizontalAlignment(JLabel.LEFT);
        setOpaque(true);
        setBackground(new Color(240, 240, 240));
        setForeground(new Color(60, 60, 60));
        setFont(new Font("Helvetica", Font.BOLD, 12));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        return this;
    }
}

