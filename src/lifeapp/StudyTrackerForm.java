package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;

import static com.mongodb.client.model.Filters.eq;

public class StudyTrackerForm {

    private JPanel mainPanel;
    private JTextField hoursField;
    private JTextField goalField;
    private JButton addStudyButton;
    private JButton saveGoalButton;
    private JButton backButton;
    private JTable studyTable;
    private JLabel statsLabel;

    private MongoCollection<Document> entriesCollection;
    private MongoCollection<Document> goalsCollection;

    public StudyTrackerForm() {

        MongoDatabase db = MongoDBConnection.getDatabase();
        entriesCollection = db.getCollection("study_entries");
        goalsCollection = db.getCollection("study_goals");

        addStudyButton.addActionListener(e -> addStudyEntry());
        saveGoalButton.addActionListener(e -> saveWeeklyGoal());

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new MyTrackersForm().getMainPanel());
            frame.revalidate();
            frame.repaint();
        });

        loadGoal();
        loadEntries();
        updateStats();
    }

    private void addStudyEntry() {
        try {
            double hours = Double.parseDouble(hoursField.getText());

            if (hours <= 0 || hours > 24) {
                JOptionPane.showMessageDialog(null, "Neispravan broj sati.");
                return;
            }

            Document doc = new Document("username", Session.currentUsername)
                    .append("date", LocalDate.now().toString())
                    .append("hours", hours);

            entriesCollection.insertOne(doc);
            hoursField.setText("");

            loadEntries();
            updateStats();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Unesite broj.");
        }
    }

    private void saveWeeklyGoal() {
        try {
            double goal = Double.parseDouble(goalField.getText());

            if (goal <= 0) {
                JOptionPane.showMessageDialog(null, "Cilj mora biti veći od 0.");
                return;
            }

            goalsCollection.deleteOne(eq("username", Session.currentUsername));

            goalsCollection.insertOne(
                    new Document("username", Session.currentUsername)
                            .append("goal", goal)
            );

            updateStats();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Unesite broj.");
        }
    }

    private void loadGoal() {
        Document goalDoc = goalsCollection.find(eq("username", Session.currentUsername)).first();
        if (goalDoc != null) {
            goalField.setText(String.valueOf(goalDoc.getDouble("goal")));
        }
    }

    private void loadEntries() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Datum");
        model.addColumn("Sati učenja");

        for (Document d : entriesCollection.find(eq("username", Session.currentUsername))) {
            model.addRow(new Object[]{
                    d.getString("date"),
                    d.getDouble("hours")
            });
        }

        studyTable.setModel(model);
    }

    private void updateStats() {
        double total = 0;

        for (Document d : entriesCollection.find(eq("username", Session.currentUsername))) {
            Double h = d.getDouble("hours");
            if (h != null) total += h;
        }

        Document goalDoc = goalsCollection.find(eq("username", Session.currentUsername)).first();
        double goal = goalDoc != null ? goalDoc.getDouble("goal") : 0;

        if (goal > 0) {
            statsLabel.setText(
                    "Ukupno: " + String.format("%.2f", total) +
                            " h / Cilj: " + goal + " h"
            );
        } else {
            statsLabel.setText(
                    "Ukupno sati učenja: " + String.format("%.2f", total) + " h"
            );
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
