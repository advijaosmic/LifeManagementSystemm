package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;

import static com.mongodb.client.model.Filters.eq;

public class RegisterForm {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton registerButton;
    private JButton backButton;

    public RegisterForm() {
        registerButton.addActionListener(e -> registerUser());

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new LoginForm().getMainPanel());
            frame.setTitle("Life Management System - Login");
            frame.revalidate();
            frame.repaint();
        });
    }

    private void registerUser() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.");
            return;
        }

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> users = db.getCollection("users");

        Document existingUser = users.find(eq("username", username)).first();
        if (existingUser != null) {
            JOptionPane.showMessageDialog(null, "Korisnik već postoji.");
            return;
        }

        Document newUser = new Document("username", username)
                .append("password", password);

        users.insertOne(newUser);

        JOptionPane.showMessageDialog(null, "Registracija uspješna!");

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.setContentPane(new LoginForm().getMainPanel());
        frame.setTitle("Life Management System - Login");
        frame.revalidate();
        frame.repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
