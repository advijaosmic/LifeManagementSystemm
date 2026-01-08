package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class HabitTrackerForm {

    private JPanel mainPanel;
    private JTextField habitField;
    private JButton addHabitButton;
    private JButton deleteHabitButton;
    private JTable habitTable;
    private JButton backButton;
    private JLabel statsLabel;

    private MongoCollection<Document> collection;

    public HabitTrackerForm() {

        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            collection = db.getCollection("habits");
        } catch (Exception e) {
            collection = null;
            JOptionPane.showMessageDialog(null, "Konekcija sa bazom neuspjela");
        }

        addHabitButton.addActionListener(e -> addHabit());
        deleteHabitButton.addActionListener(e -> deleteHabit());

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new MyTrackersForm().getMainPanel());
            frame.revalidate();
            frame.repaint();
        });

        loadHabits();
        updateStats();
    }

    private void addHabit() {
        if (collection == null) return;

        String habitName = habitField.getText().trim();

        if (habitName.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Unesite naziv navike.");
            return;
        }

        Document existing = collection.find(
                and(
                        eq("username", Session.currentUsername),
                        eq("name", habitName)
                )
        ).first();

        if (existing != null) {
            JOptionPane.showMessageDialog(null, "Ova navika već postoji.");
            return;
        }

        Document doc = new Document("username", Session.currentUsername)
                .append("name", habitName);

        collection.insertOne(doc);
        habitField.setText("");

        loadHabits();
        updateStats();
    }

    private void deleteHabit() {
        int selectedRow = habitTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "Odaberite naviku iz tabele.");
            return;
        }

        String habitName = habitTable.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Da li ste sigurni da želite obrisati naviku?",
                "Potvrda",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        collection.deleteOne(
                and(
                        eq("username", Session.currentUsername),
                        eq("name", habitName)
                )
        );

        loadHabits();
        updateStats();
    }

    private void loadHabits() {
        if (collection == null) return;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Navika");

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            model.addRow(new Object[]{ d.getString("name") });
        }

        habitTable.setModel(model);
    }

    private void updateStats() {
        if (collection == null) return;

        long count = collection.countDocuments(eq("username", Session.currentUsername));

        Document lastHabit = collection.find(eq("username", Session.currentUsername))
                .sort(new Document("_id", -1))
                .first();

        if (count == 0) {
            statsLabel.setText("Nemate unesenih navika.");
        } else {
            statsLabel.setText(
                    "Ukupno navika: " + count +
                            " | Zadnja dodana: " + lastHabit.getString("name")
            );
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
