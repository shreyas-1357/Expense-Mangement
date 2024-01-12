package src.UI;


import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date date;
    private String category;
    private double amount;

    public Expense(Date date, String category, double amount) {
        this.date = date;
        this.category = category;
        this.amount = amount;
    }

    public Date getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}

