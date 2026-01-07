package lifeapp;

import financeapp.FinanceTrackerForm;

import javax.swing.*;

public class MyTrackersForm {

    private JPanel mainPanel;
    private JButton financeButton;
    private JButton backButton;
    private JButton habitButton;
    private JButton sleepButton;
    private JButton studyButton;

    public MyTrackersForm() {

        financeButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new FinanceTrackerForm().getMainPanel());
            frame.setTitle("Life Management System - Finance Tracker");
            frame.revalidate();
            frame.repaint();
        });

        sleepButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new SleepTrackerForm().getMainPanel());
            frame.setTitle("Life Management System - Sleep Tracker");
            frame.revalidate();
            frame.repaint();
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
