package lifeapp;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {

            ThemeManager.applyDefaultTheme();

            JFrame frame = new JFrame("Life Management System");
            frame.setContentPane(new LoginForm().getMainPanel());
            frame.setSize(800, 500);
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}
