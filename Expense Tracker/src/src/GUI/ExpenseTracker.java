package src.GUI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.UI.Expense;
public class ExpenseTracker implements Serializable {
    private List<User> users;
    private User currentUser;
    private List<Expense> expenses;

    public ExpenseTracker() {
        this.users = new ArrayList<>();
        this.expenses = new ArrayList<>();
    }

    public void registerUser(String username, String password) {
        users.add(new User(username, password));
    }

    public boolean loginUser(String username, String password) {
        for (User user : users) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    public void addExpense(Date date, String category, double amount) {
        expenses.add(new Expense(date, category, amount));
    }

    public List<Expense> getExpenses() {
        return expenses;
    }

    public Map<String, Double> getCategorySummation() {
        Map<String, Double> categorySum = new HashMap<>();
        for (Expense expense : expenses) {
            String category = expense.getCategory();
            double amount = expense.getAmount();
            categorySum.put(category, categorySum.getOrDefault(category, 0.0) + amount);
        }
        return categorySum;
    }

    public void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("src/Data/expense_data.ser"))) {
            oos.writeObject(users);
            oos.writeObject(expenses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void loadDataFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("data/expense_data.ser"))) {
            users = (List<User>) ois.readObject();
            expenses = (List<Expense>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
