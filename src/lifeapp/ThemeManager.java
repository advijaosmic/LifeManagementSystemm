package lifeapp;

import javax.swing.*;
import java.awt.*;

public class ThemeManager {

    public static void applyTheme(String theme) {

        Color background;
        Color panel;
        Color button;
        Color text;

        switch (theme) {
            case "Dark":
                background = Color.decode("#751e13");
                panel = Color.decode("#0B5C3F");
                button = Color.decode("#993426");
                text = Color.WHITE;
                break;

            case "Light":
                background = Color.decode("#faf9eb");
                panel = Color.WHITE;
                button = Color.decode("#E6D3B1");
                text = Color.decode("#3A3A3A");
                break;

            default:
                background = Color.decode("#FFF1C1");
                panel = Color.decode("#FFE08A");
                button = Color.decode("#F4C16D");
                text = Color.decode("#4A3B2A");
                break;
        }

        UIManager.put("Panel.background", background);
        UIManager.put("OptionPane.background", background);
        UIManager.put("OptionPane.messageForeground", text);

        UIManager.put("Button.background", button);
        UIManager.put("Button.foreground", text);

        UIManager.put("Label.foreground", text);

        UIManager.put("Table.background", panel);
        UIManager.put("Table.foreground", text);
        UIManager.put("Table.selectionBackground", button);
        UIManager.put("Table.selectionForeground", text);

        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", Color.BLACK);
        UIManager.put("PasswordField.background", Color.WHITE);
        UIManager.put("PasswordField.foreground", Color.BLACK);
    }

    public static void applyDefaultTheme() {
        applyTheme("Default");
    }
}
