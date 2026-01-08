package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.eq;

public class UserManagementForm {

    private JPanel mainPanel;
    private JTable usersTable;
    private JButton deleteButton;
    private JButton saveButton;
    private JButton backButton;
    private JComboBox<String> themeCombo;
    private JLabel usernameLabel;
    private JLabel themeLabel;

    private MongoCollection<Document> usersCollection;

    public UserManagementForm() {

        MongoDatabase db = MongoDBConnection.getDatabase();
        usersCollection = db.getCollection("users");

        themeCombo.removeAllItems();
        themeCombo.addItem("Default");
        themeCombo.addItem("Dark");
        themeCombo.addItem("Light");

        usersTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                loadSelectedUser();
            }
        });

        saveButton.addActionListener(e -> saveTheme());
        deleteButton.addActionListener(e -> deleteUser());

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new LoginForm().getMainPanel());
            frame.setTitle("Life Management System - Login");
            frame.revalidate();
            frame.repaint();
        });

        loadUsers();
    }

    private void loadUsers() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Username");

        for (Document d : usersCollection.find()) {
            model.addRow(new Object[]{
                    d.getString("username")
            });
        }

        usersTable.setModel(model);
    }

    private void loadSelectedUser() {
        int row = usersTable.getSelectedRow();
        if (row == -1) return;

        String username = usersTable.getValueAt(row, 0).toString();
        Document user = usersCollection.find(eq("username", username)).first();
        if (user == null) return;

        usernameLabel.setText("Korisničko ime: " + username);

        String theme = user.getString("theme");
        if (theme == null) theme = "Default";

        themeLabel.setText("Tema:");
        themeCombo.setSelectedItem(theme);
    }

    private void saveTheme() {
        int row = usersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Odaberite korisnika.");
            return;
        }

        String username = usersTable.getValueAt(row, 0).toString();
        String selectedTheme = (String) themeCombo.getSelectedItem();

        usersCollection.updateOne(
                eq("username", username),
                new Document("$set", new Document("theme", selectedTheme))
        );

        JOptionPane.showMessageDialog(null, "Tema uspješno sačuvana.");
    }

    private void deleteUser() {
        int row = usersTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Odaberite korisnika.");
            return;
        }

        String username = usersTable.getValueAt(row, 0).toString();

        if (username.equals(Session.currentUsername)) {
            JOptionPane.showMessageDialog(null, "Ne možete obrisati trenutno prijavljenog korisnika.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                null,
                "Da li ste sigurni da želite obrisati korisnika?",
                "Potvrda",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm != JOptionPane.YES_OPTION) return;

        usersCollection.deleteOne(eq("username", username));
        loadUsers();

        usernameLabel.setText("Korisničko ime:");
        themeLabel.setText("Tema:");
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
