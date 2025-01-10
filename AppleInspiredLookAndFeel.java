import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.*;

public class AppleInspiredLookAndFeel extends BasicLookAndFeel {
    @Override
    public String getName() {
        return "AppleInspired";
    }

    @Override
    public String getID() {
        return "AppleInspired";
    }

    @Override
    public String getDescription() {
        return "An Apple-inspired Look and Feel";
    }

    @Override
    public boolean isNativeLookAndFeel() {
        return false;
    }

    @Override
    public boolean isSupportedLookAndFeel() {
        return true;
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
    }

    @Override
    protected void initSystemColorDefaults(UIDefaults table) {
        super.initSystemColorDefaults(table);
        
        table.put("control", new Color(240, 240, 240));
        table.put("info", new Color(242, 242, 242));
        table.put("nimbusBase", new Color(195, 195, 195));
        table.put("nimbusAlertYellow", new Color(255, 220, 35));
        table.put("nimbusDisabledText", new Color(142, 142, 142));
        table.put("nimbusFocus", new Color(115, 164, 209));
        table.put("nimbusGreen", new Color(176, 179, 50));
        table.put("nimbusInfoBlue", new Color(66, 139, 221));
        table.put("nimbusLightBackground", new Color(255, 255, 255));
        table.put("nimbusOrange", new Color(191, 98, 4));
        table.put("nimbusRed", new Color(169, 46, 34));
        table.put("nimbusSelectedText", new Color(255, 255, 255));
        table.put("nimbusSelectionBackground", new Color(57, 105, 138));
        table.put("text", new Color(0, 0, 0));
    }

    @Override
    protected void initComponentDefaults(UIDefaults table) {
        super.initComponentDefaults(table);

        Font defaultFont = new Font("Helvetica", Font.PLAIN, 12);
        table.put("Button.font", defaultFont);
        table.put("Label.font", defaultFont);
        table.put("TextField.font", defaultFont);
        table.put("TextArea.font", defaultFont);
        table.put("Table.font", defaultFont);

        table.put("Button.background", new Color(220, 220, 220));
        table.put("Button.foreground", new Color(0, 0, 0));
        table.put("Button.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        table.put("TextField.background", new Color(255, 255, 255));
        table.put("TextField.border", BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        table.put("Table.background", new Color(255, 255, 255));
        table.put("Table.gridColor", new Color(240, 240, 240));
        table.put("Table.selectionBackground", new Color(57, 105, 138));
        table.put("Table.selectionForeground", new Color(255, 255, 255));
    }
}

