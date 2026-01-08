package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class SleepTrackerForm {

    private JPanel mainPanel;
    private JTextField hoursField;
    private JButton addSleepButton;
    private JButton deleteSleepButton;
    private JTable sleepTable;
    private JLabel averageLabel;
    private JLabel statsLabel;
    private JButton backButton;

    private MongoCollection<Document> collection;

    public SleepTrackerForm() {

        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            collection = db.getCollection("sleep_entries");
        } catch (Exception e) {
            collection = null;
            JOptionPane.showMessageDialog(null, "Konekcija sa bazom neuspjela");
        }

        addSleepButton.addActionListener(e -> addSleep());
        deleteSleepButton.addActionListener(e -> deleteSleep());

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new MyTrackersForm().getMainPanel());
            frame.revalidate();
            frame.repaint();
        });

        loadSleepData();
        updateStats();
    }

    private void addSleep() {
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
            updateStats();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "Unesite broj!");
        }
    }

    private void deleteSleep() {
        int selectedRow = sleepTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Odaberite unos iz tabele.");
            return;
        }

        Double hours = (Double) sleepTable.getValueAt(selectedRow, 0);

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Da li ste sigurni da želite obrisati ovaj unos?",
                "Potvrda",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        collection.deleteOne(
                and(
                        eq("username", Session.currentUsername),
                        eq("hours", hours)
                )
        );

        loadSleepData();
        updateStats();
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

    private void updateStats() {
        if (collection == null) return;

        double sum = 0;
        double min = Double.MAX_VALUE;
        double max = 0;
        int count = 0;

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            Double h = d.getDouble("hours");
            if (h == null) continue;

            sum += h;
            count++;
            min = Math.min(min, h);
            max = Math.max(max, h);
        }

        if (count == 0) {
            averageLabel.setText("Prosječan broj sati: 0");
            statsLabel.setText("Nemate unesenih podataka.");
        } else {
            averageLabel.setText(
                    "Prosječan broj sati: " + String.format("%.2f", sum / count)
            );

            statsLabel.setText(
                    "Unosa: " + count +
                            " | Najkraće: " + min + " h" +
                            " | Najduže: " + max + " h"
            );
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
