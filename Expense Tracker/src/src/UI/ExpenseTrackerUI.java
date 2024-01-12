package src.UI;

import javax.swing.*;

import src.GUI.ExpenseTracker;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class ExpenseTrackerUI extends JFrame {
    private ExpenseTracker expenseTracker;
    private JTextField usernameField, categoryField, amountField, dateField;
    private JPasswordField passwordField;
    private JTextArea expensesTextArea;

    private JPanel loginPanel;
    private JPanel expensePanel;

    public ExpenseTrackerUI() {
        expenseTracker = new ExpenseTracker();
        initializeUI();
    }

    private void initializeUI() {
        setTitle("Expense Tracker");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        loginPanel = createLoginPanel();
        expensePanel = createExpensePanel();

        showLoginPanel(); // Initially, show the login panel

        add(loginPanel, BorderLayout.CENTER);
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (expenseTracker.loginUser(username, password)) {
                    showExpensePanel();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid username or password", "Login Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = usernameField.getText();
                String password = String.valueOf(passwordField.getPassword());

                if (username.isEmpty() || password.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Username and password cannot be empty", "Registration Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    expenseTracker.registerUser(username, password);
                    JOptionPane.showMessageDialog(null, "User registered successfully", "Registration Success", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(loginButton);
        panel.add(registerButton);

        return panel;
    }

    private JPanel createExpensePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Add Expense", createAddExpensePanel());
        tabbedPane.addTab("View Expenses", createViewExpensesPanel());
        tabbedPane.addTab("Category-wise Summation", createCategorySummationPanel());

        panel.add(tabbedPane, BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                expenseTracker.saveDataToFile();
                showLoginPanel();
            }
        });

        panel.add(logoutButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createAddExpensePanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel dateLabel = new JLabel("Date (yyyy-MM-dd):");
        JLabel categoryLabel = new JLabel("Category:");
        JLabel amountLabel = new JLabel("Amount:");

        dateField = new JTextField();
        categoryField = new JTextField();
        amountField = new JTextField();

        JButton addButton = new JButton("Add Expense");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateField.getText());
                    String category = categoryField.getText();
                    double amount = Double.parseDouble(amountField.getText());

                    expenseTracker.addExpense(date, category, amount);
                    JOptionPane.showMessageDialog(null, "Expense added successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } catch (ParseException | NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Invalid input. Please check your data.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(dateLabel);
        panel.add(dateField);
        panel.add(categoryLabel);
        panel.add(categoryField);
        panel.add(amountLabel);
        panel.add(amountField);
        panel.add(new JLabel()); // Empty label for spacing
        panel.add(addButton);

        return panel;
    }

    private JPanel createViewExpensesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        expensesTextArea = new JTextArea();
        expensesTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(expensesTextArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshExpensesTextArea();
            }
        });

        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createCategorySummationPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JTextArea categorySumTextArea = new JTextArea();
        categorySumTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(categorySumTextArea);

        panel.add(scrollPane, BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshCategorySumTextArea();
            }
        });

        panel.add(refreshButton, BorderLayout.SOUTH);

        return panel;
    }

    private void showLoginPanel() {
        getContentPane().removeAll();
        add(loginPanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void showExpensePanel() {
        getContentPane().removeAll();
        add(expensePanel, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private void refreshExpensesTextArea() {
        StringBuilder sb = new StringBuilder("List of Expenses:\n");
        for (Expense expense : expenseTracker.getExpenses()) {
            sb.append(expense.getDate()).append(" | ").append(expense.getCategory()).append(" | ").append(expense.getAmount()).append("\n");
        }
        expensesTextArea.setText(sb.toString());
    }

    private void refreshCategorySumTextArea() {
        Component selectedComponent = ((JTabbedPane) ((JPanel) getContentPane().getComponent(0)).getComponent(0)).getSelectedComponent();

        if (selectedComponent instanceof JScrollPane) {
            JTextArea categorySumTextArea = (JTextArea) ((JScrollPane) selectedComponent).getViewport().getView();
            Map<String, Double> categorySum = expenseTracker.getCategorySummation();
            StringBuilder sb = new StringBuilder("Category-wise Summation:\n");
            for (Map.Entry<String, Double> entry : categorySum.entrySet()) {
                sb.append(entry.getKey()).append(" | ").append(entry.getValue()).append("\n");
            }
            categorySumTextArea.setText(sb.toString());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ExpenseTrackerUI().setVisible(true);
            }
        });
    }
}
