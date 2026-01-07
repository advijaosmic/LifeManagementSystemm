package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.eq;

public class SleepTrackerForm {

    private JPanel mainPanel;
    private JTextField hoursField;
    private JButton addSleepButton;
    private JTable sleepTable;
    private JLabel averageLabel;
    private JButton backButton;

    private MongoCollection<Document> collection;

    public SleepTrackerForm() {

        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            collection = db.getCollection("sleep_entries");
        } catch (Exception e) {
            collection = null;
            JOptionPane.showMessageDialog(null, "Konekcija sa bazom neuspjesna");
        }

        addSleepButton.addActionListener(e -> {
            if (collection == null) return;

            try {
                double hours = Double.parseDouble(hoursField.getText());

                if (hours <= 0 || hours > 24) {
                    JOptionPane.showMessageDialog(null, "Sati moraju biti u rasponu 1–24");
                    return;
                }

                Document doc = new Document("username", Session.currentUsername)
                        .append("hours", hours);

                collection.insertOne(doc);
                hoursField.setText("");

                loadSleepData();
                updateAverage();

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Unesite broj!");
            }
        });

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new MyTrackersForm().getMainPanel());
            frame.setTitle("Life Management System - My Trackers");
            frame.revalidate();
            frame.repaint();
        });

        loadSleepData();
        updateAverage();
    }

    private void loadSleepData() {
        if (collection == null) return;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Sati spavanja");

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            model.addRow(new Object[]{ d.getDouble("hours") });
        }

        sleepTable.setModel(model);
    }

    private void updateAverage() {
        if (collection == null) return;

        double sum = 0;
        int count = 0;

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            Double h = d.getDouble("hours");
            if (h != null) {
                sum += h;
                count++;
            }
        }

        if (count == 0) {
            averageLabel.setText("Prosječan broj sati spavanja: 0 h");
        } else {
            averageLabel.setText(
                    "Prosječan broj sati spavanja: " +
                            String.format("%.2f", sum / count) + " h"
            );
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
