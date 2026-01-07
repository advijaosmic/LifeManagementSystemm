package lifeapp;

import financeapp.FinanceTrackerForm;

import javax.swing.*;
import java.awt.*;

public class MyTrackersForm {

    private JPanel mainPanel;
    private JButton financeButton;
    private JButton backButton;
    private JButton habitButton;
    private JButton sleepButton;
    private JButton studyButton;

    public MyTrackersForm() {

        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        financeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        sleepButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        habitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        studyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(financeButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(sleepButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(habitButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(studyButton);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(backButton);

        financeButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new FinanceTrackerForm().getMainPanel());
            frame.revalidate();
        });

        sleepButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new SleepTrackerForm().getMainPanel());
            frame.revalidate();
        });

        habitButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new HabitTrackerForm().getMainPanel());
            frame.setTitle("Life Management System - Habit Tracker");
            frame.revalidate();
            frame.repaint();
        });

        studyButton.addActionListener(e ->
                JOptionPane.showMessageDialog(null, "Study Tracker")
        );

        backButton.addActionListener(e -> {
            Session.currentUsername = null;
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new LoginForm().getMainPanel());
            frame.setTitle("Life Management System - Login");
            frame.revalidate();
            frame.repaint();
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
