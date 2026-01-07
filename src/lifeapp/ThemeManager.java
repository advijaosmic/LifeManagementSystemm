package lifeapp;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    private static final Color DEFAULT_BG = new Color(246, 238, 193, 195);
    private static final Color DARK_BG = new Color(14, 78, 21);
    private static final Color LIGHT_BG = new Color(250, 229, 247);

    public static void applyDefaultTheme() {
        applyTheme(DEFAULT_BG, Color.BLACK);
    }

    public static void applyDarkTheme() {
        applyTheme(DARK_BG, Color.WHITE);
    }

    public static void applyLightTheme() {
        applyTheme(LIGHT_BG, Color.BLACK);
    }

    private static void applyTheme(Color background, Color foreground) {
        UIManager.put("Panel.background", background);
        UIManager.put("Label.foreground", foreground);

        UIManager.put("Button.background", background.darker());
        UIManager.put("Button.foreground", foreground);

        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);

        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", Color.BLACK);

        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", Color.BLACK);

        UIManager.put("Table.background", Color.WHITE);
        UIManager.put("Table.foreground", Color.BLACK);

        UIManager.put("TableHeader.background", background.darker());
        UIManager.put("TableHeader.foreground", foreground);
    }
}
