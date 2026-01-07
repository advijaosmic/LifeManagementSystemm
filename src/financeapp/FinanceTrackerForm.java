package financeapp;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lifeapp.MyTrackersForm;
import lifeapp.Session;
import org.bson.Document;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import static com.mongodb.client.model.Filters.eq;

public class FinanceTrackerForm {

    private JPanel mainPanel;
    private JTextField amountField;
    private JTextField descriptionField;
    private JComboBox<String> typeCombo;
    private JButton addButton;
    private JTable transactionTable;
    private JLabel incomeLabel;
    private JLabel expenseLabel;
    private JLabel balanceLabel;
    private JButton backButton;

    private MongoCollection<Document> collection;

    public FinanceTrackerForm() {

        try {
            MongoDatabase db = MongoDBConnection.getDatabase();
            collection = db.getCollection("transactions");
        } catch (Exception e) {
            collection = null;
        }

        addButton.addActionListener(e -> {
            if (collection == null) {
                JOptionPane.showMessageDialog(null, "Konekcija sa bazom neuspjesna");
                return;
            }

            try {
                String type = (String) typeCombo.getSelectedItem();
                double amount = Double.parseDouble(amountField.getText());
                String description = descriptionField.getText();

                if (type == null || description.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Sva polja moraju biti popunjena");
                    return;
                }

                Document doc = new Document("username", Session.currentUsername)
                        .append("type", type)
                        .append("amount", amount)
                        .append("description", description);

                collection.insertOne(doc);

                loadDataIntoTable();
                updateSummary();

                amountField.setText("");
                descriptionField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Unesi broj");
            }
        });

        backButton.addActionListener(e -> {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);
            frame.setContentPane(new MyTrackersForm().getMainPanel());
            frame.setTitle("Life Management System - My Trackers");
            frame.revalidate();
            frame.repaint();
        });

        loadDataIntoTable();
        updateSummary();
    }

    private void loadDataIntoTable() {
        if (collection == null || transactionTable == null) return;

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Type");
        model.addColumn("Amount");
        model.addColumn("Description");

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            model.addRow(new Object[]{
                    d.getString("type"),
                    d.getDouble("amount"),
                    d.getString("description")
            });
        }

        transactionTable.setModel(model);
    }

    private void updateSummary() {
        if (collection == null || incomeLabel == null || expenseLabel == null || balanceLabel == null) return;

        double income = 0;
        double expense = 0;

        for (Document d : collection.find(eq("username", Session.currentUsername))) {
            Double amount = d.getDouble("amount");
            String type = d.getString("type");

            if (amount == null || type == null) continue;

            if ("prihodi".equalsIgnoreCase(type)) {
                income += amount;
            } else if ("rashodi".equalsIgnoreCase(type)) {
                expense += amount;
            }
        }

        incomeLabel.setText("Income: " + income);
        expenseLabel.setText("Expense: " + expense);
        balanceLabel.setText("Balance: " + (income - expense));
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
