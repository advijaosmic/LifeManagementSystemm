package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.eq;

public class HabitTrackerForm {

    private JPanel mainPanel;
    private JTextField habitField;
    private JButton addHabitButton;
    private JTable habitTable;
    private JButton backButton;

    private MongoCollection<Document> collection;

    public HabitTrackerForm() {
        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            collection = db.getCollection("habits");
        } catch (Exception e) {
            collection = null;
            JOptionPane.showMessageDialog(null, "Konekcija sa bazom neuspjesna");
        }

        addHabitButton.addActionListener(e -> {
            if (collection == null) return;

            String habitName = habitField.getText().trim();

            if (habitName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Morate unijeti naviku!");
                return;
            }

            Document doc = new Document("username", Session.currentUsername)
                    .append("name", habitName);

            collection.insertOne(doc);
            habitField.setText("");

            loadHabits();
        });

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new MyTrackersForm().getMainPanel());
            frame.setTitle("Life Management System - My Trackers");
            frame.revalidate();
            frame.repaint();
        });

        loadHabits();
    }

    private void loadHabits() {
        if (collection == null) return;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Habit");

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            model.addRow(new Object[]{
                    d.getString("name")
            });
        }

        habitTable.setModel(model);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
