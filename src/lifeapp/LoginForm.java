package lifeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import financeapp.MongoDBConnection;
import org.bson.Document;

import javax.swing.*;

import static com.mongodb.client.model.Filters.eq;

public class LoginForm {

    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton manageUsersButton;

    public LoginForm() {

        loginButton.addActionListener(e -> login());

        registerButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new RegisterForm().getMainPanel());
            frame.setTitle("Life Management System - Registration");
            frame.revalidate();
            frame.repaint();
        });

        manageUsersButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new UserManagementForm().getMainPanel());
            frame.setTitle("Life Management System - User Management");
            frame.revalidate();
            frame.repaint();
        });
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Molimo popunite sva polja.");
            return;
        }

        MongoDatabase db = MongoDBConnection.getDatabase();
        MongoCollection<Document> users = db.getCollection("users");

        Document user = users.find(eq("username", username)).first();

        if (user == null) {
            JOptionPane.showMessageDialog(null, "Korisnik ne postoji.");
            return;
        }

        if (!password.equals(user.getString("password"))) {
            JOptionPane.showMessageDialog(null, "Pogrešna lozinka.");
            return;
        }

        Session.currentUsername = username;

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
        frame.setContentPane(new MyTrackersForm().getMainPanel());
        frame.setTitle("Life Management System - My Trackers");
        frame.revalidate();
        frame.repaint();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
