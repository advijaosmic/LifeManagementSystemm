package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.eq;

public class UserManagementForm {

    private JPanel mainPanel;
    private JTable usersTable;
    private JButton deleteButton;
    private JButton backButton;

    private MongoCollection<Document> collection;

    public UserManagementForm() {

        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            collection = db.getCollection("users");
        } catch (Exception e) {
            collection = null;
            JOptionPane.showMessageDialog(null, "Konekcija sa bazom neuspjesna");
        }

        loadUsers();

        deleteButton.addActionListener(e -> {
            int selectedRow = usersTable.getSelectedRow();

            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(null, "Izaberi korisnika za brisanje!");
                return;
            }

            String username = usersTable.getValueAt(selectedRow, 0).toString();

            int confirm = JOptionPane.showConfirmDialog(
                    null,
                    "Obrisi korisnika " + username + "?",
                    "Potvrdi",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                collection.deleteOne(eq("username", username));
                loadUsers();
            }
        });
        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new LoginForm().getMainPanel());
            frame.setTitle("Life Management System - Login");
            frame.revalidate();
            frame.repaint();
        });
    }

    private void loadUsers() {
        if (collection == null) return;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Username");

        for (Document d : collection.find()) {
            model.addRow(new Object[]{
                    d.getString("username")
            });
        }

        usersTable.setModel(model);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
